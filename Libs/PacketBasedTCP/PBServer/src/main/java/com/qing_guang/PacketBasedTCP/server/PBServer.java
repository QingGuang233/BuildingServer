package com.qing_guang.PacketBasedTCP.server;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.*;
import com.qing_guang.PacketBasedTCP.packet.def.formatter.PublicKeyFormatter;
import com.qing_guang.PacketBasedTCP.packet.format.PacketFormatter;
import com.qing_guang.PacketBasedTCP.server.encode.PBPacketHandlerEn;
import com.qing_guang.PacketBasedTCP.server.encode.PBPacketHandlerProt;
import com.qing_guang.PacketBasedTCP.server.handler.PBHandlerManager;
import com.qing_guang.PacketBasedTCP.server.handler.def.PBDisconnHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.*;
import java.util.function.Consumer;

/**
 * 服务器
 */
public class PBServer {

    private int port;
    private long disconnDelay;
    private ServerSocketChannel sChannel;
    private Selector rqSelector;
    private Selector receive;
    private Map<UUID,PBClient> clients;
    private Map<SocketChannel,PBClient> clientsChannel;
    private Set<Consumer<PBClient>> acceptListeners;
    PBHandlerManager handlerManager;
    private Timer timer;
    PacketFormatter formatter;
    private boolean forceEncode;
    PBPacketHandlerEn handlerEn;
    private String asymAlgorithm;
    private String symAlgorithm;

    /** default */
    public PBServer(){}

    /**
     * 新建服务器
     * @param port 端口
     * @param disconnDelay 延迟断线时长(ms),具体功能在 {@code PBServer::disconn} 这个方法的说明里可以查看
     * @param forceEncode 是否开启强制加密模式
     * @param asymAlgorithm 非对称加密算法,不需要加密通信时可以传入null
     * @param symAlgorithm 对称加密算法,不需要加密通信时可以传入null
     */
    public PBServer(int port,long disconnDelay,boolean forceEncode,String asymAlgorithm,String symAlgorithm){
        this.port = port;
        this.disconnDelay = disconnDelay;
        this.forceEncode = forceEncode;
        this.asymAlgorithm = asymAlgorithm;
        this.symAlgorithm = symAlgorithm;
    }

