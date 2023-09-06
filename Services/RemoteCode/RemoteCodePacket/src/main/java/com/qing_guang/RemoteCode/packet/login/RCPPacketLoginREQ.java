package com.qing_guang.RemoteCode.packet.login;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;
import com.qing_guang.RemoteCode.packet.type.RCPPktTypeLogin;

/**
 * ��¼����
 */
@PacketInfo(name = "LOGIN_REQ",belongsTo = RCPPktTypeLogin.class)
public class RCPPacketLoginREQ extends PBPacket {

    @DataField(fieldID = 0)
    private String password;

    /** default */
    public RCPPacketLoginREQ(){}

    /**
     * ���������ݰ�
     * @param password ����
     */
    public RCPPacketLoginREQ(String password) {
        this.password = password;
    }

    /**
     * ��ȡ�ͻ��˴���������
     * @return result
     */
    public String getPassword() {
        return password;
    }

}
