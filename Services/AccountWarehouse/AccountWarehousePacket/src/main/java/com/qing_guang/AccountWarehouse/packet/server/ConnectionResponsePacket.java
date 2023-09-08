package com.qing_guang.AccountWarehouse.packet.server;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.ContainerOnly;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

import java.util.Collection;
import java.util.UUID;

@PacketInfo(name = "CONNECTION_RESPONSE",belongsTo = AWServerPacketType.class)
public class ConnectionResponsePacket extends PBPacket {

    //����������������֤������Ϊfalse,�������Ϊtrue
    @DataField(fieldID = 0)
    private boolean isSucceed;

    //���з������Ϣ
    @DataField(fieldID = 1)
    private ServiceInfo[] services;

    /** default */
    public ConnectionResponsePacket(){}

    /**
     * �½����ݰ�
     * @param isSucceed ����������������֤��������false,���������true
     * @param services ���з������Ϣ
     */
    public ConnectionResponsePacket(boolean isSucceed,ServiceInfo[] services){
        this.isSucceed = isSucceed;
        this.services = services;
    }

    /**
     * ����������������֤�����򷵻�false,�����������true
     * @return result
     */
    public boolean isSucceed() {
        return isSucceed;
    }

    /**
     * ���з������Ϣ
     * @return result
     */
    public ServiceInfo[] getServices(){
        return services;
    }

}
