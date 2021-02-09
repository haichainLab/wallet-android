package io.samos.wallet.activities.Transaction

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import io.reactivex.disposables.Disposable
import io.samos.wallet.R
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.utils.ImageUtils
import io.samos.wallet.utils.ToastUtils
import io.samos.wallet.utils.ZxingUtils
import kotlinx.android.synthetic.main.activity_into_wallet.*
import org.jetbrains.anko.dip
import java.io.File

/**
 * @author: lh on 2018/5/22 16:54.
 * Email:luchefg@gmail.com
 * Description: 转入地址显示-二维码
 */
class IntoWalletActivity : BaseActivity() {

    lateinit var address: String
    lateinit var token: String
    var amount: String = "0"
    lateinit var bitmap: Bitmap
    override fun attachLayoutRes(): Int {
        return R.layout.activity_into_wallet
    }

    override fun initViews() {
        id_toolbar.setNavigationOnClickListener {
            finish()
        }
        right.setOnClickListener {
            shareMsg(resources.getString(R.string.to_share), "", address, "")
        }
        btn_copy_url.setOnClickListener {
            val cm = this.getSystemService(Context
                    .CLIPBOARD_SERVICE) as ClipboardManager
            // 将文本内容放到系统剪贴板里。
            cm.text = address
            Toast.makeText(this, R.string.str_copy_success, Toast.LENGTH_LONG).show()
        }
        btn_save.setOnClickListener {

            if (ImageUtils.saveImageToGallery(this, this.bitmap)) {
                ToastUtils.show(R.string.save_to_album)
                finish()
            }
        }
        ev_amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                amount = p0.toString()
                gropQrStr()
            }

        })

    }

    //data
    override fun initData() {
        address = intent.getStringExtra(Constant.KEY_ADDRESS)
        token = intent.getStringExtra(Constant.TOKEN_TYPE)
        tv_address.text = address


        //lwj 0.0.1 HAI->HAIC
        if(token.equals("HAI")){
            id_toolbar_title.text = resources.getString(R.string.into) + " HAIC"
        }else{
            id_toolbar_title.text = resources.getString(R.string.into) + " " + token
        }

        gropQrStr()
    }
    /**
     * samos://pay?address=2PueS4hMYwsAgeHRDzyAhj1nA9LPmCR5XuB&amount=5&token=samos
     */
    fun gropQrStr() {
        var qr: String = "samos://pay?address=" + address + "&amount=" + amount + "&token=" + token
        ZxingUtils.createQRCode(qr, this@IntoWalletActivity.dip(148)).subscribe(object : io.reactivex.Observer<Bitmap> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(bitmap: Bitmap) {
                this@IntoWalletActivity.bitmap = bitmap
                iv_qrcode.setImageBitmap(bitmap)
            }

            override fun onError(e: Throwable) {
                ToastUtils.show("出错了")
            }

            override fun onComplete() {

            }
        })
    }

    fun shareMsg(activityTitle: String, msgTitle: String, msgText: String,
                 imgPath: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        if (imgPath == null || imgPath == "") {
            intent.type = "text/plain" // 纯文本
        } else {
            val f = File(imgPath)
            if (f != null && f.exists() && f.isFile) {
                intent.type = "image/jpg"
                val u = Uri.fromFile(f)
                intent.putExtra(Intent.EXTRA_STREAM, u)
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle)
        intent.putExtra(Intent.EXTRA_TEXT, msgText)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ContextCompat.startActivity(this@IntoWalletActivity, Intent.createChooser(intent, activityTitle), null)
    }

}


