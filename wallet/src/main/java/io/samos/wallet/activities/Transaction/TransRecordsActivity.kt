package io.samos.wallet.activities.Transaction

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.Disposable
import io.samos.wallet.R
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.beans.TokenSet
import io.samos.wallet.beans.TransInfo
import io.samos.wallet.common.Constant
import io.samos.wallet.utils.SharePrefrencesUtil
import io.samos.wallet.utils.ToastUtils
import io.samos.wallet.utils.ZxingUtils
import kotlinx.android.synthetic.main.activity_trans_records.*
import java.text.SimpleDateFormat

/**
 * @author: lh on 2018/5/22 14:47.
 * Email:luchefg@gmail.com
 * Description: 交易记录
 */
class TransRecordsActivity : BaseActivity() {

    lateinit var trans: TransInfo

    override fun attachLayoutRes(): Int {
        return R.layout.activity_trans_records
    }

    override fun initViews() {
        id_toolbar_title.text = resources.getString(R.string.trans_records)
        id_toolbar.setNavigationOnClickListener {
            finish()
        }

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        trans = intent.getSerializableExtra(Constant.KEY_TRANS) as TransInfo
        tv_amount.text =  trans.delta
        if (trans.status) {
            iv_state.visibility = View.VISIBLE
        } else {
            iv_state.visibility = View.GONE
        }


        //lwj 0.0.1 HAI->HAIC
        if(trans.token.equals("HAI")){
            tv_type.text = "HAIC"
        }else{
            tv_type.text = trans.token
        }


        tv_inputs_address.text = trans.inputs
        tv_outputs_address.text = trans.outputs

       // tv_order_info.text = resources.getString(R.string.roll_out) + " " + trans.token

        //lwj 0.0.1 HAI->HAIC
        if(trans.token.equals("HAI")){
            tv_trans_num.text =  trans.delta + " HAIC"
        }else{
            tv_trans_num.text =  trans.delta + " " + trans.token
        }


        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var str = trans.time+"000"


        val dt = dateFormat.format(str.toLong())

        tv_trans_time.text = dt

        val tokenCfgJson = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_CONF)
        val tokenSet = Gson().fromJson<TokenSet>(tokenCfgJson,
                object : TypeToken<TokenSet>() {}.type)



        val url = tokenSet.getExplorerUrlByToken(trans.token)+trans.txid

        ZxingUtils.createQRCode(url, 500).subscribe(object : io.reactivex.Observer<Bitmap> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(bitmap: Bitmap) {
                iv_qrcode.setImageBitmap(bitmap)
            }

            override fun onError(e: Throwable) {
                ToastUtils.show("出错了")
            }

            override fun onComplete() {

            }
        })

        btn_copy_url.setOnClickListener {
            val cm = this.getSystemService(Context
                    .CLIPBOARD_SERVICE) as ClipboardManager
            // 将文本内容放到系统剪贴板里。
            cm.text =url
            Toast.makeText(this, R.string.str_copy_success, Toast.LENGTH_LONG).show()
        }
    }


}