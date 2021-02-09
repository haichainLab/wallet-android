package io.samos.wallet.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;

import com.google.gson.Gson;

import java.util.Locale;

import io.samos.wallet.common.Constant;

public class LocaleUtils {
    /**
     * 中文
     */
    public static final Locale LOCALE_CHINESE = Locale.CHINESE;
    /**
     * 英文
     */
    public static final Locale LOCALE_ENGLISH = Locale.ENGLISH;


    /**
     * 获取用户设置的Locale
     *
     * @return Locale
     */
    public static Locale getUserLocale() {
        String _LocaleJson = SharePrefrencesUtil.getInstance().getString(Constant.SELE_LANGUAGE);
        return jsonToLocale(_LocaleJson);
    }

    /**
     * 获取当前的Locale
     *
     * @param pContext Context
     * @return Locale
     */
    public static Locale getCurrentLocale(Context pContext) {
        Locale _Locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0有多语言设置获取顶部的语言
            _Locale = pContext.getResources().getConfiguration().getLocales().get(0);
        } else {
            _Locale = pContext.getResources().getConfiguration().locale;
        }
        return _Locale;
    }

    /**
     * 保存用户设置的Locale
     *
     * @param pUserLocale Locale
     */
    public static void saveUserLocale( Locale pUserLocale) {
        SharePrefrencesUtil.getInstance().putBoolean(Constant.IS_LANGUAGE_ZH, pUserLocale
                .getLanguage().equalsIgnoreCase(Locale.CHINESE.toString()));
        String _LocaleJson = localeToJson(pUserLocale);
        SharePrefrencesUtil.getInstance().putString(Constant.SELE_LANGUAGE, _LocaleJson);
    }

    /**
     * Locale转成json
     *
     * @param pUserLocale UserLocale
     * @return json String
     */
    private static String localeToJson(Locale pUserLocale) {
        Gson _Gson = new Gson();
        return _Gson.toJson(pUserLocale);
    }

    /**
     * json转成Locale
     *
     * @param pLocaleJson LocaleJson
     * @return Locale
     */
    private static Locale jsonToLocale(String pLocaleJson) {
        Gson _Gson = new Gson();
        return _Gson.fromJson(pLocaleJson, Locale.class);
    }

    /**
     * 更新Locale
     *
     * @param pContext       Context
     * @param pNewUserLocale New User Locale
     */
    public static void updateLocale(Context pContext, Locale pNewUserLocale) {
        if (needUpdateLocale(pContext, pNewUserLocale)) {
            Configuration _Configuration = pContext.getResources().getConfiguration();
            Locale tempLocal;
            if (pNewUserLocale.getLanguage().equals("zh")){
                tempLocal = LocaleUtils.LOCALE_CHINESE;
            } else {
                tempLocal = LOCALE_ENGLISH;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(tempLocal);
            } else {
                _Configuration.locale = pNewUserLocale;
            }
            DisplayMetrics _DisplayMetrics = pContext.getResources().getDisplayMetrics();
            pContext.getResources().updateConfiguration(_Configuration, _DisplayMetrics);
            saveUserLocale(pNewUserLocale);
        }
    }

    /**
     * 判断需不需要更新
     *
     * @param pContext       Context
     * @param pNewUserLocale New User Locale
     * @return true / false
     */
    public static boolean needUpdateLocale(Context pContext, Locale pNewUserLocale) {
        return pNewUserLocale != null && !getCurrentLocale(pContext).equals(pNewUserLocale);
    }
}

