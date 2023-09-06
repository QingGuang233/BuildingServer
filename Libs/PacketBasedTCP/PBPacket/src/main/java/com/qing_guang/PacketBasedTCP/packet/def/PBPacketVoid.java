package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * 当有某些未知情况或需要发送心跳包时可以使用此数据包
 */
@PacketInfo(name = "VOID",belongsTo = PBPacketTypeDef.class)
public class PBPacketVoid extends PBPacket {

    /** default */
    public PBPacketVoid(){}

}
