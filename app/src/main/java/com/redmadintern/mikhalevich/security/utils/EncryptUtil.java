package com.redmadintern.mikhalevich.security.utils;


import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Alexander on 15.07.2015.
 */
public class EncryptUtil {

    public static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    public static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String encrypt(byte[] raw, String clear) throws Exception {
        byte[] utf8 = clear.getBytes("UTF8");
        byte[] encrypted = encrypt(raw, utf8);
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String decrypt(byte[] raw, String encrypted) throws Exception {
        byte[] decoded = Base64.decode(encrypted, Base64.DEFAULT);
        byte[] utf8  = decrypt(raw, decoded);
        return new String(utf8, "UTF8");
    }
}
