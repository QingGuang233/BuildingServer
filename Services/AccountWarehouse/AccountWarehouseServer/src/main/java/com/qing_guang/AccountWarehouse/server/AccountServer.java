package com.qing_guang.AccountWarehouse.server;

import com.qing_guang.AccountWarehouse.packet.AWPacketType;
import com.qing_guang.AccountWarehouse.packet.client.AWClientPacketType;
import com.qing_guang.AccountWarehouse.packet.server.AWServerPacketType;
import com.qing_guang.AccountWarehouse.server.data.intf.DataStoreSystem;
import com.qing_guang.PacketBasedTCP.packet.format.PacketFormatter;
import com.qing_guang.PacketBasedTCP.server.PBClient;
import com.qing_guang.PacketBasedTCP.server.PBServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 账户管理服务器
 */
public class AccountServer {

    private DataStoreSystem dataSystem;
    private PBServer network;
    private String password;
    private Map<UUID, PBClient> operators = new HashMap<>();
    private Map<UUID, PBClient> logins = new HashMap<>();
    private boolean isRunning;

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
        formatter.register(AWPacketType.class);
        formatter.register(AWServerPacketType.class);
        formatter.register(AWClientPacketType.class);

    }

    /**
     * 开启账户服务器
     * @throws IOException 当启动数据服务器或打开连接有IO异常时抛出
     */
    public void start() throws IOException{

        isRunning = true;

        dataSystem.start();
        network.start();

        new Thread(() -> {
            while (isRunning){
                try {
                    network.accept();
                    network.read();
                    Thread.sleep(20);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException ignored) {
                }
            }
        },"AccountWarehouse Network Thread").start();

    }

    /**
     * 关闭账户服务器
     *
     */
    public void close(){
        isRunning = false;
        dataSystem.close();
        network.stop();
    }

    /**
     * 通过连接的uid({@code PBClient::getUUID()})判断此链接是否已成功进行了此连接用途的明确
     * @param uid 连接的uid
     * @return result
     */
    public boolean hasPurposeCleared(UUID uid){
        return operators.containsKey(uid) || logins.containsKey(uid);
    }

    /**
     * 记录客户端的连接用途
     * @param client 客户端连接
     * @param loginOrOperate 填true为登录某一服务上的账号,填false为管理此账户仓库服务器
     */
    public void makePurposeCleared(PBClient client,boolean loginOrOperate){

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
