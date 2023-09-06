package com.qing_guang.PacketBasedTCP.server;

import java.io.IOException;

/**
 * ���紫���쳣������
 */
public interface PBExceptionHandler {

    /**
     * �����쳣����ʱ�׳�
     * @param exception �쳣����
     * @param where �쳣�ĳ���λ��
     * @param who ����˭�����Ӳ����г����쳣(�� {@code where == ExceptionWhere.ACCEPT} ʱ�˲���Ϊnull)
     */
    void handle(IOException exception, ExceptionWhere where, PBClient who);

}
