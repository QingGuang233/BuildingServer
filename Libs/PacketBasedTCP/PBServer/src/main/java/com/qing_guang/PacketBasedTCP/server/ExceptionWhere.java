package com.qing_guang.PacketBasedTCP.server;

/**
 * �����쳣�ĳ���λ��
 */
public enum ExceptionWhere {

    /**
     * �ڽ�����������ʱ
     */
    ACCEPT,
    /**
     * �ڴ�Channel�ж�ȡ����ʱ
     */
    READ,
    /**
     * ����Channel��д������ʱ
     */
    WRITE
}
