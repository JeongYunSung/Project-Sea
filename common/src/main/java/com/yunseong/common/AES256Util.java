package com.yunseong.common;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;

import static java.nio.charset.StandardCharsets.*;

public class AES256Util {

    private final String iv;
    private final Key keySpec;

    final static String key = "JYServerKey66908";

    public AES256Util() {
        this.iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes(UTF_8);
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);

        this.keySpec = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String str) throws GeneralSecurityException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = c.doFinal(str.getBytes(UTF_8));
        return new String(Base64.encodeBase64(encrypted));
    }

    public String decrypt(String str) {
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
            byte[] byteStr = Base64.decodeBase64(str.getBytes());
            return new String(c.doFinal(byteStr), UTF_8);
        } catch(IllegalArgumentException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new NotMatchedCryptException("Not Matched Token Value.");
        }
    }
}
