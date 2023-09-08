package com.qing_guang.AccountWarehouse.packet.server;

import com.qing_guang.AccountWarehouse.packet.AWPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketTypeInfo;

/**
 * AccountWarehouse网络通信服务端数据包总类型
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
