package com.qing_guang.AccountWarehouse.server.data.intf;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

/**
 * ���ݿ�ӿ�
 */
public interface DataStoreSystem {

    /**
     * �������ݿ����
     * @throws IOException ����������IO����ʱ�׳�
     */
    void start() throws IOException;

    /**
     * �ر����ݿ����
     *  @throws IOException ���رճ���IO����ʱ�׳�
     */
    void close() ;

    /**
     * ������ݿ����Ƿ���ָ��uid�ķ����ѱ�ע��
     * @param id ָ����uid
     * @return result
     */
    boolean hasService(UUID id);

    /**
     * ������ݿ����Ƿ���ָ�����Ƶķ����ѱ�ע��
     * @param name ָ��������
     * @return result
     */
    boolean hasService(String name);

    /**
     * ����ע��ķ��������ݿ��м�¼
     * @param name ��������
     * @param adminPwd �˷���Ĺ���Ա����(hashǰ)
     * @param authOrAcc �˷���ʹ�õĵ�¼ϵͳ����֤ϵͳ(true,ָֻ��Ҫ�����¼)�����˺�ϵͳ(false,ָ��Ҫ�˻���������)
     * @return ��ע��ķ���
     */
    ServiceData deleteServiceData(
            String name,
            String adminPwd,
            boolean authOrAcc
    );

    /**
     * �����ݿ���ɾ������ռ�ݵ�����
     * @param name ���������
     */
    void removeServiceData(String name);

    /**
     * ͨ��uid��ȡ���������
     * @param uid �����uid
     * @return result
     */
    ServiceData getService(UUID uid);

    /**
     * ͨ��uid��ȡ��ʱ��֤����,�����ݿ���δ������������Ϣ�򷵻�null
     * @param uid ��֤���Ƶ�uid
     * @return result
     */
    AccountToken getToken(UUID uid);

    /**
     * ��ȡ���ݿ������л������ڼ�¼��(������ʧЧ��)��֤����
     * @return result
     */
    Collection<AccountToken> allTokens();

}
