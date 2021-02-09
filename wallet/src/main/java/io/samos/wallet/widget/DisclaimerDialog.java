package io.samos.wallet.widget;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import io.samos.wallet.R;

/**
 * @author: lh on 2018/1/25 13:59.
 * Email:luchefg@gmail.com
 * Description: 免责声明弹出框
 */

public class DisclaimerDialog extends Dialog {

    private Context context;
    private View mainView;
    private View.OnClickListener onClickListener;
    private TextView mTxTitle;
    private ImageView mImgExit;
    private TextView mTxContent;
    private AppCompatCheckBox mCheckboxKnow;
    private TextView mTxContinue;
    private boolean isCheck;
    private TranslateAnimation animation;

    public DisclaimerDialog(@NonNull Context context,
                            View.OnClickListener onClickListener) {
        super(context, R.style.customDialog);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = LayoutInflater.from(context).inflate(R.layout.dialog_agreetext, null);

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
        this.setContentView(mainView,
                new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        //点击空白区域可以取消dialog
        this.setCanceledOnTouchOutside(false);
        //点击back键可以取消dialog
        this.setCancelable(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        //设置窗口出现和窗口隐藏的动画
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        //设置BottomDialog的宽高属性
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


        mTxTitle = findViewById(R.id.tx_title);
        mImgExit = findViewById(R.id.img_exit);
        mTxContent = findViewById(R.id.tx_content);
        mCheckboxKnow = findViewById(R.id.checkbox_know);
        mTxContinue = findViewById(R.id.tx_continue);

        mTxContinue.setOnClickListener(onClickListener);
        mImgExit.setOnClickListener(onClickListener);
        mCheckboxKnow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheck = b;
                if (isCheck) {
                    mTxTitle.setTextColor(0xff000000);
                    mTxTitle.setText(context.getText(R.string.disclaimer));
                }
            }
        });

        animation = new TranslateAnimation(0, -10, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(50);
        animation.setRepeatCount(5);
        animation.setRepeatMode(Animation.REVERSE);
    }


    public boolean isCheck() {
        return isCheck;
    }

    public void setContent(String msg) {
        mTxContent.setText(msg);
    }

    public void setTitle(String msg, int color) {
        mTxTitle.setText(msg);
        if (color != 0) {
            mTxTitle.setTextColor(0xffFF0000);
            mTxTitle.startAnimation(animation);
        }
    }


}
