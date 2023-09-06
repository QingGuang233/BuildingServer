package com.qing_guang.PacketBasedTCP.server.handler.def;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketDisconn;
import com.qing_guang.PacketBasedTCP.server.PBClient;
import com.qing_guang.PacketBasedTCP.server.handler.PBPacketHandler;

/**
 * �������ݰ�������
 */
public class PBDisconnHandler implements PBPacketHandler {

    /** ����ģʽ,ʵ�� */
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
