package com.qing_guang.PacketBasedTCP.client.encode;

import com.qing_guang.PacketBasedTCP.client.PBConnection;
import com.qing_guang.PacketBasedTCP.client.handler.PBPacketHandler;
import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketAsym;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketSym;

import java.security.PrivateKey;

/**
 * 加密相关的数据包监听器
 */
public class PBPacketHandlerEn implements PBPacketHandler {

    private PBConnection conn;
    PrivateKey temp;
    EncodeHelper helper = new EncodeHelper();

    /** default */
    public PBPacketHandlerEn(){}

    /**
     * 创建一个此处理器
     * @param conn 对应的连接
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
     * 根据当前已获得的密钥加密信息
     * @param original 原数据
     * @return result
     */
    public byte[] encode(byte[] original){
        return helper.encode(original);
    }

    /**
     * 根据当前已获得的密钥解密信息
     * @param original 原数据
     * @return result
     */
    public byte[] decode(byte[] original){
        return helper.decode(original);
    }

    /**
     * 获取服务器采用的非对称加密算法
     * @return result
     */
    public String getAsymAlgorithm(){
        return helper.asymAlgorithm;
    }

    /**
     * 获取服务器采用的对称加密算法
     * @return result
     */
    public String getSymAlgorithm(){
        return helper.symAlgorithm;
    }

}
