package com.qing_guang.AccountWarehouse.server.data.intf;

/**
 * ������ע��ʧ��ʱ�׳�
 */
public class ServiceUnregisterFailedException extends Exception{

    /**
     * {@inheritDoc}
     */
    public ServiceUnregisterFailedException(String reason){
        super(reason);
    }

}
