package io.samos.wallet.activities.found

import android.os.Build
import android.os.Bundle
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
import io.samos.wallet.utils.SharePrefrencesUtil

import kotlinx.android.synthetic.main.activity_found.*
import org.jetbrains.anko.startActivity


/**
 * 发现fragment
 * Created by 方丈
 * on 2018/8/6.
 */
class FoundFragment : BaseFragment(){
    private var mTitle: TextView?=null

    override fun attachLayoutRes(): Int {
        return R.layout.activity_found
    }

    override fun initViews(rootView: View?) {
    }

    fun getWebView():WebView {
        return this.webview_found
    }
    override fun initData() {
        val mWebSettings = webview_found.settings
        mWebSettings.setSupportZoom(true)
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.useWideViewPort = true
        mWebSettings.defaultTextEncodingName = "utf-8"
        mWebSettings.loadsImagesAutomatically = true

        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        mWebSettings.javaScriptEnabled = true

        saveData(mWebSettings)

        var webViewClient: WebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                this@FoundFragment.activity?.startActivity<Html5Activity>("Url" to url)
                return true
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest):Boolean {
                this@FoundFragment.activity?.startActivity<Html5Activity>("Url" to request.url)
                return true
            }
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
            override fun onReceivedTitle(view: WebView?,  title:String) {
                super.onReceivedTitle(view, title);
                if (title != null) {
                  //  titleView.setCenterText(title);
                }
            }

        }

        webview_found.webViewClient = webViewClient
        webview_found.webChromeClient = webChromeClient
        val tokenCfgJson = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_CONF)
        val tokenCfg = Gson().fromJson<TokenSet>(tokenCfgJson,
                object : TypeToken<TokenSet>() {}.type)

       // webview_found.loadUrl("https://otc.spo.network/teller")

        if (tokenCfg.weburl.isNotEmpty()) {
            //webview_found.webViewClient.s
            webview_found.loadUrl(tokenCfg.weburl)
           // this@FoundFragment.activity?.startActivity<Html5Activity>("Url" to tokenCfg.weburl)

        } else {
            webview_found.loadUrl("http://samos.yqkkn.com/notfound")
        }

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

    companion object {

        fun newInstance(args: Bundle?): FoundFragment {
            val instance = FoundFragment()
            instance.arguments = args
            return instance
        }
    }
}