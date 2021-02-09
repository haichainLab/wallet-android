package io.samos.wallet.activities.Wallet

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import io.samos.wallet.R
import io.samos.wallet.activities.PIN.PinsetActivity
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.utils.SamosWalletUtils
import io.samos.wallet.utils.SharePrefrencesUtil
import kotlinx.android.synthetic.main.activity_first_start.*
import org.jetbrains.anko.centerInParent
import org.jetbrains.anko.dip
import android.icu.util.ULocale.getLanguage
import java.util.*


class FirstInitAcitivity : BaseActivity() {
    /** 对话框 */
    lateinit var successDialog: Dialog
    override fun attachLayoutRes(): Int {
        return R.layout.activity_first_start
    }

    override fun isTranslucent(): Boolean = true
    override fun isStatusBarBlack(): Boolean = false
    override fun initViews() {
        val lan = Locale.getDefault().language
        if( lan == "zh") {
            SharePrefrencesUtil.getInstance().putBoolean(Constant.IS_UNIT_CNY,true)
        } else {
            SharePrefrencesUtil.getInstance().putBoolean(Constant.IS_UNIT_CNY,false)
        }



        create_wallet.setOnClickListener(View.OnClickListener {
            launchCreateWallet()
        })

        import_wallet.setOnClickListener(View.OnClickListener {
            launchImportWallet()
        })
    }

    override fun onResume() {
        super.onResume()
        if (!isCheckAndLaunchPwdSetActivity()) {
            return
        }
    }


    private fun isCheckAndLaunchPwdSetActivity(): Boolean {
        if (!SamosWalletUtils.isPinSet()) {
            launchPwdSetActivity()
            return false
        }
        return true
    }

    private fun launchPwdSetActivity() {
        val intent = Intent(this, PinsetActivity::class.java)
        startActivityForResult(intent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode== Activity.RESULT_OK) {
            if (requestCode==0) {
                Handler().postDelayed({showTransferDialog()},500)
            }
        }
    }

    /**
     * 弹出普通对话框
     */
    private fun showTransferDialog() {
        successDialog = Dialog(this@FirstInitAcitivity, R.style.SimpleDialog)
        successDialog.window.setWindowAnimations(R.style.dialogWindowAnim)
        successDialog.setCancelable(false)
        successDialog.setCanceledOnTouchOutside(false)
        var rlyRoot = RelativeLayout(this)
        rlyRoot.setBackgroundColor(Color.WHITE)
        val textView = TextView(this)
        textView.text = getString(R.string.password_set_successful)
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
        rlyRoot.addView(textView,layoutParams)
        successDialog.setContentView(rlyRoot)
        successDialog.show()
        Handler().postDelayed({
            successDialog.dismiss()
        },1000)
    }

    override fun initData() {
    }

    fun launchImportWallet() {
        val intent = Intent(this, WalletInitActivity::class.java)
        intent.putExtra("STARTWAY", "IMPORT")
        startActivity(intent)
    }

    fun launchCreateWallet() {
        val intent = Intent(this, WalletInitActivity::class.java)
        intent.putExtra("STARTWAY", "CREATE")
        startActivity(intent)
    }
}