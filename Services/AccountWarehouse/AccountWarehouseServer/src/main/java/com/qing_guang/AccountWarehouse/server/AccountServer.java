package com.qing_guang.AccountWarehouse.server;

import com.qing_guang.AccountWarehouse.server.data.intf.DataStoreSystem;
import com.qing_guang.PacketBasedTCP.packet.format.PacketFormatter;
import com.qing_guang.PacketBasedTCP.server.PBServer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

/**
 * �˻����������
 */
public class AccountServer {

    private DataStoreSystem dataSystem;
    private PBServer network;
    private String password;

    /** default */
    public AccountServer(){}

    /**
     * �½��˻�������
     * @param dataSystem �˷�����ʹ�õ����ݿ�
     * @param port �˷�����ʹ�õĶ˿�
     * @param password �Է������Ա����ݵ�����������������(hashǰ)
     */
    public AccountServer(
            DataStoreSystem dataSystem,
            int port,
            String password
    ){

        this.dataSystem = dataSystem;
        this.network = new PBServer(port,30000,true,"RSA","DES");
        this.password = getSHA256StrJava(password);

        PacketFormatter formatter = network.getFormatter();

    }

    private static String getSHA256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException ignored) {
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] byteBuffer){
        StringBuilder strHexString = new StringBuilder();
        for (byte b : byteBuffer) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                strHexString.append('0');
            }
            strHexString.append(hex);
        }
        return strHexString.toString();
    }

}
