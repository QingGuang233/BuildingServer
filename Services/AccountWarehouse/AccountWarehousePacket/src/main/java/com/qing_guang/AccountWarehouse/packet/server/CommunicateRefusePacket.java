package com.qing_guang.AccountWarehouse.packet.server;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * ��δ�������˵�������ӵ���;ʱ���Է����κ����ݰ�������������ʹ����ݰ�
 */
@PacketInfo(name = "COMMUNICATE_REFUSE",belongsTo = AWServerPacketType.class)
public class CommunicateRefusePacket extends PBPacket {
}
