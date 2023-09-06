package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketTypeInfo;

/**
 * 此API自带的数据包类型,用来标明一些必需的数据包,比如加密中互换密钥的数据包
 */
@PacketTypeInfo(
        name = "DEF",
        includes = {
                PBPacketVoid.class,
                PBPacketAsym.class,
                PBPacketSym.class,
                PBPacketServerInfo.class,
                PBPacketDisconn.class
        }
)
public class PBPacketTypeDef extends PBPacketType {
}
