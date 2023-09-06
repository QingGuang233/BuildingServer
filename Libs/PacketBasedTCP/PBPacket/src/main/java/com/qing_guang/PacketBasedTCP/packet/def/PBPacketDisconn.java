package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * ��һ�����ӽ�Ҫ���ж�ʱ����
 * �������Ͽ�ʱ(��һ�������Ͽ�),�����ݰ���Ϊ֪ͨ
 * ���쳣�Ͽ�ʱ(IO����),�����ݰ����ɻᱻ֪ͨ����ע���PBPacketHandler,�����ᷢ��
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
     * �½�һ�����ݰ�
     * @param reason ����ԭ��
     * @param description һЩ���ߵĽ���˵��,ӦΪ���˶��Ķ����Ǹ������жϵ�
     */
    public PBPacketDisconn(DisconnReason reason,String description){
        this.reason = reason;
        this.description = description;
    }

    /**
     * ����ԭ��
     * @return result
     */
    public DisconnReason getReason() {
        return reason;
    }

    /**
     * ��ȡ�˴ζ��ߵĽ���˵��
     * @return result
     */
    public String getDescription(){
        return description;
    }

    /**
     * ����ԭ��
     */
    public enum DisconnReason{

        /**
         * ��������δ��ɾͿ�ʼͨ��
         */
        ENCRYPT,
        /**
         * ���ݶ�ȡ�쳣
         */
        IO_ERROR_READ,
        /**
         * ����д���쳣
         */
        IO_ERROR_WRITE,
        /**
         * �������ر�
         */
        SERVER_CLOSE,
        /**
         * �ͻ�����������
         */
        CLIENT_CUSTOM,
        /**
         * ��������������
         */
        SERVER_CUSTOM

    }

}
