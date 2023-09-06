package com.qing_guang.PacketBasedTCP.server.encode;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketAsym;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketDisconn;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketSym;
import com.qing_guang.PacketBasedTCP.server.PBClient;
import com.qing_guang.PacketBasedTCP.server.handler.PBPacketHandler;

/**
 * �������Կ����ǰ�ͷ����������ݰ���û�а����̽�����Կ����ʹ�ͻ���ǿ�ƶ���
 */
public class PBPacketHandlerProt implements PBPacketHandler {

    private final PBPacketHandlerEn handler;

    /**
     * �½�һ��ʵ��
     * @param handler �����������ڼ����� PBPacketHandlerEn
     */
    public PBPacketHandlerProt(PBPacketHandlerEn handler){
        this.handler = handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PBHandlePriority priority() {
        return PBHandlePriority.LOWEST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(PBClient client, PBPacket packet, boolean in) {
        if(!in || !client.getServer().isForceEncode())
            return;
        EncodeHelper helper = handler.helper;
        if(packet instanceof PBPacketSym && !helper.asymPubs.containsKey(client)){
            client.getServer().disconn(
                    client, PBPacketDisconn.DisconnReason.ENCRYPT,
                    "Unsafe process of encryption: asymmetric encryption should be available first"
            );
        }else if(
                !(packet instanceof PBPacketSym) && !(packet instanceof PBPacketAsym) &&
                        (!helper.asymPubs.containsKey(client) || !helper.symKey.containsKey(client))
        ){
            client.getServer().disconn(client, PBPacketDisconn.DisconnReason.ENCRYPT, "Encryption process isn't accomplished");
        }
    }

}
