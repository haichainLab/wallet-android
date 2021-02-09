package io.samos.wallet.activities

import android.app.Dialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.samos.wallet.BuildConfig
import io.samos.wallet.R
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.WalletManager
import io.samos.wallet.push.WalletPush
import io.samos.wallet.utils.AppManager
import io.samos.wallet.utils.SamosWalletUtils
import io.samos.wallet.utils.ToastUtils
import io.samos.wallet.widget.LoadingDialog
import kotlinx.android.synthetic.main.activity_mnemonicword.*
import kotlinx.android.synthetic.main.dialog_ban_screenshots.*
import mobile.Mobile
import org.jetbrains.anko.centerInParent
import org.jetbrains.anko.dip

class MnemonicWordActivity : BaseActivity() {
    var isSecondStep = false
    var showType: String = ""
    var walletName: String = ""
    var pwd: String = ""
    var seed: String = ""
    lateinit var loadingDialog: LoadingDialog
    var startFrom: Boolean = false
    var avatarId: Int = 0

    override fun attachLayoutRes(): Int {
        return R.layout.activity_mnemonicword
    }

    override fun initViews() {
        loadingDialog = LoadingDialog(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        generate_seed.setOnClickListener(View.OnClickListener {
            generateAndShowSeeds()
        })
    }

    override fun initData() {
        walletName = intent.getStringExtra("WALLET_NAME")
        startFrom = intent.getBooleanExtra("STARTFROM", false)

        pwd = intent.getStringExtra("WALLET_PWD")
        avatarId = intent.getIntExtra("WALLET_AVATAR", 0)
        generateAndShowSeeds()
        showDialog()
        showFirstStep()
    }

    override fun onBackPressed() {
        if (isSecondStep) {
            showFirstStep()
            return
        }
        super.onBackPressed()
    }

    fun showFirstStep() {
        isSecondStep = false
        next.text = getString(R.string.common_next)
        mnemonic_hint_title.text = getString(R.string.write_down_mnemonic)
        mnemonic_hint_content.text = getString(R.string.write_down_mnemonic_hint)
        mnemonic_word_show.isCursorVisible = false
        mnemonic_word_show.isFocusable = false
        mnemonic_word_show.isFocusableInTouchMode = false
        generate_seed.visibility = View.VISIBLE
     //   if (BuildConfig.DEBUG) {
            mnemonic_word_show.setOnLongClickListener(View.OnLongClickListener {
                val cm = getSystemService(Context
                        .CLIPBOARD_SERVICE) as ClipboardManager
                // 将文本内容放到系统剪贴板里。
                cm.text = mnemonic_word_show.text.toString()
                ToastUtils.show(R.string.str_copy_success)
                true
            })
       // }

        mnemonic_word_show.setText(seed)
        next.setOnClickListener(View.OnClickListener {
            showSecondStep()
            iv_mnemonic_back.setOnClickListener {
                showFirstStep()
                iv_mnemonic_back.setOnClickListener {
                    finish()
                }
            }
        })
        iv_mnemonic_back.setOnClickListener {
            finish()
        }
    }

    fun showSecondStep() {
        isSecondStep = true
        next.text = getString(R.string.confirm)
        mnemonic_word_show.setText("")
        mnemonic_hint_title.text = getString(R.string.make_sure_remember)
        mnemonic_hint_content.text = getString(R.string.retype_mnenonic)
        mnemonic_word_show.isFocusable = true
        mnemonic_word_show.isCursorVisible = true
        mnemonic_word_show.isFocusableInTouchMode = true
        mnemonic_word_show.requestFocus()
        generate_seed.visibility = View.GONE
        mnemonic_word_show.setOnLongClickListener(null)
        next.setOnClickListener(View.OnClickListener {
            try {
                if (!TextUtils.equals(mnemonic_word_show.text, seed)) {
                    ToastUtils.show(R.string.pls_confirm_seed)
                    return@OnClickListener
                }
                createWallet()
            } catch (e: Exception) {


            }

        })
    }

    fun createWallet() {
        loadingDialog.show(resources.getString(R.string.plase_loading))
        Observable.create(ObservableOnSubscribe<Boolean> {
//            it.onNext(WalletManager.getInstance().newWallet(Constant.COIN_TYPE_SAMO, walletName, avatarId, seed))
            it.onNext(WalletManager.getInstance().newWalletDB( walletName, avatarId, seed))

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onComplete() {
                        loadingDialog.dismiss()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        loadingDialog.dismiss()
                        if (!TextUtils.isEmpty(e.message) && e.message?.contains("exist") == true) {
                            ToastUtils.show(resources.getString(R.string
                                    .wallet_name_existed))
                        }
//                        ToastUtils.show(e.message)
                    }

                    override fun onNext(t: Boolean) {
                        loadingDialog.dismiss()
                        if (t) {
                            showTransferDialog()
                        }
                    }

                })


    }

    /**
     * 禁止拍照对话框
     */
    lateinit var dialog: Dialog

    private fun showDialog() {
        dialog = Dialog(this@MnemonicWordActivity, R.style.SimpleDialog)
        dialog.window.setWindowAnimations(R.style.dialogWindowAnim)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_ban_screenshots)
        dialog.tv_text.text = resources.getString(R.string.ban_screen_hint)

        dialog.tv_got.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    lateinit var successDialog: Dialog
    private fun showTransferDialog() {
        successDialog = Dialog(this@MnemonicWordActivity, R.style.SimpleDialog)
        successDialog.window.setWindowAnimations(R.style.dialogWindowAnim)
        successDialog.setCancelable(false)
        successDialog.setCanceledOnTouchOutside(false)
        var rlyRoot = RelativeLayout(this)
        rlyRoot.setBackgroundColor(Color.WHITE)
        val textView = TextView(this)
        textView.text = getString(R.string.wallet_set_successful)
        textView.textSize = 14.0F
        textView.setTextColor(Color.parseColor("#6d6f71"))
        val drawable = resources.getDrawable(R.drawable.transfer_success)
        drawable.setBounds(0, 0, dip(60), dip(60))
        textView.setCompoundDrawables(null, drawable, null, null)
        textView.compoundDrawablePadding = dip(20)
        rlyRoot.setPadding(dip(30), dip(30), dip(30), dip(30))
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        val rootLayoutParams = RelativeLayout.LayoutParams(dip(180), RelativeLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.centerInParent()
        rlyRoot.layoutParams = rootLayoutParams
        rlyRoot.addView(textView, layoutParams)
        successDialog.setContentView(rlyRoot)
        successDialog.show()
        Handler().postDelayed({
            successDialog.dismiss()
            //发送通知
            WalletPush.getInstance().walletUpdate()
            intent = Intent(this@MnemonicWordActivity, MainActivity::class.java)
            startActivity(intent)
            if (startFrom) {
                AppManager.getAppManager().finishActivitiesBelow(this@MnemonicWordActivity, true)
            }
            finish()
        }, 1000)
    }

    override fun onPause() {
        super.onPause()
        if (loadingDialog != null && !this.isFinishing) {
            loadingDialog.dismiss()
        }
    }

    private fun creatFiveWallet(id: String) {
        try {
            for (i in 0..3) {
                Mobile.newAddress(id, 1, SamosWalletUtils.getPin16())
            }
        } catch (e: Exception) {

        }

    }

    fun generateAndShowSeeds(): String? {
        seed = Mobile.newSeed()
        mnemonic_word_show.setText(seed)
        return seed
    }

    companion object {
        val MnemonicWordActivityLaunchType = "MNEMONICWORDLAUNCHTYPE"
        val MnemonicWordActivityNewWallet = "MNEMONICWORDNEWWALLET"
    }
}