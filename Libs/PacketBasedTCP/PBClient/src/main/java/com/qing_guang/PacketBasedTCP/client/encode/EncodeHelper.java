package com.qing_guang.PacketBasedTCP.client.encode;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.UUID;

class EncodeHelper {

    PublicKey asymPub;
    PrivateKey asymPri;
    byte[] symKey;

    String asymAlgorithm;
    String symAlgorithm;

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

    byte[] encode(byte[] original){
        if(asymPub == null){
            return original;
        }
        try {
            if(symKey == null){
                Cipher cipher = Cipher.getInstance(asymAlgorithm);
                cipher.init(Cipher.ENCRYPT_MODE, asymPub);
                return cipher.doFinal(original);
            }else{
                SecretKeySpec key = new SecretKeySpec(symKey, symAlgorithm);
                Cipher cipher = Cipher.getInstance(symAlgorithm);
                cipher.init(Cipher.ENCRYPT_MODE,key);
                return cipher.doFinal(original);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    byte[] decode(byte[] original){
        if(asymPri == null){
            return original;
        }
        try {
            if(symKey == null){
                Cipher cipher = Cipher.getInstance(asymAlgorithm);
                cipher.init(Cipher.DECRYPT_MODE, asymPri);
                return cipher.doFinal(original);
            }else{
                SecretKeySpec key = new SecretKeySpec(symKey, symAlgorithm);
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
