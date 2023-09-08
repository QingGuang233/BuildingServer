package com.qing_guang.PacketBasedTCP.client.handler.def;

import com.qing_guang.PacketBasedTCP.client.PBConnection;
import com.qing_guang.PacketBasedTCP.client.handler.PBPacketHandler;
import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketDisconn;

/**
 * 断线数据包处理器
 */
public class PBDisconnHandler implements PBPacketHandler {

    private PBConnection conn;

    /** default */
    public PBDisconnHandler(){}

    /**
     * 创建一个此处理器
     * @param conn 对应的连接
     */
    public PBDisconnHandler(PBConnection conn){
        this.conn = conn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(PBPacket packet, boolean in) {
        if(in){
            PBPacketDisconn pkt = (PBPacketDisconn) packet;
            conn.disconn(pkt.getReason(),pkt.getDescription());
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PBHandlePriority priority() {
        return PBHandlePriority.MONITOR;
    }

}
