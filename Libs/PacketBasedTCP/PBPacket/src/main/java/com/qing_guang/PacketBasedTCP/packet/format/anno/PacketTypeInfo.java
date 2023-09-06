package com.qing_guang.PacketBasedTCP.packet.format.anno;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据包类型的信息,包括名称和所有所属的 {@code PBPacket}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketTypeInfo {
    /**
     * 类型名称,数据传输时作为标识
     * @return result
     */
    String name();
    /**
     * 此类型包含的数据包
     * @return result
     */
    Class<? extends PBPacket>[] includes();
}
