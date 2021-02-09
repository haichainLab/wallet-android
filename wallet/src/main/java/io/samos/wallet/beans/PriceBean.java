package io.samos.wallet.beans;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by hanyouhong on 2018/10/11.
 */

public class PriceBean  implements Serializable {
    int ok;
    LinkedList<TokenPrice> data;

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public LinkedList<TokenPrice> getData() {
        return data;
    }

    public void setData(LinkedList<TokenPrice> data) {
        this.data = data;
    }

    public Double getPrice(String token, String type) {
        TokenPrice v=null;
        if (data != null) {
            for (TokenPrice item : data) {
                if (token.equals(item.getToken())) {
                    v = item;
                    break;
                }

            }
            if (v != null) {

                return v.getPrice(type);
            }
        }
        return 0.0d;

    }

}
