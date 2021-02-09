package io.samos.wallet.activities.ManagerWallet

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
import android.widget.RelativeLayout
import android.widget.TextView
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.samos.wallet.R
import io.samos.wallet.activities.BackupsWallet.BackUpWalletActivity
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.Wallet
import io.samos.wallet.datas.WalletDB
import io.samos.wallet.datas.WalletManager
import io.samos.wallet.push.WalletPush
import io.samos.wallet.utils.SharePrefrencesUtil
import io.samos.wallet.utils.SamosWalletUtils
import io.samos.wallet.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_manager_walletinfo.*
import kotlinx.android.synthetic.main.dialog_delete.*
import kotlinx.android.synthetic.main.dialog_wallet_pwd.*
import kotlinx.android.synthetic.main.dialog_wallet_pwd.view.*
import org.jetbrains.anko.centerInParent
import org.jetbrains.anko.dip

/**
 * @author: lh on 2018/5/22 17:43.
 * Email:luchefg@gmail.com
 * Description: 钱包管理-详情
 */
class ManagerWalletInfoActivity : BaseActivity() {

    lateinit var walletDB: WalletDB
     var wallets: List<Wallet> = arrayListOf()
    var walletViewModel: WalletViewModel = WalletViewModel()
    var type: Int = 0

    override fun attachLayoutRes(): Int {
        return R.layout.activity_manager_walletinfo
    }

    override fun initViews() {
        id_toolbar.setNavigationOnClickListener {
            finish()
        }
        walletDB = intent.getSerializableExtra(Constant.WALLET_INDEX) as WalletDB
        id_toolbar_title.text = walletDB.name
        ev_name.setText(walletDB.name)

        btn_backup.setOnClickListener {
            type = 1
            showWalletPwd()
        }
        btn_delete.setOnClickListener {
            type = 2
            showDelDialog()

        }
        iv_wallet_info.background = resources.getDrawable(walletDB.avatarResourceId)
        ev_pwd.setText(SharePrefrencesUtil.getInstance().getString(Constant.PIN_HINt))
        right.setOnClickListener {
            if (checkEmpty()) {
                walletDB.name = ev_name.text.toString()
                WalletManager.getInstance().updateWalletDB(id_toolbar_title.text.toString(), walletDB)
                WalletManager.getInstance().saveDefaultWalletDB(walletDB)
                SharePrefrencesUtil.getInstance().putString(Constant.PIN_HINt, ev_pwd.text.toString())
                showTransferDialog()
            }
        }
    }

    /**
     * 保存成功
     */
    lateinit var successDialog: Dialog

