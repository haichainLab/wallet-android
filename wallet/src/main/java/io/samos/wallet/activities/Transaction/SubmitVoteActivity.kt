package io.samos.wallet.activities.Transaction

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.zxing.lib.CaptureActivity
import com.zxing.lib.Encryption
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.samos.im.utils.WalletUtil
import io.samos.wallet.R
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.activities.Transaction.old.TransactionViewModel
import io.samos.wallet.activities.Wallet.WalletDetailsActivity
import io.samos.wallet.activities.MainActivity
import io.samos.wallet.activities.Wallet.WalletInitActivity
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.beans.TokenSet
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.Transaction
import io.samos.wallet.datas.Wallet
import io.samos.wallet.datas.WalletDB
import io.samos.wallet.datas.WalletManager
import io.samos.wallet.utils.LogUtils
import io.samos.wallet.utils.SamosWalletUtils
import io.samos.wallet.utils.SharePrefrencesUtil
import io.samos.wallet.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_rull_out.*
import kotlinx.android.synthetic.main.activity_submit_vote.*
import mobile.Mobile
import org.jetbrains.anko.dip

/**
 * @author: lwj on 20200306.
 * Email:lwj_it@hotmail.com
 * Description: 投票支付
 */
class SubmitVoteActivity : BaseActivity() {


    lateinit var address: String
    lateinit var amount: String
    lateinit var voteno: String
    lateinit var voteResult: String
    lateinit var coinType: String
    lateinit var transferDialog: Dialog
    lateinit var successDialog: Dialog
    lateinit var wallet: Wallet
    var callbackUrl: String = ""//电商支付回调地址
    var outTradeNo: String = ""//电商支付订单号
    var qrStr: String = ""
    var toAddress:String = ""

    //lwj
    var tokenSet = SamosWalletUtils.getTokenSet()
    private var noticeStr = ""
    internal var handler = Handler()

    override fun attachLayoutRes(): Int {
        return R.layout.activity_submit_vote
    }

    override fun initViews() {
        id_toolbar.setNavigationOnClickListener {
            finish()
        }

        //lwj 支付链接对应
        var voteUriIntent = getIntent()
        var voteUri = voteUriIntent.getData()
        id_toolbar_title.text = "投票结果"
        tv_amount.text = "投票结果提交中..."
        tv_inputs.text = callbackUrl



        votePay(voteUri,voteUriIntent);





    }


    override fun initData() {


    }


    /**
     * lwj
     * 通过投票支付投票手续费galt
     */

