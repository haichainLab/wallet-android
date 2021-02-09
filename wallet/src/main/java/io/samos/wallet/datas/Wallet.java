package io.samos.wallet.datas;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.samos.wallet.beans.Token;

/**
 * Created by zjy on 2018/1/22.
 */

public class Wallet implements  Comparable <Wallet>,Serializable{
    private static final long serialVersionUID = 3L;

    String walletID; //samos_WHJSz...
    String walletType; //简写:SAMO
    String tokenName;//samos
    String walletName;//mywallet
    int seq = 0;

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    int walletDeep;
    public Double balance = 0.0d;
    public Long coinHour = 0l;


    public Boolean selected = true;

    public void setWalletID(String walletID) {
        this.walletID = walletID;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean isSelected() {
        return selected;
    }

    //ArrayList<String> supportCoinType;

    public Long getCoinHour() {
        return coinHour;
    }

    public void setCoinHour(Long coinHour) {
        this.coinHour = coinHour;
    }


    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }

    Long hours = 0L;


    public String getUpperTokename() {
        return  tokenName.substring(0, 1).toUpperCase() + tokenName.substring(1);
    }

    public String getBalanceStr() {
        return balance+"";
    }
    public static String getCoinHourStr(String rawString) {

        return String.format("%,d", getHoursFromRawData( rawString));

    }
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getWalletID() {
        return walletID;
    }


//    public Wallet(String walletType, String walletName, String walletID) {
//        this.walletType = walletType;
//        this.walletName = walletName;
//        this.walletID = walletID;
//    }
    public Wallet(String tokenName,String token, String walletName, String walletID) {
        this.tokenName = tokenName;
        this.walletType = token;
        this.walletName = walletName;
        this.walletID = walletID;
    }

    private Wallet() {
      //  supportCoinType = new ArrayList<>();
      //  supportCoinType.add("samos");
    }

    public String getWalletType() {
        return walletType;
    }

    public String getToken() {

//        if(walletType.equals("HAI")){
//            return "HAIC";
//        }else{
//            return walletType;
//        }

        return walletType;
    }

    public void setToken(String t) {
        this.walletType = t;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public int getWalletDeep() {
        return walletDeep;
    }

    public void setWalletDeep(int walletDeep) {
        this.walletDeep = walletDeep;
    }

    public Wallet restoreWalletFromLocal() {
        Wallet wallet = new Wallet();
        return wallet;
    }

    public void save() {
        io.samos.wallet.datas.WalletManager.getInstance().saveWallet(this);
    }


    @Override
    public String toString() {
        return walletName;
    }

    public static Wallet buildTestData() {
        Wallet wallet = new Wallet();
        wallet.setWalletType("sky");
        wallet.setWalletName("test");
        wallet.setWalletDeep(15);
        return wallet;
    }

    public static double getBalanceFromRawData(String rawString) {
        try {
            JSONObject jsonObject = new JSONObject(rawString);
            return jsonObject.optDouble("balance");
        } catch (JSONException e) {

            return 0d;
        }
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public static Long getHoursFromRawData(String rawString) {
        try {
            JSONObject jsonObject = new JSONObject(rawString);
            return jsonObject.optLong("hours");
        } catch (JSONException e) {

            return 0L;
        }
    }

    public int compareTo(Wallet p) {
        if (this.getSeq() < p.getSeq()) {
            return -1;

        } else if (this.getSeq() > p.getSeq()) {
            return  1;
        } else {
            return 0;
        }
    }

}
