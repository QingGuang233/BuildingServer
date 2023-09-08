package com.qing_guang.AccountWarehouse.server.data.intf;

import java.util.Collection;
import java.util.UUID;

/**
 * 账号数据
 */
public interface AccountData {

    /**
     * 获取此账号对应的服务
     * @return result
     */
    ServiceData getService();

    /**
     * 获取此账号的uid
     * @return result
     */
    UUID getUUID();

    /**
     * 获取此账号的用户名
     * @return result
     */
    String getUserName();

    /**
     * 设置新的用户名
     * @param username 新的用户名
     */
    void setUserName(String username);

    /**
     * 获取此账号的手机号码,当所在服务不支持手机号码验证时返回null
     * @return result
     */
    String getPhoneNumber();

    /**
     * 设置此账号的手机号码,当所在服务不支持手机号码验证时调用此方法不会发生任何事
     * @param phoneNumber 手机号码
     */
    void setPhoneNumber(String phoneNumber);

    /**
     * 获取此账号的邮箱,当所在服务不支持邮箱验证时返回null
     * @return result
     */
    String getEmail();

    /**
     * 设置此账号的邮箱,当所在服务不支持邮箱验证时调用此方法不会发生任何事
     * @param email 邮箱
     */
    void setEmail(String email);

    /**
     * 获取此账号的密码(hash后)
     * @return result
     */
    String getPassword();

    /**
     * 设置此账号的密码(hash前)
     * @param pwd 密码
     */
    void setPassword(String pwd);

    /**
     * 生成一份此账户的临时认证令牌
     * @param time 令牌有效的时间(ms)
     * @param ddlOrFixt 填true则参数 {@code time} 表示令牌有效期截止的时间(时间戳),否则表示从申请开始时令牌的有效期
     * @return result
     */
    AccountToken allocateAccToken(long time,boolean ddlOrFixt);

    /**
     * 获取数据库中该账户所有还留存在记录的(包括已失效的)认证令牌
     * @return result
     */
    Collection<AccountToken> allTokens();

    /**
     * 获取该账户上次成功登录的时间戳
     * @return result
     */
    long lastLogin();

    /**
     * 获取该账户从上次成功登录后登录失败的次数
     * @return result
     */
    int failedTimes();

    /**
     * 设置该账户从上次成功登录后登录失败的次数
     */
    void setFailedTimes();

    /**
     * 注销账号
     */
    void unregister();

}
