package com.qing_guang.AccountWarehouse.packet.server;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.ContainerOnly;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

import java.util.Collection;
import java.util.UUID;

@PacketInfo(name = "CONNECTION_RESPONSE",belongsTo = AWServerPacketType.class)
public class ConnectionResponsePacket extends PBPacket {

    //若申请管理的密码验证错误则为false,其余情况为true
    @DataField(fieldID = 0)
    private boolean isSucceed;

    //所有服务的信息
    @DataField(fieldID = 1)
    private ServiceInfo[] services;

    /** default */
    public ConnectionResponsePacket(){}

    /**
     * 新建数据包
     * @param isSucceed 若申请管理的密码验证错误则填false,其余情况填true
     * @param services 所有服务的信息
     */
    public ConnectionResponsePacket(boolean isSucceed,ServiceInfo[] services){
        this.isSucceed = isSucceed;
        this.services = services;
    }

    /**
     * 若申请管理的密码验证错误则返回false,其余情况返回true
     * @return result
     */
    public boolean isSucceed() {
        return isSucceed;
    }

    /**
     * 所有服务的信息
     * @return result
     */
    public ServiceInfo[] getServices(){
        return services;
    }

}
