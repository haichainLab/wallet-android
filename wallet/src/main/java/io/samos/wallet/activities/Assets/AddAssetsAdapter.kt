package io.samos.wallet.activities.Assets

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import io.samos.wallet.R
import io.samos.wallet.beans.TokenSet
import io.samos.wallet.common.Constant
import io.samos.wallet.utils.ImageTools
import io.samos.wallet.utils.ImageUtils
import io.samos.wallet.utils.SamosWalletUtils
import kotlinx.android.synthetic.main.item_add_assets.view.*
import kotlinx.android.synthetic.main.item_main_wallet.view.*

/**
 * @author: lh on 2018/5/17 15:11.
 * Email:luchefg@gmail.com
 * Description: 添加新资产
 */

class AddAssetsAdapter(var assets: List<AssetsTypeBean>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * 回调接口
     */
    var assetListener: AddAssetsListener? = null
    var tokenSet = SamosWalletUtils.getTokenSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return AssetsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_add_assets,
                null))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tv_name.text = assets!![position].token
      //  holder.itemView.iv_logo.setBackgroundResource(R.drawable.sky_logo)

        ImageTools.loadIcon(holder.itemView.iv_logo,assets!![position].imageUrl!!)


        if (TextUtils.equals(tokenSet.defaultToken, assets!![position].token)) {
            holder.itemView.switch_on.visibility = View.GONE
            holder.itemView.switch_on.isEnabled = false
        }
        holder.itemView.tv_hint.text = assets!![position].tokenName!!.capitalize()


        holder.itemView.switch_on.isChecked = assets!![position].isSele!!
        holder.itemView.switch_on.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            assetListener!!.onCheckClick(
                    position, b, assets?.get(position)!!
            )
        }
    }

    override fun getItemCount(): Int {
        return assets?.size ?: 0
    }


    class AssetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
