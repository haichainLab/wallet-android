package io.samos.wallet.activities.Alerts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import io.samos.wallet.R
import io.samos.wallet.activities.Assets.AddAssetsAdapter
import io.samos.wallet.common.OnItemClickListener

/**
 * @author: lh on 2018/5/23 16:17.
 * Email:luchefg@gmail.com
 * Description:
 */
class AlertsAdapter(var note: List<String>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onItemClick: OnItemClickListener<String>

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return AddAssetsAdapter.AssetsViewHolder(LayoutInflater.from(parent!!.context).inflate(R
                .layout.item_alerts,
                null))
    }

    override fun getItemCount(): Int {
        return note?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        var hd: ViewHolder = holder as ViewHolder
        hd.tv_adress.text = note?.get(position)
                ?: hd.lin_content.setOnClickListener {
            this.onItemClick.onClick(it.id, position, note?.get(position))
        }.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var lin_content: LinearLayout
        var tv_adress: TextView
        var tv_date: TextView
        var tx_type: TextView


        init {
            lin_content = itemView.findViewById(R.id.lin_content)
            tv_date = itemView.findViewById(R.id.tv_date)
            tv_adress = itemView.findViewById(R.id.tv_adress)
            tx_type = itemView.findViewById(R.id.tx_type)
        }
    }


}