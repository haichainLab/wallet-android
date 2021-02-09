package io.samos.wallet.activities.BackupsWallet

import android.app.Dialog
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.samos.wallet.BuildConfig
import io.samos.wallet.R
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_mnemonicword.*
import kotlinx.android.synthetic.main.dialog_ban_screenshots.*
import org.jetbrains.anko.centerInParent
import org.jetbrains.anko.dip

class BackUpWalletActivity : BaseActivity() {

    var isSecondStep = false
    var showType: String = ""
    var walletName: String = ""
    var pwd: String = ""
    var seed: String = ""
    var startFrom: Boolean = false
    lateinit var WalletId: String
    lateinit var isBackupWallet: String


    override fun attachLayoutRes(): Int {
        return R.layout.activity_mnemonicword
    }

    override fun initViews() {
        WalletId = intent.getStringExtra(Constant.KEY_WALLET)
        isBackupWallet = intent.getStringExtra(Constant.IS_BACKUP_WALLET)
        getSeed()
    }

    override fun initData() {
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

        if("1".equals(isBackupWallet)) {
            next.text = ""

        }

        mnemonic_hint_title.text = getString(R.string.write_down_mnemonic)
        mnemonic_hint_content.text = getString(R.string.write_down_mnemonic_hint)
        mnemonic_word_show.isCursorVisible = false
        mnemonic_word_show.isFocusable = false
        mnemonic_word_show.isFocusableInTouchMode = false
        generate_seed.visibility = View.GONE
        //if (BuildConfig.DEBUG) {
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
                if (!TextUtils.equals(mnemonic_word_show.text.toString(), seed)) {
                    ToastUtils.show(R.string.pls_confirm_seed)
                    return@OnClickListener
                }
              showTransferDialog()
            } catch (e: Exception) {


            }

        })
    }
    /**
     * 弹出普通对话框
     */
    lateinit var successDialog:Dialog
    private fun showTransferDialog() {
        successDialog = Dialog(this@BackUpWalletActivity, R.style.SimpleDialog)
        successDialog.window.setWindowAnimations(R.style.dialogWindowAnim)
        successDialog.setCancelable(false)
        successDialog.setCanceledOnTouchOutside(false)
        var rlyRoot = RelativeLayout(this)
        rlyRoot.setBackgroundColor(Color.WHITE)
        val textView = TextView(this)
        textView.text = getString(R.string.back_up_wallet)
        textView.textSize = 14.0F
        textView.setTextColor(Color.parseColor("#6d6f71"))
        val drawable = resources.getDrawable(R.drawable.fc_backupsuccess)
        drawable.setBounds(0, 0, dip(60), dip(60))
        textView.setCompoundDrawables(null, drawable, null, null)
        textView.compoundDrawablePadding = dip(20)
        rlyRoot.setPadding(dip(30), dip(30), dip(30), dip(30))
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        val rootLayoutParams = RelativeLayout.LayoutParams(dip(180), RelativeLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.centerInParent()
        rlyRoot.layoutParams = rootLayoutParams
        rlyRoot.addView(textView,layoutParams)
        successDialog.setContentView(rlyRoot)
        successDialog.show()
        Handler().postDelayed({
            successDialog.dismiss()
            this.finish()
        },1000)
    }

    var walletViewModel: WalletViewModel = WalletViewModel()
    fun getSeed() {
        showDialog(1)
        walletViewModel.getWalletSeed(WalletId)
                .compose(this.bindUntilEvent<String>(com.trello.rxlifecycle2.android.ActivityEvent.DESTROY))
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(s: String) {
                        dismissDialog(1)
                        seed = s
                        mnemonic_word_show.setText(s)
                        showDialog()
                    }

                    override fun onError(e: Throwable) {
                        dismissDialog(1)
                    }

                    override fun onComplete() {
                    }
                })
    }
    /**
     * 禁止拍照对话框
     */
    lateinit var dialog: Dialog

    private fun showDialog() {
        dialog = Dialog(this@BackUpWalletActivity, R.style.SimpleDialog)
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


}