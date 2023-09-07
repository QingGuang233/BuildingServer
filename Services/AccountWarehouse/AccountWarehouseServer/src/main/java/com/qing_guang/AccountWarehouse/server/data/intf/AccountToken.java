package com.qing_guang.AccountWarehouse.server.data.intf;

/**
 * 临时认证令牌,用于登录后快捷认证使用,对于已过期的令牌处理由实现内部自定义
 */
public interface AccountToken {

    /**
     * 获取此令牌对应的账户
     * @return result
     */
    AccountData getAccount();

    /**
     * 获取此令牌生成的时间戳(单位ms)
     * @return result
     */
    long generateTime();

    /**
     * 返回true则 {@code vaildTime()} 表示令牌有效期截止的时间(时间戳),否则表示从申请开始时令牌的有效期
     * @return result
     */
    boolean ddlOrFixt();

    /**
     * 返回此令牌的有效期(截止时间)(单位ms)
     * @return result
     */
    long vaildTime();

}
