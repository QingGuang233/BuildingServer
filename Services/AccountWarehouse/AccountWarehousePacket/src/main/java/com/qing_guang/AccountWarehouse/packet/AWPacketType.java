package com.qing_guang.AccountWarehouse.packet;

import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketTypeInfo;

/**
 * AccountWarehouse网络通信数据包总类型
 */
@PacketTypeInfo(name = "ACCOUNT_WAREHOUSE",includes = {})
public class AWPacketType extends PBPacketType {
}
