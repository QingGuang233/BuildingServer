package com.qing_guang.AccountWarehouse.packet.server;

import com.qing_guang.AccountWarehouse.packet.AWPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketTypeInfo;

/**
 * AccountWarehouse����ͨ�ŷ�������ݰ�������
 */
@PacketTypeInfo(
        name = "ACCOUNT_WAREHOUSE_SERVER",
        includes = {
                ConnectionResponsePacket.class,
                CommunicateRefusePacket.class
        }
)
public class AWServerPacketType extends AWPacketType {
}
