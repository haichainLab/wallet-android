package io.samos.wallet.datas

/**
 * Created by hanyouhong on 2018/10/11.
 */

/**
 * Created by Administrator
 * on 2018/8/7.
 */
data class WalletCfg(
        var tokens: List<TokenCfg>,
        var weburl: String
)

data class TokenCfg(
        var tokenName: String,
        var token: String,
        var tokenIcon: String,
        var hostApi: String
//        var price_usd: String,
//        var price_btc: String,
//        var price_cny: String
)