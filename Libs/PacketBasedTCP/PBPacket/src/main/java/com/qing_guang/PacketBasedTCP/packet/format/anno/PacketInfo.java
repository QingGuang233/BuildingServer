package com.qing_guang.PacketBasedTCP.packet.format.anno;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据包的信息,包括名称和所属的 {@code PBPacketType}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketInfo {
    /**
     * 数据包名称,数据传输时作为标识
     * @return result
     */
    String name();
    /**
     * 此类型包含的数据包
     * @return result
     */
    Class<? extends PBPacketType> belongsTo();
}
