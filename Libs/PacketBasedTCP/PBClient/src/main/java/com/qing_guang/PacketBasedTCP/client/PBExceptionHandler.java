package com.qing_guang.PacketBasedTCP.client;

import java.io.IOException;

/**
 * ���紫���쳣������
 */
public interface PBExceptionHandler {

    /**
     * �����쳣����ʱ�׳�
     * @param exception �쳣����
     * @param where �쳣�ĳ���λ��
     */
    void handle(IOException exception, ExceptionWhere where);

}
