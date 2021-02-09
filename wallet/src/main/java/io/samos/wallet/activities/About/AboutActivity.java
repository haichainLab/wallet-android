package io.samos.wallet.activities.About;

import android.os.Bundle;
import android.app.Activity;

import io.samos.wallet.R;
import io.samos.wallet.base.BaseActivity;


public class AboutActivity extends BaseActivity {

    private String version = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public int attachLayoutRes() {

        return R.layout.activity_aboutus;
    }
    @Override
    public void initViews() {

    }
    @Override
    public void initData() {

    }
    public boolean isStatusBarBlack() {
        return false;
    }


}
