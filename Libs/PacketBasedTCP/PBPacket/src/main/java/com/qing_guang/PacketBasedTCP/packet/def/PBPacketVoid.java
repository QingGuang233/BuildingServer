package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * ����ĳЩδ֪�������Ҫ����������ʱ����ʹ�ô����ݰ�
 */
@PacketInfo(name = "VOID",belongsTo = PBPacketTypeDef.class)
public class PBPacketVoid extends PBPacket {

    /** default */
    public PBPacketVoid(){}

}
