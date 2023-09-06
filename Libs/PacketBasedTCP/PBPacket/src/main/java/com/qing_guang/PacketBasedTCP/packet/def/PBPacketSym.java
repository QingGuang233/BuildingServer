package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * ����ԳƼ�����Կ�����ݰ�
 */
@PacketInfo(name = "SYM",belongsTo = PBPacketTypeDef.class)
public class PBPacketSym extends PBPacket {

    @DataField(fieldID = 0)
    private byte[] key;

    /** default */
    public PBPacketSym(){}

    /**
     * �½�һ�����ݰ�
     * @param key �ԳƼ��ܵ���Կ
     */
    public PBPacketSym(byte[] key){
        this.key = key;
    }

    /**
     * ��ȡ�����ݰ��д������Կ
     * @return result
     */
    public byte[] getKey() {
        return key;
    }

}