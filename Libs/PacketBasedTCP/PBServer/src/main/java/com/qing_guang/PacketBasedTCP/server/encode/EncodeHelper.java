package com.qing_guang.PacketBasedTCP.server.encode;

import com.qing_guang.PacketBasedTCP.server.PBClient;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class EncodeHelper {

    String asymAlgorithm;
    String symAlgorithm;
    Map<PBClient, PublicKey> asymPubs = new HashMap<>();
    Map<PBClient, PrivateKey> asymPris = new HashMap<>();
    Map<PBClient, byte[]> symKey = new HashMap<>();

    KeyPair genAsym(){
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(asymAlgorithm);
            keyPairGen.initialize(1024,new SecureRandom());
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    byte[] genSym(){
        return UUID.randomUUID().toString().replace("-","").substring(0,8).getBytes();
    }

    byte[] encode(byte[] original,PBClient client){
        if(!asymPubs.containsKey(client)){
            return original;
        }
        try {
            if(!symKey.containsKey(client)){
                Cipher cipher = Cipher.getInstance(asymAlgorithm);
                cipher.init(Cipher.ENCRYPT_MODE, asymPubs.get(client));
                return cipher.doFinal(original);
            }else{
                SecretKeySpec key = new SecretKeySpec(symKey.get(client), symAlgorithm);
                Cipher cipher = Cipher.getInstance(symAlgorithm);
                cipher.init(Cipher.ENCRYPT_MODE,key);
                return cipher.doFinal(original);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    byte[] decode(byte[] original,PBClient client){
        if(!asymPris.containsKey(client)){
            return original;
        }
        try {
            if(!symKey.containsKey(client)){
                Cipher cipher = Cipher.getInstance(asymAlgorithm);
                cipher.init(Cipher.DECRYPT_MODE, asymPris.get(client));
                return cipher.doFinal(original);
            }else{
                SecretKeySpec key = new SecretKeySpec(symKey.get(client), symAlgorithm);
                Cipher cipher = Cipher.getInstance(symAlgorithm);
                cipher.init(Cipher.DECRYPT_MODE,key);
                return cipher.doFinal(original);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
