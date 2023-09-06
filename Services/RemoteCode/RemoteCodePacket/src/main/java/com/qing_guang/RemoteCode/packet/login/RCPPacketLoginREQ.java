package com.qing_guang.RemoteCode.packet.login;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;
import com.qing_guang.RemoteCode.packet.type.RCPPktTypeLogin;

/**
 * 登录请求
 */
@PacketInfo(name = "LOGIN_REQ",belongsTo = RCPPktTypeLogin.class)
public class RCPPacketLoginREQ extends PBPacket {

    @DataField(fieldID = 0)
    private String password;

    /** default */
    public RCPPacketLoginREQ(){}

    /**
     * 创建此数据包
     * @param password 密码
     */
    public RCPPacketLoginREQ(String password) {
        this.password = password;
    }

    /**
     * 获取客户端传来的密码
     * @return result
     */
    public String getPassword() {
        return password;
    }

}
