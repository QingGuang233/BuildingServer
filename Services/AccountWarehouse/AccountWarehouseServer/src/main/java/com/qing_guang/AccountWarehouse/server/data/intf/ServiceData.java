package com.qing_guang.AccountWarehouse.server.data.intf;

import java.util.UUID;

/**
 * ��������ݽӿ�
 */
public interface ServiceData {

    /**
     * ��ȡ�˷��������
     * @return result
     */
    String getName();

    /**
     * ��ȡ�˷���uid
     * @return result
     */
    UUID getUUID();

    /**
     * �Ƿ�֧��ͨ���ֻ�������֤�һ�����,Ĭ��Ϊfalse,���˷���Ϊ��֤ģʽʱ����false
     * @return result
     */
    boolean isPhoneVeriOpen();

    /**
     * �����Ƿ�֧���ֻ�������֤�һ�����,�� {@code isPhoneVeriOpen()} ����falseʱ���ô˷������ᷢ���κ���
     */
    void setPhoneVeriOpen(boolean isOpen);

    /**
     * �Ƿ�ǿ��Ҫ���˺���Ҫ���ֻ�����,�� {@code isPhoneVeriOpen()} ����falseʱ����false
     * @return result
     */
    boolean isPhoneForced();

    /**
     * �����Ƿ�ǿ��Ҫ���˺���Ҫ���ֻ�����,�� {@code isPhoneVeriOpen()} ����falseʱ���ô˷������ᷢ���κ���
     */
    void setPhoneForced(boolean isForced);

    /**
     * ͨ���ֻ������ҵ��˺�����
     * @param phoneNumber �ֻ�����
     * @return �ҵ����˺�����,����˷���֧���ֻ�������֤���ߴ��˺���δ��ʱ����null
     */
    AccountData getAccountByPhone(String phoneNumber);

    /**
     * �Ƿ�֧��ͨ��������֤�һ�����,Ĭ��Ϊfalse
     * @return result
     */
    boolean isEmailVeriOpen();

    /**
     * �����Ƿ�֧��������֤�һ�����,�� {@code isEmailVeriOpen()} ����falseʱ���ô˷������ᷢ���κ���
     */
    void setEmailVeriOpen(boolean isOpen);

    /**
     * �Ƿ�ǿ��Ҫ���˺���Ҫ������,�� {@code isEmailVeriOpen()} ����falseʱ����false
     * @return result
     */
    boolean isEmailForced();

    /**
     * �����Ƿ�ǿ��Ҫ���˺���Ҫ������,�� {@code isEmailVeriOpen()} ����falseʱ���ô˷������ᷢ���κ���
     */
    void setEmailForced(boolean isForced);

    /**
     * ͨ�������ҵ��˺�����
     * @param email �ֻ�����
     * @return �ҵ����˺�����,����˷���֧���ֻ�������֤���ߴ��˺���δ���򷵻�null
     */
    AccountData getAccountByEmail(String email);

    /**
     * ����һ���˻�
     * @param username �û���
     * @param pwd ����(hashǰ)
     * @param phoneNumber ���˺Ű󶨵��ֻ�����,���˷���Ϊ��֤ģʽ����null,���˷���ǿ����д�������null
     * @param email ���˺Ű󶨵�����,���˷���Ϊ��֤ģʽ����null,���˷���ǿ����д�������null
     * @return result
     */
    AccountData createAccount(String username,String pwd,String phoneNumber,String email);

    /**
     * ��ȡ����Ա����(hash��)
     * @return result
     */
    String getAdminPwd();

    /**
     * ���ù���Ա����(hashǰ)
     */
    void setAdminPwd();

    /**
     * ����һ��ָ���˻�����ʱ��֤����
     * @param data ָ�����˻�
     * @param time ������Ч(��ֹ)��ʱ��(��λ��Ϊms)
     * @param ddlOrFixt ��true����� {@code time} ��ʾ������Ч�ڽ�ֹ��ʱ��(ʱ���),�����ʾ�����뿪ʼʱ���Ƶ���Ч��
     * @return result
     */
    AccountToken allocateAccToken(AccountData data, long time, boolean ddlOrFixt);

    /**
     * ͨ��uid��ȡ��ʱ��֤����,�����ݿ���δ������������Ϣ�򷵻�null
     * @param uid ��֤���Ƶ�uid
     * @return result
     */
    AccountToken getToken(UUID uid);

}
