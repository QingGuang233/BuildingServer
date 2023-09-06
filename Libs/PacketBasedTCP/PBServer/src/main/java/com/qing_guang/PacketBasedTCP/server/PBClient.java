package com.qing_guang.PacketBasedTCP.server;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketDisconn;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * 客户端
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
     * 新建一个客户端对象
     * @param server 此客户端对应的服务器对象
     * @param channel 链接
     * @param uid 客户端UUID
     */
    public PBClient(PBServer server,SocketChannel channel,UUID uid){
        this.server = server;
        this.channel = channel;
        this.uid = uid;
    }

    /**
     * 获取此客户端对应的服务器对象
     * @return result
     */
    public PBServer getServer(){
        return server;
    }

    /**
     * 获取此客户端对应的链接
     * @return result
     */
    public SocketChannel getChannel(){
        return channel;
    }

    /**
     * 获取此客户端的UUID
     * @return result
     */
    public UUID getUUID(){
        return uid;
    }

    /**
     * 向此客户端发送一个数据包
     * @param packet 数据包
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
     * 此客户端是否在线
     * @return result
     */
    public boolean isOnline(){
        return isOnline;
    }

}
