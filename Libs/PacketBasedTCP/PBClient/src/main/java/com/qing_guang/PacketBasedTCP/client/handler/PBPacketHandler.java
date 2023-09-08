package com.qing_guang.PacketBasedTCP.client.handler;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;

/**
 * 代表了一个数据包监听器
 */
public interface PBPacketHandler {

    /**
     * 数据包事件
     * @param packet 数据包
     * @param in true为写入,false为写入
     */
    boolean accept(PBPacket packet, boolean in);

    /**
     * 此监听器的优先级
     * 强烈建议此方法返回的优先级始终不变,否则会发生意外后果
     * @return result
     */
    default PBHandlePriority priority(){
        return PBHandlePriority.DEFAULT;
    }

}
