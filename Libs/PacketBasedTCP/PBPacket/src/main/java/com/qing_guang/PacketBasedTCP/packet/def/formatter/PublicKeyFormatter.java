package com.qing_guang.PacketBasedTCP.packet.def.formatter;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * 公钥解析器
 */
public class PublicKeyFormatter implements DataFormatter<PublicKey> {

    private String algorithm;
    private KeyFactory keyFactory;

    /** default */
    public PublicKeyFormatter(){}

    /**
     * 新建一个公钥解析器
     * @param algorithm 加密算法
     * @throws NoSuchAlgorithmException 当未找到加密算法时抛出
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
     * 获取加密算法
     * @return result
     */
    public String getAlgorithm(){
        return algorithm;
    }

    /**
     * 设置加密算法
     * @param algorithm 新加密算法
     * @throws NoSuchAlgorithmException 当未找到加密算法时抛出
     */
    public void setAlgorithm(String algorithm) throws NoSuchAlgorithmException {
        keyFactory = KeyFactory.getInstance(this.algorithm = algorithm);
    }

}
