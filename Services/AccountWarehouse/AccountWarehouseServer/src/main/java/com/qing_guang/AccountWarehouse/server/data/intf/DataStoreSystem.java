package com.qing_guang.AccountWarehouse.server.data.intf;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

/**
 * 数据库接口
 */
public interface DataStoreSystem {

    /**
     * 开启数据库服务
     * @throws IOException 当开启出现IO问题时抛出
     */
    void start() throws IOException;

    /**
     * 关闭数据库服务
     *  @throws IOException 当关闭出现IO问题时抛出
     */
    void close() ;

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
     * 将新注册的服务在数据库中记录
     * @param name 服务名称
     * @param adminPwd 此服务的管理员密码(hash前)
     * @param authOrAcc 此服务使用的登录系统是认证系统(true,指只需要密码登录)还是账号系统(false,指需要账户名和密码)
     * @return 新注册的服务
     */
    ServiceData deleteServiceData(
            String name,
            String adminPwd,
            boolean authOrAcc
    );

    /**
     * 在数据库中删除服务占据的数据
     * @param name 服务的名称
     */
    void removeServiceData(String name);

    /**
     * 通过uid获取服务的数据
     * @param uid 服务的uid
     * @return result
     */
    ServiceData getService(UUID uid);

    /**
     * 通过uid获取临时认证令牌,若数据库中未检索到令牌信息则返回null
     * @param uid 认证令牌的uid
     * @return result
     */
    AccountToken getToken(UUID uid);

    /**
     * 获取数据库中所有还留存在记录的(包括已失效的)认证令牌
     * @return result
     */
    Collection<AccountToken> allTokens();

}
