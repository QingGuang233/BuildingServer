package com.qing_guang.RemoteCode.packet.login;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;
import com.qing_guang.RemoteCode.packet.type.RCPPktTypeLogin;

/**
 * 登录结果
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
     * 新建此数据包
     * @param result 登录结果
     * @param description 结果描述
     */
    public RCPPacketLoginRESP(Result result, String description) {
        this.result = result;
        this.description = description;
    }

    /**
     * 登录结果
     * @return result
     */
    public Result getResult() {
        return result;
    }

    /**
     * 结果描述
     * @return result
     */
    public String getDescription() {
        return description;
    }

    public enum Result{
        /** 登录成功 */
        SUCCEED,
        /** 密码错误 */
        WRONG_PWD,
        /** 服务器拒绝登录,使用此理由建议将原因通过description说清楚 */
        REFUSE_LOGIN,
        /** 尝试次数过多 */
        TOO_MANY_ATTEMPTS
    }

}
