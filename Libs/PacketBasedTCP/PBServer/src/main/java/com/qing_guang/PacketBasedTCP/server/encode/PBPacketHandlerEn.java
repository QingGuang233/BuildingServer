package com.qing_guang.PacketBasedTCP.server.encode;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketAsym;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketSym;
import com.qing_guang.PacketBasedTCP.server.PBClient;
import com.qing_guang.PacketBasedTCP.server.handler.PBPacketHandler;

import java.security.KeyPair;

/**
 * 加密相关的数据包监听器
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
     * 根据当前已获得的密钥加密信息
     * @param original 原数据
     * @param client 对应的客户端
     * @return result
     */
    public byte[] encode(byte[] original, PBClient client){
        return helper.encode(original,client);
    }

    /**
     * 根据当前已获得的密钥解密信息
     * @param original 原数据
     * @param client 对应的客户端
     * @return result
     */
    public byte[] decode(byte[] original, PBClient client){
        return helper.decode(original,client);
    }

}
