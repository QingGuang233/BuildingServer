package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * 一般只由服务端发送,在连接时通知客户端服务器的一些信息
 */
@PacketInfo(name = "SERVER_INFO",belongsTo = PBPacketTypeDef.class)
public class PBPacketServerInfo extends PBPacket{

    @DataField(fieldID = 0)
    private boolean isForced;
    @DataField(fieldID = 1)
    private String symAlgorithm;
    @DataField(fieldID = 2)
    private String asymAlgorithm;

    /** default */
    public PBPacketServerInfo(){}

    /**
     * 新建一个数据包
     * @param isForced 服务器是否采取强制加密
     * @param symAlgorithm 对称加密算法
     * @param asymAlgorithm 非对称加密算法
     */
    public PBPacketServerInfo(boolean isForced,String symAlgorithm,String asymAlgorithm){
        this.isForced = isForced;
        this.symAlgorithm = symAlgorithm;
        this.asymAlgorithm = asymAlgorithm;
    }

    /**
     * 服务器是否强制采用加密传输数据
     * @return result
     */
    public boolean isForced(){
        return isForced;
    }

    /**
     * 服务器使用的对称加密算法
     * @return result
     */
    public String getSymAlgorithm() {
        return symAlgorithm;
    }

    /**
     * 服务器使用的非对称加密算法
     * @return result
     */
    public String getAsymAlgorithm() {
        return asymAlgorithm;
    }

}

