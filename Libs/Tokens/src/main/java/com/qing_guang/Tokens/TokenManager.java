package com.qing_guang.Tokens;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 代表一个令牌管理器
 */
public class TokenManager<T> {

    private Map<UUID,Token<T>> tokens = new HashMap<>();
    private long timeout = -1;

    /**
     * default
     */
    public TokenManager(){}

    /**
     * 创建一个令牌管理器
     * @param timeout 设置令牌的过期时间
     */
    public TokenManager(long timeout){
        this.timeout = timeout;
    }

    /**
     * 获取令牌过期时间
     * @return result
     */
    public long getTimeout(){
        return timeout;
    }

    /**
     * 设置令牌过期时间
     * @param timeout 令牌过期时间
     */
    public void setTimeout(long timeout){
        this.timeout = timeout;
    }

    /**
     * 创建一个令牌
     * @param data 令牌对应的数据
     * @return result
     */
    public Token<T> createOne(T data){
        UUID uid;
        do{
            uid = UUID.randomUUID();
        }while (tokens.containsKey(uid));
        Token<T> result = new Token<>(this,uid,data);
        tokens.put(uid,result);
        return result;
    }

    /**
     * 创建一个指定UUID的令牌
     * @param data 令牌对应的数据
     * @param uid 令牌的UUID
     * @param replaceOld 如果令牌事先存在,是否替换
     * @return result
     */
    public Token<T> createOne(T data,UUID uid,boolean replaceOld){
        check(uid);
        Token<T> result = null;
        if(!contains(uid) || replaceOld){
            result = new Token<>(this,uid,data);
            tokens.put(uid,result);
        }
        return result;
    }

    /**
     * 获取一个UUID指定的令牌
     * @param uid 令牌的UUID
     * @return 若令牌不存在或存在过但过期了,返回null
     */
    public Token<T> getToken(UUID uid){
        check(uid);
        return tokens.get(uid);
    }

    /**
     * 注销一个令牌
     * @param uid 令牌的UUID
     */
    public void unregister(UUID uid){
        check(uid);
        tokens.remove(uid);
    }

    /**
     * 令牌是否存在
     * @param uid 令牌UUID
     */
    public boolean contains(UUID uid){
        return tokens.containsKey(uid);
    }

    private void check(UUID uid){
        Token<?> token = tokens.get(uid);
        if(token != null && token.getCreateTime() + timeout <= System.currentTimeMillis()){
            tokens.remove(uid);
        }
    }

}
