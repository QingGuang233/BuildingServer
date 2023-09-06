package com.qing_guang.PacketBasedTCP.client;

import com.qing_guang.PacketBasedTCP.client.encode.PBSeverInfoHandler;
import com.qing_guang.PacketBasedTCP.client.encode.PBPacketHandlerEn;
import com.qing_guang.PacketBasedTCP.client.handler.PBHandlerManager;
import com.qing_guang.PacketBasedTCP.client.handler.def.PBDisconnHandler;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketAnalyser;
import com.qing_guang.PacketBasedTCP.packet.def.*;
import com.qing_guang.PacketBasedTCP.packet.format.PacketFormatter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * 代表一个连接
 */
public class PBConnection {

    private String ip;
    private SocketChannel channel;
    private int port;

    private Selector receive;
    private PBHandlerManager handlerManager;
    private Map<Integer, PBPacketAnalyser> analysers;
    private PBPacketHandlerEn handlerEn;
    private PacketFormatter formatter;

    private final ByteBuffer lengthBuffer = ByteBuffer.allocate(4);

    /** default */
    public PBConnection() {}

    /**
     * 新建一个连接
     * @param ip 服务器地址
     * @param port 端口号
     */
    public PBConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 建立连接
     * @throws IOException 连接初始化有异常时抛出
     */
    public void start() throws IOException{

        channel = SocketChannel.open(new InetSocketAddress(ip,port));
        receive = Selector.open();
        handlerManager = new PBHandlerManager();
        analysers = new HashMap<>();
        handlerEn = new PBPacketHandlerEn();
        formatter = new PacketFormatter();

        channel.configureBlocking(false);
        channel.register(receive, SelectionKey.OP_READ);
        handlerManager.addPacketHandler(new PBDisconnHandler(this),PBPacketDisconn.class);

        handlerManager.addPacketHandler(handlerEn = new PBPacketHandlerEn(this), PBPacketAsym.class, PBPacketSym.class);
        handlerManager.addPacketHandler(new PBSeverInfoHandler(this,handlerEn), PBPacketServerInfo.class);

        while(!channel.finishConnect());

    }

    /**
     * 添加一个数据包解析器
     * @param analyser 数据包解析器
     * @param packetIDs 此解析器可以解析的数据包ID
     */
    public void addPacketAnalyser(PBPacketAnalyser analyser,int... packetIDs){
        for(int packetID : packetIDs){
            if(!analysers.containsKey(packetID)){
                analysers.put(packetID,analyser);
            }
        }
    }

    /**
     * 删除一个数据包ID对应的解析器
     * @param packetID 数据包ID
     */
    public void removePacketAnalyser(int packetID){
        analysers.remove(packetID);
    }

    /**
     * 从Channel中读取一个数据包(还没有数据传入时不进行任何操作)
     */
    public void read(){
        try {
            if(receive.selectNow() != 0){
                read0(channel,lengthBuffer);
                int length = formatter.getIntFormatter().toData(lengthBuffer.array(),0,4);
                ByteBuffer packetDataBuffer = ByteBuffer.allocate(length);
                read0(channel,packetDataBuffer);
                byte[] packetData = packetDataBuffer.array();
                packetData = handlerEn.decode(packetData);
                handlerManager.handle(formatter.toPacket(packetData),true);
                lengthBuffer.clear();
                receive.selectedKeys().clear();
            }
        }catch (IOException e) {
            disconn(PBPacketDisconn.DisconnReason.IO_ERROR_READ,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 断开与服务器的连接
     * @param reason 断线原因
     * @param description 断线的一些解释说明
     */
    public void disconn(PBPacketDisconn.DisconnReason reason,String description){
        try {
            if(reason.equals(PBPacketDisconn.DisconnReason.CLIENT_CUSTOM)){
                sendPacket(new PBPacketDisconn(reason,description));
                channel.close();
            }else{
                if(reason.equals(PBPacketDisconn.DisconnReason.IO_ERROR_READ) || reason.equals(PBPacketDisconn.DisconnReason.IO_ERROR_WRITE)){
                    handlerManager.handle(new PBPacketDisconn(reason,description),false);
                }
                channel.close();
            }
        } catch (IOException ignored) {}
    }

    /**
     * 向服务器发送一个数据包
     * @param packet 数据包
     */
    public void sendPacket(PBPacket packet){
        try {
            formatter.toBytes(packet).write(channel,(original) -> handlerEn.encode(original));
            handlerManager.handle(packet,false);
        } catch (IOException e) {
            disconn(PBPacketDisconn.DisconnReason.IO_ERROR_WRITE,null);
        }
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
     * 获取此连接使用的channel
     * @return channel
     */
    public SocketChannel getChannel(){
        return channel;
    }

    private void read0(SocketChannel channel, ByteBuffer buffer) throws IOException{
        while(buffer.hasRemaining()){
            channel.read(buffer);
        }
    }

}
