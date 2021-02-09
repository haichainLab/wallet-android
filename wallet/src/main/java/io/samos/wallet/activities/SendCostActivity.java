package io.samos.wallet.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxing.lib.CaptureActivity;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.samos.wallet.R;
import io.samos.wallet.activities.Main.WalletViewModel;
import io.samos.wallet.activities.Transaction.old.TransactionViewModel;
import io.samos.wallet.base.BaseActivity;
import io.samos.wallet.common.Constant;
import io.samos.wallet.datas.Transaction;
import io.samos.wallet.datas.Wallet;
import io.samos.wallet.push.WalletPush;
import io.samos.wallet.utils.StatusBarUtils;
import io.samos.wallet.utils.ToastUtils;

/**
 * 发送界面
 */
public class SendCostActivity extends BaseActivity {

    ImageView close, qrcode;
    AppCompatSpinner appCompatSpinner;
    EditText  toWallet, amount, nodes;
    TextView cancle, send;

    TransactionViewModel transactionViewModel = new TransactionViewModel();
    Wallet wallet;
    Transaction transaction;

    Handler handler = new Handler();

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_send_cost;
    }

    @Override
    protected void initViews() {
        int topPadding = StatusBarUtils.getActionBarSize(this) + StatusBarUtils.getStatusBarHeight(this);
        getWindow().getDecorView().setPadding(0, topPadding, 0, 0);
        close = findViewById(R.id.close);
        qrcode = findViewById(R.id.img_qrcode);
        appCompatSpinner = findViewById(R.id.from_wallet);
        toWallet = findViewById(R.id.to_wallet);
        amount = findViewById(R.id.amount);
        nodes = findViewById(R.id.nodes);
        cancle = findViewById(R.id.cancle);
        send = findViewById(R.id.send);

        cancle.setOnClickListener(createClose());
        close.setOnClickListener(createClose());
        send.setOnClickListener(createSend());
        qrcode.setOnClickListener(createQrcode());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.send_cost_out);
    }

    private View.OnClickListener createQrcode() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendCostActivity.this, CaptureActivity.class);
                startActivityForResult(intent, Constant.REQUEST_QRCODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_QRCODE && resultCode == RESULT_OK) {
            String code = data.getData().toString();
            toWallet.setText(code);
        }
    }

    private View.OnClickListener createSend() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkEmpty()) {
                    transaction = new Transaction();
                    transaction.setAmount(amount.getText().toString());
                    transaction.setCoinType(Constant.COIN_TYPE_SKY);
                    transaction.setFromWallet(wallet.getWalletID());
                    transaction.setToWallet(toWallet.getText().toString());
                    transaction.setNodes(nodes.getText().toString());
                    transactionViewModel.sendTransaction(transaction)
                            .subscribe(new Observer<Transaction>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onNext(Transaction transaction) {
                                    if (transaction == null) {
                                        ToastUtils.show(getString(R.string.str_send_error));
                                    }else{
                                        sendSuccess();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }

            }
        };
    }

    private void sendSuccess(){
        /**
         * 发送成功后，先暂停2秒回到首页，然后8s后自动刷新钱包余额
         */
        close.setEnabled(false);
        ToastUtils.show(R.string.str_send_success);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WalletPush.getInstance().walletUpdate();
                finish();
            }
        },2000);
    }

    /**
     * 拦截返回按键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }

    private boolean checkEmpty() {
        if (TextUtils.isEmpty(appCompatSpinner.getSelectedItem().toString())) {
            ToastUtils.show(getResources().getString(R.string.input_id));
            return false;
        }
        if (TextUtils.isEmpty(toWallet.getText())) {
            ToastUtils.show(getResources().getString(R.string.input_address));
            return false;
        }
        if (TextUtils.isEmpty(amount.getText())) {
            ToastUtils.show(getResources().getString(R.string.input_amount));
            return false;
        }
        if(wallet == null){
            return false;
        }
        if (wallet.getBalance() < Double.parseDouble(amount.getText().toString().trim())) {
            ToastUtils.show(getResources().getString(R.string.insufficient_balance));
            return false;
        }
        return true;
    }

    private View.OnClickListener createClose() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    @Override
    protected void initData() {
        //初始化发送钱包
        final List<Wallet> wallets = WalletViewModel.wallets;
        if(wallets != null && wallets.size() > 0){
            ArrayAdapter<Wallet> walletArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wallets);
            appCompatSpinner.setAdapter(walletArrayAdapter);
            appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    wallet = wallets.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
