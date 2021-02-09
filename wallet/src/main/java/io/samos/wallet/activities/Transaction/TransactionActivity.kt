package io.samos.wallet.activities.Transaction

import android.app.Dialog
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.samos.wallet.R
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.activities.Transaction.old.TransactionViewModel
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.beans.TransInfo
import io.samos.wallet.beans.TransactionInfo
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.Wallet
import io.samos.wallet.datas.WalletDB
import io.samos.wallet.datas.WalletManager
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.options_dialog_layout.view.*
import java.util.*

/**
 * 钱包交易记录的界面
 */
class TransactionActivity : BaseActivity(), TransactionAdapter.OnItemClickListener, AdapterView.OnItemClickListener {


    internal lateinit var transactionAdapter: TransactionAdapter
    private var walletViewModel = WalletViewModel()
    internal var wallets: List<Wallet> = arrayListOf()
    var transactionViewModel: TransactionViewModel = TransactionViewModel()
    lateinit var dialog: Dialog
    lateinit var adapter: SwitchWalletAdapter
    val mutableListOf = mutableListOf<String>()
    var walletDBs: List<WalletDB> = arrayListOf()
    lateinit var walletDB: WalletDB

    override fun attachLayoutRes(): Int {
        return R.layout.activity_transaction
    }

    override fun initViews() {
        id_toolbar.setNavigationOnClickListener {
            finish()
        }
        id_toolbar_title.text = WalletManager.getInstance().restorDefaultWalletDB().name
        iv_right.setOnClickListener {
            showWalletIndex()
        }
        refresh.setOnRefreshListener { getSelectedWallet(walletDB) }
        recyclerview.layoutManager = LinearLayoutManager(this)
        transactionAdapter = TransactionAdapter(this, null)
        recyclerview.adapter = transactionAdapter
        transactionAdapter.TransactionListener = this
        dialog = Dialog(this@TransactionActivity, R.style.SimpleDialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun initData() {
        walletDB = WalletManager.getInstance().restorDefaultWalletDB()
        getSelectedWallet(walletDB)
        getWalletDatas()
    }

    override fun OnClick(trans: TransInfo?) {
//        intent = Intent(this, TransRecordsActivity::class.java)
//        intent.putExtra(Constant.KEY_TRANS, trans)
//        startActivity(intent)
    }

    /**
     * 先获取默认钱包->再获取钱包下所有币种的交易记录
     */
    fun getSelectedWallet(walletDB: WalletDB) {
        walletViewModel.getAllDetailWallets(walletDB)
                .compose(this.bindUntilEvent(com.trello.rxlifecycle2.android.ActivityEvent.DESTROY))
                .subscribe(object : Observer<List<Wallet>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(wallet: List<Wallet>) {
                        this@TransactionActivity.wallets = wallet
                       // getAlltransction()
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        refresh.isRefreshing = false
                    }
                })
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        walletDB = walletDBs[p2]
        dialog.dismiss()
        refresh.post {
            refresh.isRefreshing = true
            getSelectedWallet(walletDB)
            id_toolbar_title.text = walletDB.name
        }
    }

    /**
     * 弹出框
     */
    private fun showWalletIndex() {
        val rootView = LayoutInflater.from(this@TransactionActivity).inflate(R.layout.options_dialog_layout, null)
        val dialogWindow = dialog.window
        dialogWindow.decorView.setPadding(0, 0, 0, 0)
        dialogWindow.setGravity(Gravity.BOTTOM)
        val lp = dialogWindow.attributes // 获取对话框当前的参数值
        lp.width = resources.displayMetrics.widthPixels // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT // 高度
        dialogWindow.attributes = lp
        rootView.iv_exit.setOnClickListener {
            dialog.dismiss()
        }
        rootView.title.text = resources.getString(R.string.switch_the_purse)
        adapter = SwitchWalletAdapter(mutableListOf, this)
        rootView.listview.adapter = adapter
        rootView.listview.onItemClickListener = this
        dialog.setContentView(rootView)
        dialog.show()
    }

    private fun getWalletDatas() {
        walletViewModel.allWalletDB
                .compose(this.bindUntilEvent(com.trello.rxlifecycle2.android.ActivityEvent.DESTROY))
                .subscribe(object : Observer<List<WalletDB>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(wallets: List<WalletDB>) {
                        walletDBs = wallets
                        for (i in wallets.indices) {
                            mutableListOf.add(wallets[i].name)
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        refresh.isRefreshing = false
                    }
                })
    }

    /**
     * 获取交易记录
     * */
//    fun getAlltransction() =
//            this.transactionViewModel.getAllTransaction(wallets)
//                    .compose(this.bindUntilEvent<ArrayList<TransInfo>>(com.trello.rxlifecycle2.android.ActivityEvent.DESTROY))
//                    .subscribe(object : Observer<ArrayList<TransInfo>> {
//                        override fun onSubscribe(d: Disposable) {
//
//                        }
//
//                        override fun onNext(transactionInfos: ArrayList<TransInfo>) {
//                            transactionAdapter.transaction = transactionInfos
//                            transactionAdapter.notifyDataSetChanged()
//
//                        }
//
//                        override fun onError(e: Throwable) {
//                            e.printStackTrace()
//                            onComplete()
//                        }
//
//                        override fun onComplete() {
//                            refresh.isRefreshing = false
//                        }
//                    })
}
