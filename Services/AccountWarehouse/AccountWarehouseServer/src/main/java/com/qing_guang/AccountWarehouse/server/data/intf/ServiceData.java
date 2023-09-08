package com.qing_guang.AccountWarehouse.server.data.intf;

import java.util.Collection;
import java.util.UUID;

/**
 * 服务的数据接口
 */
public interface ServiceData {

    /**
     * 获取此服务的名称
     * @return result
     */
    String getName();

    /**
     * 获取此服务uid
     * @return result
     */
    UUID getUUID();

    /**
     * 是否支持通过手机号码验证找回密码,默认为false,当此服务为认证模式时返回false
     * @return result
     */
    boolean isPhoneVeriOpen();

    /**
     * 设置是否支持手机号码验证找回密码,当 {@code isPhoneVeriOpen()} 返回false时调用此方法不会发生任何事
     */
    void setPhoneVeriOpen(boolean isOpen);

    /**
     * 是否强制要求账号需要绑定手机号码,当 {@code isPhoneVeriOpen()} 返回false时返回false
     * @return result
     */
    boolean isPhoneForced();

    /**
     * 设置是否强制要求账号需要绑定手机号码,当 {@code isPhoneVeriOpen()} 返回false时调用此方法不会发生任何事
     */
    void setPhoneForced(boolean isForced);

    /**
     * 通过手机号码找到账号数据
     * @param phoneNumber 手机号码
     * @return 找到的账号数据,如果此服务不支持手机号码验证或者此账号尚未绑定时返回null
     */
    AccountData getAccountByPhone(String phoneNumber);

    /**
     * 是否支持通过邮箱验证找回密码,默认为false
     * @return result
     */
    boolean isEmailVeriOpen();

    /**
     * 设置是否支持邮箱验证找回密码,当 {@code isEmailVeriOpen()} 返回false时调用此方法不会发生任何事
     */
    void setEmailVeriOpen(boolean isOpen);

    /**
     * 是否强制要求账号需要绑定邮箱,当 {@code isEmailVeriOpen()} 返回false时返回false
     * @return result
     */
    boolean isEmailForced();

    /**
     * 设置是否强制要求账号需要绑定邮箱,当 {@code isEmailVeriOpen()} 返回false时调用此方法不会发生任何事
     */
    void setEmailForced(boolean isForced);

    /**
     * 通过邮箱找到账号数据
     * @param email 手机号码
     * @return 找到的账号数据,如果此服务不支持手机号码验证或者此账号尚未绑定则返回null
     */
    AccountData getAccountByEmail(String email);

    /**
     * 通过用户名找到账号数据
     * @param username 用户名
     * @return 找到的账号数据,如果未找到返回null
     */
    AccountData getAccountByUsername(String username);

    /**
     * 创建一个账户
     * @param username 用户名
     * @param pwd 密码(hash前)
     * @param phoneNumber 此账号绑定的手机号码,若此服务为认证模式则填null,若此服务不强制填写则可以填null
     * @param email 此账号绑定的邮箱,若此服务为认证模式则填null,若此服务不强制填写则可以填null
     * @return result
     */
    AccountData createAccount(String username,String pwd,String phoneNumber,String email);

    /**
     * 生成一份指定账户的临时认证令牌
     * @param data 指定的账户
     * @param time 令牌有效(截止)的时间(单位均为ms)
     * @param ddlOrFixt 填true则参数 {@code time} 表示令牌有效期截止的时间(时间戳),否则表示从申请开始时令牌的有效期
     * @return result
     */
    AccountToken allocateAccToken(AccountData data, long time, boolean ddlOrFixt);

    /**
     * 获取数据库中此服务所有还留存在记录的(包括已失效的)认证令牌
     * @return result
     */
    Collection<AccountToken> allTokens();

    /**
     * 此服务是否允许同一个账户同时有多个可用的认证令牌
     * @return result
     */
    boolean isMultiTokenAllowed();

    /**
     * 设置此服务是否允许同一个账户同时有多个可用的认证令牌
     * @param isMultiTokenAllowed 此服务是否允许同一个账户同时有多个可用的认证令牌
     */
    void setMultiTokenAllowed(boolean isMultiTokenAllowed);

    /**
     * 获取服务中所有已注册的账户
     * @return result
     */
    Collection<AccountData> getAccounts();

    /**
     * 注销此服务
     */
    void removeData();

}
