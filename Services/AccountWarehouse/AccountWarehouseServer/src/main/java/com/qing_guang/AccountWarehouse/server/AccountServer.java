package com.qing_guang.AccountWarehouse.server;

import com.qing_guang.AccountWarehouse.server.data.intf.DataStoreSystem;
import com.qing_guang.PacketBasedTCP.packet.format.PacketFormatter;
import com.qing_guang.PacketBasedTCP.server.PBServer;

/**
 * 账户管理服务器
 */
public class AccountServer {

    private DataStoreSystem dataSystem;
    private PBServer network;

    /** default */
    public AccountServer(){}

    /**
     * 新建账户服务器
     * @param dataSystem 此服务器使用的数据库
     * @param port 此服务器使用的端口
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
