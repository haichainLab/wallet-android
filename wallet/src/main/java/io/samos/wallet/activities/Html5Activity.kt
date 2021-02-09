package io.samos.wallet.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import io.samos.wallet.R
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.options_dialog_layout.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.share
import android.text.TextUtils
import android.webkit.WebView
import android.webkit.WebViewClient





class Html5Activity : AppCompatActivity() {

    private var mUrl: String? = null
    private var progressBar: ProgressBar? = null
    private var mLayout: LinearLayout? = null
    private var mWebView: WebView? = null
    private var mTitle:TextView?=null

    internal var webViewClient: WebViewClient = object : WebViewClient() {

        /**
         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
         */
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            //判断是否为重定向
            return view.hitTestResult == null
        }

    }

    private var webChromeClient: WebChromeClient = object : WebChromeClient() {




        override fun onReceivedTitle(view: WebView, title: String?) {
            super.onReceivedTitle(view, title)

            if (title != null) {
                mTitle!!.text = title
            }
        }

        override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
            callback.invoke(origin, true, false)//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback)
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if(newProgress==100){
                progressBar?.visibility = View.GONE//加载完网页进度条消失
              //  mTitle.title =

            }
            else{
                progressBar?.visibility = View.VISIBLE//开始加载网页时显示进度条
                progressBar?.progress = newProgress//设置进度值
            }
        }
        //=========HTML5定位==========================================================

        //=========多窗口的问题==========================================================
        override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
            val transport = resultMsg.obj as WebView.WebViewTransport
            transport.webView = view
            resultMsg.sendToTarget()
            return true
        }
        //=========多窗口的问题==========================================================
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val bundle = intent.getBundleExtra("bundle")
        mUrl = intent.getStringExtra("Url")
        progressBar = find(R.id.progressBar)
        mLayout = find(R.id.web_layout)
        mTitle = find(R.id.tv_web_title)
        iv_web_back.setOnClickListener { if (mWebView!!.canGoBack()){mWebView!!.goBack()}else{finish()} }
        iv_web_refresh.setOnClickListener { mWebView!!.reload() }
        iv_web_share.setOnClickListener { share(mWebView!!.url) }
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mWebView = WebView(applicationContext)
        mWebView!!.layoutParams = params
        mLayout!!.addView(mWebView)

        val mWebSettings = mWebView!!.settings
        mWebSettings.setSupportZoom(true)
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.useWideViewPort = true
        mWebSettings.defaultTextEncodingName = "utf-8"
        mWebSettings.loadsImagesAutomatically = true

        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        mWebSettings.javaScriptEnabled = true

        saveData(mWebSettings)

        newWin(mWebSettings)

        mWebView!!.webChromeClient = webChromeClient
        mWebView!!.webViewClient = webViewClient
        mWebView!!.loadUrl(mUrl)


    }

    public override fun onPause() {
        super.onPause()
        mWebView!!.onPause()
        mWebView!!.pauseTimers() //小心这个！！！暂停整个 WebView 所有布局、解析、JS。
    }

    public override fun onResume() {
        super.onResume()
        mWebView!!.onResume()
        mWebView!!.resumeTimers()
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

    /**
     * HTML5数据存储
     */
    private fun saveData(mWebSettings: WebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        mWebSettings.domStorageEnabled = true
        mWebSettings.databaseEnabled = true
        mWebSettings.setAppCacheEnabled(true)
        val appCachePath = applicationContext.cacheDir.absolutePath
        mWebSettings.setAppCachePath(appCachePath)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView!!.canGoBack()) {
            mWebView!!.goBack()
            return true
        } else {
            finish()
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mWebView != null) {
            mWebView!!.clearHistory()
            (mWebView!!.parent as ViewGroup).removeView(mWebView)
            mWebView!!.loadUrl("about:blank")
            mWebView!!.stopLoading()
            mWebView!!.webChromeClient = null
            mWebView!!.webViewClient = null
            mWebView!!.destroy()
            mWebView = null
        }
    }

}