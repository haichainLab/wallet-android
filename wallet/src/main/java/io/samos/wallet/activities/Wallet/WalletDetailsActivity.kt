package io.samos.wallet.activities.Wallet

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.samos.wallet.R
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.activities.Transaction.*
import io.samos.wallet.activities.Transaction.old.TransactionViewModel
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.beans.*
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.Address
import io.samos.wallet.datas.Wallet
import io.samos.wallet.datas.WalletManager
import io.samos.wallet.utils.*
import kotlinx.android.synthetic.main.activity_wallet_details.*
import kotlinx.android.synthetic.main.item_main_wallet.view.*
import mobile.Mobile
import java.text.DecimalFormat

/**
 * @author: lh on 2018/5/22 14:47.
 * Email:luchefg@gmail.com
 * Description: 钱包详情
 */
class WalletDetailsActivity : BaseActivity(), TransactionAdapter.OnItemClickListener {


    internal lateinit var transactionAdapter: TransactionAdapter
    internal lateinit var wallet: Wallet
    var wallets: MutableList<Wallet> = arrayListOf()
    internal  lateinit var tokenSet:TokenSet
    /**
     * 钱包控制层
     */
   // internal var walletViewModel = WalletViewModel()
    var transactionViewModel: TransactionViewModel = TransactionViewModel()
    var defaultAddress:String = ""



    override fun attachLayoutRes(): Int {
        return R.layout.activity_wallet_details
    }

    override fun initViews() {
        id_toolbar.setNavigationOnClickListener {
            finish()
        }



        btn_out.setOnClickListener {
            intent = Intent(this, RullOutActivity::class.java)
            intent.putExtra(Constant.KEY_WALLET, wallet)
            intent.putExtra(Constant.SCAN_FLAG, "")
            startActivity(intent)
        }

        btn_into.setOnClickListener {
            intent = Intent(this, IntoWalletActivity::class.java)
//            intent.putExtra(Constant.KEY_WALLET, wallet)
//            intent = Intent(this, IntoWalletActivity::class.java)

            intent.putExtra(Constant.KEY_ADDRESS, defaultAddress)
            intent.putExtra(Constant.TOKEN_TYPE, wallet.token)

            startActivity(intent)
        }
        refresh.setOnRefreshListener { refreshDatas() }
        recyclerview.layoutManager = LinearLayoutManager(this)
        transactionAdapter = TransactionAdapter(this, null)
        recyclerview.adapter = transactionAdapter
        transactionAdapter.TransactionListener = this
    }

    override fun OnClick(trans: TransInfo?) {
        intent = Intent(this, TransRecordsActivity::class.java)
        intent.putExtra(Constant.KEY_TRANS, trans)
        startActivity(intent)
    }

    /**
     * 刷新数据
     */
    private fun refreshDatas() {
        getAlltransction()
    }


    override fun initData() {
        wallet = intent.getSerializableExtra(Constant.KEY_WALLET) as Wallet
        var addressList  = WalletManager.getInstance().getAddressesByWalletId(wallet.tokenName,wallet.walletID)
        if(addressList.size>0) {
            defaultAddress = addressList[0].address
        } else {
            ToastUtils.show("Disk Error,Please backup your wallet and reinstall")
        }
        wallets.add(wallet)
        //iv_token_detail_icon.setBackgroundResource(R.drawable.samos_logo)

         tokenSet = SamosWalletUtils.getTokenSet()
        // ImageUtils.loaderIcon(iv_token_detail_icon, tokenSet.getIconByToken(wallet.token))
        ImageTools.loadIcon(iv_token_detail_icon, tokenSet.getIconByToken(wallet.token))


        //lwj 0.0.1 HAI->HAIC
        if(wallet.token.equals("HAI")){
            tv_token_detail_count.text = wallet.balanceStr + " HAIC"
            id_toolbar_title.text = "HAIC"
        }else{
            tv_token_detail_count.text = wallet.balanceStr + " " + wallet.token
            id_toolbar_title.text = wallet.token
        }


        tv_token_detail_des.text = wallet.upperTokename




    }

    override fun onResume() {
        super.onResume()
        getAlltransction()
    }

    fun getAlltransction()  {
        val transListKey = wallet.token+wallet.walletID
        this.transactionViewModel.getAllTransaction(wallet.token,defaultAddress)
             //   .compose(this.bindUntilEvent<String>(com.trello.rxlifecycle2.android.ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {

                    }
                    override fun onNext(s: String) {


                        val gson = Gson()
                        try {
                            val transList = gson.fromJson<TransList>(s, TransList::class.java)

                            Log.d("Time coin data: ", (System.currentTimeMillis() / 1000).toString())

                            // noticeStr = "Loading Price Data..."
                            //全部的价格

                            if (transList != null && transList.transList != null) {
                                SharePrefrencesUtil.getInstance().putString(transListKey,
                                        s)
                                val elems = transList.transList

                                transactionAdapter.transaction = elems
                                transactionAdapter.notifyDataSetChanged()
                            }
                        } catch(ex:Exception ) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onError(e: Throwable) {
                        //请求网络失败，此时读取上次保存的配置

                        val s = SharePrefrencesUtil.getInstance().getString(transListKey)
                        val gson = Gson()
                        val transList = gson.fromJson<TransList>(s, TransList::class.java)
                        if(transList == null) {
                            SharePrefrencesUtil.getInstance().removeObject(transListKey)
                        } else {
                            val elems = transList.transList
                            if(elems != null) {
                                transactionAdapter.transaction = elems
                                transactionAdapter.notifyDataSetChanged()
                            }
                        }


                        onComplete()

                    }

                    override fun onComplete() {
                        refresh.isRefreshing = false
                        try {
                            val str = Mobile.getBalance(wallet.tokenName, defaultAddress)
                            val tmps = Wallet.getBalanceFromRawData(
                                    str)


                            //lwj 0.0.1 HAI->HAIC
                            if(wallet.token.equals("HAI")){
                                tv_token_detail_count.text = tmps.toString() + " HAIC"
                            }else{
                                tv_token_detail_count.text = tmps.toString() + " " + wallet.token
                            }


                            if (tokenSet.enableCoinHOur(wallet.token)) {
                                tv_token_detail_des.text = Wallet.getCoinHourStr(str) + " CoinHour"
                            }
                        }catch (ex:Exception) {

                            //lwj 0.0.1 HAI->HAIC
                            if(wallet.token.equals("HAI")){
                                tv_token_detail_count.text = "* HAIC"
                            }else{
                                tv_token_detail_count.text = "* " + wallet.token
                            }


                            if (tokenSet.enableCoinHOur(wallet.token)) {
                                tv_token_detail_des.text =  "* CoinHour"
                            }

                        }




                    }
                })
    }

}
