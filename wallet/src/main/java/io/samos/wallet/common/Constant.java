package io.samos.wallet.common;


import android.Manifest;

/**
 * Created by kimi on 2018/1/24.
 */

public interface Constant {

    String APP_ID = "samos";

    String TOKEN_PRCIE = "token_price";
    String TOKEN_CONF = "token_config";

    String  WALLET_DEFAULT_WALLET_DB = "samos_default_wallet_db_v11";


     String  WALLET_DB_PREFERENCES = "samos_wallet_db_preferences_v31";

    String  WALLET_PREFERENCES = "samos_wallet_config_preferences_v31";













    Integer DEFAULT_WALLET_NUM = 1; //默认钱包地址

    String WALLET_STORE_DIR = "samos_data_v3";
  //  String WALLET_CONFIG = "samos_wallet_config_v3"; //配置文件名字
    String WALLET_INDEX =  "samos_wallet_db_v3";

   // String  WALLET_PREFERENCES = "samos_wallet_preferences_v3";
    String  WALLET_INDEX_PREFERENCES = "samos_walletindex_preferences_v3";
    String  WALLET_DEFAULT_WALLET_INDEX = "samos_default_wallet_index_v3";

    /**
     * sp keys
     */
    String KEY_PIN = "key_pin";

    /**
     * haic支付回调验签key
     * lwj
     */
    String SIGN_KEY = "XPQJfOgYOQufnXpM";



    String COIN_TYPE_SKY = "skycoin";
    String COIN_TYPE_SAMO = "samos";
    String COIN_TYPE_YBC = "yongbang";
    String COIN_TYPE_SHC = "shihu";


    String COIN_SAMO = "SAMO";
    String COIN_SKY = "SKY";
    String COIN_YBC = "YBC";
    String COIN_SHC = "SHC";



    String COIN_TYPE_SUN = "suncoin";
    String KEY_WALLET_ID = "wallet_id";

    String IS_BACKUP_WALLET = "0";

    String KEY_WALLET = "wallet";
    String KEY_TRANS = "transaction";
    String KEY_PAGE = "page";
    String KEY_ADDRESS = "address";

    String EXCHAGECOIN = "ExchangeCoin";


    String CNYEXCHAGECOIN = "cnyExchangeCoin";
    String USDEXCHAGECOIN = "usdExchangeCoin";
    String IS_LANGUAGE_ZH = "is_language_en";
    String IS_UNIT_CNY = "is_unit_currency";
    String SELE_LANGUAGE = "language";
    String SELE_UNIT = "unit";
    String CH = "chinese";
    String EN = "english";
    String KEY_SHOW_WALLETS = "keyShowWallets";
    String PIN_HINt = "PIN_HINT";
    /*交易类型*/
    String TX_OUT = "out";
    String TX_INTO = "into";
    //扫码标识
    String SCAN_FLAG = "scan";
  //  String COIN_TYPE = "Type";
    String TOKEN_TYPE = "Token";
    String CNY = "CNY";
    String USD = "USD";


    /**
     * request code
     */
    int REQUEST_QRCODE = 202;


    /**
     * 6.0动态权限，sd卡读写操作和相机二维码扫描
     */
    String[] ALL_PERMISSIONS = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    /**
     * 所有动态权限对应的请求码
     */
    int ALL_RERMISSIONS_REQUEST_CODE = 101;
}
