package io.samos.wallet.db

import java.io.Serializable

/*
/// token全称：samos、skycoin、yongbang、shihu
@property (nonatomic, copy) NSString *tokenName;
/// token简称：SAMO、SKY、YBC、SHC
@property (nonatomic, copy) NSString *token;
/// token图标：icon有两个，以逗号分隔
@property (nonatomic, copy) NSString *tokenIcon;
/// 交易地址，交易地址有两个，以逗号分隔,默认现在一个
@property (nonatomic, copy) NSString *hostApi;
/// 币种的类型，比如SKY/ETH/BTC ...
@property (nonatomic, copy) NSString *tokenType;
/// 是否显示币时
@property (nonatomic, assign) BOOL coinHour;
/// 币显示的顺序
@property (nonatomic, copy) NSString *seq;

 */

class SAMTokenInfo : Serializable {
    var tokenName: String? = null
    var token: String? = null
    var tokenIcon: String? = null
    var hostApi: String? = null
    var tokenType: String? = null
    var seq: String? = null
    var coinHour:Boolean?=true

}
