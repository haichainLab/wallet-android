package io.samos.wallet.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hanyouhong on 2018/10/15.
 */

public class TransList implements Serializable {
    ArrayList<TransInfo> transList;
    String exploerUrl;

    public String getExploerUrl() {
        return exploerUrl;
    }

    public void setExploerUrl(String exploerUrl) {
        this.exploerUrl = exploerUrl;
    }

    public ArrayList<TransInfo> getTransList() {
        return transList;
    }

    public void setTransList(ArrayList<TransInfo> transList) {
        this.transList = transList;
    }
}
