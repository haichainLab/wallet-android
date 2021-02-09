package io.samos.wallet.activities.Transaction

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.zxing.lib.CaptureActivity
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.samos.wallet.R
import io.samos.wallet.activities.Transaction.old.TransactionViewModel
import io.samos.wallet.activities.Wallet.WalletDetailsActivity
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.Transaction
import io.samos.wallet.datas.Wallet
import kotlinx.android.synthetic.main.activity_rull_out.*
import kotlinx.android.synthetic.main.dialog_rollout_payment_detail.view.*
import kotlinx.android.synthetic.main.dialog_wallet_pwd.view.*
import org.jetbrains.anko.dip
import java.text.SimpleDateFormat
import java.util.*
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.datas.WalletDB
import io.samos.wallet.datas.WalletManager
import com.zxing.lib.Encryption
import io.samos.im.utils.WalletUtil
import io.samos.wallet.beans.TokenSet
import io.samos.wallet.utils.*
import mobile.Mobile


/**
 * @author: lh on 2018/5/21 17:28.
 * Email:luchefg@gmail.com
 * Description: 转出
 */
class RullOutActivity : BaseActivity() {

    lateinit var address: String
    lateinit var amount: String
    lateinit var coinType: String
    lateinit var transferDialog: Dialog
    lateinit var successDialog: Dialog
    lateinit var wallet: Wallet
    var callbackUrl: String = ""//电商支付回调地址
    var outTradeNo: String = ""//电商支付订单号
    var qrStr: String = ""

    //lwj
    var tokenSet = SamosWalletUtils.getTokenSet()
    private var noticeStr = ""
    internal var handler = Handler()

    override fun attachLayoutRes(): Int {
        return R.layout.activity_rull_out
    }

    override fun initViews() {
        main_toolbar.setNavigationOnClickListener {
            finish()
        }

        //lwj 支付链接对应
        var payUriIntent = getIntent()
        var payUri = payUriIntent.getData()

        if(payUri != null ){
            onlinePay(payUri,payUriIntent)
        }else{
            wallet = intent.getSerializableExtra(Constant.KEY_WALLET) as Wallet
            qrStr = intent.getStringExtra(Constant.SCAN_FLAG)

            if (qrStr.equals("")) {
                //lwj 0.0.1 HAI->HAIC
                if(wallet.token.equals("HAI")){
                    toolbar_title.text = resources.getString(R.string.roll_out) + " HAIC"
                    tv_dw.text = "HAIC"
                    tv_balance.text = wallet.balanceStr + " HAIC"
                }else{
                    toolbar_title.text = resources.getString(R.string.roll_out) + " " + wallet.token
                    tv_dw.text = wallet.token
                    tv_balance.text = wallet.balanceStr + " " + wallet.token
                }


            } else {
                scanFormat(qrStr)

            }

        }

        iv_scan.setOnClickListener {
            intent = Intent(this, CaptureActivity::class.java)
            startActivityForResult(intent, Constant.REQUEST_QRCODE)
        }
        next.setOnClickListener {
            //展示交易信息底部对话框
            if (TextUtils.isEmpty(out_amount.text)) {
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(out_adress.text)) {
                return@setOnClickListener
            }
            showPaymentDetail()
        }


    }

