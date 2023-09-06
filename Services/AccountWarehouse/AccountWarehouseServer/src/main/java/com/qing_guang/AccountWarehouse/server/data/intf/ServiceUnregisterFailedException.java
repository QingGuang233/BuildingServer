package com.qing_guang.AccountWarehouse.server.data.intf;

/**
 * 当服务注销失败时抛出
 */
public class ServiceUnregisterFailedException extends Exception{

    /**
     * {@inheritDoc}
     */
    public ServiceUnregisterFailedException(String reason){
        super(reason);
    }

}
