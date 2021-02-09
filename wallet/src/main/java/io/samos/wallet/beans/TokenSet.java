package io.samos.wallet.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by hanyouhong on 2018/10/11.
 */

public class TokenSet implements Serializable {
    String weburl;
    int version;

    public String getDefaultToken() {
        if(defaultToken != null && !"".equals(defaultToken) && tokens != null) {
            for(Token t:tokens) {
                if( defaultToken.equals(t.token)) {

//                    //xxl add for HAI -> HAIC
//                    if(defaultToken.equals("HAI")){
//                        defaultToken = "HAIC";
//                    }

                    return defaultToken;
                }
            }
        }
        return "SAMO";
    }

    public void setDefaultToken(String defaultToken) {
        this.defaultToken = defaultToken;
    }


    String defaultToken;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    LinkedList<Token> tokens;

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public LinkedList<Token> getTokens() {
        Collections.sort(tokens);
        return tokens;
    }

    public void setTokens(LinkedList<Token> tokens) {
        this.tokens = tokens;
    }

    TokenSet()  {

        tokens  = new LinkedList<Token>();

    }
    public void addToken(Token token) {

         tokens.add(token);
    }

    public void removeToken(Token token) {
         tokens.remove(token);
    }


    public Token getToken(String type) {
        for(Token token:tokens) {
            if(type.equals(token.getToken())) {
                return token;
            }
        }
        return null;
    }

    public String getTokenName(String token) {
        Token t = getToken(token);
        if(t != null) {
            return t.getTokenName();

        }
        return "";

    }

    public String getTokenByName(String tokenName) {
        for(Token token:tokens) {
            if(tokenName.equals(token.getTokenName())) {
                return token.getToken();
            }
        }
        return "";
    }

    public String getIconByToken(String token) {
        Token s = getToken(token);
        if(s != null) {
            return s.getTokenIcon();
        }
        return "";
    }

    public String getExplorerUrlByToken(String token) {
        Token s = getToken(token);
        if(s != null) {
            return s.getExplorerUrl();
        }
        return "";
    }
    public boolean enableCoinHOur(String token) {
        Token t = getToken(token);
        if(t != null) {
            return t.isCoinHour();
        }
        return false;
    }
}
