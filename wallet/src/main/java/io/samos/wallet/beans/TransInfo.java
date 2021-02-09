package io.samos.wallet.beans;

import java.io.Serializable;

/**
 * Created by hanyouhong on 2018/10/15.
 */

public class TransInfo implements Serializable {
    Boolean status;
    String delta;
    String time;
    String txid;
    String token;
    String inputs;
    String outputs;

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

    public String getInputs() {
        return inputs;
    }

    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    public String getOutputs() {
        return outputs;
    }

    public void setOutputs(String outpus) {
        this.outputs = outpus;
    }

    public String getState() {
        if(status) {
          return "confirmed";
        } else {
            return "unconfirmed";
        }
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }
}
