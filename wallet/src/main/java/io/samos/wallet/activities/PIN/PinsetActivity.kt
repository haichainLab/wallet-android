package io.samos.wallet.activities.PIN

import android.text.TextUtils
import android.view.View
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.samos.wallet.R
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.beans.TokenSet
import io.samos.wallet.common.Constant
import io.samos.wallet.utils.*

import kotlinx.android.synthetic.main.activity_pin_init.*
import mobile.Mobile

class PinsetActivity : BaseActivity() {
    override fun attachLayoutRes(): Int {
        return R.layout.activity_pin_init
    }

    override fun initViews() {
        val topPadding = StatusBarUtils.getActionBarSize(this) + StatusBarUtils.getStatusBarHeight(
                this)
        window.decorView.setPadding(0, 0, 0, 0)
        next.setOnClickListener(View.OnClickListener {
            initPin()
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.pin_anim_out)
    }

    override fun initData() {
    }

    override fun onBackPressed() {
    }

    fun initPin() {
        if (TextUtils.isEmpty(wallet_pwd_et.text)) {
            ToastUtils.show(getString(R.string.pls_input_pwd))
            wallet_pwd_et.requestFocus()
            return
        }
        if (TextUtils.isEmpty(wallet_pwd_repeat_et.text)) {
            ToastUtils.show(getString(R.string.pls_confirm_pwd))
            wallet_pwd_repeat_et.requestFocus()
            return
        }
        if (!TextUtils.equals(wallet_pwd_repeat_et.text, wallet_pwd_et.text)) {
            ToastUtils.show(getString(R.string.pwd_not_equal))
            return
        }
        SamosWalletUtils.setPin(wallet_pwd_et.text.toString())
        SharePrefrencesUtil.getInstance().putString(Constant.PIN_HINt, wallet_pwd_hint_et.text.toString())
        initWallet()
    }

    fun initWallet() {
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(init())
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Boolean) {
                        if (t) {
                            this@PinsetActivity.setResult(android.app.Activity.RESULT_OK)
                            finish()
                        }
                    }

                    override fun onError(e: Throwable) {
//                        ToastUtils.show(e.message)
                    }

                })
    }

    fun init(): Boolean {
        try {
            WalletViewModel().tokenCfg
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : Observer<String> {
                        override fun onSubscribe(d: Disposable) {

                        }
                        override fun onNext(s: String) {
                            //网络请求成功
                            //存储配置文件
                            val cfgstr = DES3.decode(s)
                            if(!cfgstr.isNullOrEmpty()) {
                                SharePrefrencesUtil.getInstance().putString(Constant.TOKEN_CONF, cfgstr)
                                walletInit(cfgstr)
                            } else {
                                val walletCfgJson = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_CONF)
                                walletInit(walletCfgJson)

                            }


                        }

                        override fun onError(e: Throwable) {
                            //如果第一次进来请求配置失败，则按照如下默认配置
                            var s = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_CONF)
                            walletInit(s)

                        }

                        override fun onComplete() {

                        }
                    })


            return true
        } catch (e: Exception) {
            return false
        }
    }



    private  fun walletInit(s: String) {
        try {


            val tokenCfg = Gson().fromJson<TokenSet>(s, TokenSet::class.java)

            val walletDir = applicationContext.filesDir.toString() + "/"+ Constant.WALLET_STORE_DIR

            if (!TextUtils.isEmpty(SamosWalletUtils.getPin())) {

                Mobile.init(walletDir, SamosWalletUtils.getPin16())

                tokenCfg.tokens.forEach {
                    Mobile.registerNewCoin(it.tokenName, it.hostApi)
                }

            }
        } catch (e: Exception){
            e.printStackTrace()
        }

    }
}