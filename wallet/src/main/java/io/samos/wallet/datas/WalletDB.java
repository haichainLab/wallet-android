package io.samos.wallet.datas;

import java.io.Serializable;
import java.util.HashMap;

import io.samos.wallet.beans.Token;
import io.samos.wallet.beans.TokenSet;

/**
 * Created by hanyouhong on 2018/10/11.
 */

public class WalletDB implements Serializable {
    private static final long serialVersionUID = 4L;
    public String name;
    public int avatarResourceId;
    public boolean selected; //是不是当前的
    HashMap<String,Wallet> tokenWlt;


    //一个钱包里面有多少个支持的Token

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatarResourceId() {
        return avatarResourceId;
    }

    public void setAvatarResourceId(int avatarResourceId) {
        this.avatarResourceId = avatarResourceId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public HashMap<String, Wallet> getTokenWlt() {
        return tokenWlt;
    }

    public void setTokenWlt(HashMap<String, Wallet> tokenWlt) {
        this.tokenWlt = tokenWlt;
    }


    public WalletDB(String name, int resourceId) {
        this.name = name;
        avatarResourceId = resourceId;
        tokenWlt = new HashMap<String,Wallet>();
    }

    public WalletDB() {
        tokenWlt = new HashMap<String,Wallet>();
    }

    public void addWallet(String token,Wallet wlt) {
        tokenWlt.put(token,wlt);
    }

    public Wallet getWlt(String token) {
        return tokenWlt.get(token);
    }

    public Wallet getWalltByTokenName(String tokenName) {
        for(String token:tokenWlt.keySet()) {
            Wallet t = tokenWlt.get(token);
            if(tokenName.equals(t.tokenName)) {
                return t;
            }
        }
        return null;
    }

    public   HashMap<String,Wallet> getWallets() {
        return tokenWlt;
    }


    public void save() {
        WalletManager.getInstance().saveWalletDB(this);
    }

}
