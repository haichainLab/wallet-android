package io.samos.wallet.api;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.HEAD;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

/**
 * @author: lh on 2018/1/23 11:17.
 * Email:luchefg@gmail.com
 * Description: 钱包Api接口
 */

public interface ApiService {

//    @GET("v1/ticker/skycoin/?convert=CNY")
//    Observable<String> getCNYcoinExchange();
//
//    @GET("v1/ticker/skycoin/?convert=USD")
//    Observable<String> getUSDcoinExchange();

//    @GET("api/price?token=all")
//    Observable<String> getAllcoinExchange();
//
//
//
//    @GET("wallet-cn.cfg")
//    Observable<String> getWalletCfg();

    @HEAD
    Observable<Response<Void>> executeGetWithHeaders(@Url String url, @QueryMap Map<String, String> maps);

// xxl real
    @GET("api/transaction")
    Observable<String> getTransaction(@Query("token") String token,@Query("address") String address,@Query("ts") String ts);

    @GET("api/token-price")
    Observable<String> getTokenPrice(@Query("ts") String ts);

//xxl just for test
//    @GET("api/transaction.php")
//    Observable<String> getTransaction(@Query("token") String token,@Query("address") String address,@Query("ts") String ts);
//
//    @GET("api/token-price.php")
//    Observable<String> getTokenPrice(@Query("ts") String ts);


    @GET("api/version")
    Observable<String> getAppVersion(@Query("tag") String token,@Query("ts") String ts);

    @GET("config/galt-token-enc.cfg")
    Observable<String> getTokenCfg(@Query("ts") String ts);

    //lwj 电商支付回调通知
    @POST
    Observable<String> postPayResult(@Url String url
            ,@Query("trade_no") String trade_no
            ,@Query("trade_status") String trade_status
            ,@Query("out_trade_no") String out_trade_no
            ,@Query("total_amount") String total_amount
            ,@Query("sign") String sign
    );

    //lwj 投票回调通知
    @POST
    @FormUrlEncoded
    Observable<String> postVoteResult(@Url String url
            ,@Field("address") String address
            ,@Field("vote_no") String vote_no
            ,@Field("vote_result") String vote_result

    );

}
