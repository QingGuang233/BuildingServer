package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * 当一条连接将要被切断时发送
 * 在正常断开时(有一方主动断开),此数据包作为通知
 * 在异常断开时(IO问题),此数据包依旧会被通知给已注册的PBPacketHandler,但不会发送
 */
@PacketInfo(name = "DISCONNECT",belongsTo = PBPacketTypeDef.class)
public class PBPacketDisconn extends PBPacket {

    @DataField(fieldID = 0)
    private DisconnReason reason;
    @DataField(fieldID = 1)
    private String description;

    /** default */
    public PBPacketDisconn(){}

    /**
     * 新建一个数据包
     * @param reason 断线原因
     * @param description 一些断线的解释说明,应为给人读的而不是给程序判断的
     */
    public PBPacketDisconn(DisconnReason reason,String description){
        this.reason = reason;
        this.description = description;
    }

    /**
     * 断线原因
     * @return result
     */
    public DisconnReason getReason() {
        return reason;
    }

    /**
     * 获取此次断线的解释说明
     * @return result
     */
    public String getDescription(){
        return description;
    }

    /**
     * 断线原因
     */
    public enum DisconnReason{

        /**
         * 加密流程未完成就开始通信
         */
        ENCRYPT,
        /**
         * 数据读取异常
         */
        IO_ERROR_READ,
        /**
         * 数据写出异常
         */
        IO_ERROR_WRITE,
        /**
         * 服务器关闭
         */
        SERVER_CLOSE,
        /**
         * 客户端主动断线
         */
        CLIENT_CUSTOM,
        /**
         * 服务器主动断线
         */
        SERVER_CUSTOM

    }

}
