package com.qing_guang.PacketBasedTCP.packet.def;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

/**
 * һ��ֻ�ɷ���˷���,������ʱ֪ͨ�ͻ��˷�������һЩ��Ϣ
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
     * �½�һ�����ݰ�
     * @param isForced �������Ƿ��ȡǿ�Ƽ���
     * @param symAlgorithm �ԳƼ����㷨
     * @param asymAlgorithm �ǶԳƼ����㷨
     */
    public PBPacketServerInfo(boolean isForced,String symAlgorithm,String asymAlgorithm){
        this.isForced = isForced;
        this.symAlgorithm = symAlgorithm;
        this.asymAlgorithm = asymAlgorithm;
    }

    /**
     * �������Ƿ�ǿ�Ʋ��ü��ܴ�������
     * @return result
     */
    public boolean isForced(){
        return isForced;
    }

    /**
     * ������ʹ�õĶԳƼ����㷨
     * @return result
     */
    public String getSymAlgorithm() {
        return symAlgorithm;
    }

    /**
     * ������ʹ�õķǶԳƼ����㷨
     * @return result
     */
    public String getAsymAlgorithm() {
        return asymAlgorithm;
    }

}

