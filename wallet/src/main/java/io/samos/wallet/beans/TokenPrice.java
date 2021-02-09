package io.samos.wallet.beans;

import java.io.Serializable;

/**
 * Created by hanyouhong on 2018/10/11.
 */

public class TokenPrice implements Serializable{



    Double btc;
    Double cny;
    Double usd;
    String token;

    TokenPrice(String token) {
        btc=0.0d;
        cny=0.0d;
        usd=0.0d;
        token=token;
    }
    public Double getBtc() {
        return btc;
    }

    public void setBtc(Double btc) {
        this.btc = btc;
    }

    public Double getCny() {
        return cny;
    }

    public void setCny(Double cny) {
        this.cny = cny;
    }

    public Double getUsd() {
        return usd;
    }

    public void setUsd(Double usd) {
        this.usd = usd;
    }

    public String getToken() {

//        if(token.equals("HAI")){
//            return "HAIC";
//        }else{
//            return token;
//        }

        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getPrice(String type) {
        if("btc".equals(type)) {
            return btc;
        } else if("cny".equals(type)) {
            return cny;
        } else if("usd".equals(type)) {
            return usd;
        }
        return 0.0d;
    }
}
