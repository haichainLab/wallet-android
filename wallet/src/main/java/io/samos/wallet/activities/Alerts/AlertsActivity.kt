package io.samos.wallet.activities.Alerts

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import io.samos.wallet.R
import io.samos.wallet.activities.Transaction.TransRecordsActivity
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.OnItemClickListener
import kotlinx.android.synthetic.main.activity_alerts_list.*
import kotlinx.android.synthetic.main.fragment_main_wallet.*

/**
 * @author: lh on 2018/5/23 15:51.
 * Email:luchefg@gmail.com
 * Description: 通知列表
 */
class AlertsActivity : BaseActivity() ,OnItemClickListener<String>{


    override fun onClick(viewId: Int, positon: Int, bean: String?) {
        intent = Intent(this,TransRecordsActivity::class.java)
        startActivity(intent)
    }

    lateinit var adapter:AlertsAdapter

    override fun attachLayoutRes(): Int {
        return R.layout.activity_alerts_list
    }

    override fun initViews() {
        id_toolbar.setNavigationOnClickListener {
            finish()
        }
        id_toolbar_title.text = resources.getString(R.string.alerts)
        right.setOnClickListener {

        }
        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = AlertsAdapter(null)
        recyclerview.adapter = adapter
    }

    override fun initData() {
    }

}