package io.samos.wallet.activities.WebViews

import android.os.Build
import android.os.Bundle
import android.os.Message
import android.support.annotation.RequiresApi
import android.view.View
import android.webkit.*
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.samos.wallet.R
import io.samos.wallet.activities.Html5Activity
import io.samos.wallet.base.BaseFragment
import io.samos.wallet.beans.TokenSet
import io.samos.wallet.common.Constant
import io.samos.wallet.datas.Wallet
import io.samos.wallet.datas.WalletManager
import io.samos.wallet.utils.SharePrefrencesUtil
import io.samos.wallet.utils.ToastUtils

import kotlinx.android.synthetic.main.activity_vote.*
import mobile.Mobile
import org.jetbrains.anko.startActivity
import android.net.Uri

import android.content.Intent

/**
 * 投票用webview lwj 2.1.1
 */
class VoteFragment : BaseFragment() {

    internal lateinit var wallet: Wallet

    var defaultAddress: String = ""
    var voteUrl: String = ""
    var voteNo: String = ""

    private var mTitle: TextView? = null

    override fun attachLayoutRes(): Int {
        return R.layout.activity_vote
    }

    override fun initViews(rootView: View?) {
    }

    fun getWebView(): WebView {
        return this.webview_vote
    }

    fun setVoteParam(str1: String, str2: String) {
        this.voteUrl = str1
        this.voteNo = str2

    }

    override fun initData() {

        var defaultwallet = WalletManager.getInstance().restorDefaultWalletDB()
        val mapWlts = defaultwallet.getWallets()
        val wallet1 = mapWlts["HAI"]!!.walletID
        var addressList = WalletManager.getInstance().getAddressesByWalletId("haicoin", wallet1)
        if (addressList.size > 0) {
            defaultAddress = addressList[0].address
        } else {
            ToastUtils.show("Disk Error,Please backup your wallet and reinstall")
        }


        val mWebSettings = webview_vote.settings
        mWebSettings.setSupportZoom(true)
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.useWideViewPort = true
        mWebSettings.defaultTextEncodingName = "utf-8"
        mWebSettings.loadsImagesAutomatically = true

        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        mWebSettings.javaScriptEnabled = true

        saveData(mWebSettings)

        newWin(mWebSettings)

        var webViewClient: WebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                this@VoteFragment.activity?.startActivity<Html5Activity>("Url" to url)


                var uri = Uri.parse(url);
                if (uri.getScheme().equals("haichainvote")) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } else {
                    view.loadUrl(url)
                }
                return true

//                return view.hitTestResult == null
//                return true
            }

//            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//                this@VoteFragment.activity?.startActivity<Html5Activity>("Url" to request.url)
//                return true
//            }
        }
        var webChromeClient: WebChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    progressBar?.visibility = View.GONE//加载完网页进度条消失
                } else {
                    progressBar?.visibility = View.VISIBLE//开始加载网页时显示进度条
                    progressBar?.progress = newProgress//设置进度值
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String) {
                super.onReceivedTitle(view, title);
                if (title != null) {
                    //  titleView.setCenterText(title);
                }
            }

            //=========多窗口的问题==========================================================
            override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = view
                resultMsg.sendToTarget()
                return true
            }
            //=========多窗口的问题==========================================================
        }

        webview_vote.webViewClient = webViewClient
        webview_vote.webChromeClient = webChromeClient
        webview_vote.loadUrl(this.voteUrl+ "?address=" + defaultAddress + "&vote_no=" + this.voteNo)

    }

    /**
     * HTML5数据存储
     */
    private fun saveData(mWebSettings: WebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        mWebSettings.domStorageEnabled = true
        mWebSettings.databaseEnabled = true
        mWebSettings.setAppCacheEnabled(true)
        val appCachePath = activity?.cacheDir?.absolutePath
        mWebSettings.setAppCachePath(appCachePath)
    }

    /**
     * 多窗口的问题
     */
    private fun newWin(mWebSettings: WebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(false)
        mWebSettings.javaScriptCanOpenWindowsAutomatically = true
    }

    companion object {

        fun newInstance(args: Bundle?): VoteFragment {
            val instance = VoteFragment()
            instance.arguments = args
            return instance
        }
    }
}