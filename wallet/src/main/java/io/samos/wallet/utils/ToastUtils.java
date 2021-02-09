package io.samos.wallet.utils;

import android.widget.Toast;

import io.samos.wallet.base.SamosAppliacation;

/**
 * Created by kimi on 2018/1/23.
 */

public class ToastUtils {

    private static Toast toast;

    public static void show(int messageId) {
        String msg =  SamosAppliacation.mInstance.getResources().getString(messageId);
        show(msg);
    }
    public static void show(String message){
        if(toast == null){
            toast = Toast.makeText(SamosAppliacation.mInstance,message,Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.show();
    }
}
