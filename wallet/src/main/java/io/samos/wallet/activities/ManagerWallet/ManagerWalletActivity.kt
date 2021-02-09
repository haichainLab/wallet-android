package io.samos.wallet.activities.ManagerWallet

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.samos.wallet.R
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.activities.Wallet.WalletInitActivity
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.common.OnItemClickListener
import io.samos.wallet.datas.WalletDB
import kotlinx.android.synthetic.main.activity_manager_wallet.*

/**
 * @author: lh on 2018/5/22 17:31.
 * Email:luchefg@gmail.com
 * Description:钱包管理
 */
class ManagerWalletActivity : BaseActivity(), OnItemClickListener<WalletDB> {


    private var walletViewModel = WalletViewModel()

    override fun onClick(viewId: Int, positon: Int, bean: WalletDB?) {
        intent = Intent(this, ManagerWalletInfoActivity::class.java)
        intent.putExtra(Constant.WALLET_INDEX, bean)
        startActivity(intent)
    }

    lateinit var adapter: ManagerWalletAdapter

    override fun attachLayoutRes(): Int {
        return R.layout.activity_manager_wallet
    }

    override fun initViews() {
        id_toolbar_title.text = resources.getString(R.string.management_of_the_purse)
        id_toolbar.setNavigationOnClickListener {
            finish()
        }
        btn_create.setOnClickListener {
            intent = Intent(this, WalletInitActivity::class.java)
            intent.putExtra("STARTWAY", "CREATE")
            startActivity(intent)
        }
        btn_import.setOnClickListener {
            intent = Intent(this, WalletInitActivity::class.java)
            intent.putExtra("STARTWAY", "IMPORT")
            startActivity(intent)
        }
        rec_assets.layoutManager = LinearLayoutManager(this)
        adapter = ManagerWalletAdapter(null)
        rec_assets.adapter = adapter
        adapter.onItemClick=this
        refresh.setOnRefreshListener {
            refreshWalletDatas()
        }

    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        refresh.post {
            refresh.isRefreshing = true
            refreshWalletDatas()
        }
    }

    private fun refreshWalletDatas() {
        walletViewModel.allWalletDB
                .compose(this.bindUntilEvent(com.trello.rxlifecycle2.android.ActivityEvent.DESTROY))
                .subscribe(object : Observer<List<WalletDB>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(wallets: List<WalletDB>) {
                        adapter.wallet = wallets
                        adapter.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                        refresh.isRefreshing = false

                    }

                    override fun onComplete() {
                        refresh.isRefreshing = false
                    }
                })
    }
}