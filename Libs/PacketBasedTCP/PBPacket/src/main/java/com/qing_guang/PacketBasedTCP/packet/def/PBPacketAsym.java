package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

import java.security.PublicKey;

/**
 * 传输非对称加密密钥的公钥的数据包
 */
@PacketInfo(name = "ASYM",belongsTo = PBPacketTypeDef.class)
public class PBPacketAsym extends PBPacket {

    @DataField(fieldID = 0)
    private PublicKey key;

    /** default */
    public PBPacketAsym(){}

    /**
     * 新建一个此数据包
     * @param key 公钥
     */
    public PBPacketAsym(PublicKey key){
        this.key = key;
    }

    /**
     * 获取公钥
     * @return result
     */
    public PublicKey getKey() {
        return key;
    }

}
