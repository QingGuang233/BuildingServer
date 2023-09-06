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
 * ����һ������
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
     * �½�һ������
     * @param ip ��������ַ
     * @param port �˿ں�
     */
    public PBConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * ��������
     * @throws IOException ���ӳ�ʼ�����쳣ʱ�׳�
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
     * ���һ�����ݰ�������
     * @param analyser ���ݰ�������
     * @param packetIDs �˽��������Խ��������ݰ�ID
     */
    public void addPacketAnalyser(PBPacketAnalyser analyser,int... packetIDs){
        for(int packetID : packetIDs){
            if(!analysers.containsKey(packetID)){
                analysers.put(packetID,analyser);
            }
        }
    }

    /**
     * ɾ��һ�����ݰ�ID��Ӧ�Ľ�����
     * @param packetID ���ݰ�ID
     */
    public void removePacketAnalyser(int packetID){
        analysers.remove(packetID);
    }

    /**
     * ��Channel�ж�ȡһ�����ݰ�(��û�����ݴ���ʱ�������κβ���)
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
     * �Ͽ��������������
     * @param reason ����ԭ��
     * @param description ���ߵ�һЩ����˵��
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
     * �����������һ�����ݰ�
     * @param packet ���ݰ�
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
     * ��ȡ������ʹ�õ�channel
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
