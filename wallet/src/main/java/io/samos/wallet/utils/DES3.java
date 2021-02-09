package io.samos.wallet.utils;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by hanyouhong on 2018/10/21.
 */

public class DES3 {
    // 密钥
    private final static String secretKey = "akyfrZ@H;*3j03.$(I%VC/1}" ;
    // 向量
    private final static String iv = "^pk^3:0~" ;
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8" ;

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) throws Exception {
        Key deskey = null ;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede" );
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance( "desede/CBC/PKCS5Padding" );
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte [] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64.encode(encryptData);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText)  {
        Key deskey = null ;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

            byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

            return new String(decryptData, encoding);
        }catch(Exception ex) {
            return "";
        }
    }

    public static String padding(String str) {
        byte[] oldByteArray;
        try {
            oldByteArray = str.getBytes("UTF8");
            int numberToPad = 8 - oldByteArray.length % 8;
            byte[] newByteArray = new byte[oldByteArray.length + numberToPad];
            System.arraycopy(oldByteArray, 0, newByteArray, 0,
                    oldByteArray.length);
            for (int i = oldByteArray.length; i < newByteArray.length; ++i) {
                newByteArray[i] = 0;
            }
            return new String(newByteArray, "UTF8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Crypter.padding UnsupportedEncodingException");
        }
        return null;
    }



}
