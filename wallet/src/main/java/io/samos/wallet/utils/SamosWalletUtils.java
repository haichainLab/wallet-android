package io.samos.wallet.utils;

/**
 * Created by zjy on 2018/1/20.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.samos.wallet.R;
import io.samos.wallet.api.Const;
import io.samos.wallet.beans.TokenSet;
import io.samos.wallet.common.Constant;
import com.google.gson.Gson;

/**
 * 目前没有啥需要的类，所以以后复杂了再拆分
 */
public class SamosWalletUtils {

    public static final String PIN_KEY = "PIN_SET";
    /**
     * 是否同意免责声明
     */
    public static final String GGREE = "GRAEE_SELE";
    /**
     * 存储钱包数据
     */
    public static final String Wallet_KEY = "Wallet_KEY";

    /**
     * pin输入超限开始时间
     */
    public static final String PIN_OUT = "PIN_TIME";

    public static boolean isPinSet() {
        return !TextUtils.isEmpty(getPin());
    }


    public static boolean isHasWallet() {
        return false;
    }

    public static boolean getGgreeState() {
        return SharePrefrencesUtil.getInstance().getBoolean(GGREE);
    }

    public static void setGgreeState(Boolean state) {
        SharePrefrencesUtil.getInstance().putBoolean(GGREE, state);
    }

    /**
     * pin错误次数限制提示
     * 比较两次时间差5分钟
     *
     * @return 布尔值
     */
    public static boolean returnPinHint() {
        int Nm, Om;
        Om = getPinTime();
        Nm = DateUtils.getNowMinute();
        if (Om == 0) {
            return true;
        }
        if (Nm > Om) {
            if ((Nm - Om) >= Const.PINTIME) {
                setPinTime(true);
                return true;
            } else {
                return false;
            }
        } else if (Nm < Om) {
            if (((60 - Om) + Nm) >= Const.PINTIME) {
                setPinTime(true);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * pin错误次数限制提示
     * 比较两次时间差5分钟
     *
     * @return 分钟
     */
    public static int getOutPinTime() {
        int Nm, Om;
        Om = getPinTime();
        Nm = DateUtils.getNowMinute();
        if (Nm > Om) {
            return Const.PINTIME - (Nm - Om);
        } else if (Nm < Om) {
            return Const.PINTIME -((60 - Om) + Nm);
        } else {
            return Const.PINTIME;
        }

    }

    private static int getPinTime() {
        try {
            Integer value = SharePrefrencesUtil.getInstance().getInt(PIN_OUT);
            return value;
        } catch (Exception e) {

        }
        return 0;
    }

    public static void setPinTime() {
        int minutes = DateUtils.getNowMinute();
        SharePrefrencesUtil.getInstance().putInt(PIN_OUT, minutes);
    }

    public static void setPinTime(boolean isOut) {
        int minutes = DateUtils.getNowMinute();
        SharePrefrencesUtil.getInstance().putInt(PIN_OUT, isOut ? 0 : minutes);
    }


    public static String getPin() {
        try {
            Log.d("Time getPin data: ",(System.currentTimeMillis()/1000)+"");
            String value = SharePrefrencesUtil.getInstance().getString(PIN_KEY);
            String pincode = DES.decryptDES(value, Const.DESKey);
            LogUtils.d("decrypt = " + pincode);
            Log.d("Time getPin end: ",(System.currentTimeMillis()/1000)+"");

            return pincode;
        } catch (Exception e) {

        }
        return null;
    }

    public static void setPin(String pin) {
        try {
            String value = DES.encryptDES(pin, Const.DESKey);
            SharePrefrencesUtil.getInstance().putString(PIN_KEY, value);
            LogUtils.d("encryp = " + value);
        } catch (Exception e) {

        }
    }

    public static File saveBitmap(Bitmap bitmap){
        String path = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();//保存到sd根目录下
        }


        //        File f = new File(path, System.currentTimeMillis() + ".jpg");
        File f = new File(path, SystemClock.elapsedRealtime() + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return f;
        }
    }

    public static void systemShareImage(Context context, File file){
        Uri imageUri = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(R.string.share)));
    }

    /**
     * 因为密码是16位的，所以取pin的hash的前16位作为加密的数据
     *
     * @return
     */
    public static String getPin16() {
        String pinTemp = String.valueOf(getPin().hashCode());
        return getPin() + pinTemp;
    }

    /**
     * 判断钱包是否已经创建过
     *
     * @param coinType
     * @param walletName
     * @return
     */
    public static boolean isWalletExist(String coinType, String walletName) {
        return io.samos.wallet.datas.WalletManager.getInstance().isExitWallet(walletName);
    }

    /**
     * 保存生成过的钱包
     *
     * @param coinType
     * @param walletName
     */
    public static void saveNewWallet(String coinType, String walletName) {
    }

    /**
     * 获取生成过的钱包数据
     */
    public static String getEncryptWallet() {
        return SharePrefrencesUtil.getInstance().getString(Wallet_KEY);
    }

    /**
     * 保存生成过的钱包数据
     */
    public static void setEncryptWallet(String data) {
        SharePrefrencesUtil.getInstance().putString(Wallet_KEY, data);
    }

    public static TokenSet getTokenSet() {
        String  s = SharePrefrencesUtil.getInstance().getString(Constant.TOKEN_CONF);
        Gson gson = new Gson();
        TokenSet tokenCfg = gson.fromJson(s,TokenSet.class);

        //.fromJson<TokenSet>(s, TokenSet)
        return tokenCfg;

    }
}
