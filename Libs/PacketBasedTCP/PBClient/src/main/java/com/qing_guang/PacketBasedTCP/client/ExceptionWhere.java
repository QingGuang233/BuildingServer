package com.qing_guang.PacketBasedTCP.client;

/**
 * �����쳣�ĳ���λ��
 */
public enum ExceptionWhere {

    /**
     * �����ӷ�����ʱ
     */
    CONNECTING,
    /**
     * �ڴ�Channel�ж�ȡ����ʱ
     */
    READ,
    /**
     * ����Channel��д������ʱ
     */
    WRITE

}
