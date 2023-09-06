package com.qing_guang.Tokens;

import java.util.UUID;

/**
 * ����һ������
 * @param <T> ��������
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
     * ��ȡ���ƶ�Ӧ�����ƹ�����
     * @return result
     */
    public TokenManager<T> getManager() {
        return manager;
    }

    /**
     * ��ȡ���Ƶ�UUID
     * @return result
     */
    public UUID getUUID() {
        return uid;
    }

    /**
     * ��ȡ���ƶ�Ӧ������
     * @return result
     */
    public T getData() {
        return data;
    }

    /**
     * �������ƶ�Ӧ������
     * @param data ��Ӧ������
     */
    public void setData(T data){
        this.data = data;
    }

    /**
     * ��ȡ���ƴ���ʱ��
     * @return result
     */
    public long getCreateTime() {
        return createTime;
    }

}
