package io.samos.wallet.activities.ManagerWallet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.samos.wallet.R
import io.samos.wallet.common.OnItemClickListener
import io.samos.wallet.datas.WalletDB
import kotlinx.android.synthetic.main.item_manager_wallet.view.*

/**
 * @author: lh on 2018/5/23 16:17.
 * Email:luchefg@gmail.com
 * Description:
 */
class ManagerWalletAdapter(var wallet: List<WalletDB>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>
() {

    lateinit var onItemClick: OnItemClickListener<WalletDB>

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R
                .layout.item_manager_wallet,
                null))
    }

    override fun getItemCount(): Int {
        return wallet?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder!!.itemView.tv_name.text = wallet?.get(position)!!.name
        holder.itemView.iv_icon.setImageResource( wallet?.get(position)!!.avatarResourceId)
        holder.itemView.setOnClickListener {
            onItemClick.onClick(it.id, position, wallet?.get(position))
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}