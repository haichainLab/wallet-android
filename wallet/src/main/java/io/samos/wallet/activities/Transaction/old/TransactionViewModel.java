package io.samos.wallet.activities.Transaction.old;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.samos.wallet.api.ApiService;
import io.samos.wallet.api.RetrofitService;
import io.samos.wallet.beans.TransactionInfo;
import io.samos.wallet.datas.Transaction;
import io.samos.wallet.datas.TransactionManager;
import io.samos.wallet.datas.Wallet;
import io.samos.wallet.utils.LogUtils;
import mobile.Mobile;

/**
 *
 * @author kimi
 * @date 2018/2/11
 */

public class TransactionViewModel {


    public      Observable<String> getAllTransaction(final String token,final String address) {
        String ts = System.currentTimeMillis()+"";
        return RetrofitService.getInstance().retrofit.create(ApiService.class).getTransaction(token,address,ts);

    }

        /**
         * 查询本地缓存的所有交易记录
         */
    public Observable<ArrayList<TransactionInfo>> getAllTransaction(final List<Wallet> wallets) {
        return Observable.create(new ObservableOnSubscribe<ArrayList<Transaction>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<Transaction>> emitter) {
                emitter.onNext(
                        TransactionManager.getInstance().getTransaction(wallets));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<ArrayList<Transaction>, ArrayList<TransactionInfo>>() {
                    @Override
                    public ArrayList<TransactionInfo> apply(ArrayList<Transaction> transactions) {
                        ArrayList<TransactionInfo> result = new ArrayList<>();
                        Gson gson = new Gson();
                        for (Transaction transaction : transactions) {
                            try {
                                String valuesJson = Mobile.getTransactionByID(transaction.coinType,
                                        transaction.txid);
                                TransactionInfo transactionInfo = gson.fromJson(valuesJson,
                                        TransactionInfo.class);
                                transactionInfo.setTime(transaction.time);
                                transactionInfo.setToWallet(transaction.toWallet);
                                transactionInfo.setAmount(transaction.amount);
                                transactionInfo.setCoinType(transaction.coinType);
                                transactionInfo.setFromWallet(transaction.fromWallet);
                                result.add(transactionInfo);
                            } catch(Exception ex) {
                                LogUtils.d("Get Transaction Failed");
                            }
                        }
                        return result;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送交易
     */
    public Observable<Transaction> sendTransaction(final Transaction transaction) {
        return Observable.create(new ObservableOnSubscribe<Transaction>() {
            @Override
            public void subscribe(ObservableEmitter<Transaction> emitter) {
                emitter.onNext(TransactionManager.getInstance().sendTransaction(transaction));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
