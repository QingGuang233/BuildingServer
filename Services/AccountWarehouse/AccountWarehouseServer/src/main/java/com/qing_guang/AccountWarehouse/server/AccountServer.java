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
 * �˻����������
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
        formatter.register(AWPacketType.class);
        formatter.register(AWServerPacketType.class);
        formatter.register(AWClientPacketType.class);

    }

    /**
     * �����˻�������
     * @throws IOException ���������ݷ��������������IO�쳣ʱ�׳�
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
     * �ر��˻�������
     *
     */
    public void close(){
        isRunning = false;
        dataSystem.close();
        network.stop();
    }

    /**
     * ͨ�����ӵ�uid({@code PBClient::getUUID()})�жϴ������Ƿ��ѳɹ������˴�������;����ȷ
     * @param uid ���ӵ�uid
     * @return result
     */
    public boolean hasPurposeCleared(UUID uid){
        return operators.containsKey(uid) || logins.containsKey(uid);
    }

    /**
     * ��¼�ͻ��˵�������;
     * @param client �ͻ�������
     * @param loginOrOperate ��trueΪ��¼ĳһ�����ϵ��˺�,��falseΪ������˻��ֿ������
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
