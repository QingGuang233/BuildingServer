package com.qing_guang.AccountWarehouse.packet.client;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * 连接服务器的客户端类型选择
 */
@PacketInfo(name = "CONNECTION_TYPE",belongsTo = AWClientPacketType.class)
public class ConnectionTypePacket extends PBPacket {

    //填true为登录某一服务上的账号,填false为管理此账户仓库服务器
    @DataField(fieldID = 0)
    private boolean loginOrOperate;

    //若 loginOrOperate 为true则不需要提供(hash前)
    @DataField(fieldID = 1)
    private String operatorPwd;

    /** default */
    public ConnectionTypePacket(){}

    /**
     * 新建数据包
     * @param loginOrOperate 填true为登录某一服务上的账号,填false为管理此账户仓库服务器
     * @param operatorPwd 若 {@code loginOrOperate} 为true则不需要提供(hash前)
     */
    public ConnectionTypePacket(boolean loginOrOperate,String operatorPwd){
        this.loginOrOperate = loginOrOperate;
        this.operatorPwd = operatorPwd;
    }

    /**
     * 为true为登录某一服务上的账号,为false为管理此账户仓库服务器
     * @return false
     */
    public boolean loginOrOperate() {
        return loginOrOperate;
    }

    /**
     * 若 {@code loginOrOperate} 为false则为所需的管理员密码(hash前),否则返回null
     * @return result
     */
    public String getOperatorPwd() {
        return operatorPwd;
    }

}
