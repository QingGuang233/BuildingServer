package com.qing_guang.AccountWarehouse.server.data.intf;

/**
 * ���·���ע��ʧ��ʱ�׳�
 */
public class ServiceCreateFailedException extends Exception{

    /**
     * {@inheritDoc}
     */
    public ServiceCreateFailedException(String reason){
        super(reason);
    }

}
