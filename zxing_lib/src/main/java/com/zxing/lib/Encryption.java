package com.zxing.lib;




import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static final String ENCODE = "UTF-8";
    public static String KEY = "AUTOSHENGDA";

    /**
     * 32位MD5加密算法
     *
     * @param plainText
     * @return
     */
    public static String MD5(String plainText) {
        StringBuffer buf = new StringBuffer();
        if (plainText == null) plainText = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes(ENCODE));
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    /**
     * 16位MD5加密算法
     *
     * @param plainText
     * @return
     */
    public static String shortMD5(String plainText) {
        return MD5(plainText).substring(8, 24);
    }

    /**
     * AES + Base64解密
     *
     * @param content     待解密内容
     * @param password    解密密钥
     * @return
     */
    public static String decode(String content, String password) throws Exception {
            System.out.println("第一步，扫描的结果content：" + content);
            System.out.println("第一步，扫描的结果password：" + password);
            String charsetName = ENCODE;
            // KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // kgen.init(128, new SecureRandom(password.getBytes()));
            // SecretKey secretKey = kgen.generateKey();
            // byte[] enCodeFormat = secretKey.getEncoded();
            // 为了和.NET加密一致修改为
            System.out.println("第二步，密码MD5处理后的password：" + shortMD5(password));
            byte[] enCodeFormat = shortMD5(password).getBytes(charsetName);
            // ///////////////////////以上已经修改/////////////////////////
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] base = Base64.decode(content, Base64.DEFAULT);

            System.out.println("-------------解密前-------------");
            for (int i = 0; i < base.length; i++) {
                System.out.print(base[i] + " ");
            }
            System.out.println("\n--------------------------");

            byte[] result = cipher.doFinal(base);// 解密

            System.out.println("-------------解密后-------------");
            for (int i = 0; i < result.length; i++) {
                System.out.print(result[i] + " ");
            }
            System.out.println("\n--------------------------");


            return new String(result, charsetName);
    }

    public static byte[] encrypt(String content, String password) {
        try {
            byte[] enCodeFormat = Encryption.shortMD5(password).getBytes(ENCODE);
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes(ENCODE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            System.out.println("加密后：" + result);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleDecodeInternally2(String rawResult) {
        String[] texts = new String[]{};
        texts = rawResult.split("\\|");
        try {
            texts[0] = Encryption.decode(texts[0], texts[1]);
        } catch (Exception e) {
            texts = new String[0];
        }
    }

    public static String encode(String content) {
        byte[] input = null;
        try {
            input = content.getBytes(ENCODE);
            content = new String(Base64.encode(input, Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
        }
        return content;
    }

    public static String encode(String content, String password) {
        content = new String(Base64.encode(encrypt(content, password), Base64.DEFAULT));
        return content;
    }


    public static void main(String[] args) {
        String content = "1000PF000000201405090000000026";
        String password = KEY;
        // 加密
        System.out.println("加密前：" + content);
        //content  = Base64.encode(encrypt(content, password));
        //System.out.println("加密后：" + content);

        content = encode(content, password);
        System.out.println("加密后：" + content);

        //content ="EwYwl+LsBAew6BU8m9HtaA==";
        // 解密
        try {
            content = decode(content, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("解密后：" + content);
    }

}