package io.samos.wallet.activities.Main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.Toast

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.samos.wallet.R
import io.samos.wallet.activities.Assets.AddNewAssetsActivity
import io.samos.wallet.activities.Transaction.RullOutActivity
import io.samos.wallet.activities.Wallet.FirstInitAcitivity
import io.samos.wallet.activities.Wallet.WalletCreatActivity
import io.samos.wallet.activities.Wallet.WalletDetailsActivity
import io.samos.wallet.base.BaseFragment
import io.samos.wallet.beans.ExchangeBean
import io.samos.wallet.beans.PriceBean
import io.samos.wallet.beans.Token
import io.samos.wallet.beans.TokenSet
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.*
import io.samos.wallet.push.WalletPush
import io.samos.wallet.push.WalletPushListener
import io.samos.wallet.utils.SamosWalletUtils
import io.samos.wallet.utils.SharePrefrencesUtil
import kotlinx.android.synthetic.main.fragment_main_wallet.*
import java.text.DecimalFormat
import java.util.ArrayList

/**
 * 钱包主页视图碎片
 * Created by kimi on 2018/1/25.
 * edit by 方丈 2018/05/10
 */

class WalletFragment : BaseFragment(), WalletListener {
    internal  var mainWalletAdapter=WalletAdapter(null, mutableListOf())


   // internal  var walletIndex=WalletIndex()

    var tokenSet = SamosWalletUtils.getTokenSet()
    internal var walletDb = WalletDB() //

    var bean= PriceBean()
    /**
     * 标记，true表示cny，false表示usd
     */
    var exchangeCoinCny: Boolean = true

  //  var selectedWallets: MutableList<String> = ArrayList()

    private var decimalFormat = DecimalFormat("#.###")

    /**
     * 钱包控制层
     */
    private var walletViewModel = WalletViewModel()

    /**
     * 定时任务专用handler
     */
    internal var taskHandler = Handler()

    private val listener = object : WalletPushListener() {
        override fun walletUpdate() {
            taskHandler.removeCallbacksAndMessages(null)
            taskHandler.postDelayed({ refreshDatas() }, 2000)
        }

        override fun transactionUpdate() {

        }

        override fun balanceUpdate() {

        }
    }

    override fun attachLayoutRes(): Int {
        return R.layout.fragment_main_wallet
    }

