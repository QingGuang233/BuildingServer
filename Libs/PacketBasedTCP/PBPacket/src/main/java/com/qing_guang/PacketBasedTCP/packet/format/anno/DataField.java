package com.qing_guang.PacketBasedTCP.packet.format.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��ʶ������Ҫ������͵��ֶ�(������ʱ���ֶε�����Ϊnull������������)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataField {
    /**
     * �����ݵı�ʶID,ֻҪ���뵱ǰ����������и���������ֶ�id�ظ�����
     * @return result
     */
    int fieldID();
}
