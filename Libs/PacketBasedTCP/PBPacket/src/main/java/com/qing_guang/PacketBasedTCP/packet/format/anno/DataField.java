package com.qing_guang.PacketBasedTCP.packet.format.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识类内需要打包发送的字段(若发送时此字段的数据为null则跳过该数据)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataField {
    /**
     * 此数据的标识ID,只要不与当前类和它的所有父类的其他字段id重复即可
     * @return result
     */
    int fieldID();
}
