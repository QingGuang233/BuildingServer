package com.qing_guang.AccountWarehouse.packet.client;

import com.qing_guang.AccountWarehouse.packet.AWPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketTypeInfo;

/**
 * AccountWarehouse����ͨ�ſͻ������ݰ�������
 */
@PacketTypeInfo(
        name = "ACCOUNT_WAREHOUSE_CLIENT",
        includes = ConnectionTypePacket.class
)
public class AWClientPacketType extends AWPacketType {
}
