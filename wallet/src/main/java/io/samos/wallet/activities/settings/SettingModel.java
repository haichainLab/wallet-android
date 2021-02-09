package io.samos.wallet.activities.settings;

import android.content.res.Resources;

import io.samos.wallet.R;
import io.samos.wallet.common.Constant;
import io.samos.wallet.utils.AppUtils;
import io.samos.wallet.utils.SharePrefrencesUtil;
import io.samos.wallet.utils.ToastUtils;

/**
 * @author: lh on 2018/5/29 15:32.
 * Email:luchefg@gmail.com
 * Description: app设置
 */
public class SettingModel {


    /**
     * 设置语言
     */
    public boolean updateLanuage(Resources resources, String lanuage) {
        if (lanuage.equals(Constant.CH)) {
            //chinese
            if (!SharePrefrencesUtil.getInstance().getBoolean(Constant.IS_LANGUAGE_ZH)) {
                SharePrefrencesUtil.getInstance().putString(Constant.SELE_LANGUAGE, Constant.CH);
                AppUtils.changeAppLanguage(resources, Constant.CH);
                ToastUtils.show(resources.getString(R.string.hint_lanuage));
                return true;
            }

        } else {
            //english
            if (SharePrefrencesUtil.getInstance().getBoolean(Constant.IS_LANGUAGE_ZH)) {
                SharePrefrencesUtil.getInstance().putString(Constant.SELE_LANGUAGE, Constant.EN);
                AppUtils.changeAppLanguage(resources, Constant.EN);
                ToastUtils.show(resources.getString(R.string.hint_lanuage));
                return true;
            }
        }
        return false;
    }

    /**
     * 设置货币单位
     */
    public void updateUnit(String unit) {
        if (unit.equals(Constant.CNY)) {
            //CNY
            SharePrefrencesUtil.getInstance().putString(Constant.SELE_UNIT, Constant.CNY);
        } else {
            //USD
            SharePrefrencesUtil.getInstance().putString(Constant.SELE_UNIT, Constant.USD);
        }
        SharePrefrencesUtil.getInstance().putBoolean(Constant.IS_UNIT_CNY,
                unit == Constant.CNY);
    }


}
