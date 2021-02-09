package io.samos.wallet.datas;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.samos.wallet.base.SamosAppliacation;
import io.samos.wallet.utils.LogUtils;
import io.samos.wallet.utils.SamosWalletUtils;
import io.samos.wallet.utils.ToastUtils;
import mobile.Mobile;

/**
 * 交易管理
 * Created by kimi on 2018/2/11.</br>
 */

public class TransactionManager {

    private static final String SEPARATOR = "-";
    SharedPreferences transactionSharedPreferences;
    ArrayList<Transaction> transactions = new ArrayList<>();
    Gson gson = new Gson();

    private TransactionManager() {
        String name = "transaction_preferences";
        transactionSharedPreferences = SamosAppliacation.mInstance.getSharedPreferences(name,
                Context.MODE_PRIVATE);
    }

    public static TransactionManager getInstance() {
        return TransactionManagerHolder.mInstance;
    }

    /**
     * 保存交易记录
     */
    public boolean saveTransaction(Transaction transaction) {
        SharedPreferences.Editor edit = transactionSharedPreferences.edit();
        edit.putString(transaction.fromWallet + SEPARATOR + transaction.coinType + SEPARATOR
                + transaction.txid, gson.toJson(transaction));
        edit.commit();
        return true;
    }

    /**
     * 获取交易记录
     */
    public ArrayList<Transaction> getTransaction(List<Wallet> wallets) {
        transactions.clear();
        Map<String, String> all = (Map<String, String>) transactionSharedPreferences.getAll();
        Iterator<Map.Entry<String, String>> iterator = all.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                for (int i = 0; i < wallets.size(); i++) {
                    if (key.startsWith(
                            wallets.get(i).walletID)) { //walletId== samos_xxxxxxxx; skycoin_xxxxxx 足够区分了
                        transactions.add(gson.fromJson(next.getValue(), Transaction.class));
                    }
                }
        }
        return transactions;
    }

    /**
     * 发送交易
     * walletID：钱包ID
     * toAddr：收件人地址
     * 金额：你将发送的硬币，它的值必须是0.001的倍数。
     */
    public Transaction sendTransaction(Transaction transaction) {
        try {
            String state = Mobile.send(transaction.coinType, transaction.fromWallet,
                    transaction.toWallet, transaction.amount, SamosWalletUtils.getPin16());
            LogUtils.d("send transaction result = " + state);
            //发送成功的话，缓存到本地
            transaction.txid = gson.fromJson(state, Transaction.class).txid;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            transaction.time = formatter.format(System.currentTimeMillis());
            saveTransaction(transaction);
            return transaction;
        } catch(Exception ex) {
//            ToastUtils.show("Send Transaction Error");
            ex.printStackTrace();
        }
        return null;
    }


    private static class TransactionManagerHolder {
        private static final TransactionManager mInstance = new TransactionManager();
    }
}
