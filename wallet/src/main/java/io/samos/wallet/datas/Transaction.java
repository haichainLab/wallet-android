package io.samos.wallet.datas;

/**
 * Created by zjy on 2018/2/10.
 * 交易发送信息
 */

public class Transaction {
    public String coinType;
    public String txnType;
    public String txid;
    public String fromWallet;
    public String toWallet;
    public String amount;
    public String nodes;
    public String state;
    public String time;

    public Transaction(String coinType, String txnType, String txid, String fromWallet,
            String toWallet, String amount, String nodes, String state, String time) {
        this.coinType = coinType;
        this.txnType = txnType;
        this.txid = txid;
        this.fromWallet = fromWallet;
        this.toWallet = toWallet;
        this.amount = amount;
        this.nodes = nodes;
        this.state = state;
        this.time = time;
    }

    public Transaction() {
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "coinType='" + coinType + '\'' +
                ", txnType='" + txnType + '\'' +
                ", txid='" + txid + '\'' +
                ", fromWallet='" + fromWallet + '\'' +
                ", toWallet='" + toWallet + '\'' +
                ", amount='" + amount + '\'' +
                ", nodes='" + nodes + '\'' +
                ", state='" + state + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getFromWallet() {
        return fromWallet;
    }

    public void setFromWallet(String fromWallet) {
        this.fromWallet = fromWallet;
    }

    public String getToWallet() {
        return toWallet;
    }

    public void setToWallet(String toWallet) {
        this.toWallet = toWallet;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