    /**
     * 让服务器开始工作
     * @throws IOException 服务器初始化有异常时抛出
     */
    public void start() throws IOException{

        sChannel = ServerSocketChannel.open();
        sChannel.bind(new InetSocketAddress(port));

        clients = new HashMap<>();
        clientsChannel = new HashMap<>();
        acceptListeners = new HashSet<>();
        rqSelector = Selector.open();
        receive = Selector.open();
        handlerManager = new PBHandlerManager();
        timer = new Timer();
        formatter = new PacketFormatter();

        sChannel.configureBlocking(false);
        sChannel.register(rqSelector,SelectionKey.OP_ACCEPT);
        handlerManager.addPacketHandler(PBDisconnHandler.INSTANCE,PBPacketDisconn.class);

        handlerEn = new PBPacketHandlerEn(asymAlgorithm,symAlgorithm);
        handlerManager.addPacketHandler(handlerEn, PBPacketAsym.class, PBPacketSym.class);
        handlerManager.addPacketHandlerAll(new PBPacketHandlerProt(handlerEn));

        if(isForceEncode()){
            try {
                formatter.addDataFormatter(PublicKey.class,new PublicKeyFormatter(asymAlgorithm));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 服务器是否强制采用加密传输数据
     * @return result
     */
    public boolean isForceEncode() {
        return forceEncode;
    }

    /**
     * 接受一轮连接请求
     * @throws IOException 接受连接请求时有IO错误时抛出
     */
    public void accept() throws IOException{
        if(rqSelector.selectNow() != 0){
            UUID uid;
            do {
                uid = UUID.randomUUID();
            }while(clients.containsKey(uid));
            SocketChannel clientChannel = sChannel.accept();
            while(!clientChannel.finishConnect());
            clientChannel.configureBlocking(false);
            clientChannel.register(receive,SelectionKey.OP_READ);
            PBClient client = new PBClient(this, clientChannel,uid);
            clients.put(uid,client);
            clientsChannel.put(clientChannel,client);
            client.sendPacket(new PBPacketServerInfo(forceEncode,symAlgorithm,asymAlgorithm));
            acceptListeners.forEach((action) -> action.accept(client));
            rqSelector.selectedKeys().clear();
        }
    }

    /**
     * 从所有Channel中读取一轮数据(还没有数据传入的不进行任何操作)
     */
    public void read(){
        PBClient now = null;
        try {
            if(receive.selectNow() != 0){
                Set<SelectionKey> keys = receive.selectedKeys();
                for(SelectionKey key : keys){
                    PBClient client = now = clientsChannel.get(key.channel());
                    SocketChannel sChannel = client.getChannel();
                    if(client.len == -1){
                        sChannel.read(client.lengthBuffer);
                        if(!client.lengthBuffer.hasRemaining()){
                            client.len = formatter.getIntFormatter().toData(client.lengthBuffer.array(),0,4);
                            client.lengthBuffer.clear();
                        }
                    }
                    if(client.len != -1){
                        if(client.packetDataBuffer == null){
                            client.packetDataBuffer = ByteBuffer.allocate(client.len);
                        }
                        sChannel.read(client.packetDataBuffer);
                    }
                    if(client.packetDataBuffer != null && !client.packetDataBuffer.hasRemaining()){
                        PBPacket packet = formatter.toPacket(handlerEn.decode(client.packetDataBuffer.array(),client));
                        handlerManager.handle(client,packet,true);
                        client.packetDataBuffer = null;
                        client.len = -1;
                    }
                }
                keys.clear();
            }
        }catch (IOException e) {
            if(clients.containsKey(now.getUUID())){
                disconn(now, PBPacketDisconn.DisconnReason.IO_ERROR_READ,null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 添加一个连接建立监听器
     * @param acceptListener 连接建立监听器
     */
    public void addAcceptListener(Consumer<PBClient> acceptListener){
        acceptListeners.add(acceptListener);
    }

    /**
     * 添加一个连接建立监听器
     * @param acceptListener 连接建立监听器
     */
    public void removeAcceptListener(Consumer<PBClient> acceptListener){
        acceptListeners.remove(acceptListener);
    }

    /**
     * 获取数据包监听系统
     * @return result
     */
    public PBHandlerManager getHandlerManager(){
        return handlerManager;
    }

    /**
     * 获取数据包解析器
     * @return result
     */
    public PacketFormatter getFormatter(){
        return formatter;
    }

    /**
     * 获取服务器采用的非对称加密算法
     * @return result
     */
    public String getAsymAlgorithm() {
        return asymAlgorithm;
    }

    /**
     * 获取服务器采用的对称加密算法
     * @return result
     */
    public String getSymAlgorithm() {
        return symAlgorithm;
    }

    /**
     * 将一个客户端断线
     * 没有网络IO异常或是因为加密步骤出现问题的,延迟指定秒数后直接断线(为了防止给客户端发出断线通知后过长时间未断线)
     * 其中,如果断线原因是SERVER_CUSTOM的,建议写上断线的解释说明发送给客户端
     * 其余的情况都是直接断线,没有延时
     * 如果断线原因是SERVER_CLOSE,会向客户端发送PBPacketDisconn数据包,如果是IO异常则只会内部处理一下数据包事件
     * @param client 客户端
     * @param reason 断线原因
     * @param description 断线的一些解释说明
     */
    public void disconn(PBClient client, PBPacketDisconn.DisconnReason reason, String description){
        try {
            if(reason.equals(PBPacketDisconn.DisconnReason.SERVER_CUSTOM) || reason.equals(PBPacketDisconn.DisconnReason.ENCRYPT)){
                client.sendPacket(new PBPacketDisconn(reason,description));
                TimerTask disconnTest = new TimerTask() {
                    public void run() {
                        try{
                            ByteBuffer buffer = ByteBuffer.allocate(1);
                            if(client.getChannel().read(buffer) != -1){
                                client.getChannel().close();
                            }
                        }catch (IOException ignored){}
                    }
                };
                timer.schedule(disconnTest,disconnDelay);
            }else{
                if(reason.equals(PBPacketDisconn.DisconnReason.SERVER_CLOSE)){
                    client.sendPacket(new PBPacketDisconn(reason,description));
                }else if(reason.equals(PBPacketDisconn.DisconnReason.IO_ERROR_READ) || reason.equals(PBPacketDisconn.DisconnReason.IO_ERROR_WRITE)){
                    handlerManager.handle(client,new PBPacketDisconn(reason,description),false);
                }
                client.getChannel().close();
            }
        } catch (IOException ignored) {}
        finally {
            client.isOnline = false;
            clients.remove(client.getUUID());
            clientsChannel.remove(client.getChannel());
        }
    }

    /**
     * 关闭服务器
     */
    public void stop(){
        try {
            timer.cancel();
            rqSelector.close();
            receive.close();
            for(PBClient client : allClients()){
                disconn(client, PBPacketDisconn.DisconnReason.SERVER_CLOSE, "Server stopped");
            }
            sChannel.close();
        } catch (IOException ignored){}
    }

    /**
     * 所有已连接的客户端
     * @return result
     */
    public Collection<PBClient> allClients(){
        return clients.values();
    }

}