    override fun initViews(rootView: View) {
        recyclerview.layoutManager = LinearLayoutManager(rootView.context)
        mainWalletAdapter.mainWalletListener = this



        recyclerview.adapter = mainWalletAdapter
        //添加观察者
        WalletPush.getInstance().addObserver(listener)
        swiprefreshlayout.setColorSchemeColors(Color.parseColor("#0075FF"), Color.parseColor("#00B9FF"))
        swiprefreshlayout.setOnRefreshListener {
            if (WalletManager.getInstance().restorDefaultWalletDB() == null) {
                var intent = Intent(activity, FirstInitAcitivity::class.java)
                startActivity(intent)
                swiprefreshlayout.isRefreshing = false
                return@setOnRefreshListener
            }
            updateWalletDB(WalletManager.getInstance().restorDefaultWalletDB())
        }
        iv_wallet_add?.setOnClickListener {
//            startActivity(intent)

            if(mainWalletAdapter.wallets != null && mainWalletAdapter.wallets!!.size>0) {
                var intent = Intent(activity, AddNewAssetsActivity::class.java)


                intent.putExtra(Constant.KEY_WALLET, mainWalletAdapter.wallets!![0])
                startActivity(intent)
            } else {
               var toast = Toast.makeText(this.context,
                        "Internal Error,please restart your app", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }

        }


    }

    override fun initData() {
        if (walletViewModel.priceBean != null) {
            bean = walletViewModel.priceBean
        } else {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //移除观察者
        WalletPush.getInstance().deleteObserver(listener)
        taskHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 刷新数据
     */
    private fun refreshDatas() {
        swiprefreshlayout.post(Runnable {
            swiprefreshlayout.isRefreshing = true
        })
    }

    /**
     * 当前钱包的
     */
    fun updateWalletDB(index: WalletDB) {
        walletDb = index
        if (index.avatarResourceId <= 0) {
            index.avatarResourceId = R.drawable.avata1
        }
        try {
            iv_wallet_head.setImageResource(index.avatarResourceId)
        } catch (e: Exception) {

        }

        walletViewModel.getAllDetailWallets(walletDb)
                .compose(this.bindUntilEvent(com.trello.rxlifecycle2.android.FragmentEvent.DESTROY))
                .subscribe(object : Observer<List<Wallet>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(wallets: List<Wallet>) {
                        swiprefreshlayout?.isRefreshing = false
                        /**
                         * 根据钱包字段展示数量是否显示sky
                         */
                        exchangeCoinCny = SharePrefrencesUtil.getInstance().getBoolean(Constant.IS_UNIT_CNY)
                        mainWalletAdapter.setCnyBoolean(exchangeCoinCny)



                        var showWallets = mutableListOf<Wallet>()
                        var tokenCfgs = mutableListOf<Token>()

                        wallets.forEach {
                            if(tokenSet.getToken(it.walletType) != null) {
                                showWallets.add(it)
                                tokenCfgs.add(tokenSet.getToken(it.walletType))

                            }

                        }




                        mainWalletAdapter.wallets = showWallets
                        mainWalletAdapter.tokenCfgs = tokenCfgs
                        mainWalletAdapter.notifyDataSetChanged()

                    }

                    override fun onError(e: Throwable) {
                        swiprefreshlayout?.isRefreshing = false

                    }

                    override fun onComplete() {
                        swiprefreshlayout?.isRefreshing = false
                        setExchangeCoinValue() //用的上面过滤后的结果

                    }
                })
    }

    //扫描转出
    fun satrtRullOut(str: String) {
        //lwj haic电商支付链接对应
        var coinType = str.split("=")[1].split("&")[0]
        if(coinType.equals("HAIC")){
            coinType = "HAI"
        }

        val intent = Intent(activity, RullOutActivity::class.java)
        for (wallet in mainWalletAdapter.wallets!!.iterator()) {
            if(str!!.split("=").size>3) {

                if (wallet.walletType.equals(str.split("=")[3])) {
                    intent.putExtra(Constant.KEY_WALLET, wallet)
                }
                //lwj haic电商支付链接对应 splitCode[1].split("&")[0]
                if (wallet.walletType.equals(coinType)) {
                    intent.putExtra(Constant.KEY_WALLET, wallet)
                }
            }

        }
        intent.putExtra(Constant.SCAN_FLAG, str)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        refreshDatas()
    }


    override fun onItemClick(position: Int, bean: Wallet) {
        val intent = Intent(activity, WalletDetailsActivity::class.java)
        intent.putExtra(Constant.KEY_WALLET, bean)
        startActivity(intent)
    }

    override fun onCreateWallet() {
        val intent = Intent(activity, WalletCreatActivity::class.java)
        startActivityForResult(intent, 1)

    }

    override fun onImportWallet() {
        val intent = Intent(activity, WalletCreatActivity::class.java)
        intent.putExtra(Constant.KEY_PAGE, 1)
        startActivityForResult(intent, 1)
    }


    /**
     * 设置总余额
     */
    private fun setExchangeCoinValue( ) {
        var value = 0.0;
        if (exchangeCoinCny && bean != null  ) {


            for (wallet in mainWalletAdapter.wallets!!.iterator()) {
               value += wallet.balance * bean.getPrice(wallet.token,"cny")
            }
            tv_total_asset_1!!.text = resources.getString(R.string.total_asset) + " (￥)"

        } else {
            for (wallet in mainWalletAdapter.wallets!!.iterator()) {
                value += wallet.balance * bean.getPrice(wallet.token,"usd")
            }
            tv_total_asset_1!!.text = resources.getString(R.string.total_asset) + " ($)"

        }
        tv_total_asset!!.text = decimalFormat.format(value)

    }

    companion object {
        fun newInstance(args: Bundle?): WalletFragment {
            val instance = WalletFragment()
            instance.arguments = args
            return instance
        }
    }
}
