package com.qing_guang.Tokens;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ����һ�����ƹ�����
 */
public class TokenManager<T> {

    private Map<UUID,Token<T>> tokens = new HashMap<>();
    private long timeout = -1;

    /**
     * default
     */
    public TokenManager(){}

    /**
     * ����һ�����ƹ�����
     * @param timeout �������ƵĹ���ʱ��
     */
    public TokenManager(long timeout){
        this.timeout = timeout;
    }

    /**
     * ��ȡ���ƹ���ʱ��
     * @return result
     */
    public long getTimeout(){
        return timeout;
    }

    /**
     * �������ƹ���ʱ��
     * @param timeout ���ƹ���ʱ��
     */
    public void setTimeout(long timeout){
        this.timeout = timeout;
    }

    /**
     * ����һ������
     * @param data ���ƶ�Ӧ������
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
     * ����һ��ָ��UUID������
     * @param data ���ƶ�Ӧ������
     * @param uid ���Ƶ�UUID
     * @param replaceOld ����������ȴ���,�Ƿ��滻
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
     * ��ȡһ��UUIDָ��������
     * @param uid ���Ƶ�UUID
     * @return �����Ʋ����ڻ���ڹ���������,����null
     */
    public Token<T> getToken(UUID uid){
        check(uid);
        return tokens.get(uid);
    }

    /**
     * ע��һ������
     * @param uid ���Ƶ�UUID
     */
    public void unregister(UUID uid){
        check(uid);
        tokens.remove(uid);
    }

    /**
     * �����Ƿ����
     * @param uid ����UUID
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
