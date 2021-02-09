package io.samos.wallet.activities.Transaction

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.samos.wallet.R
import io.samos.wallet.beans.TransInfo
import io.samos.wallet.beans.TransactionInfo
import io.samos.wallet.common.Constant
import kotlinx.android.synthetic.main.dialog_rollout_payment_detail.view.*
import kotlinx.android.synthetic.main.item_wallet_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(var context: Context, var transaction: ArrayList<TransInfo>?) : RecyclerView
.Adapter<RecyclerView.ViewHolder>() {
    /**
     * 回调接口
     */
    var TransactionListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return MainWalletViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wallet_transaction, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tag = position
        var trans: TransInfo = transaction?.get(position)!!

        holder.itemView.setOnClickListener {
            TransactionListener?.OnClick(trans)
        }

        holder.itemView.tv_amount.text = trans.delta
        holder.itemView.tv_state.text = trans.state



        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var str = trans.time+"000"


        val dt = dateFormat.format(str.toLong())
        holder.itemView.tv_date.text = dt
        holder.itemView.tv_address.text = trans.txid
    }

    override fun getItemCount(): Int {
        return transaction?.size ?: 0
    }


    /**
     * 钱包对应的holder
     */
    class MainWalletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_address: TextView
        var tv_state: TextView
        var tv_amount: TextView
        var tv_date: TextView

        init {
            tv_address = itemView.findViewById(R.id.tv_address)
            tv_state = itemView.findViewById(R.id.tv_state)
            tv_amount = itemView.findViewById(R.id.tv_amount)
            tv_date = itemView.findViewById(R.id.tv_date)

        }

    }

    interface OnItemClickListener {
        fun OnClick(trans: TransInfo?)
    }
}