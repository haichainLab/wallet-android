package io.samos.wallet.beans;

import java.io.Serializable;

/**
 * Created by hanyouhong on 2018/10/11.
 */

public class Token implements  Comparable < Token >  {
    public String tokenName;
    public String token;

    public int seq;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String tokenIcon;
    public String hostApi;
    public String explorerUrl; //SKY,ETH,EOS

    public String getExplorerUrl() {
        return explorerUrl;
    }

    public void setExplorerUrl(String explorerUrl) {
        this.explorerUrl = explorerUrl;
    }

    public String tokenType; //SKY,ETH,EOS


    public boolean coinHour=false;

    public boolean isCoinHour() {
        return coinHour;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setCoinHour(boolean coinHour) {
        this.coinHour = coinHour;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getToken() {

        //xxl add for HAI -> HAIC
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

    public String getTokenIcon() {
        return tokenIcon;
    }

    public void setTokenIcon(String tokenIcon) {
        this.tokenIcon = tokenIcon;
    }

    public String getHostApi() {
        return hostApi;
    }

    public void setHostApi(String hostApi) {
        this.hostApi = hostApi;
    }

    Token() {

    }
    public int compareTo(Token p) {
        if (this.getSeq() < p.getSeq()) {
            return -1;

        } else if (this.getSeq() > p.getSeq()) {
            return  1;
        } else {
            return 0;
        }
    }

}
