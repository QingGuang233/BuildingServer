package com.qing_guang.Tokens;

import java.util.UUID;

/**
 * 代表一个令牌
 * @param <T> 数据类型
 */
public class Token<T> {

    private TokenManager<T> manager;
    private UUID uid;
    private T data;
    private long createTime;

    Token(TokenManager<T> manager,UUID uid,T data){
        this.createTime = System.currentTimeMillis();
        this.manager = manager;
        this.data = data;
    }

    /**
     * 获取令牌对应的令牌管理器
     * @return result
     */
    public TokenManager<T> getManager() {
        return manager;
    }

    /**
     * 获取令牌的UUID
     * @return result
     */
    public UUID getUUID() {
        return uid;
    }

    /**
     * 获取令牌对应的数据
     * @return result
     */
    public T getData() {
        return data;
    }

    /**
     * 设置令牌对应的数据
     * @param data 对应的数据
     */
    public void setData(T data){
        this.data = data;
    }

    /**
     * 获取令牌创建时间
     * @return result
     */
    public long getCreateTime() {
        return createTime;
    }

}