    /**
     * 展示转账交易信息对话框
     */
    private fun showPaymentDetail() {
        val dialog = Dialog(this@RullOutActivity, R.style.SimpleDialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val rootView = LayoutInflater.from(this@RullOutActivity).inflate(R.layout.dialog_rollout_payment_detail, null)
        val dialogWindow = dialog.window
        dialogWindow.decorView.setPadding(0, 0, 0, 0)
        dialogWindow.setGravity(Gravity.BOTTOM)
        //            dialogWindow.setWindowAnimations(R.style.dialogstyle) // 添加动画
        val lp = dialogWindow.attributes // 获取对话框当前的参数值
        lp.width = resources.displayMetrics.widthPixels // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT // 高度
        dialogWindow.attributes = lp

        rootView.tv_order_information.text = resources.getString(R.string.roll_out) + " " + wallet.token


        rootView.tv_the_transferee.text = out_adress.text.toString()
        rootView.tv_remark.text = out_note.text.toString()
        rootView.tv_transaction_number.text = out_amount.text.toString()
        val day = Date()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        rootView.tv_transaction_time.text = df.format(day)
        rootView.iv_payment_details_back.setOnClickListener {
            dialog.dismiss()
        }
        rootView.btn_payment_detail_ok.setOnClickListener {
            dialog.dismiss()
            showWalletPwd()
        }
        dialog.setContentView(rootView)
        dialog.show()
    }

    /**
     * 展示钱包密码对话框
     */
    private fun showWalletPwd() {
        val dialog = Dialog(this@RullOutActivity, R.style.SimpleDialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val rootView = LayoutInflater.from(this@RullOutActivity).inflate(R.layout.dialog_wallet_pwd, null)
        val dialogWindow = dialog.window
        dialogWindow.decorView.setPadding(0, 0, 0, 0)
        dialogWindow.setGravity(Gravity.BOTTOM)
        //            dialogWindow.setWindowAnimations(R.style.dialogstyle) // 添加动画
        val lp = dialogWindow.attributes // 获取对话框当前的参数值
        lp.width = resources.displayMetrics.widthPixels // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT // 高度
        dialogWindow.attributes = lp
        rootView.et_wallet_pwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        if (!TextUtils.isEmpty(SharePrefrencesUtil.getInstance().getString(Constant.PIN_HINt))) {
            rootView.tv_pwdhint.text = resources.getString(R.string.pwd_hint) + SharePrefrencesUtil.getInstance().getString(Constant.PIN_HINt)
        } else {
            rootView.tv_pwdhint.visibility = View.GONE
        }
        rootView.iv_wallet_pwd_back.setOnClickListener {
            dialog.dismiss()
            showPaymentDetail()
        }
        rootView.btn_wallet_pwd_ok.setOnClickListener {
            if (rootView.et_wallet_pwd.text.toString().equals(SamosWalletUtils.getPin())) {
                dialog.dismiss()
                goTransfer()
            } else {
                ToastUtils.show(this@RullOutActivity.resources.getString(R.string.pin_error))
            }
        }
        dialog.setContentView(rootView)
        dialog.show()
    }

    fun goTransfer() {
        showTransferDialog()
        var transaction = Transaction()
        transaction.setAmount(out_amount.text.toString())
        transaction.setCoinType(wallet.tokenName)
        transaction.setFromWallet(wallet.walletID)
        transaction.setToWallet(out_adress.text.toString())
        transaction.setNodes(out_note.text.toString())
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
                            //lwj 支付链接回调
                            //https://haicshop.com/payment_order_haicpayment_notify.php?trade_no=1231231231&trade_status=SUCCESS&out_trade_no=20191202174548221567&total_amount=168
                            if(!TextUtils.isEmpty(callbackUrl)){
                                postPayResult(transaction.txid)
                            }
                            Handler().postDelayed({
                                successDialog.dismiss()
                                finish()
                                startActivity(Intent(this@RullOutActivity, WalletDetailsActivity::class.java)
                                        .putExtra(Constant.KEY_WALLET, wallet))
                            }, 1000)


                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        transferDialog.dismiss()
//                        ToastUtils.show(e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }

    /**
     * 转账成功的对话框
     */
    private fun showTransferSuccessDialog() {
        successDialog = Dialog(this@RullOutActivity, R.style.SimpleDialog)
        successDialog.setCancelable(false)
        successDialog.setCanceledOnTouchOutside(false)
        var llyRoot = LinearLayout(this)
        llyRoot.setBackgroundColor(Color.WHITE)
        val textView = TextView(this)
        textView.text = getString(R.string.transfer_success)
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
        transferDialog = Dialog(this@RullOutActivity, R.style.SimpleDialog)
        transferDialog.setCancelable(false)
        transferDialog.setCanceledOnTouchOutside(false)
        var llyRoot = LinearLayout(this)
        llyRoot.setBackgroundColor(Color.WHITE)
        val textView = TextView(this)
        textView.text = getString(R.string.in_transfer)
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

    override fun initData() {


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.REQUEST_QRCODE && resultCode == Activity.RESULT_OK) {
            val code = data!!.data!!.toString()
            scanFormat(code)
        }
    }

    /**
     * 扫码数据格式
     * samos://pay?address=2PueS4hMYwsAgeHRDzyAhj1nA9LPmCR5XuB&amount=5&token=samos
     * haichain://pay?CoinName=HAIC&ReceivingAddress=PnVozdkAPmCBcCZGh7YqBpKH6BWWC65RpJ&amount=1129.77&CallbackUrl=https://haicshop.com/payment_order_haicpay_notify.php&outTradeNo=20191208152211625029
     */
    fun scanFormat(code: String) {
        if (code.equals("")) {
            return
        }

        var splitCode = code.split("=")
        if(splitCode.size>3) {

            //lwj 支付类型 钱包转账；电商支付；
            var payType = splitCode[0].split(":")[0]
            if(payType.equals("haichain")){

                address = splitCode[2].split("&")[0]
                coinType = splitCode[1].split("&")[0]
                if(coinType.equals("HAIC")){
                    coinType = "HAI"
                }
                amount = splitCode[3].split("&")[0]
                callbackUrl = splitCode[4].split("&")[0]
                outTradeNo = splitCode[5]

            }else{
                address = splitCode[1].split("&")[0]
                coinType = splitCode[3]
                amount = splitCode[2].split("&")[0]
            }


            //lwj 0.0.1 HAI->HAIC
            if(wallet.walletType.equals("HAI")){
                toolbar_title.text = resources.getString(R.string.roll_out) + " HAIC"
                tv_dw.text = "HAIC"
                tv_balance.text = wallet.balanceStr + " HAIC"
            }else{
                toolbar_title.text = resources.getString(R.string.roll_out) + " " + wallet.walletType
                tv_dw.text = wallet.walletType
                tv_balance.text = wallet.balanceStr + " " + wallet.walletType
            }

            out_adress.setText(address)
            out_amount.setText(amount)
            if(!TextUtils.isEmpty(callbackUrl)){
                out_adress.setEnabled(false)
            }
            if(!amount.equals("0")){
                out_amount.setEnabled(false)
            }
        } else {
            out_adress.setText(code)
        }


    }

    /**
     * lwj
     * 通过点击网页支付链接支付
     */

    fun onlinePay(payUri:Uri,payUriIntent:Intent){

        address = payUri.getQueryParameter("ReceivingAddress")
        coinType = payUri.getQueryParameter("CoinName")
        amount = payUri.getQueryParameter("amount")
        callbackUrl = payUri.getQueryParameter("CallbackUrl")
        outTradeNo = payUri.getQueryParameter("outTradeNo")

        if(coinType.equals("HAIC")){
            coinType = "HAI"
        }

        var defaultwallet = WalletManager.getInstance().restorDefaultWalletDB()

        val mapWlts = defaultwallet.getWallets()
        val wallet1 = mapWlts[coinType]

        val exist = Mobile.isExist(wallet1!!.walletID)

        if(exist){
            Log.d("wallet", "已启动")

        }else{
            Log.d("wallet", "未启动")
            //读取保存的配置初始化钱包
            val walletCfgJson = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_CONF)
            walletInit(walletCfgJson)

        }

        //获取当前钱包币种详情
        getDefaultWallet(defaultwallet,payUriIntent)

        out_adress.setText(address)
        out_amount.setText(amount)
        if(!TextUtils.isEmpty(callbackUrl)){
            out_adress.setEnabled(false)
        }
        if(!amount.equals("0")){
            out_amount.setEnabled(false)
        }

    }


    /**
     * lwj
     * 获取当前钱包币种详情
     */
    fun getDefaultWallet(walletDB: WalletDB,payUriIntent:Intent) {
        if (walletDB.avatarResourceId <= 0) {
            walletDB.avatarResourceId = R.drawable.avata1
        }


        WalletViewModel().getAllDetailWallets(walletDB)
                .compose(this.bindUntilEvent(com.trello.rxlifecycle2.android.ActivityEvent.DESTROY))
                .subscribe(object : Observer<List<Wallet>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(wallets: List<Wallet>) {

                        wallets.forEach {
                            if(tokenSet.getToken(it.walletType) != null) {
                                if (it.walletType.equals(coinType)) {
                                    payUriIntent.putExtra(Constant.KEY_WALLET, it)
                                }
                            }
                        }

                        wallet = payUriIntent.getSerializableExtra(Constant.KEY_WALLET) as Wallet

                        //lwj 0.0.1 HAI->HAIC
                        if(wallet.token.equals("HAI")){
                            toolbar_title.text = resources.getString(R.string.roll_out) + " HAIC"
                            tv_dw.text = "HAIC"
                            tv_balance.text = wallet.balanceStr + " HAIC"
                        }else{
                            toolbar_title.text = resources.getString(R.string.roll_out) + " " + wallet.token
                            tv_dw.text = wallet.token
                            tv_balance.text = wallet.balanceStr + " " + wallet.token
                        }

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })
    }


    /**
     * 电商支付成功后回调通知
     */
    private fun postPayResult(txid:String){

        var sign = Encryption.MD5("out_trade_no="+outTradeNo+"&key="+Constant.SIGN_KEY).toUpperCase()

        WalletViewModel().postPayResult(callbackUrl,txid,"SUCCESS",outTradeNo,amount,sign)
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






}