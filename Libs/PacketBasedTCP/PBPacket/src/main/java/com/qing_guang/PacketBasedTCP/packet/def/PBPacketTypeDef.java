package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketTypeInfo;

/**
 * ��API�Դ������ݰ�����,��������һЩ��������ݰ�,��������л�����Կ�����ݰ�
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
