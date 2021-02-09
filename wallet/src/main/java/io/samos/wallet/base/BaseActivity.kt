package io.samos.wallet.base

import android.app.ActivityManager
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.samos.wallet.R
import io.samos.wallet.utils.AppManager
import io.samos.wallet.utils.hasKitKat
import io.samos.wallet.utils.hasLollipop
import io.samos.wallet.utils.hasMarshmallow

/**
 * des:基类Activity
 * Created by zjy on 2018/1/20.
 * edit by 方丈 on 2018/05/11 修改BaseActivity为kotlin，并添加沉浸式状态栏代码
 */

abstract class BaseActivity : RxAppCompatActivity(), View.OnClickListener {

    open var savedInstanceState: Bundle? = null
    private var isActive = true

    /*ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if(!TextUtils.isEmpty(curPackageName)&&curPackageName.equals(getPackageName())){
            return true;
        }
        return false;*/
    private val isAppOnFreground: Boolean
        get() {
            val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val curPackageName = applicationContext.packageName
            val app = am.runningAppProcesses ?: return false
            return app.any { it.processName == curPackageName && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND }
        }

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    protected abstract fun attachLayoutRes(): Int

    /**
     * 初始化视图控件
     */
    protected abstract fun initViews()

    override fun onStop() {
        super.onStop()
        if (!isAppOnFreground) {
            isActive = false
        }
    }

    /**
     * 初始化数据
     */
    protected abstract fun initData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        try {
            setContentView(attachLayoutRes())
            setImmersiveStatusBar()
            AppManager.getAppManager().addActivity(this)
            initViews()
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)



            initData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onClick(v: View) {

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v!!.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 设置沉浸式状态栏
     */
    private fun setImmersiveStatusBar() {
        if (isNeedImmersive()) {
            if (hasKitKat() && !hasLollipop()) {
                // 透明状态栏
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                // 透明导航栏
                // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            } else if (hasLollipop()) {
                val window = window
                if (hasMarshmallow() ||isTranslucent()) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                }
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or getStatusBarLightMode()
                // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    private fun getStatusBarLightMode(): Int {
        return if (isStatusBarBlack()) {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }else {
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    /**
     * 是否设置沉浸式状态栏
     * 默认返回true，如不需要设置，子类重写该方法返回false
     */
    open fun isNeedImmersive(): Boolean = true

    /**
     * 是否需要状态栏透明，true 完全透明，false 有个遮罩
     * 因为5.0默认无法设置状态栏颜色，所以当白色字体跟背景颜色相似时，可以设置false，有个灰色遮罩
     */
    open fun isTranslucent(): Boolean = false

    /**
     * 设置状态栏颜色，true 黑色，false 白色，默认为白色
     */
    open fun isStatusBarBlack(): Boolean = true

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return false
    }

    override fun onDestroy() {
        try {
            AppManager.getAppManager().finishActivity(this)
        } catch (e: Exception) {

        }

        super.onDestroy()
    }

    override fun onCreateDialog(id: Int): Dialog {
        super.onCreateDialog(id)
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle(R.string.app_name)
        progressDialog.setCancelable(false)
        progressDialog.setMessage(getString(R.string.loading_str))
        return progressDialog
    }

}