package io.samos.im.utils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import io.samos.wallet.beans.TokenSet;
import io.samos.wallet.datas.Address;
import io.samos.wallet.datas.Wallet;
import io.samos.wallet.datas.WalletDB;
import io.samos.wallet.datas.WalletManager;
import io.samos.wallet.utils.SamosWalletUtils;
import mobile.Mobile;

public class WalletUtil {


    /**
     * 从当前默认仓库的地址里面获取
     * 获取一个地址的公钥
     * @param passwd
     * @param addr
     * @return
     */
    public static String getPubkey( String passwd, String addr) {
        try {

            String walletId = getDefaultWalletID();
            String pubkey = Mobile.getPubkey( walletId, passwd, addr);
            return pubkey;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getDefaultWalletID() {
        WalletDB db = WalletManager.getInstance().restorDefaultWalletDB(); //默认的walletDB
        HashMap<String,Wallet> wallets  = db.getWallets();
        Wallet w;
        for(String token:wallets.keySet()) { //遍历一个
            w = wallets.get(token);
            return w.getWalletID();
        }
        return  "";
    }

    /**
     * 获取默认的钱包
     * @return
     */
    public static Wallet getDefaultWallet() {
        TokenSet set = SamosWalletUtils.getTokenSet();
        String defaultToken = set.getDefaultToken();

        WalletDB db = WalletManager.getInstance().restorDefaultWalletDB(); //默认的walletDB
        HashMap<String,Wallet> wallets  = db.getWallets();
        return wallets.get(defaultToken);

    }


    public static String getDefautlWalletAddress() {
        Wallet wallet = getDefaultWallet();

        try {
            String rawAddresses = Mobile.getAddresses(wallet.getWalletID());
            JSONObject jsonObject = new JSONObject(rawAddresses);
            JSONArray addressArray = jsonObject.optJSONArray("addresses");
            if (addressArray != null && addressArray.length() > 0) {
                int len = addressArray.length();
                for (int i = 0; i < 1; i++) { //获取第一个地址
                    Address address = new Address();
                    address.setAddressId(String.valueOf(i + 1));
                    return String.valueOf(addressArray.get(i));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return  "";


    }


    public static String signData(String hash,  String passwd, String addr)
    {
        try {


            String walletId = getDefaultWalletID();
            String pubkey = Mobile.signData(hash, walletId, passwd, addr);
            return pubkey;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

    }


}
