package com.qing_guang.PacketBasedTCP.packet.format.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识此类无视对应的 {@code DataFormatter} ,只被当作是类内一些字段的集合,包含在此集合内的字段应当被 {@code DataField} 注释修饰
 * 若在字段上添加,则此字段的类型在当前数据包内被看作为被此注释修饰
 * 注意,应当注意避免两个或多个类中同时包含互相的实例,否则会陷入死循环
 * 也不要试图添加在数组字段上,这样做除了让控制台里多几条报错以外什么也不会发生
 */
@Target(value = {ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContainerOnly {
}
