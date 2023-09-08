package com.qing_guang.AccountWarehouse.server;

import com.qing_guang.AccountWarehouse.server.data.intf.DataStoreSystem;
import com.qing_guang.PacketBasedTCP.packet.format.PacketFormatter;
import com.qing_guang.PacketBasedTCP.server.PBServer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

/**
 * 账户管理服务器
 */
public class AccountServer {

    private DataStoreSystem dataSystem;
    private PBServer network;
    private String password;

    /** default */
    public AccountServer(){}

    /**
     * 新建账户服务器
     * @param dataSystem 此服务器使用的数据库
     * @param port 此服务器使用的端口
     * @param password 以服务管理员的身份登入服务器所需的密码(hash前)
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
