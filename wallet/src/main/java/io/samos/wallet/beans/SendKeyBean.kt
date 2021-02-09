package io.samos.wallet.beans

/**
 * @author: lh on 2018/1/25 15:15.
 * Email:luchefg@gmail.com
 * Description: SendKey弹出框信息展示
 */

class SendKeyBean {

    var num: String? = null
    var dollar: String? = null
    var status: String? = null
    var from: String? = null
    var date: String? = null
    var notes: String? = null
    var to: String? = null
    var time: String? = null

    override fun toString(): String {

        return "SendKeyBean{" +
                "num='" + num + '\'' +
                ", dollar='" + dollar + '\'' +
                ", status='" + status + '\'' +
                ", from='" + from + '\'' +
                ", date='" + date + '\'' +
                ", notes='" + notes + '\'' +
                ", to='" + to + '\'' +
                ", time='" + time + '\'' +
                '}'
    }
}