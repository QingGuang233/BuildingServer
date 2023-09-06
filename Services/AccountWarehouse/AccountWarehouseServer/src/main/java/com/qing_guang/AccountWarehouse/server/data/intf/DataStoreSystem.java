package com.qing_guang.AccountWarehouse.server.data.intf;

import java.util.UUID;

/**
 * 数据库接口
 */
public interface DataStoreSystem {

    /**
     * 检查数据库中是否有指定uid的服务已被注册
     * @param id 指定的uid
     * @return result
     */
    boolean hasService(UUID id);

    /**
     * 检查数据库中是否有指定名称的服务已被注册
     * @param name 指定的名称
     * @return result
     */
    boolean hasService(String name);

    /**
     * 新注册一个服务
     * @param name 服务名称
     * @param adminPwd 此服务的管理员密码
     * @param authOrAcc 此服务使用的登录系统是认证系统(true,指只需要密码登录)还是账号系统(false,指需要账户名和密码)
     * @return 新注册的服务
     * @throws ServiceCreateFailedException 当新服务注册失败时抛出
     */
    UUID createService(String name, String adminPwd, boolean authOrAcc) throws ServiceCreateFailedException;

    /**
     * 注销服务,若服务此时为正在使用状态应视为注销失败
     * @param name 服务的名称
     * @throws ServiceCreateFailedException 当服务注销失败时抛出
     */
    void unregisterService(String name);

    /**
     * 注销服务,若服务此时为正在使用状态应视为注销失败
     * @param uid 服务的uid
     * @throws ServiceCreateFailedException 当服务注销失败时抛出
     */
    void unregisterService(UUID uid);

    /**
     * 通过名称获取服务
     * @param name 服务的名称
     * @return result
     */
    ServiceData getService(String name);

    /**
     * 通过uid获取服务
     * @param uid 服务的uid
     * @return result
     */
    ServiceData getService(UUID uid);

}
