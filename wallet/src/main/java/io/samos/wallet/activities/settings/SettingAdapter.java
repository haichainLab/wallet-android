package io.samos.wallet.activities.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.samos.wallet.R;

/**
 * @author: lh on 2018/5/29 10:30.
 * Email:luchefg@gmail.com
 * Description:
 */
public class SettingAdapter extends BaseAdapter {

    List<String> items;
    Context mContext;

    public SettingAdapter(List<String> items, Context context) {
        this.items = items;
        mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_setting, null);
        ImageView imageView = view.findViewById(R.id.select);
        TextView tv_type = view.findViewById(R.id.tv_type);
        imageView.setVisibility(View.INVISIBLE);
        if (items.get(i).contains("#")) {
            imageView.setVisibility(View.VISIBLE);
        }
        tv_type.setText(items.get(i).replace("#", ""));
        return view;
    }
}
