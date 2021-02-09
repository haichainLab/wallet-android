package io.samos.wallet.activities.ManagerWallet

import io.samos.wallet.R
import io.samos.wallet.base.BaseActivity
import kotlinx.android.synthetic.main.activity_change_pwd.*

/**
 * @author: lh on 2018/5/23 12:03.
 * Email:luchefg@gmail.com
 * Description: 修改密码
 */
class WalletChangePwdActivity : BaseActivity() {

    override fun attachLayoutRes(): Int {
        return R.layout.activity_change_pwd
    }

    override fun initViews() {
        id_toolbar_title.text = resources.getString(R.string.change_the_psaaword)
        id_toolbar.setNavigationOnClickListener {
            finish()
        }
        next.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
    }
}