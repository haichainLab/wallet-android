package io.samos.im.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhangke.websocket.ErrorResponse;
import com.zhangke.websocket.Response;

import io.samos.im.CommonResponse;
import io.samos.im.utils.CmdUtil;
import io.samos.im.utils.UiUtil;
import io.samos.wallet.R;
import io.samos.wallet.activities.Transaction.RullOutActivity;
import io.samos.wallet.utils.SamosWalletUtils;
import io.samos.wallet.utils.SharePrefrencesUtil;
import io.samos.wallet.utils.ToastUtils;
import kotlin.jvm.internal.Intrinsics;

public class LoginActivity extends AbsWebSocketActivity {

    /**
     * 假设这是登陆的接口Path
     */
    private static final String LOGIN_PATH = "Login";

    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin,btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_login);

        initView();
    }

    private void initView() {
        etAccount = (EditText) findViewById(R.id.et_account);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister =  (Button) findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();
                if(TextUtils.isEmpty(account) || TextUtils.isEmpty(password)){
                    UiUtil.showToast(LoginActivity.this, "输入不能为空");
                    return;
                }
                login(account, password);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 展示钱包密码对话框
                 */

                showWalletPwd();

            }
        });
    }

    private void showWalletPwd() {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final View rootView = LayoutInflater.from(this).inflate(R.layout.dialog_wallet_pwd,null);
        Window dialogWindow = dialog.getWindow();
        Resources var10001 = this.getResources();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setGravity(80);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();



        lp.width = var10001.getDisplayMetrics().widthPixels;
        lp.height = -2;
        dialogWindow.setAttributes(lp);
        ((EditText)rootView.findViewById(R.id.et_wallet_pwd)).addTextChangedListener((TextWatcher)(new TextWatcher() {
            public void afterTextChanged(@org.jetbrains.annotations.Nullable Editable p0) {
            }

            public void beforeTextChanged(@org.jetbrains.annotations.Nullable CharSequence p0, int p1, int p2, int p3) {
            }

            public void onTextChanged(@org.jetbrains.annotations.Nullable CharSequence p0, int p1, int p2, int p3) {
            }
        }));



        ((Button)rootView.findViewById(R.id.btn_wallet_pwd_ok)).setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                View var10000 = rootView;
                EditText var2 = (EditText)var10000.findViewById(R.id.et_wallet_pwd);
                if (var2.getText().toString().equals(SamosWalletUtils.getPin())) {
                    dialog.dismiss();
                    tryToRegister();
                } else {
                    ToastUtils.show("please input password");
                }

            }
        }));
        dialog.setContentView(rootView);
        dialog.show();



    }

    //输入的密码
    private void tryToRegister() {

        String cmd = CmdUtil.getRegisterWithSignCmd(SamosWalletUtils.getPin());
        sendWsText(cmd);

    }

    private void login(String account, String password){
        JSONObject params = new JSONObject();
        JSONObject command = new JSONObject();
        JSONObject parameters = new JSONObject();

        command.put("path", LOGIN_PATH);

        parameters.put("username", account);
        parameters.put("password", password);

        params.put("command", command);
        params.put("parameters", parameters);
        sendWsText(params.toJSONString());
    }

    @Override
    public void onMessageResponse(Response message) {
        UiUtil.showToast(LoginActivity.this, "登陆成功: " + ((CommonResponse)message).getResponseEntity().getBody());
        Log.i("1111", "onConnected: "+message);

    }

    @Override
    public void onSendMessageError(ErrorResponse error) {
        UiUtil.showToast(LoginActivity.this, error.getDescription());
    }
}
