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
     * �����ݿ���ɾ������ռ�ݵ�����
     * @param uid �����uid
     */
    void removeServiceData(UUID uid);

    /**
     * ͨ�����ƻ�ȡ���������
     * @param name ���������
     * @return result
     */
    ServiceData getService(String name);

    /**
     * ͨ��uid��ȡ���������
     * @param uid �����uid
     * @return result
     */
    ServiceData getService(UUID uid);

}
