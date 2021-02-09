package io.samos.wallet.activities.Main

import android.content.Intent
import android.os.Bundle
import android.view.View
import io.samos.wallet.R
import io.samos.wallet.activities.About.AboutUsActivity
import io.samos.wallet.activities.Alerts.AlertsActivity
import io.samos.wallet.activities.ManagerWallet.ManagerWalletActivity
import io.samos.wallet.activities.Transaction.TransactionActivity
import io.samos.wallet.activities.settings.SystemSettingActivity
import io.samos.wallet.base.BaseFragment
import io.samos.wallet.datas.Wallet
import kotlinx.android.synthetic.main.fragment_me.*

/**
 * @author: lh on 2018/3/21 11:14.
 * Email:luchefg@gmail.com
 * Description: 主页 -我
 * edit by 方丈 on 2018/05/10 引入kotlin anko
 */

class MeFragment : BaseFragment(), View.OnClickListener, MeListentr {

    internal lateinit var wallet: Wallet

    override fun attachLayoutRes(): Int {
        return R.layout.fragment_me
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_head -> {
                managerWallet()
            }
            R.id.tv_managerwallet -> {
                managerWallet()
            }
            R.id.lin_jyjl -> {
                transactionRecord()
            }
            R.id.lin_xxtz -> {
                alerts()
            }
            R.id.lin_xtsz -> {
                systemSettings()
            }
//            R.id.lin_bzzx -> {
//                helpCenter()
//            }
            R.id.lin_gywm -> {
                aboutUs()
            }
        }
    }


    override fun initViews(rootView: View?) {
        lin_jyjl.setOnClickListener(this)
        lin_gywm.setOnClickListener(this)
        lin_xxtz.setOnClickListener(this)
        lin_xtsz.setOnClickListener(this)
//        lin_bzzx.setOnClickListener(this)
        iv_head.setOnClickListener(this)
        tv_managerwallet.setOnClickListener(this)

    }

    override fun initData() {
    }

    override fun managerWallet() {
        val inte = Intent(activity, ManagerWalletActivity::class.java)
        startActivity(inte)
    }

    override fun transactionRecord() {
        val inte = Intent(activity, TransactionActivity::class.java)
        startActivity(inte)
    }

    override fun alerts() {
        val inte = Intent(activity, AlertsActivity::class.java)
        startActivity(inte)
    }

    override fun systemSettings() {
        val inte = Intent(activity, SystemSettingActivity::class.java)
        startActivity(inte)
    }

    override fun helpCenter() {
    }

    override fun aboutUs() {
        val inte = Intent(activity, AboutUsActivity::class.java)
        startActivity(inte)
    }

    companion object {

        fun newInstance(args: Bundle?): MeFragment {
            val instance = MeFragment()
            instance.arguments = args
            return instance
        }
    }

}