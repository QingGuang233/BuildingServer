package com.qing_guang.AccountWarehouse.packet.server;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * 当未向服务器说明此连接的用途时尝试发送任何数据包后服务器将发送此数据包
 */
@PacketInfo(name = "COMMUNICATE_REFUSE",belongsTo = AWServerPacketType.class)
public class CommunicateRefusePacket extends PBPacket {
}
