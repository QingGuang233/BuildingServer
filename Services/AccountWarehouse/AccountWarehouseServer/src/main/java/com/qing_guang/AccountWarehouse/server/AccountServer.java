package com.qing_guang.AccountWarehouse.server;

import com.qing_guang.AccountWarehouse.server.data.intf.DataStoreSystem;
import com.qing_guang.PacketBasedTCP.packet.format.PacketFormatter;
import com.qing_guang.PacketBasedTCP.server.PBServer;

/**
 * �˻����������
 */
public class AccountServer {

    private DataStoreSystem dataSystem;
    private PBServer network;

    /** default */
    public AccountServer(){}

    /**
     * �½��˻�������
     * @param dataSystem �˷�����ʹ�õ����ݿ�
     * @param port �˷�����ʹ�õĶ˿�
     */
    public AccountServer(
            DataStoreSystem dataSystem,
            int port
    ){

        this.dataSystem = dataSystem;
        this.network = new PBServer(port,3000,true,"RSA","DES");

        PacketFormatter formatter = network.getFormatter();

    }

}
