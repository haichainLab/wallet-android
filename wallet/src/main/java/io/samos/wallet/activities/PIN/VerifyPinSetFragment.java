package io.samos.wallet.activities.PIN;//package io.samos.wallet.activities.PIN;
//
//import android.os.Bundle;
//import android.view.View;
//
//import io.samos.wallet.R;
//import io.samos.wallet.base.BaseFragment;
//import io.samos.wallet.utils.StatusBarUtils;
//import io.samos.wallet.widget.KeyboardProgress;
//import io.samos.wallet.widget.NumberKeyboard;
//import io.samos.wallet.widget.NumberKeyboardAdapter;
//
///**
// * Pin确认界面碎片
// * Created by kimi on 2018/1/23.
// */
//
//public class VerifyPinSetFragment extends BaseFragment {
//
//    /**
//     * 数字键盘
//     */
//    NumberKeyboard numberKeyboard;
//
//    /**
//     * 数字键盘进度控件
//     */
//    KeyboardProgress keyboardProgress;
//
//    /**
//     * PIN监听器
//     */
//    PinSetListener pinSetListener;
//
//    /**
//     * PIN安全码
//     */
//    StringBuilder pinCode = new StringBuilder();
//
//    public static VerifyPinSetFragment newInstance(Bundle args){
//        VerifyPinSetFragment instance = new VerifyPinSetFragment();
//        instance.setArguments(args);
//        return instance;
//    }
//
//    @Override
//    protected int attachLayoutRes() {
//        return R.layout.fragment_verify_pin_set;
//    }
//
//    @Override
//    protected void initViews(View rootView) {
//
//        numberKeyboard = rootView.findViewById(R.id.numberkeyboard);
//        keyboardProgress = rootView.findViewById(R.id.keyboardtips);
//        numberKeyboard.setOnResetEnable(true);
//        numberKeyboard.setOnNumberKeyboardDownListener(onNumberKeyboardDownListener);
//        if(mActivity instanceof PinSetListener){
//            pinSetListener = (PinSetListener) mActivity;
//        }
//    }
//
//    /**
//     * 数字键盘结果回调
//     */
//    NumberKeyboardAdapter.OnNumberKeyboardDownListener onNumberKeyboardDownListener = new NumberKeyboardAdapter.OnNumberKeyboardDownListener(){
//
//        @Override
//        public void onNumberKeyboardDown(int number) {
//            keyboardProgress.insetUpdate();
//            if(pinCode.length() < 6){
//                pinCode.append(number);
//            }
//            if(pinCode.length() == 6){
//                pinSetListener.onPinSetVerifySuccess(pinCode.toString());
//            }
//        }
//
//        @Override
//        public void onNumberKeyboardDelete() {
//            keyboardProgress.deleteUpdate();
//            if(pinCode.length() > 0){
//                pinCode.deleteCharAt(pinCode.length() - 1);
//            }
//        }
//
//        @Override
//        public void onNumberKeyboardReset() {
//            pinSetListener.onPinReset();
//        }
//    };
//
//    @Override
//    protected void initData() {
//
//    }
//}
