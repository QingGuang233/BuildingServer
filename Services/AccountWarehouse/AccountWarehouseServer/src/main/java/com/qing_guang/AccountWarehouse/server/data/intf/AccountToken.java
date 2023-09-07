package com.qing_guang.AccountWarehouse.server.data.intf;

/**
 * ��ʱ��֤����,���ڵ�¼������֤ʹ��,�����ѹ��ڵ����ƴ�����ʵ���ڲ��Զ���
 */
public interface AccountToken {

    /**
     * ��ȡ�����ƶ�Ӧ���˻�
     * @return result
     */
    AccountData getAccount();

    /**
     * ��ȡ���������ɵ�ʱ���(��λms)
     * @return result
     */
    long generateTime();

    /**
     * ����true�� {@code vaildTime()} ��ʾ������Ч�ڽ�ֹ��ʱ��(ʱ���),�����ʾ�����뿪ʼʱ���Ƶ���Ч��
     * @return result
     */
    boolean ddlOrFixt();

    /**
     * ���ش����Ƶ���Ч��(��ֹʱ��)(��λms)
     * @return result
     */
    long vaildTime();

}
