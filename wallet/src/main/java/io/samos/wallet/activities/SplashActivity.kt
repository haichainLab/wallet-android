package io.samos.wallet.activities

import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import io.reactivex.Flowable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.samos.im.activity.LoginActivity
import io.samos.im.utils.WalletUtil.getPubkey
import io.samos.wallet.R

import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.activities.Wallet.FirstInitAcitivity
import io.samos.wallet.base.BaseActivity
//import io.samos.wallet.beans.ExchangeBean
import io.samos.wallet.beans.PriceBean
import io.samos.wallet.beans.TokenSet
import io.samos.wallet.common.Constant
//import io.samos.wallet.datas.WalletCfg
import io.samos.wallet.datas.WalletManager
import io.samos.wallet.utils.*
//import io.samos.wallet.utils.getVersion
import mobile.Mobile
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit

import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {
    internal var handler = Handler()
    override fun attachLayoutRes(): Int {
        return R.layout.activity_splash
    }

    private var noticeStr = ""



    var mSubscription: Subscription? = null // Subscription 对象，用于取消订阅关系，防止内存泄露
    //开始倒计时，用 RxJava2 实现
    private fun timer() {
        val count = 10L
        Flowable.interval(0, 1200, TimeUnit.MILLISECONDS)//设置0延迟，每隔一秒发送一条数据
                .onBackpressureBuffer()//加上背压策略
                .take(count) //设置循环次数
                .map{ aLong ->
                    count - aLong //
                }
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(object : Subscriber<Long> {
                    override fun onSubscribe(s: Subscription?) {
                        mSubscription = s
                        s?.request(Long.MAX_VALUE)//设置请求事件的数量，重要，必须调用
                    }

                    override fun onNext(aLong: Long?) {
                        splash_counter.text = "${aLong}s" //接受到一条就是会操作一次UI
                    }

                    override fun onComplete() {
                      //  splash_counter.text = "Done!"
                        mSubscription?.cancel()//取消订阅，防止内存泄漏
                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                    }
                })
    }




    override fun isTranslucent(): Boolean = true

    override fun initViews() {
            splash_notice.setText("欢迎加入Haichain的世界!")
            splash_biz.setText("Welcome to Haichain World")

    }

    override fun onResume() {
        super.onResume()

        if (EasyPermissions.hasPermissions(this, *Constant.ALL_PERMISSIONS)) {
            initWallet()
        } else {
            EasyPermissions.requestPermissions(this, "", Constant.ALL_RERMISSIONS_REQUEST_CODE, *Constant.ALL_PERMISSIONS)
        }

    }

    //价格数据
    override fun initData() {
       // splash_notice.setText("v"
     //   handler.postDelayed(runnable, 1000);
      //  Log.d("Time coin: ",(System.currentTimeMillis()/1000).toString())

        timer()
        WalletViewModel().tokenPrice
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(s: String) {

                        Log.d("Time coin data: ",(System.currentTimeMillis()/1000).toString())

                        val gson = Gson()
                        val bean = gson.fromJson<PriceBean>(s, PriceBean::class.java)

                       Log.d("Time coin data: ",(System.currentTimeMillis()/1000).toString())

                        noticeStr = "Loading Price Data..."
                        //全部的价格
                        SharePrefrencesUtil.getInstance().putString(Constant.TOKEN_PRCIE,
                                s)
                    }

                    override fun onError(e: Throwable) {
                        //请求网络失败，此时读取上次保存的配置

                        val s = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_PRCIE)
                        val gson = Gson()
                        val bean = gson.fromJson<PriceBean>(s, PriceBean::class.java)
                        if(bean == null) {
                            SharePrefrencesUtil.getInstance().removeObject(Constant.TOKEN_PRCIE)
                        }

                        noticeStr = "Network is unstable,please try again later!"



                    }

                    override fun onComplete() {

                    }
                })
        //6.0动态权限申请
        val _UserLocale = LocaleUtils.getUserLocale()

        LocaleUtils.updateLocale(this, _UserLocale)

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms) && requestCode == Constant.ALL_RERMISSIONS_REQUEST_CODE) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        if (requestCode == Constant.ALL_RERMISSIONS_REQUEST_CODE && perms?.size == Constant.ALL_PERMISSIONS.size) {
            initWallet()
        }
    }



    private fun initWallet() {


        try {
            noticeStr = "Initializing the wallet tokens..."




            WalletViewModel().tokenCfg
                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())//指定运行在main线程就可以解决了
                    .subscribe(object : Observer<String> {
                        override fun onSubscribe(d: Disposable) {

                        }
                        override fun onNext(s: String) {
                           // Log.d("wallet",s)
                            Log.d("wallet-log",s)

                            val cfgstr = DES3.decode(s)
                            //
                            //var newCfgstr = cfgstr.replace("HAI","HAIC")
                            //var newCfgstr = cfgstr

                             Log.d("wallet-log",cfgstr)
                            if(!cfgstr.isNullOrEmpty()) {
                                SharePrefrencesUtil.getInstance().putString(Constant.TOKEN_CONF, cfgstr)
                                walletInit(cfgstr)
                            } else {
                                val walletCfgJson = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_CONF)
                                walletInit(walletCfgJson)

                            }

                            jumpCheckoutWallet()


                        }

                        override fun onError(e: Throwable) {
                            //请求网络失败，此时读取上次保存的配置
                            val walletCfgJson = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_CONF)
                            walletInit(walletCfgJson)
                            jumpCheckoutWallet()

                        }

                        override fun onComplete() {

                        }
                    })
        } catch (e: Exception){
            e.printStackTrace()
        }


    }

    /**
     * 根据配置文件初始化钱包
     * 初始化币种
     * @param s json配置文件
     */
    private  fun walletInit(s: String) {
        try {
          //  var notice="";
           // val walletCfg = Gson().fromJson<WalletCfg>(s,
           //         object : TypeToken<WalletCfg>() {}.type)

            val tokenSet = Gson().fromJson<TokenSet>(s, TokenSet::class.java)

            val walletDir = applicationContext.filesDir.toString() + "/"+ Constant.WALLET_STORE_DIR
            noticeStr = "Init Wallet Directory"
        //    System.currentTimeMillis()
            LogUtils.d("walletInit begin:"+            System.currentTimeMillis())

            if (!TextUtils.isEmpty(SamosWalletUtils.getPin())) {


                tokenSet.tokens.forEach {
                //    notice = "Initializing the "+it.tokenName+ " wallet...\n正在初始化"+it.tokenName+"..."
                 //   view_notice.setText(notice)
               //     splash_notice.setText("Initializiddng the token:"+it.tokenName)
                    noticeStr = "Register："+it.token


                    Mobile.registerNewCoin(it.tokenName, it.hostApi)
                }

                Mobile.init(walletDir, SamosWalletUtils.getPin16())


            }
            LogUtils.d("walletInit end"+            System.currentTimeMillis())

            // Log.d("Time cfg : ",(System.currentTimeMillis()/1000).toString())
//            var addr = "21qUA6J5nBdU5kCXW3rTXHQZd9Wmon2mzZs"
//            var pwd =  SamosWalletUtils.getPin16()
//            var pubkey = WalletUtil.getPubkey(pwd,addr)
//            Log.d("pubkey", pubkey)
//
//            var str = "aaaaa" + "bbbb" +"cccc"
//            var h = StringUtils.sha256(str)
//
//            val v = WalletUtil.signData(h, pwd, addr)
//            Log.d("hash",v)
           // WsUtil.run()

            getPubkey( "qwerty",  "2J7fGEXzn44KXwvCAmq17oQGJn64gND9Jo1");



            } catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun jumpCheckoutWallet() {
        if (isWalletExist()) {
            if (!isFirstStartForTheVersion()) {
                handler.postDelayed({
                    launchToIntroActivity()
                    finish()
                }, 300)
            } else {
                handler.postDelayed({
//                    launchToImActivity()
                    launchToMainActivity()
                    finish()
                }, 300)
            }
        } else {
            handler.postDelayed({
                launchToWalletInitctvity()
                finish()
            }, 300)
        }
    }

    private fun isFirstStartForTheVersion(): Boolean {
        return SharePrefrencesUtil.getInstance().getBoolean("FIRST_START" + getVersion())
    }

    private fun launchToWalletInitctvity() {
        val intent = Intent(this, FirstInitAcitivity::class.java)
        startActivity(intent)
    }

    private fun launchToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun launchToIntroActivity() {
        val intent = Intent(this, SlideIntroActivity::class.java)
        startActivity(intent)
    }

    private fun isWalletExist(): Boolean {
        return WalletManager.getInstance().allWalletDB.size > 0
    }

    private fun launchToImActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}