    private fun showTransferDialog() {
        successDialog = Dialog(this@ManagerWalletInfoActivity, R.style.SimpleDialog)
        successDialog.window.setWindowAnimations(R.style.dialogWindowAnim)
        successDialog.setCancelable(false)
        successDialog.setCanceledOnTouchOutside(false)
        var rlyRoot = RelativeLayout(this)
        rlyRoot.setBackgroundColor(Color.WHITE)
        val textView = TextView(this)
        textView.text = getString(R.string.save_success)
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
            this@ManagerWalletInfoActivity.finish()
        }, 1000)
    }

    fun checkEmpty(): Boolean {
        if (TextUtils.isEmpty(ev_name.text)) {
            ToastUtils.show(getString(R.string.pls_input_wallet_name))
            return false
        }
        return true
    }

    /**
     * 删除提示对话框
     */
    lateinit var dialog: Dialog

    private fun showDelDialog() {
        dialog = Dialog(this@ManagerWalletInfoActivity, R.style.SimpleDialog)
        dialog.window.setWindowAnimations(R.style.dialogWindowAnim)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_delete)
        dialog.tv_cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.tv_confirm.setOnClickListener {
            dialog.dismiss()
            showWalletPwd()
        }
        dialog.show()

    }

    /**
     * 展示钱包密码对话框
     */
    private fun showWalletPwd() {
        val dialog = Dialog(this@ManagerWalletInfoActivity, R.style.SimpleDialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val rootView = LayoutInflater.from(this@ManagerWalletInfoActivity).inflate(R.layout
                .dialog_wallet_pwd, null)
        val dialogWindow = dialog.window
        dialogWindow.decorView.setPadding(0, 0, 0, 0)
        dialogWindow.setGravity(Gravity.BOTTOM)
        //            dialogWindow.setWindowAnimations(R.style.dialogstyle) // 添加动画
        val lp = dialogWindow.attributes // 获取对话框当前的参数值
        lp.width = resources.displayMetrics.widthPixels // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT // 高度
        dialogWindow.attributes = lp
        if(!TextUtils.isEmpty(SharePrefrencesUtil.getInstance().getString(Constant.PIN_HINt))) {
            rootView.tv_pwdhint.text = resources.getString(R.string.pwd_hint) + SharePrefrencesUtil.getInstance().getString(Constant.PIN_HINt)
        } else{
            rootView.tv_pwdhint.visibility = View.GONE
        }

        rootView.et_wallet_pwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        rootView.iv_wallet_pwd_back.setOnClickListener {
            dialog.dismiss()
        }
        rootView.btn_wallet_pwd_ok.setOnClickListener {
            if (dialog.et_wallet_pwd.text.toString().equals(SamosWalletUtils.getPin())) {
                dialog.dismiss()
                if (wallets !=null){
                if (type == 1) {
                    var intent = Intent(this@ManagerWalletInfoActivity, BackUpWalletActivity::class.java)
                    intent.putExtra(Constant.KEY_WALLET, wallets[0].walletID)
                    intent.putExtra(Constant.IS_BACKUP_WALLET,"1")


                    startActivity(intent)
                } else if (type == 2) {
                    deleteWallet()
                }
                }
            }else{
                ToastUtils.show(this@ManagerWalletInfoActivity.resources.getString(R.string.pin_error))
            }
        }
        dialog.setContentView(rootView)
        dialog.show()
    }

    override fun initData() {
        walletViewModel.getAllDetailWallets(walletDB)
                .compose(this.bindUntilEvent(com.trello.rxlifecycle2.android.ActivityEvent.DESTROY))
                .subscribe(object : Observer<List<Wallet>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(wallets: List<Wallet>) {
                        this@ManagerWalletInfoActivity.wallets = wallets
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                    }
                })
    }

    lateinit var delDialog: Dialog
    /**
     * 保存钱包修改
     */
    fun showStateDialog() {
        delDialog = Dialog(this@ManagerWalletInfoActivity, R.style.SimpleDialog)
        delDialog.window.setWindowAnimations(R.style.dialogWindowAnim)
        delDialog.setCancelable(false)
        delDialog.setCanceledOnTouchOutside(false)
        var rlyRoot = RelativeLayout(this)
        rlyRoot.setBackgroundColor(Color.WHITE)
        val textView = TextView(this)
        textView.textSize = 14.0F
        textView.setTextColor(Color.parseColor("#6d6f71"))
        if (type == 1) {
            textView.text = getString(R.string.wallet_save_successful)
        } else if (type == 2) {
            textView.text = getString(R.string.del_success)
        }
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
        delDialog.setContentView(rlyRoot)
        delDialog.show()
        Handler().postDelayed({
            delDialog.dismiss()
            WalletPush.getInstance().walletUpdate()
            finish()
        }, 1000)
    }

    /**
     * 删除钱包
     */
    fun deleteWallet() {

        showDialog(1)
        walletViewModel.deleteWallet(wallets, walletDB)
                .compose(this.bindUntilEvent<Boolean>(ActivityEvent.DESTROY))
                .subscribe(object : Observer<Boolean> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Boolean) {
                        //通知首页钱包进行更新
                        if (t) {
                            showStateDialog()
                        }
                    }

                    override fun onError(e: Throwable) {
                        ToastUtils.show(getString(R.string.delete_wallet_error_msg))
                    }

                    override fun onComplete() {
                        dismissDialog(1)
                    }
                })
    }
}