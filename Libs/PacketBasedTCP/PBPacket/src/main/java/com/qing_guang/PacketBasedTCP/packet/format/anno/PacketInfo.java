package com.qing_guang.PacketBasedTCP.packet.format.anno;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ���ݰ�����Ϣ,�������ƺ������� {@code PBPacketType}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketInfo {
    /**
     * ���ݰ�����,���ݴ���ʱ��Ϊ��ʶ
     * @return result
     */
    String name();
    /**
     * �����Ͱ��������ݰ�
     * @return result
     */
    Class<? extends PBPacketType> belongsTo();
}
