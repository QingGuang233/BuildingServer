package com.qing_guang.PacketBasedTCP.server;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketDisconn;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * �ͻ���
 */
public class PBClient {

    ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
    ByteBuffer packetDataBuffer;
    int len = -1;

    boolean isOnline = true;
    private PBServer server;
    private SocketChannel channel;
    private UUID uid;


    /** default */
    public PBClient(){}

    /**
     * �½�һ���ͻ��˶���
     * @param server �˿ͻ��˶�Ӧ�ķ���������
     * @param channel ����
     * @param uid �ͻ���UUID
     */
    public PBClient(PBServer server,SocketChannel channel,UUID uid){
        this.server = server;
        this.channel = channel;
        this.uid = uid;
    }

    /**
     * ��ȡ�˿ͻ��˶�Ӧ�ķ���������
     * @return result
     */
    public PBServer getServer(){
        return server;
    }

    /**
     * ��ȡ�˿ͻ��˶�Ӧ������
     * @return result
     */
    public SocketChannel getChannel(){
        return channel;
    }

    /**
     * ��ȡ�˿ͻ��˵�UUID
     * @return result
     */
    public UUID getUUID(){
        return uid;
    }

    /**
     * ��˿ͻ��˷���һ�����ݰ�
     * @param packet ���ݰ�
     */
    public void sendPacket(PBPacket packet){
        try {
            server.formatter.toBytes(packet).write(channel,(original) -> server.handlerEn.encode(original,this));
            server.handlerManager.handle(this,packet,false);
        } catch (IOException e) {
            if(server.allClients().contains(this)){
                server.disconn(this, PBPacketDisconn.DisconnReason.IO_ERROR_WRITE,null);
            }
        }
    }

    /**
     * �˿ͻ����Ƿ�����
     * @return result
     */
    public boolean isOnline(){
        return isOnline;
    }

}
