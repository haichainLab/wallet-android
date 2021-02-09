package io.samos.wallet.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDex;


import com.tencent.bugly.crashreport.CrashReport;
import com.zhangke.websocket.WebSocketService;
import com.zhangke.websocket.WebSocketSetting;

import java.util.Locale;

import io.samos.im.AppResponseDispatcher;
import io.samos.im.beans.ImServerConfig;
import io.samos.im.utils.ImConfigUtil;
import io.samos.wallet.BuildConfig;
import io.samos.wallet.R;
import io.samos.wallet.activities.MainActivity;
import io.samos.wallet.common.Constant;
import io.samos.wallet.utils.AppUtils;
import io.samos.wallet.utils.LocaleUtils;
import io.samos.wallet.utils.SharePrefrencesUtil;

import static io.samos.wallet.utils.AppUtils.getProcessName;

//import io.samos.wallet.api.RetrofitService;

/**
 * Created by zjy on 2018/1/20.
 */

public class SamosAppliacation extends Application {

    public static SamosAppliacation mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        initNet();
        initBugly();
       // initWs();

    }

    private void initBugly() {
        Context context = getApplicationContext();
// 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly
        CrashReport.initCrashReport(context, "aa26391fd7", false, strategy);


    }

    private void initWs() {
        //配置 WebSocket，必须在 WebSocket 服务启动前设置
        WebSocketSetting.setConnectUrl(ImConfigUtil.getWsUrl());//必选
        WebSocketSetting.setResponseProcessDelivery(new AppResponseDispatcher());
        WebSocketSetting.setReconnectWithNetworkChanged(true);

        //启动 WebSocket 服务
        startService(new Intent(this, WebSocketService.class));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Locale _UserLocale = LocaleUtils.getUserLocale();
        //系统语言改变了应用保持之前设置的语言
        if (_UserLocale != null) {
            Locale.setDefault(_UserLocale);
            Configuration _Configuration = new Configuration(newConfig);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(_UserLocale);
            } else {
                _Configuration.locale = _UserLocale;
            }
            getResources().updateConfiguration(_Configuration, getResources().getDisplayMetrics());
        }
    }


    private void setLanuage() {
        //存储当前本地语言环境
        String language = Locale.getDefault().getLanguage();
        SharePrefrencesUtil.getInstance().putBoolean(Constant.IS_LANGUAGE_ZH,
                language == "zh");


    }

    /**
     * 初始化网络
     */
    private void initNet() {

    }



}
