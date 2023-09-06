package com.qing_guang.PacketBasedTCP.packet.def.formatter;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * ��Կ������
 */
public class PublicKeyFormatter implements DataFormatter<PublicKey> {

    private String algorithm;
    private KeyFactory keyFactory;

    /** default */
    public PublicKeyFormatter(){}

    /**
     * �½�һ����Կ������
     * @param algorithm �����㷨
     * @throws NoSuchAlgorithmException ��δ�ҵ������㷨ʱ�׳�
     */
    public PublicKeyFormatter(String algorithm) throws NoSuchAlgorithmException {
        keyFactory = KeyFactory.getInstance(this.algorithm = algorithm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PublicKey toData(byte[] ori, int start, int length) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Arrays.copyOfRange(ori,start,start + length));
        try {
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes(Object data) {
        return ((PublicKey)data).getEncoded();
    }

    /**
     * ��ȡ�����㷨
     * @return result
     */
    public String getAlgorithm(){
        return algorithm;
    }

    /**
     * ���ü����㷨
     * @param algorithm �¼����㷨
     * @throws NoSuchAlgorithmException ��δ�ҵ������㷨ʱ�׳�
     */
    public void setAlgorithm(String algorithm) throws NoSuchAlgorithmException {
        keyFactory = KeyFactory.getInstance(this.algorithm = algorithm);
    }

}
