package io.samos.wallet.activities.Main

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.samos.wallet.R

import io.samos.wallet.common.OnItemClickListener
import io.samos.wallet.datas.WalletDB
//import io.samos.wallet.datas.WalletIndex
import kotlinx.android.synthetic.main.item_main_slide.view.*

//策划
class WalletSlideAdapter(var walletDBList: List<WalletDB>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * 回调接口
     */
    var mainWalletListener: OnItemClickListener<WalletDB>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return MainWalletViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main_slide, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tag = position
        holder.itemView.wallet_image.setBackgroundResource(walletDBList!![position].avatarResourceId)
        holder.itemView.wallet_name.text = walletDBList?.get(position)?.name
        holder.itemView.setOnClickListener({ it ->
            mainWalletListener?.onClick(it.id, position, walletDBList?.get(position))
        })
        if (walletDBList!![position].isSelected) {
            holder.itemView.setBackgroundColor(Color.parseColor("#efeeda"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#fcfbf0"))
        }
    }

    override fun getItemCount(): Int {
        return walletDBList?.size ?: 0
    }


    /**
     * 钱包对应的holder
     */
    class MainWalletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}