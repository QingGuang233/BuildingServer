package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

import java.security.PublicKey;

/**
 * ����ǶԳƼ�����Կ�Ĺ�Կ�����ݰ�
 */
@PacketInfo(name = "ASYM",belongsTo = PBPacketTypeDef.class)
public class PBPacketAsym extends PBPacket {

    @DataField(fieldID = 0)
    private PublicKey key;

    /** default */
    public PBPacketAsym(){}

    /**
     * �½�һ�������ݰ�
     * @param key ��Կ
     */
    public PBPacketAsym(PublicKey key){
        this.key = key;
    }

    /**
     * ��ȡ��Կ
     * @return result
     */
    public PublicKey getKey() {
        return key;
    }

}