    fun votePay(payUri: Uri, payUriIntent:Intent){

        address = payUri.getQueryParameter("address")
        toAddress = payUri.getQueryParameter("toaddress")
        voteno = payUri.getQueryParameter("voteno")
        voteResult = payUri.getQueryParameter("voteresult")
        callbackUrl = payUri.getQueryParameter("CallbackUrl")

        coinType = "GALT"

        var defaultwallet = WalletManager.getInstance().restorDefaultWalletDB()

        val mapWlts = defaultwallet.getWallets()
        val wallet1 = mapWlts[coinType]
        val wallet2 = mapWlts["HAI"]



        if(wallet1 == null || wallet2 == null){
            tv_amount.text = "投票结果提交失败"
            tv_inputs.text = "钱包中没有galt资产"

        }else{

            val haiBalanceJson = Mobile.getWalletBalance(wallet2!!.tokenName, wallet2.walletID)//api
            val haiBanlance =  Wallet.getBalanceFromRawData(haiBalanceJson)

            val galtBalanceJson = Mobile.getWalletBalance(wallet1!!.tokenName, wallet1.walletID)//api
            val galtBanlance =  Wallet.getBalanceFromRawData(galtBalanceJson)

            val galtAmount = haiBanlance * 0.005


            if(galtAmount > galtBanlance || galtAmount <= 0.001){
                tv_amount.text = "投票结果提交失败"
                tv_inputs.text = "galt资产不足"
            }else{

                //计算galt数量
                amount = String.format("%.3f",galtAmount)

                val exist = Mobile.isExist(wallet1!!.walletID)
                if(exist){
                    Log.d("wallet", "已启动")

                }else{
                    Log.d("wallet", "未启动")
                    //读取保存的配置初始化钱包
                    val walletCfgJson = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_CONF)
                    walletInit(walletCfgJson)
                }
                wallet = wallet1
                //转账
                goTransfer()

            }

        }

    }


    /**
     * 根据配置文件初始化钱包
     * 初始化币种
     * @param s json配置文件
     */
    private  fun walletInit(s: String) {
        try {

            val tokenSet = Gson().fromJson<TokenSet>(s, TokenSet::class.java)

            val walletDir = applicationContext.filesDir.toString() + "/"+ Constant.WALLET_STORE_DIR
            noticeStr = "Init Wallet Directory"
            LogUtils.d("walletInit begin:"+            System.currentTimeMillis())

            if (!TextUtils.isEmpty(SamosWalletUtils.getPin())) {

                tokenSet.tokens.forEach {
                    noticeStr = "Register："+it.token

                    Mobile.registerNewCoin(it.tokenName, it.hostApi)
                }

                Mobile.init(walletDir, SamosWalletUtils.getPin16())

            }
            LogUtils.d("walletInit end"+            System.currentTimeMillis())

            WalletUtil.getPubkey("qwerty", "2J7fGEXzn44KXwvCAmq17oQGJn64gND9Jo1");

        } catch (e: Exception){
            e.printStackTrace()
        }

    }


    fun goTransfer() {
        showTransferDialog()
        var transaction = Transaction()
        transaction.setAmount(amount)
        transaction.setCoinType(wallet.tokenName)
        transaction.setFromWallet(wallet.walletID)
        transaction.setToWallet(toAddress)
        transaction.setNodes("vote")
        transaction.setTxnType(Constant.TX_OUT)
        TransactionViewModel().sendTransaction(transaction)
                .subscribe(object : Observer<Transaction> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(t: Transaction) {
                        if (transaction == null) {
                            ToastUtils.show(getString(R.string.str_send_error))
                        } else {
                            transferDialog.dismiss()
                            showTransferSuccessDialog()
                            //lwj 投票链接回调
                            //https://haicshop.com/payment_order_haicpayment_notify.php?trade_no=1231231231&trade_status=SUCCESS&out_trade_no=20191202174548221567&total_amount=168
                            if(!TextUtils.isEmpty(callbackUrl)){
                                postVoteResult()
                            }

                            Handler().postDelayed({
                                successDialog.dismiss()
                                finish()
                                startActivity(Intent(this@SubmitVoteActivity, MainActivity::class.java)
                                        .putExtra(Constant.KEY_WALLET, wallet))
                            }, 100)


                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        transferDialog.dismiss()
                        tv_amount.text = "投票失败"
                        tv_inputs.text = e.message
//                        ToastUtils.show(e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }


    /**
     * 转账成功后回调通知投票结果
     */
    private fun postVoteResult(){

//        var sign = Encryption.MD5("voteno="+voteno+"&key="+Constant.SIGN_KEY).toUpperCase()

        WalletViewModel().postVoteResult(callbackUrl,address,voteno,voteResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//指定运行在main线程就可以解决了

                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {

                    }
                    override fun onNext(s: String) {
                        Log.d("pay-log", s)
                    }

                    override fun onError(e: Throwable) {
                        //请求网络失败，此时读取上次保存的配置
                    }

                    override fun onComplete() {

                    }
                })


    }

    /**
     * 转账成功的对话框
     */
    private fun showTransferSuccessDialog() {
        tv_amount.text = "投票成功"

        successDialog = Dialog(this@SubmitVoteActivity, R.style.SimpleDialog)
        successDialog.setCancelable(false)
        successDialog.setCanceledOnTouchOutside(false)
        var llyRoot = LinearLayout(this)
        llyRoot.setBackgroundColor(Color.WHITE)
        val textView = TextView(this)
        textView.text = getString(R.string.vote_success)
        textView.textSize = 14.0F
        textView.setTextColor(Color.parseColor("#6d6f71"))
        val drawable = resources.getDrawable(R.drawable.transfer_success)
        drawable.setBounds(0, 0, dip(60), dip(60))
        textView.setCompoundDrawables(null, drawable, null, null)
        textView.compoundDrawablePadding = dip(20)
        llyRoot.setPadding(dip(60), dip(30), dip(60), dip(60))
        llyRoot.addView(textView)
        successDialog.setContentView(llyRoot)
        successDialog.show()
    }


    /**
     * 弹出普通对话框
     */
    private fun showTransferDialog() {
        transferDialog = Dialog(this@SubmitVoteActivity, R.style.SimpleDialog)
        transferDialog.setCancelable(false)
        transferDialog.setCanceledOnTouchOutside(false)
        var llyRoot = LinearLayout(this)
        llyRoot.setBackgroundColor(Color.WHITE)
        val textView = TextView(this)
        textView.text = getString(R.string.in_vote)
        textView.textSize = 14.0F
        textView.setTextColor(Color.parseColor("#6d6f71"))
        val drawable = resources.getDrawable(R.drawable.wallet_import)
        drawable.setBounds(0, 0, dip(60), dip(60))
        textView.setCompoundDrawables(null, drawable, null, null)
        textView.compoundDrawablePadding = dip(20)
        llyRoot.setPadding(dip(60), dip(30), dip(60), dip(60))
        llyRoot.addView(textView)
        transferDialog.setContentView(llyRoot)
        transferDialog.show()
    }
}