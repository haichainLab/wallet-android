package io.samos.wallet.activities.Main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.samos.wallet.R
import io.samos.wallet.beans.ExchangeBean
import io.samos.wallet.beans.PriceBean
import io.samos.wallet.beans.Token
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.TokenCfg
import io.samos.wallet.datas.Wallet
import io.samos.wallet.utils.ImageTools
import io.samos.wallet.utils.ImageUtils
import io.samos.wallet.utils.SharePrefrencesUtil
import kotlinx.android.synthetic.main.item_main_wallet.view.*
import java.text.DecimalFormat

/**
 * 钱包主页视图碎片适配器
 * Created by kimi on 2018/1/29.
 * Edit by 方丈 on 2018/05/11 修改为kotlin，精简代码
 */

class WalletAdapter(var wallets: List<Wallet>?,var tokenCfgs
: List<Token>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var WalletViewModel: WalletViewModel = WalletViewModel()
//    var beanEx = WalletViewModel.allExchange
    var nBalance: Double = 0.0

    //货币单位
    var exchangeCoinCny: Boolean = SharePrefrencesUtil.getInstance().getBoolean(Constant.IS_UNIT_CNY)
    var decimalFormat = DecimalFormat("#.###")
    //交易所价格
//    var exchangeBean: ExchangeBean = ExchangeBean();
    var priceBean:PriceBean = PriceBean()
    /**
     * 回调接口
     */
    var mainWalletListener: WalletListener? = null
    private val onClickListener = View.OnClickListener { v ->
        if (mainWalletListener == null) {
            return@OnClickListener
        }

        val position = v.tag as Int
        mainWalletListener!!.onItemClick(position, wallets?.get(position)!!)
    }

    fun setCnyBoolean(isCny: Boolean) {
        this.exchangeCoinCny = isCny
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return MainWalletViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main_wallet, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.rly_content.tag = position
        nBalance = wallets?.get(position)?.balance!!

        //加载图片
        ImageTools.loadIcon(holder.itemView.iv_token_icon, tokenCfgs[position].tokenIcon)

      //  var coinType = tokenCfgs[position].tokenName.toLowerCase();

        var token = tokenCfgs[position].token
        priceBean = WalletViewModel().priceBean


        //设置价格
        if (exchangeCoinCny) {
            nBalance *= priceBean.getPrice(token, "cny")
            holder.itemView.tv_token_total_price.text = "￥" + decimalFormat.format(nBalance)


            //lwj 0.0.1 HAI->HAIC
            if(tokenCfgs[position].token.equals("HAI")){
                holder.itemView.tv_token_price.text = "1 HAIC = ￥ " +
                        decimalFormat.format(priceBean.getPrice(token, "cny"))
            }else{
                holder.itemView.tv_token_price.text = "1 " + tokenCfgs[position].token + " = ￥ " +
                        decimalFormat.format(priceBean.getPrice(token, "cny"))
            }


        } else {
            nBalance *= priceBean.getPrice(token, "usd")
            holder.itemView.tv_token_total_price.text = "$" + decimalFormat.format(nBalance)


            //lwj 0.0.1 HAI->HAIC
            if(tokenCfgs[position].token.equals("HAI")){
                holder.itemView.tv_token_price.text = "1 HAIC = $ " +
                        decimalFormat.format(priceBean.getPrice(token, "usd"))
            }else{
                holder.itemView.tv_token_price.text = "1 " + tokenCfgs[position].token + "= $ " +
                        decimalFormat.format(priceBean.getPrice(token, "usd"))
            }

        }

        holder.itemView.rly_content.setOnClickListener(onClickListener)

        holder.itemView.tv_token_name.text = tokenCfgs[position].tokenName.capitalize()
        if (wallets?.get(position)?.balance != null) {
            holder.itemView.tv_token_count.text = "" + wallets?.get(position)?.balance
        } else {
            holder.itemView.tv_token_count.text = "0.0"
        }
    }



    override fun getItemCount(): Int {
        return wallets?.size ?: 0
    }


    /**
     * 钱包对应的holder
     */
    class MainWalletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
