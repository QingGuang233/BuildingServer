package com.qing_guang.AccountWarehouse.server.data.intf;

import java.util.Collection;
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
     * ͨ���û����ҵ��˺�����
     * @param username �û���
     * @return �ҵ����˺�����,���δ�ҵ�����null
     */
    AccountData getAccountByUsername(String username);

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
     * ����һ��ָ���˻�����ʱ��֤����
     * @param data ָ�����˻�
     * @param time ������Ч(��ֹ)��ʱ��(��λ��Ϊms)
     * @param ddlOrFixt ��true����� {@code time} ��ʾ������Ч�ڽ�ֹ��ʱ��(ʱ���),�����ʾ�����뿪ʼʱ���Ƶ���Ч��
     * @return result
     */
    AccountToken allocateAccToken(AccountData data, long time, boolean ddlOrFixt);

    /**
     * ��ȡ���ݿ��д˷������л������ڼ�¼��(������ʧЧ��)��֤����
     * @return result
     */
    Collection<AccountToken> allTokens();

    /**
     * �˷����Ƿ�����ͬһ���˻�ͬʱ�ж�����õ���֤����
     * @return result
     */
    boolean isMultiTokenAllowed();

    /**
     * ���ô˷����Ƿ�����ͬһ���˻�ͬʱ�ж�����õ���֤����
     * @param isMultiTokenAllowed �˷����Ƿ�����ͬһ���˻�ͬʱ�ж�����õ���֤����
     */
    void setMultiTokenAllowed(boolean isMultiTokenAllowed);

    /**
     * ��ȡ������������ע����˻�
     * @return result
     */
    Collection<AccountData> getAccounts();

    /**
     * ע���˷���
     */
    void removeData();

}
