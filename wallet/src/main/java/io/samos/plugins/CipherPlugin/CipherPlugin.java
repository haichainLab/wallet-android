package io.samos.plugins.CipherPlugin;

import android.widget.Toast;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.samos.wallet.beans.TokenSet;
import io.samos.wallet.datas.Address;
import io.samos.wallet.datas.Wallet;
import io.samos.wallet.datas.WalletDB;
import io.samos.wallet.datas.WalletManager;
import io.samos.wallet.utils.SamosWalletUtils;
import mobile.Mobile;


/**
 * This class echoes a string called from JavaScript.
 */
public class CipherPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getPubkey")) {

            JSONObject jsonObj = args.getJSONObject(0);

            String passwd =  jsonObj .getString("passwd");
            String addr =  jsonObj .getString("addr");

            String ret = getPubkey(  passwd,  addr);
            if("".equals(ret)) {
                callbackContext.error("get pubkey failed");
            } else {
                callbackContext.success(ret);
            }

        } else if(action.equals("signData")) {
            JSONObject jsonObj = args.getJSONObject(0);
            String hash = (String) jsonObj .get("hash");
            String passwd = (String) jsonObj .get("passwd");
            String addr = (String) jsonObj .get("addr");


            String ret = signData( hash,   passwd,  addr);
            callbackContext.success(ret);


        }
        return false;
    }

    private void getPubkey(String message, CallbackContext callbackContext) {
        Toast.makeText(cordova.getActivity(),"getPubkey",Toast.LENGTH_LONG).show();
        //h5端传给我什么参数，此处再传回去
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    /**
     * 从当前默认仓库的地址里面获取
     * 获取一个地址的公钥
     * @param passwd
     * @param addr
     * @return
     */
    public static String getPubkey( String passwd, String addr) {
        try {

            String walletId = getDefaultWalletID();
            String pubkey = Mobile.getPubkey( walletId, passwd, addr);
            return pubkey;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getDefaultWalletID() {
        WalletDB db = WalletManager.getInstance().restorDefaultWalletDB(); //默认的walletDB
        HashMap<String,Wallet> wallets  = db.getWallets();
        Wallet w;
        for(String token:wallets.keySet()) { //遍历一个
            w = wallets.get(token);
            return w.getWalletID();
        }
        return  "";
    }

    /**
     * 获取默认的钱包
     * @return
     */
    public static Wallet getDefaultWallet() {
        TokenSet set = SamosWalletUtils.getTokenSet();
        String defaultToken = set.getDefaultToken();

        WalletDB db = WalletManager.getInstance().restorDefaultWalletDB(); //默认的walletDB
        HashMap<String,Wallet> wallets  = db.getWallets();
        return wallets.get(defaultToken);

    }


    public static String getDefautlWalletAddress() {
        Wallet wallet = getDefaultWallet();

        try {
            String rawAddresses = Mobile.getAddresses(wallet.getWalletID());
            JSONObject jsonObject = new JSONObject(rawAddresses);
            JSONArray addressArray = jsonObject.optJSONArray("addresses");
            if (addressArray != null && addressArray.length() > 0) {
                int len = addressArray.length();
                for (int i = 0; i < 1; i++) { //获取第一个地址
                    Address address = new Address();
                    address.setAddressId(String.valueOf(i + 1));
                    return String.valueOf(addressArray.get(i));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return  "";


    }


    public static String signData(String hash,  String passwd, String addr)
    {
        try {


            String walletId = getDefaultWalletID();
            String pubkey = Mobile.signData(hash, walletId, passwd, addr);
            return pubkey;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

    }

    private void ui() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 你要执行的代码
            }
        });

    }

    private void consumeManyCpu() {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 你要执行的代码
            }
        });
    }
    /*
    在 Java 代码中如果想调用 js 方法，并且想要传递一个 Json 格式的数据到 js 的方法，当 Json 格式数据中包含引号时，如果不注意的话，很有可能会导致数据传递错误。具体解决方法如下：


     */
    private void f() {
//        String format = "window.plugins.yourPlugin.function(%s)";
//        final String js = String.format(format, jsonData.toString());
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                webView.loadUrl("javascript:" + js);
//            }
//        });

        /*
        // 然后在 js 端的解析调用：
      jsonStr = JSON.stringify(jsonStr);
      var obj = JSON.parse(jsonStr);
         */
    }
    /*
    Android 中的 CallbackContext.success() 方法默认只会执行一次回调，如果想通过一个 CallbackContext 重复发送回调结果就会失败。
这时我们可以自定义 PluginResult 对象，并调用 setKeepCallback(true); 方法保持 CallbackContext

作者：Hevin丶
链接：https://www.jianshu.com/p/58017c929b90
来源：简书
简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, value);
pluginResult.setKeepCallback(true);
mCallback.sendPluginResult(pluginResult);
     */
}
