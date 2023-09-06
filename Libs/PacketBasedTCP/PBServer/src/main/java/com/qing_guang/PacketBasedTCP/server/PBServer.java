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
 * ������
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
     * �½�������
     * @param port �˿�
     * @param disconnDelay �ӳٶ���ʱ��(ms),���幦���� {@code PBServer::disconn} ���������˵������Բ鿴
     * @param forceEncode �Ƿ���ǿ�Ƽ���ģʽ
     * @param asymAlgorithm �ǶԳƼ����㷨,����Ҫ����ͨ��ʱ���Դ���null
     * @param symAlgorithm �ԳƼ����㷨,����Ҫ����ͨ��ʱ���Դ���null
     */
    public PBServer(int port,long disconnDelay,boolean forceEncode,String asymAlgorithm,String symAlgorithm){
        this.port = port;
        this.disconnDelay = disconnDelay;
        this.forceEncode = forceEncode;
        this.asymAlgorithm = asymAlgorithm;
        this.symAlgorithm = symAlgorithm;
    }

    /**
     * �÷�������ʼ����
     * @throws IOException ��������ʼ�����쳣ʱ�׳�
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
     * �������Ƿ�ǿ�Ʋ��ü��ܴ�������
     * @return result
     */
    public boolean isForceEncode() {
        return forceEncode;
    }

    /**
     * ����һ����������
     * @throws IOException ������������ʱ��IO����ʱ�׳�
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
     * ������Channel�ж�ȡһ������(��û�����ݴ���Ĳ������κβ���)
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
     * ���һ�����ӽ���������
     * @param acceptListener ���ӽ���������
     */
    public void addAcceptListener(Consumer<PBClient> acceptListener){
        acceptListeners.add(acceptListener);
    }

    /**
     * ���һ�����ӽ���������
     * @param acceptListener ���ӽ���������
     */
    public void removeAcceptListener(Consumer<PBClient> acceptListener){
        acceptListeners.remove(acceptListener);
    }

    /**
     * ��ȡ���ݰ�����ϵͳ
     * @return result
     */
    public PBHandlerManager getHandlerManager(){
        return handlerManager;
    }

    /**
     * ��ȡ���ݰ�������
     * @return result
     */
    public PacketFormatter getFormatter(){
        return formatter;
    }

    /**
     * ��ȡ���������õķǶԳƼ����㷨
     * @return result
     */
    public String getAsymAlgorithm() {
        return asymAlgorithm;
    }

    /**
     * ��ȡ���������õĶԳƼ����㷨
     * @return result
     */
    public String getSymAlgorithm() {
        return symAlgorithm;
    }

    /**
     * ��һ���ͻ��˶���
     * û������IO�쳣������Ϊ���ܲ�����������,�ӳ�ָ��������ֱ�Ӷ���(Ϊ�˷�ֹ���ͻ��˷�������֪ͨ�����ʱ��δ����)
     * ����,�������ԭ����SERVER_CUSTOM��,����д�϶��ߵĽ���˵�����͸��ͻ���
     * ������������ֱ�Ӷ���,û����ʱ
     * �������ԭ����SERVER_CLOSE,����ͻ��˷���PBPacketDisconn���ݰ�,�����IO�쳣��ֻ���ڲ�����һ�����ݰ��¼�
     * @param client �ͻ���
     * @param reason ����ԭ��
     * @param description ���ߵ�һЩ����˵��
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
     * �رշ�����
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
     * ���������ӵĿͻ���
     * @return result
     */
    public Collection<PBClient> allClients(){
        return clients.values();
    }

}
