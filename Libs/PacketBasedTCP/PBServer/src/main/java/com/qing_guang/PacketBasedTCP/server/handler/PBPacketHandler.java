package com.qing_guang.PacketBasedTCP.server.handler;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.server.PBClient;

/**
 * 代表了一个数据包监听器
 */
public interface PBPacketHandler {

    /**
     * 数据包事件
     * @param client 客户端
     * @param packet 数据包
     * @param in true为写入,false为写入
     * @return 此数据包是否应该交给剩下的监听器处理
     */
    boolean accept(PBClient client,PBPacket packet,boolean in);

    /**
     * 此监听器的优先级
     * 强烈建议此方法返回的优先级始终不变,否则会发生意外后果
     * @return result
     */
    default PBHandlePriority priority(){
        return PBHandlePriority.DEFAULT;
    }

}
