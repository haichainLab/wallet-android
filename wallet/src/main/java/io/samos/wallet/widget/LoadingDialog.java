package io.samos.wallet.widget;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by luch on 2018/5/22.
 */

public class LoadingDialog extends ProgressDialog {

    private ProgressDialog dialog;
    private Context context;

    public LoadingDialog(Context contexts) {
        super(contexts);
        this.context=contexts;
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show(String msg){
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        dialog.setMessage(msg);
        dialog.show();
    }

    public void dismiss(){
        if (dialog != null){
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

}
