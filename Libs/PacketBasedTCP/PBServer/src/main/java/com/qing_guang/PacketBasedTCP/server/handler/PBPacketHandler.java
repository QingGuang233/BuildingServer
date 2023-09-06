package com.qing_guang.PacketBasedTCP.server.handler;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.server.PBClient;

/**
 * ������һ�����ݰ�������
 */
public interface PBPacketHandler {

    /**
     * ���ݰ��¼�
     * @param client �ͻ���
     * @param packet ���ݰ�
     * @param in trueΪд��,falseΪд��
     */
    void accept(PBClient client,PBPacket packet,boolean in);

    /**
     * �˼����������ȼ�
     * ǿ�ҽ���˷������ص����ȼ�ʼ�ղ���,����ᷢ��������
     * @return result
     */
    default PBHandlePriority priority(){
        return PBHandlePriority.DEFAULT;
    }

}
