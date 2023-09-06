package com.qing_guang.PacketBasedTCP.server.handler.def;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketDisconn;
import com.qing_guang.PacketBasedTCP.server.PBClient;
import com.qing_guang.PacketBasedTCP.server.handler.PBPacketHandler;

/**
 * 断线数据包处理器
 */
public class PBDisconnHandler implements PBPacketHandler {

    /** 单例模式,实例 */
    public static final PBDisconnHandler INSTANCE = new PBDisconnHandler();

    private PBDisconnHandler(){}

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(PBClient client, PBPacket packet, boolean in) {
        if(in){
            PBPacketDisconn pkt = (PBPacketDisconn) packet;
            client.getServer().disconn(client, pkt.getReason(),pkt.getDescription());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PBHandlePriority priority() {
        return PBHandlePriority.HIGHEST;
    }

}
