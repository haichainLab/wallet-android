package io.samos.wallet.activities.Main;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.samos.wallet.api.ApiService;
import io.samos.wallet.api.RetrofitService;
import io.samos.wallet.beans.PriceBean;
import io.samos.wallet.beans.Token;
import io.samos.wallet.beans.TokenSet;
import io.samos.wallet.common.Constant;
import io.samos.wallet.datas.Address;
import io.samos.wallet.datas.Wallet;
import io.samos.wallet.datas.WalletDB;
import io.samos.wallet.datas.WalletManager;
import io.samos.wallet.utils.LogUtils;
import io.samos.wallet.utils.SharePrefrencesUtil;
import io.samos.wallet.utils.SamosWalletUtils;
import mobile.Mobile;

/**
 * 钱包控制层
 * Created by kimi on 2018/2/12.</br>
 */

public class WalletViewModel {

    public PriceBean getPriceBean() {
        String bean = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_PRCIE);
        return new Gson().fromJson(bean, PriceBean.class);
    }




   
    /**
     *
     */
    public static List<Wallet> wallets;






    /**
     * 获取多币钱包下的所有单币种钱包详情
     */
    public Observable<List<Wallet>> getAllDetailWallets(final WalletDB index) {
        return Observable.create(new ObservableOnSubscribe<WalletDB>() {
            @Override
            public void subscribe(ObservableEmitter<WalletDB> emitter) {
                emitter.onNext(index);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<WalletDB, List<Wallet>>() {
                    @Override
                    public List<Wallet> apply(WalletDB wallets) {
                        LinkedList<Wallet> result = new LinkedList<>();


                        TokenSet tokenSet = SamosWalletUtils.getTokenSet();
                        HashMap<String,Wallet> mapWlts = wallets.getWallets();
                        for(String token:mapWlts.keySet()) {
                            Wallet wallet = mapWlts.get(token);
                            String walletBalanceJson = null;
                            double walletBalance = 0.0d;
                            long hours = 0;


                            if(wallet != null && wallet.isSelected()) {//被选中了
                                String tokenName = tokenSet.getTokenName(token);

                                try {
                                    if(!"".equals(tokenName)) { //新的配置文件里面，有可能TokenSet里面不再支持特定的币种
                                        Log.d("wallet", tokenName);
                                        walletBalanceJson = Mobile.getWalletBalance(tokenName, wallet.getWalletID());//api
                                        walletBalance = Wallet.getBalanceFromRawData(walletBalanceJson);
                                        hours = Wallet.getHoursFromRawData(walletBalanceJson);

                                        wallet.setBalance(walletBalance);
                                        wallet.setHours(hours);

                                        Token t = tokenSet.getToken(wallet.getToken());
                                        wallet.setSeq(t.getSeq());

                                        mapWlts.put(token, wallet);
                                        result.add(wallet);
                                    } else {
                                        LogUtils.d("token:"+token+" not supported");
                                    }
                                } catch(Exception ex) {
                                    ex.printStackTrace();
                                }

                            }


                        }
                        Collections.sort(result);
                        return result;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询所有的本地钱包
     */
    public Observable<List<WalletDB>> getAllWalletDB() {
        return Observable.create(new ObservableOnSubscribe<List<WalletDB>>() {
            @Override
            public void subscribe(ObservableEmitter<List<WalletDB>> emitter) {
                emitter.onNext(WalletManager.getInstance().getAllWalletDB());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<List<WalletDB>, List<WalletDB>>() {
                    @Override
                    public List<WalletDB> apply(List<WalletDB> wallets) {
                        return wallets;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取当前钱包已使用的所有地址
     *
     * @param tokenName 钱包类型
     * @param walletID   钱包id
     */
    public Observable<List<Address>> getAllAddressByWalletId(final String tokenName,
            final String walletID) {
        return Observable.create(new ObservableOnSubscribe<List<Address>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Address>> emitter) {
                emitter.onNext(
                        WalletManager.getInstance().getAddressesByWalletId(tokenName, walletID));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 备份钱包,根据钱包id获取钱包种子
     */
    public Observable<String> getWalletSeed(final String walletId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String seed = Mobile.getSeed(walletId, SamosWalletUtils.getPin16());
                emitter.onNext(seed);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 删除单币钱包
     */
    public Observable<Boolean> deleteWallet(final List<Wallet> wallet, final WalletDB walletIndex) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                WalletManager.getInstance().removeWallet(wallet, walletIndex);
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 新增钱包
     */
    public Observable<Boolean> addWallet(final Context context, final String tokenName,
            final String name, final String
            seed) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                emitter.onNext(
                        WalletManager.getInstance().addWalletDB(context, tokenName, name, seed));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }



    public Observable<String> getTokenPrice() {
        String ts = System.currentTimeMillis()+"";

        return RetrofitService.getInstance().retrofit.create(ApiService.class).getTokenPrice(ts);
    }



    /**
     * get token config from network
     * @return
     */
    public Observable<String> getTokenCfg() {
        String ts = System.currentTimeMillis()+"";
        return RetrofitService.getInstance().retrofit.create(ApiService.class).getTokenCfg(ts);
    }

    /**
     * get token config from network
     * @return
     */
    public Observable<String> getAppVersion(String tag) {
        String ts = System.currentTimeMillis()+"";
        return RetrofitService.getInstance().retrofit.create(ApiService.class).getAppVersion(tag,ts);
    }

    /**
     * post pay result
     * @return
     */
    public Observable<String> postPayResult(String callbackUrl,String txid,String rlt,String outTradeNo,String amount,String sign) {

        return RetrofitService.getInstance().retrofit.create(ApiService.class).postPayResult(
                callbackUrl
                , txid
                , rlt
                , outTradeNo
                , amount
                , sign
        );

    }

    /**
     * lwj
     * post vote result
     * @return
     */
    public Observable<String> postVoteResult(String callbackUrl,String address,String vote_no,String vote_result) {

        return RetrofitService.getInstance().retrofit.create(ApiService.class).postVoteResult(
                callbackUrl
                , address
                , vote_no
                , vote_result
        );

    }


}
