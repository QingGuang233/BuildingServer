package com.qing_guang.AccountWarehouse.server.data.intf;

import java.util.UUID;

/**
 * �˺�����
 */
public interface AccountData {

    /**
     * ��ȡ���˺Ŷ�Ӧ�ķ���
     * @return result
     */
    ServiceData getService();

    /**
     * ��ȡ���˺ŵ�uid
     * @return result
     */
    UUID getUUID();

    /**
     * ��ȡ���˺ŵ��û���
     * @return result
     */
    String getUserName();

    /**
     * �����µ��û���
     * @param username �µ��û���
     */
    void setUserName(String username);

    /**
     * ��ȡ���˺ŵ��ֻ�����,�����ڷ���֧���ֻ�������֤ʱ����null
     * @return result
     */
    String getPhoneNumber();

    /**
     * ���ô��˺ŵ��ֻ�����,�����ڷ���֧���ֻ�������֤ʱ���ô˷������ᷢ���κ���
     * @param phoneNumber �ֻ�����
     */
    void setPhoneNumber(String phoneNumber);

    /**
     * ��ȡ���˺ŵ�����,�����ڷ���֧��������֤ʱ����null
     * @return result
     */
    String getEmail();

    /**
     * ���ô��˺ŵ�����,�����ڷ���֧��������֤ʱ���ô˷������ᷢ���κ���
     * @param email ����
     */
    void setEmail(String email);

    /**
     * ��ȡ���˺ŵ�����(hash��)
     * @return result
     */
    String getPassword();

    /**
     * ���ô��˺ŵ�����(hashǰ)
     * @param pwd ����
     */
    void setPassword(String pwd);

    /**
     * ����һ�ݴ��˻�����ʱ��֤����
     * @param time ������Ч��ʱ��(ms)
     * @param ddlOrFixt ��true����� {@code time} ��ʾ������Ч�ڽ�ֹ��ʱ��(ʱ���),�����ʾ�����뿪ʼʱ���Ƶ���Ч��
     * @return result
     */
    AccountToken allocateAccToken(long time,boolean ddlOrFixt);

}
