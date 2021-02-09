package io.samos.wallet.activities.About

import android.app.Dialog
import android.content.Context
import android.os.Environment
import android.os.Looper
import android.text.TextUtils.replace
import android.util.Log
import com.google.gson.Gson
import io.samos.wallet.R
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.beans.VersionBean
import kotlinx.android.synthetic.main.activity_aboutus.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.allenliu.versionchecklib.callback.OnCancelListener;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadFailedListener
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.allenliu.versionchecklib.v2.ui.VersionService.builder
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.samos.upgrade.BaseDialog
import io.samos.wallet.activities.Main.WalletViewModel
import io.samos.wallet.common.Constant
import io.samos.wallet.utils.*
import kotlinx.android.synthetic.main.custom_dialog_two_layout.*
import java.lang.Exception

/**
 * @author: lh on 2018/5/23 15:51.
 * Email:luchefg@gmail.com
 * Description: 系统设置
 */
class AboutUsActivity : BaseActivity() {


    override fun attachLayoutRes(): Int {
        return R.layout.activity_aboutus
    }

    override fun isStatusBarBlack(): Boolean = false
    var version = "";


    override fun initData() {
        LogUtils.d("init data")
    }


    override fun initViews() {
        id_toolbar.setNavigationOnClickListener {
            finish()
        }
        tv_version.text = AppUtils.getVersionName(this)
        version = AppUtils.getVersionName(this)
        id_toolbar_title.text = resources.getString(R.string.about_us)


        tv_update.setOnClickListener {
            ToastUtils.show("检查最新版本...")
            checkVersion()
        }

    }
    private fun checkVersion() {

        WalletViewModel().getAppVersion("samos")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//指定运行在main线程就可以解决了

                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {

                    }
                    override fun onNext(s: String) {

                        val cfgstr = DES3.decode(s)
                        Log.d("version-data-log", cfgstr)


                        if (!cfgstr.isNullOrEmpty()) {
                            try {
                                val versionBean = Gson().fromJson<VersionBean>(cfgstr, VersionBean::class.java)
                                if (versionBean != null && versionBean.needsUpdate(versionBean.version,version)) {
                                    sendRequest();
                                } else {
                                 //   Looper.prepare();
                                    ToastUtils.show("已经是最新版本了!")
                                   // Looper.loop();

                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace();

                            }


                        }


                    }

                    override fun onError(e: Throwable) {
                        //请求网络失败，此时读取上次保存的配置
                    }

                    override fun onComplete() {

                    }
                })
    }





    private fun sendRequest() {


        builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl("http://appapi.haicshop.com/api/version?tag=samos")
                .request(object : RequestVersionListener {
                    override fun onRequestVersionSuccess(s: String): UIData {
                        val cfgstr = DES3.decode(s)

                        if (!cfgstr.isNullOrEmpty()) {
                            try {
                                val versionBean = Gson().fromJson<VersionBean>(cfgstr, VersionBean::class.java)
                                if (versionBean != null && versionBean.needsUpdate(versionBean.version,version)) {

                                    return createUIData(versionBean.title, versionBean.notice, versionBean.url)

                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace();

                            }


                        }
                        return createUIData("", "", "")
                    }

                    override fun onRequestVersionFailure(message: String) {
                        ToastUtils.show("request failed")
                    }
                })



        builder.setShowDownloadingDialog(true)
        builder.setShowNotification(true)
//        builder.setNotificationBuilder(createCustomNotification())
        builder.setShowDownloadFailDialog(true)


        builder.setOnCancelListener(OnCancelListener {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
        }
        )

        builder.setCustomVersionDialogListener(createCustomDialogTwo())
        builder.setCustomDownloadingDialogListener(createCustomDownloadingDialog())


        //自定义下载路径
        builder.setDownloadAPKPath(Environment.getExternalStorageDirectory().toString() + "/ALLEN/AllenVersionPath2/")

        builder.setOnCancelListener({
            ToastUtils.show("cancle")
        })
        builder.executeMission(this)
    }

    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     */
    private fun createUIData(title: String, notice: String, url: String): UIData {
        val uiData = UIData.create()
        uiData.title = title
        uiData.downloadUrl = url

        uiData.content = notice.replace("\\n", "\n");
        return uiData
    }

    override fun onDestroy() {
        super.onDestroy()
        AllenVersionChecker.getInstance().cancelAllMission(this)
    }

//    private fun createCustomNotification(): NotificationBuilder {
//        return NotificationBuilder.create()
//                .setRingtone(true)
//                //   .setIcon(R.mipmap.dialog4)
//                .setTicker("custom_ticker")
//                .setContentTitle("custom title")
//                .setContentText(getString(R.string.custom_content_text))
//    }

    private fun createCustomDownloadingDialog(): CustomDownloadingDialogListener {
        return object : CustomDownloadingDialogListener {
            override fun getCustomDownloadingDialog(context: Context, progress: Int, versionBundle: UIData): Dialog {
                return BaseDialog(context, R.style.BaseDialog, R.layout.custom_download_layout)
            }

            override fun updateUI(dialog: Dialog, progress: Int, versionBundle: UIData) {
                val tvProgress = dialog.findViewById<TextView>(R.id.tv_progress)
                val progressBar = dialog.findViewById<ProgressBar>(R.id.pb)
                progressBar.progress = progress
                tvProgress.text = getString(R.string.versionchecklib_progress, progress)
            }
        }
    }

    /**
     * 务必用库传回来的context 实例化你的dialog
     *
     * @return
     */
    private fun createCustomDownloadFailedDialog(): CustomDownloadFailedListener {
        return object : CustomDownloadFailedListener {
            override fun getCustomDownloadFailed(context: Context, versionBundle: UIData): Dialog {
                return BaseDialog(context, R.style.BaseDialog, R.layout.custom_download_layout)

            }
        }
    }

    private fun createCustomDialogTwo(): CustomVersionDialogListener {
        return object : CustomVersionDialogListener {
            override fun getCustomVersionDialog(context: Context, versionBundle: UIData): Dialog {
                val baseDialog = BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_two_layout)

                val textView = baseDialog.tv_msg
                textView.setText(versionBundle.getContent())

                val textViewTitle = baseDialog.tv_title
                textViewTitle.setText(versionBundle.title)

                baseDialog.setCanceledOnTouchOutside(true)
                return baseDialog

            }
        }


    }

}