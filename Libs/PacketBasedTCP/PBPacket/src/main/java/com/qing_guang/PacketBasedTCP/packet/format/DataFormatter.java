package com.qing_guang.PacketBasedTCP.packet.format;

/**
 * һ�����ݵĴ�����
 * @param <T> Ҫ�������������
 */
public interface DataFormatter<T>{

    /**
     * ��һ��byte�����е�һ��ת�ɶ�Ӧ������
     * @param ori byte����
     * @param start ��ʼ��ȡ��λ��
     * @param length ���ݳ���
     * @return result
     */
    T toData(byte[] ori,int start,int length);

    /**
     * ��һ������ת��һ��byte����
     * @param data ����������
     * @return �����洢����һ��λ��,������������������������
     */
    byte[] toBytes(Object data);

}
