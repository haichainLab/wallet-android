package io.samos.wallet.utils;

import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by zjy on 2018/1/29.
 */

public class StringUtils {

    private static final String FORMAT_WALLET_ADDRESS_MIDDLE = "...";

    public static String converListToString(List<String> source){
        StringBuilder sb = new StringBuilder();
        for (String s : source) {
            sb.append(s);
        }
        return sb.toString();
    }


    /**
     * 格式化钱包地址
     * @param address
     * @return
     */
    public static String formatWalletAddress(String address){
        if(TextUtils.isEmpty(address) || address.length() < 12)
            return address;
        int length = address.length();
        return address.substring(0,6) + FORMAT_WALLET_ADDRESS_MIDDLE + address.substring(length-6,length);
    }

    static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }


    public static String sha256(String tmp) {
        MessageDigest digest=null;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(tmp.getBytes());

            hash = bin2hex(digest.digest());

            return hash;

        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return "";

    }
}
