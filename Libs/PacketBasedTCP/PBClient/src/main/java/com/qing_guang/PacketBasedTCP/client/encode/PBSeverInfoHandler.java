package com.qing_guang.PacketBasedTCP.client.encode;

import com.qing_guang.PacketBasedTCP.client.PBConnection;
import com.qing_guang.PacketBasedTCP.client.handler.PBPacketHandler;
import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketAsym;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketServerInfo;
import com.qing_guang.PacketBasedTCP.packet.def.formatter.PublicKeyFormatter;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 * ���շ������������Ϣ
 */
public class PBSeverInfoHandler implements PBPacketHandler {

    private PBConnection conn;
    private PBPacketHandlerEn handlerEn;

    /**
     * ����һ���˴�����
     * @param conn ��Ӧ������
     * @param handlerEn ���������ڼ����� PBPacketHandlerEn
     */
    public PBSeverInfoHandler(PBConnection conn, PBPacketHandlerEn handlerEn){
        this.conn = conn;
        this.handlerEn = handlerEn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(PBPacket packet, boolean in) {
        PBPacketServerInfo pkt = (PBPacketServerInfo) packet;
        if(pkt.isForced()){
            handlerEn.helper.asymAlgorithm = pkt.getAsymAlgorithm();
            handlerEn.helper.symAlgorithm = pkt.getSymAlgorithm();
            KeyPair pair = handlerEn.helper.genAsym();
            handlerEn.temp = pair.getPrivate();
            try {
                conn.getFormatter().addDataFormatter(PublicKey.class,new PublicKeyFormatter(pkt.getAsymAlgorithm()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            conn.sendPacket(new PBPacketAsym(pair.getPublic()));
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PBHandlePriority priority() {
        return PBHandlePriority.DEFAULT;
    }

}
