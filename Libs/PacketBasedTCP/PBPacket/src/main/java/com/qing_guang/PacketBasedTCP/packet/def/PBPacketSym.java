package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * 传输对称加密密钥的数据包
 */
@PacketInfo(name = "SYM",belongsTo = PBPacketTypeDef.class)
public class PBPacketSym extends PBPacket {

    @DataField(fieldID = 0)
    private byte[] key;

    /** default */
    public PBPacketSym(){}

    /**
     * 新建一个数据包
     * @param key 对称加密的密钥
     */
    public PBPacketSym(byte[] key){
        this.key = key;
    }

    /**
     * 获取此数据包中储存的密钥
     * @return result
     */
    public byte[] getKey() {
        return key;
    }

}