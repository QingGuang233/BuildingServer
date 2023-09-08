package com.qing_guang.PacketBasedTCP.client.encode;

import com.qing_guang.PacketBasedTCP.client.PBConnection;
import com.qing_guang.PacketBasedTCP.client.handler.PBPacketHandler;
import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketAsym;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketSym;

import java.security.PrivateKey;

/**
 * ������ص����ݰ�������
 */
public class PBPacketHandlerEn implements PBPacketHandler {

    private PBConnection conn;
    PrivateKey temp;
    EncodeHelper helper = new EncodeHelper();

    /** default */
    public PBPacketHandlerEn(){}

    /**
     * ����һ���˴�����
     * @param conn ��Ӧ������
     */
    public PBPacketHandlerEn(PBConnection conn){
        this.conn = conn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(PBPacket packet, boolean in) {
        if(in){
            if (packet instanceof PBPacketAsym){
                helper.asymPub = ((PBPacketAsym)packet).getKey();
                helper.asymPri = temp;
                byte[] symKey = helper.genSym();
                conn.sendPacket(new PBPacketSym(symKey));
                helper.symKey = symKey;
            }else{
                helper.symKey = ((PBPacketSym)packet).getKey();
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PBHandlePriority priority() {
        return PBHandlePriority.DEFAULT;
    }

    /**
     * ���ݵ�ǰ�ѻ�õ���Կ������Ϣ
     * @param original ԭ����
     * @return result
     */
    public byte[] encode(byte[] original){
        return helper.encode(original);
    }

    /**
     * ���ݵ�ǰ�ѻ�õ���Կ������Ϣ
     * @param original ԭ����
     * @return result
     */
    public byte[] decode(byte[] original){
        return helper.decode(original);
    }

    /**
     * ��ȡ���������õķǶԳƼ����㷨
     * @return result
     */
    public String getAsymAlgorithm(){
        return helper.asymAlgorithm;
    }

    /**
     * ��ȡ���������õĶԳƼ����㷨
     * @return result
     */
    public String getSymAlgorithm(){
        return helper.symAlgorithm;
    }

}
