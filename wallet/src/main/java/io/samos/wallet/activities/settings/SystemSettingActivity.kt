package io.samos.wallet.activities.settings

import android.app.Dialog
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import io.samos.wallet.R
import io.samos.wallet.base.BaseActivity
import io.samos.wallet.common.Constant
import io.samos.wallet.utils.AppManager
import io.samos.wallet.utils.LocaleUtils
import io.samos.wallet.utils.SharePrefrencesUtil
import kotlinx.android.synthetic.main.activity_system_setting.*
import kotlinx.android.synthetic.main.options_dialog_layout.view.*


/**
 * @author: lh on 2018/5/23 15:51.
 * Email:luchefg@gmail.com
 * Description: 系统设置
 */
class SystemSettingActivity : BaseActivity(), AdapterView.OnItemClickListener {


    lateinit var dialog: Dialog
    lateinit var adapter: SettingAdapter
    val mutableListOf = mutableListOf<String>()
    var type: Int = 0
    var num: Int = -1
    lateinit var settingModel: SettingModel
    var isCny: Boolean = false
    var isZh: Boolean = false

    override fun attachLayoutRes(): Int {
        return R.layout.activity_system_setting
    }

    override fun initViews() {
        id_toolbar.setNavigationOnClickListener {
            finish()
        }
        id_toolbar_title.text = resources.getString(R.string.system_settings)

        language_layout.setOnClickListener {
            showLanuage()
        }
        monetary_unit_layout.setOnClickListener {
            showUnit()
        }
        dialog = Dialog(this@SystemSettingActivity, R.style.SimpleDialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        settingModel = SettingModel()
        isCny = SharePrefrencesUtil.getInstance().getBoolean(Constant.IS_UNIT_CNY)
        var language = LocaleUtils.getCurrentLocale(this@SystemSettingActivity).language
        if (language.equals("zh")){
            LocaleUtils.saveUserLocale(LocaleUtils.LOCALE_CHINESE)
        }
        isZh = SharePrefrencesUtil.getInstance().getBoolean(Constant.IS_LANGUAGE_ZH)
        if (isZh) {
            tv_language.text = resources.getString(R.string.chinese)
        } else {
            tv_language.text = resources.getString(R.string.english)
        }
        if (isCny) {
            tv_multi.text = Constant.CNY
        } else {
            tv_multi.text = Constant.USD
        }
    }

    fun showUnit() {
        type = 1
        mutableListOf.clear()
        if (isCny) {
            mutableListOf.add(Constant.CNY + "#")
            mutableListOf.add(Constant.USD)
        } else {
            mutableListOf.add(Constant.CNY)
            mutableListOf.add(Constant.USD + "#")
        }

        showSetting()
    }

    fun showLanuage() {
        type = 2
        mutableListOf.clear()
        if (isZh) {
            mutableListOf.add(resources.getString(R.string.chinese) + "#")
            mutableListOf.add(resources.getString(R.string.english))
        } else {
            mutableListOf.add(resources.getString(R.string.chinese))
            mutableListOf.add(resources.getString(R.string.english) + "#")
        }
        showSetting()
    }


    fun restart() {
        AppManager.getAppManager().AppExit(this)
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


    /**
     * 弹出框
     */
    private fun showSetting() {
        val rootView = LayoutInflater.from(this@SystemSettingActivity).inflate(R.layout.options_dialog_layout, null)
        val dialogWindow = dialog.window
        dialogWindow.decorView.setPadding(0, 0, 0, 0)
        dialogWindow.setGravity(Gravity.BOTTOM)
        // dialogWindow.setWindowAnimations(R.style.dialogstyle) // 添加动画
        val lp = dialogWindow.attributes // 获取对话框当前的参数值
        lp.width = resources.displayMetrics.widthPixels // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT // 高度
        dialogWindow.attributes = lp
        rootView.iv_exit.setOnClickListener {
            dialog.dismiss()
            if (num == -1) return@setOnClickListener
            if (type == 1) {
                if (num == 0) {
                    //CNY
                    settingModel.updateUnit(Constant.CNY)
                } else {
                    //USD
                    settingModel.updateUnit(Constant.USD)
                }
            } else {
                if (num == 0) {
                    //chinese
                    if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_CHINESE)) {
                        LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_CHINESE)
                        restart()
                    }

                } else {
                    //english
                    if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_ENGLISH)) {
                        LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_ENGLISH)
                        restart()
                    }
                }
            }
        }
        if (type == 1) {
            rootView.title.text = resources.getString(R.string.monetary_unit)
        } else {
            rootView.title.text = resources.getString(R.string.multilingual)
        }
        adapter = SettingAdapter(mutableListOf, this)
        rootView.listview.adapter = adapter
        rootView.listview.onItemClickListener = this
        dialog.setContentView(rootView)
        dialog.show()
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        mutableListOf[p2] = mutableListOf[p2] + "#"
        var oldPos: Int = if (p2 == 1) 0 else 1
        mutableListOf[oldPos] = mutableListOf[oldPos].replace("#", "")
        num = p2
        adapter.notifyDataSetChanged()
        if (type == 1) {
            if (p2 == 0) {
                tv_multi.text = Constant.CNY
            } else {
                tv_multi.text = Constant.USD
            }
        } else {
            if (p2 == 0) {
                tv_language.text = resources.getString(R.string.chinese)
            } else {
                tv_language.text = resources.getString(R.string.english)
            }
        }
    }

    override fun initData() {
    }

}