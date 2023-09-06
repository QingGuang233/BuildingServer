package com.qing_guang.AccountWarehouse.server.data.intf;

import java.util.UUID;

/**
 * ���ݿ�ӿ�
 */
public interface DataStoreSystem {

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
     * ��ע��һ������
     * @param name ��������
     * @param adminPwd �˷���Ĺ���Ա����
     * @param authOrAcc �˷���ʹ�õĵ�¼ϵͳ����֤ϵͳ(true,ָֻ��Ҫ�����¼)�����˺�ϵͳ(false,ָ��Ҫ�˻���������)
     * @return ��ע��ķ���
     * @throws ServiceCreateFailedException ���·���ע��ʧ��ʱ�׳�
     */
    UUID createService(String name, String adminPwd, boolean authOrAcc) throws ServiceCreateFailedException;

    /**
     * ע������,�������ʱΪ����ʹ��״̬Ӧ��Ϊע��ʧ��
     * @param name ���������
     * @throws ServiceCreateFailedException ������ע��ʧ��ʱ�׳�
     */
    void unregisterService(String name);

    /**
     * ע������,�������ʱΪ����ʹ��״̬Ӧ��Ϊע��ʧ��
     * @param uid �����uid
     * @throws ServiceCreateFailedException ������ע��ʧ��ʱ�׳�
     */
    void unregisterService(UUID uid);

    /**
     * ͨ�����ƻ�ȡ����
     * @param name ���������
     * @return result
     */
    ServiceData getService(String name);

    /**
     * ͨ��uid��ȡ����
     * @param uid �����uid
     * @return result
     */
    ServiceData getService(UUID uid);

}
