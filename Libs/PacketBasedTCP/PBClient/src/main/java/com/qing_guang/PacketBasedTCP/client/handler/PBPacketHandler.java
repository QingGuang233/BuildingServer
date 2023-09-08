package com.qing_guang.PacketBasedTCP.client.handler;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;

/**
 * ������һ�����ݰ�������
 */
public interface PBPacketHandler {

    /**
     * ���ݰ��¼�
     * @param packet ���ݰ�
     * @param in trueΪд��,falseΪд��
     */
    boolean accept(PBPacket packet, boolean in);

    /**
     * �˼����������ȼ�
     * ǿ�ҽ���˷������ص����ȼ�ʼ�ղ���,����ᷢ��������
     * @return result
     */
    default PBHandlePriority priority(){
        return PBHandlePriority.DEFAULT;
    }

}
