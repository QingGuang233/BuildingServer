package com.qing_guang.PacketBasedTCP.packet;

import java.util.function.BiFunction;

/**
 * 数据包解析器
 */
public interface PBPacketAnalyser extends BiFunction<Integer,byte[],PBPacket> {
}
