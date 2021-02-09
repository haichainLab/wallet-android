package io.samos.wallet.activities;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
    private List<View> viewGroups;
    private ViewPager viewPager;
    private onSelectItemListener mOnSelectItemListener;

    public MyPagerAdapter(List<View> viewGroups, ViewPager viewPager,onSelectItemListener mOnSelectItemListener) {
        this.viewGroups = viewGroups;
        this.viewPager = viewPager;
        this.mOnSelectItemListener = mOnSelectItemListener;
    }

    /**
     * 返回的int的值, 会作为ViewPager的总长度来使用.
     */
    @Override
    public int getCount() {
        return viewGroups.size();//Integer.MAX_VALUE伪无限循环
    }

    /**
     * 判断是否使用缓存, 如果返回的是true, 使用缓存. 不去调用instantiateItem方法创建一个新的对象
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 初始化一个条目
     * position 就是当前需要加载条目的索引
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 把position对应位置的ImageView添加到ViewPager中
        View iv = viewGroups.get(position % viewGroups.size());
        viewPager.addView(iv);
        mOnSelectItemListener.onSelectClick(position);
        return iv;
    }

    public interface onSelectItemListener{
        void onSelectClick(int position);
    }


    /**
     * 销毁一个条目
     * position 就是当前需要被销毁的条目的索引
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 把ImageView从ViewPager中移除掉
        viewPager.removeView(viewGroups.get(position % viewGroups.size()));
    }


}

