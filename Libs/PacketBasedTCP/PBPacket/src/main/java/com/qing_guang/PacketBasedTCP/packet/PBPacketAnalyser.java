package com.qing_guang.PacketBasedTCP.packet;

import java.util.function.BiFunction;

/**
 * ���ݰ�������
 */
public interface PBPacketAnalyser extends BiFunction<Integer,byte[],PBPacket> {
}
