package io.samos.wallet.activities.Assets

/**
 * @author: lh on 2018/6/8 17:07.
 * Email:luchefg@gmail.com
 * Description:
 */
class AssetsTypeBean {
    var tokenName: String? = null //tokenname
    var token: String? = null //tokename
    var isSele: Boolean? = null
    var id: String? = null
    var imageUrl: String? = null //iconurl

    constructor(tokenName: String?, token: String?, isSele: Boolean?, id: String?, imageUrl: String?) {
        this.tokenName = tokenName
        this.token = token
        this.isSele = isSele
        this.id = id
        this.imageUrl = imageUrl
    }
}
