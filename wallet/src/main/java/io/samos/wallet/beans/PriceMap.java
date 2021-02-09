package io.samos.wallet.beans;

import java.util.HashMap;

/**
 * Created by hanyouhong on 2018/10/11.
 */

public class PriceMap {
    HashMap<String,TokenPrice> priceMap;

    PriceMap() {
        priceMap =new HashMap<String,TokenPrice>();
    }



    public void addPrice(String token,TokenPrice price) {
        priceMap.put(token,price);
       }

    public TokenPrice getPrice(String token) {
        if(priceMap.containsKey(token)) {
            return priceMap.get(token);
        }
        return new TokenPrice(token);
    }
}
