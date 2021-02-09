package io.samos.wallet.widget;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.samos.wallet.R;
import io.samos.wallet.base.SamosAppliacation;
import io.samos.wallet.utils.LogUtils;

/**
 * 数字键盘的适配器
 * Created by kimi on 2018/1/23.
 */

public class NumberKeyboardAdapter extends
        RecyclerView.Adapter<NumberKeyboardAdapter.NumberKeyboardViewHolder> {

    private int NUMBER_SIZI = 12;
    private OnNumberKeyboardDownListener onNumberKeyboardDownListener;

    public void setShowBackward(boolean showBackward) {
        this.showBackward = showBackward;
        notifyDataSetChanged();
    }

    private boolean showBackward;//是否要显示重新输入

    @Override
    public NumberKeyboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NumberKeyboardViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.number_keyboard_item,
                        parent, false));
    }

    @Override
    public void onBindViewHolder(NumberKeyboardViewHolder holder, int position) {
        holder.number.setEnabled(true);
        holder.number.setTextSize(30);
        holder.numdel.setVisibility(View.GONE);
        holder.lin_num.setTag(position);
        if (position == 9) {
            if (showBackward){
                holder.number.setTextSize(20);
                holder.number.setEnabled(true);
                holder.number.setText(SamosAppliacation.mInstance.getResources().getString(R.string.reset_pin));
            } else {
                holder.number.setEnabled(false);
                holder.number.setText("");
            }
        } else if (position == 10) {
            holder.number.setText("0");
        } else if (position == NUMBER_SIZI - 1) {
            holder.number.setText("");
            holder.numdel.setBackgroundResource(R.mipmap.del);
            holder.number.setVisibility(View.GONE);
            holder.numdel.setVisibility(View.VISIBLE);
        } else {
            holder.number.setText((position + 1) + "");
        }
        holder.lin_num.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return NUMBER_SIZI;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (onNumberKeyboardDownListener != null) {
                if (position == 9) {
                    onNumberKeyboardDownListener.onNumberKeyboardReset();
                } else if (position == NUMBER_SIZI - 1) {
                    onNumberKeyboardDownListener.onNumberKeyboardDelete();
                } else if (position == 10) {
                    onNumberKeyboardDownListener.onNumberKeyboardDown(0);
                } else {
                    onNumberKeyboardDownListener.onNumberKeyboardDown(position + 1);
                }
            }
        }
    };

    /**
     * 设置监听器
     */
    public void setOnNumberKeyboardDownListener(
            OnNumberKeyboardDownListener onNumberKeyboardDownListener) {
        this.onNumberKeyboardDownListener = onNumberKeyboardDownListener;
    }


    /**
     * numberKeyboard viewholder
     */
    public static class NumberKeyboardViewHolder extends RecyclerView.ViewHolder {

        public TextView number;
        public ImageView numdel;
        public LinearLayout lin_num;

        public NumberKeyboardViewHolder(View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.tv_number);
            numdel = itemView.findViewById(R.id.img_del);
            lin_num = itemView.findViewById(R.id.lin_num);
        }
    }


    /**
     * 数字键盘监听器
     */
    public interface OnNumberKeyboardDownListener {
        /**
         * @param number 数字键盘选中的数字
         */
        void onNumberKeyboardDown(int number);

        /**
         * 数字键盘删除按键
         */
        void onNumberKeyboardDelete();

        /**
         * 数字键盘重设键
         */
        void onNumberKeyboardReset();
    }

}
