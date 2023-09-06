package com.qing_guang.PacketBasedTCP.server.encode;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketAsym;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketSym;
import com.qing_guang.PacketBasedTCP.server.PBClient;
import com.qing_guang.PacketBasedTCP.server.handler.PBPacketHandler;

import java.security.KeyPair;

/**
 * ������ص����ݰ�������
 */
public class PBPacketHandlerEn implements PBPacketHandler {

    EncodeHelper helper = new EncodeHelper();

    /** default */
    public PBPacketHandlerEn(){}

    public PBPacketHandlerEn(String asymAlgorithm,String symAlgorithm){
        helper.asymAlgorithm = asymAlgorithm;
        helper.symAlgorithm = symAlgorithm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(PBClient client, PBPacket packet, boolean in) {
        if(!in)
            return;
        if (packet instanceof PBPacketAsym){
            KeyPair pair = helper.genAsym();
            client.sendPacket(new PBPacketAsym(pair.getPublic()));
            helper.asymPubs.put(client,((PBPacketAsym)packet).getKey());
            helper.asymPris.put(client,pair.getPrivate());
        }else{
            helper.symKey.put(client,((PBPacketSym)packet).getKey());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PBHandlePriority priority() {
        return PBHandlePriority.DEFAULT;
    }

    /**
     * ���ݵ�ǰ�ѻ�õ���Կ������Ϣ
     * @param original ԭ����
     * @param client ��Ӧ�Ŀͻ���
     * @return result
     */
    public byte[] encode(byte[] original, PBClient client){
        return helper.encode(original,client);
    }

    /**
     * ���ݵ�ǰ�ѻ�õ���Կ������Ϣ
     * @param original ԭ����
     * @param client ��Ӧ�Ŀͻ���
     * @return result
     */
    public byte[] decode(byte[] original, PBClient client){
        return helper.decode(original,client);
    }

}
