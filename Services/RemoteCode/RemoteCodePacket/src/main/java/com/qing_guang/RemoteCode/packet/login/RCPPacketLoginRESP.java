package com.qing_guang.RemoteCode.packet.login;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;
import com.qing_guang.RemoteCode.packet.type.RCPPktTypeLogin;

/**
 * ��¼���
 */
@PacketInfo(name = "LOGIN_REQ",belongsTo = RCPPktTypeLogin.class)
public class RCPPacketLoginRESP extends PBPacket {

    @DataField(fieldID = 0)
    private Result result;
    @DataField(fieldID = 1)
    private String description;

    /** default */
    public RCPPacketLoginRESP(){}

    /**
     * �½������ݰ�
     * @param result ��¼���
     * @param description �������
     */
    public RCPPacketLoginRESP(Result result, String description) {
        this.result = result;
        this.description = description;
    }

    /**
     * ��¼���
     * @return result
     */
    public Result getResult() {
        return result;
    }

    /**
     * �������
     * @return result
     */
    public String getDescription() {
        return description;
    }

    public enum Result{
        /** ��¼�ɹ� */
        SUCCEED,
        /** ������� */
        WRONG_PWD,
        /** �������ܾ���¼,ʹ�ô����ɽ��齫ԭ��ͨ��description˵��� */
        REFUSE_LOGIN,
        /** ���Դ������� */
        TOO_MANY_ATTEMPTS
    }

}
