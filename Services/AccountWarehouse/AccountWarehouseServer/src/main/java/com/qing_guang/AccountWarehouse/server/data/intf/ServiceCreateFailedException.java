package com.qing_guang.AccountWarehouse.server.data.intf;

/**
 * 当新服务注册失败时抛出
 */
public class ServiceCreateFailedException extends Exception{

    /**
     * {@inheritDoc}
     */
    public ServiceCreateFailedException(String reason){
        super(reason);
    }

}
