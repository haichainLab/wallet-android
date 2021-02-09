package io.samos.wallet.activities.Transaction

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.samos.wallet.R
import io.samos.wallet.common.OnItemClickListener
import io.samos.wallet.datas.Address
import kotlinx.android.synthetic.main.item_rull_into.view.*

/**
 * @author: lh on 2018/5/23 16:17.
 * Email:luchefg@gmail.com
 * Description:
 */
class RullIntoAdapter(var note: List<Address>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onItemClick: OnItemClickListener<Address>

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R
                .layout.item_rull_into,
                null))
    }

    override fun getItemCount(): Int {
        return note?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder!!.itemView.tv_adress.text = note?.get(position)!!.address

        holder.itemView.tv_num.text = note!!.get(position).addresBalance

        holder.itemView.setOnClickListener {
            this.onItemClick.onClick(holder.itemView.id, position, note?.get(position))
        }.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}