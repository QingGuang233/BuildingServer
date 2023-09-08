package com.qing_guang.PacketBasedTCP.packet.format.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��ʶ�������Ӷ�Ӧ�� {@code DataFormatter} ,ֻ������������һЩ�ֶεļ���,�����ڴ˼����ڵ��ֶ�Ӧ���� {@code DataField} ע������
 * ���б� {@code DataField} ���ε��ֶα��������µ����ݼ�����,���Բ��õ��� {@code DataField.fieldID()} ������һ���г�ͻ
 * �����ֶ�������,����ֶε������ڵ�ǰ���ݰ��ڱ�����Ϊ����ע������
 * ע��,Ӧ��ע�����������������ͬʱ���������ʵ��,�����������ѭ��
 * Ҳ��Ҫ��ͼ�����������ֶ���,�����������ÿ���̨��༸����������ʲôҲ���ᷢ��
 */
@Target(value = {ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContainerOnly {
}