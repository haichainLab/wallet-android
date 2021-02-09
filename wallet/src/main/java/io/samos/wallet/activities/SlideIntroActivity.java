package io.samos.wallet.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import io.samos.wallet.R;
import io.samos.wallet.base.BaseActivity;
import io.samos.wallet.utils.SharePrefrencesUtil;

import static io.samos.wallet.utils.BuildsKt.getVersion;

public class SlideIntroActivity extends BaseActivity {
    ViewPager viewPager;
    ArrayList<View> viewGroups;
    View view1;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_slide_intro;
    }

    @Override
    protected void initViews() {
        viewPager = findViewById(R.id.view_pager);
    }

    @Override
    protected void initData() {
        SharePrefrencesUtil.getInstance().putBoolean("FIRST_START" + getVersion(), true);

        int[] imageResIDs = {
                R.drawable.splash_1,
                R.drawable.splash_2,
           //     R.drawable.splash_3
        };
        viewGroups = new ArrayList<>();
        View view;
        for (int i = 0; i < imageResIDs.length; i++) {
            view = LayoutInflater.from(this).inflate(R.layout.view_slide_intro, null);
            ImageView iv = view.findViewById(R.id.image);
            if (i == imageResIDs.length - 1) {
                view1 = view.findViewById(R.id.click_zone);
                view1.setVisibility(View.GONE);
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SlideIntroActivity.this, MainActivity.class));
                    }
                });
            }
            iv.setBackgroundResource(imageResIDs[i]);
            viewGroups.add(view);
        }

        viewPager.setAdapter(new MyPagerAdapter(viewGroups, viewPager,
                new MyPagerAdapter.onSelectItemListener() {
                    @Override
                    public void onSelectClick(int position) {
                        if (position == viewGroups.size() -1){
                            view1.setVisibility(View.VISIBLE);
                        }
                    }
                }));


    }
}
