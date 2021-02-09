package io.samos.wallet.activities.Wallet

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.samos.wallet.R
import io.samos.wallet.activities.MainActivity
import io.samos.wallet.activities.MnemonicWordActivity
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.WalletManager
import io.samos.wallet.push.WalletPush
import io.samos.wallet.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_wallet_init.*
import org.jetbrains.anko.centerInParent
import org.jetbrains.anko.dip

/**
 * 钱包引导-选择头像
 */
class WalletInitActivity : BaseActivity(), View.OnClickListener {
    var startType: String? = null
    var startFrom: Boolean? = false
    var avatarId: Int = 0
    var avatarIv: View? = null
    override fun attachLayoutRes(): Int {
        return R.layout.activity_wallet_init
    }

    override fun onClick(v: View) {
        when {
            v.id == R.id.avata1 -> avatarId = R.drawable.avata1
            v.id == R.id.avata2 -> avatarId = R.drawable.avata2
            v.id == R.id.avata3 -> avatarId = R.drawable.avata3
            v.id == R.id.avata4 -> avatarId = R.drawable.avata4
            v.id == R.id.avata5 -> avatarId = R.drawable.avata5
            v.id == R.id.avata6 -> avatarId = R.drawable.avata6
            v.id == R.id.avata7 -> avatarId = R.drawable.avata7
            v.id == R.id.avata8 -> avatarId = R.drawable.avata8
            v.id == R.id.avata9 -> avatarId = R.drawable.avata9
            v.id == R.id.avata10 -> avatarId = R.drawable.avata10
            v.id == R.id.avata11 -> avatarId = R.drawable.avata11
            v.id == R.id.avata12 -> avatarId = R.drawable.avata12
        }
        avatarIv?.background = null
        avatarIv = v
        wallet_avatar.setImageDrawable(resources.getDrawable(avatarId))
        super.onClick(v)
    }

    override fun initViews() {
        avata1.setOnClickListener(this)
        avata2.setOnClickListener(this)
        avata3.setOnClickListener(this)
        avata4.setOnClickListener(this)
        avata5.setOnClickListener(this)
        avata6.setOnClickListener(this)
        avata7.setOnClickListener(this)
        avata8.setOnClickListener(this)
        avata9.setOnClickListener(this)
        avata10.setOnClickListener(this)
        avata11.setOnClickListener(this)
        avata12.setOnClickListener(this)
        next.setOnClickListener(View.OnClickListener {
            initWallet()
        })
        avatarId = R.drawable.avata1
        iv_back.setOnClickListener { finish() }
    }

    override fun initData() {
        startType = intent.getStringExtra("STARTWAY")
        startFrom = intent.getBooleanExtra("STARTFROM", false)
        if (TextUtils.isEmpty(startType) || TextUtils.equals(startType, "CREATE")) {
            input_seed_hint.visibility = GONE
            seed.visibility = GONE
            view_seed.visibility = GONE
            next.setText(R.string.common_next)
            toolbar_title.text = getText(R.string.new_wallet)
        } else {
            input_seed_hint.visibility = VISIBLE
            seed.visibility = VISIBLE
            view_seed.visibility = VISIBLE
            next.setText(R.string.import_)
            toolbar_title.text = getText(R.string.load_wallet)

        }

    }

    fun initWallet() {
        if (TextUtils.isEmpty(startType) || TextUtils.equals(startType, "CREATE")) {
            if (TextUtils.isEmpty(wallet_name_et.text)) {
                ToastUtils.show(getString(R.string.pls_input_wallet_name))
                seed.requestFocus()
                return
            }

            val intent = Intent(this, MnemonicWordActivity::class.java)
            intent.putExtra("WALLET_NAME", wallet_name_et.text.toString())
            intent.putExtra("WALLET_PWD", wallet_pwd_et.text.toString())
            intent.putExtra("WALLET_AVATAR", avatarId)
            intent.putExtra(MnemonicWordActivity.MnemonicWordActivityLaunchType, MnemonicWordActivity.MnemonicWordActivityNewWallet)
            startActivity(intent)
        } else {
            if (TextUtils.isEmpty(seed.text)) {
                ToastUtils.show(getString(R.string.pls_input_seed))
                seed.requestFocus()
                return
            }
            if (TextUtils.isEmpty(wallet_name_et.text)) {
                ToastUtils.show(getString(R.string.pls_input_wallet_name))
                seed.requestFocus()
                return
            }

                importWallet()
        }
    }


    fun importWallet() {
        showTransferDialog()
        io.reactivex.Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(WalletManager.getInstance().newWalletDB(
                    wallet_name_et.text
                            .toString(), avatarId,
                    seed.text.toString()))
            it.onComplete()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Boolean) {
                        if (t) {
                            //发送通知
                            this@WalletInitActivity.textView.text = resources.getString(R.string.import_success)
                            Handler().postDelayed({
                                dialog.dismiss()
                                WalletPush.getInstance().walletUpdate()
                                val intent = Intent(this@WalletInitActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 1000)
                        }else{
                            dialog.dismiss()
                        }
                    }

                    override fun onError(e: Throwable) {
                        dialog.dismiss()
                        if (!TextUtils.isEmpty(e.message) && e.message?.contains("exist") == true) {
                            Toast.makeText(this@WalletInitActivity, resources.getString(R.string
                                    .wallet_is_existed), Toast.LENGTH_SHORT).show()
                        }
//                        Toast.makeText(this@WalletInitActivity, e.message, Toast.LENGTH_SHORT).show()
                    }

                })
    }

    /**
     * 弹出普通对话框
     */
    lateinit var dialog: Dialog
    lateinit var textView :TextView
    private fun showTransferDialog() {
        dialog = Dialog(this@WalletInitActivity, R.style.SimpleDialog)
        dialog.window.setWindowAnimations(R.style.dialogWindowAnim)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        var rlyRoot = RelativeLayout(this)
        rlyRoot.setBackgroundColor(Color.WHITE)
        textView = TextView(this)
        textView.text = getString(R.string.load_import)
        textView.textSize = 14.0F
        textView.setTextColor(Color.parseColor("#6d6f71"))
        val drawable = resources.getDrawable(R.drawable.fc_importwallet)
        drawable.setBounds(0, 0, dip(60), dip(60))
        textView.setCompoundDrawables(null, drawable, null, null)
        textView.compoundDrawablePadding = dip(20)
        rlyRoot.setPadding(dip(30), dip(30), dip(30), dip(30))
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        val rootLayoutParams = RelativeLayout.LayoutParams(dip(180), RelativeLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.centerInParent()
        rlyRoot.layoutParams = rootLayoutParams
        rlyRoot.addView(textView, layoutParams)
        dialog.setContentView(rlyRoot)
        dialog.show()
    }



}