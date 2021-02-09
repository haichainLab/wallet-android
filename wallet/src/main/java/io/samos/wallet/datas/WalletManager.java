package io.samos.wallet.datas;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.samos.wallet.beans.Token;
import io.samos.wallet.beans.TokenSet;
import io.samos.wallet.utils.LogUtils;
import io.samos.wallet.utils.SamosWalletUtils;
import io.samos.wallet.R;
import io.samos.wallet.base.SamosAppliacation;
import io.samos.wallet.common.Constant;
import io.samos.wallet.utils.SamosWalletUtils;
import mobile.Mobile;

import static io.samos.wallet.common.Constant.DEFAULT_WALLET_NUM;
import static io.samos.wallet.utils.SamosWalletUtils.getTokenSet;


/**
 * 钱包管理
 * Created by kimi on 2018/2/11.</br>
 */

public class WalletManager {
    public static String DEFAULT_WALLET_DB = Constant.WALLET_DEFAULT_WALLET_DB;


   // public static String DEFAULT_WALLET_INDEX = Constant.WALLET_DEFAULT_WALLET_INDEX;
    //"SAMOS_DEFAULT_WALLET_INDEX";
    /**
     * 存储钱包的sp文件
     */
    private SharedPreferences walletSharedPreferences;
    private SharedPreferences walletIndexSharedPreferences;
    //同样一个editor
    private SharedPreferences.Editor walletIndexEditor,walletEditor;


    /**
     * 钱包集合，meiyige qianbaolim
     */
    //  private List<Wallet> wallets = new ArrayList<>();
   // private List<WalletIndex> walletIndices = new ArrayList<>();
    private List<WalletDB> walletDBs = new ArrayList<>();

    /**
     * 监听器集合
     */
    private List<OnWalletChangeListener> onWalletChangeListenerList = new ArrayList<>();

    private Gson gson = new Gson();

    private WalletManager() {
        String name = Constant.WALLET_PREFERENCES;
        //"samos_wallet_preferences";
        String indexname = Constant.WALLET_DB_PREFERENCES;
        //"samos_walletindex_preferences";
        walletSharedPreferences = SamosAppliacation.mInstance.getSharedPreferences(name,
                Context.MODE_MULTI_PROCESS);
        walletIndexSharedPreferences = SamosAppliacation.mInstance.getSharedPreferences(indexname,
                Context.MODE_MULTI_PROCESS);

        walletEditor = walletSharedPreferences.edit();
        walletIndexEditor = walletIndexSharedPreferences.edit();
    }

    public static WalletManager getInstance() {
        return WalletManagerHolder.minstance;
    }

    /**
     * 添加监听器
     */
    public void addOnWalletChangeListener(OnWalletChangeListener onWalletChangeListener) {
        if (!onWalletChangeListenerList.contains(onWalletChangeListener)) {
            onWalletChangeListenerList.add(onWalletChangeListener);
        }
    }

    /**
     * 移除监听器
     */
    public void deleteOnWalletChangeListener(OnWalletChangeListener onWalletChangeListener) {
        onWalletChangeListenerList.remove(onWalletChangeListener);
    }

//    /**
//     * 直接增加
//     * @param context
//     * @param coinType
//     * @param walletName
//     * @param seed
//     * @return
//     *
//     * coinType, lable, seed, passwd string)
//     */
//    public boolean addWalletIndex(Context context, String coinType, String walletName,
//                                  String seed) {
//        try {
//            String walletId = Mobile.newWallet(coinType, walletName, seed,
//                    SamosWalletUtils.getPin16());
//
//            WalletIndex walletIndex = getWalletIndex(walletName);
//
//            walletIndex.setCoinWalletID(coinType, walletId);//一种类型的coin对应哪一个wallet
//
//            creatDefaultWallet(walletId);
//            walletIndex.setDepth(coinType, DEFAULT_WALLET_NUM);
//            saveDefaultWalletIndex(walletIndex);
//            walletIndex.save();
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            if (!TextUtils.isEmpty(e.getMessage()) && e.getMessage().contains("exist")) {
//                Toast.makeText(context,
//                        context.getResources().getString(R.string.wallet_name_existed),
//                        Toast.LENGTH_SHORT).show();
//                return false;
//            }
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        return false;
//    }


    /**
     * 直接增加，新多币种（比如运行一段时间后增加新多币种了）
//     * @param context
//     * @param tokenName
//     * @param walletName
//     * @param seed
//     * @return
     *
     * coinType, lable, seed, passwd string)
     */
    public boolean addWalletDB(Context context, String tokeName, String walletName,
                                  String seed) {
        try {
            String token = SamosWalletUtils.getTokenSet().getTokenByName(tokeName);
            if("".equals(token)) {
                Log.e("samos-wallet","token is empty:addWalletDB");
                return false;
            }


            String walletId = Mobile.newWallet(tokeName, walletName, seed,
                    SamosWalletUtils.getPin16());
            //添加地址
            addWltAddress(walletId);


            Wallet wlt = new Wallet(tokeName,token,walletName,walletId);
            wlt.setSelected(true); //默认在添加资产的地方添加



            WalletDB walletDb = getWalletDB(walletName);
            walletDb.addWallet(token,wlt);

            walletDb.save();
            saveDefaultWalletDB(walletDb);//流程是在当前的钱包里面添加新资产

            //通知监听器
            for (OnWalletChangeListener onWalletChangeListener : onWalletChangeListenerList) {
                onWalletChangeListener.onWalletDBChanged(walletDb);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();

            if (!TextUtils.isEmpty(e.getMessage()) && e.getMessage().contains("exist")) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.wallet_name_existed),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    /**
     * 最开始助计词
   //  * @param coinType
     * @param walletName
     * @param avataResourceId
     * @param seed
     * @return
     * @throws Exception
     */
//    public boolean newWallet( String coinType, String walletName, int
//            avataResourceId, String seed) throws Exception {
//        String walletId = Mobile.newWallet(coinType, walletName, seed,
//                SamosWalletUtils.getPin16());
//        WalletIndex walletIndex = new WalletIndex(walletName, avataResourceId);
//        walletIndex.setCoinWalletID(coinType, walletId);
//
//        creatDefaultWallet(walletId);
//        walletIndex.setDepth(coinType, DEFAULT_WALLET_NUM);
//        saveDefaultWalletIndex(walletIndex);
//        walletIndex.save();
//        return true;
//    }

    public boolean newWalletDB(String walletName, int
            avataResourceId, String seed)  {

        TokenSet tokenCfg = getTokenSet();
        WalletDB wltDb = new WalletDB(walletName, avataResourceId);

        String token = tokenCfg.getDefaultToken();
        String tokenName = tokenCfg.getTokenName(token);

        LogUtils.d("token:"+token+" ,tokeName:"+tokenName);
        try {

            String walletId = Mobile.newWallet(tokenName, walletName, seed,
                    SamosWalletUtils.getPin16());
            //添加地址
            addWltAddress(walletId);
            //保存钱包
            Wallet wlt = new Wallet(tokenName, token, walletName, walletId);
            wltDb.addWallet(token, wlt);
        } catch(Exception ex) {
            ex.printStackTrace();
        }



//        for(Token token:tokenCfg.getTokens()) {
//            try {
//                //创建钱包
//                String walletId = Mobile.newWallet(token.getTokenName(), walletName, seed,
//                        SamosWalletUtils.getPin16());
//                //添加地址
//                addWltAddress(walletId);
//                //保存钱包
//                Wallet wlt = new Wallet(token.getTokenName(),token.getToken(),walletName,walletId);
//                wltDb.addWallet(token.getToken(),wlt);
//                break; //默认只创建一个钱包，这样开始的时候速度更快一点
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//        }

        if(wltDb.tokenWlt.size()>0) {
            saveDefaultWalletDB(wltDb);
            wltDb.save();
            return true;
        }
        return false;
    }

    private void addWltAddress(String walletID) {
        try {
            Mobile.newAddress(walletID, DEFAULT_WALLET_NUM, SamosWalletUtils.getPin16());

        }catch (Exception e) {
            e.printStackTrace();
        }

    }


//        private void creatDefaultWallet(String walletID) {
//        try {
//            // for (int i = 0; i < DEFAULT_WALLET_NUM; i++) {
//            Mobile.newAddress(walletID, DEFAULT_WALLET_NUM, SamosWalletUtils.getPin16());
//            //}
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }

    public boolean saveWalletDB(WalletDB walletDb) {
        try {
//            SharedPreferences.Editor edit = walletIndexSharedPreferences.edit();
//            edit.putString(walletIndex.name, gson.toJson(walletIndex));
//            edit.commit();
            walletIndexEditor.putString(walletDb.name, gson.toJson(walletDb));
            boolean flag = walletIndexEditor.commit();
            Log.d("commit flag:",flag+"");
            //通知监听器
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }
//
//
//    public boolean saveWalletIndex(WalletIndex walletIndex) {
//        try {
////            SharedPreferences.Editor edit = walletIndexSharedPreferences.edit();
////            edit.putString(walletIndex.name, gson.toJson(walletIndex));
////            edit.commit();
//            walletIndexEditor.putString(walletIndex.name, gson.toJson(walletIndex));
//            boolean flag = walletIndexEditor.commit();
//            Log.d("commit flag:",flag+"");
//            //通知监听器
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        return false;
//    }
    /**
     * 更新钱包
     */
//    public boolean updateWalletIndex(String name,WalletIndex walletIndex) {
//        try {
////            SharedPreferences.Editor edit = walletIndexSharedPreferences.edit();
////            edit.remove(name);
////            edit.putString(walletIndex.name, gson.toJson(walletIndex));
////            edit.commit();
//
//            walletIndexEditor.remove(name);
//            walletIndexEditor.putString(walletIndex.name, gson.toJson(walletIndex));
//            walletIndexEditor.commit();
//            //通知监听器
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        return false;
//    }

    public boolean updateWalletDB(String name,WalletDB walletDb) {
        try {
//            SharedPreferences.Editor edit = walletIndexSharedPreferences.edit();
//            edit.remove(name);
//            edit.putString(walletIndex.name, gson.toJson(walletIndex));
//            edit.commit();

            walletIndexEditor.remove(name);
            walletIndexEditor.putString(walletDb.name, gson.toJson(walletDb));
            walletIndexEditor.commit();
            //通知监听器
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("updateWalletDB failed:"+name);

        }
        return false;
    }

    //保存钱包数据库
    public boolean saveDefaultWalletDB(WalletDB walletDB) {
        try {
            walletIndexEditor.putString(DEFAULT_WALLET_DB, gson.toJson(walletDB));
            walletIndexEditor.commit();
            //通知监听器
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return false;
    }


    public WalletDB restorDefaultWalletDB() {
        return restorWalletDB(DEFAULT_WALLET_DB);
    }

    public WalletDB restorWalletDB(String name) {
        String tempResult = walletIndexSharedPreferences.getString(name, "");
        if (TextUtils.isEmpty(tempResult)) {
            return null;
        }
        return gson.fromJson(tempResult, WalletDB.class);
    }



    /**
     * 获取所有的钱包
     */
    public List<WalletDB> getAllWalletDB() {
        walletDBs.clear();
        Map<String, String> all = (Map<String, String>) walletIndexSharedPreferences.getAll();
        if (all.isEmpty()) {
            return walletDBs;
        }
        Iterator<Map.Entry<String, String>> iterator = all.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            if (next.getKey().equalsIgnoreCase(DEFAULT_WALLET_DB)) {//会重复，默认下面多都有
                continue;
            }
            WalletDB wallet = gson.fromJson(next.getValue(), WalletDB.class);

            walletDBs.add(wallet);
        }
        return walletDBs;
    }
//
//    public List<WalletIndex> getAllWalletIndex() {
//        walletIndices.clear();
//        Map<String, String> all = (Map<String, String>) walletIndexSharedPreferences.getAll();
//        if (all.isEmpty()) {
//            return walletIndices;
//        }
//        Iterator<Map.Entry<String, String>> iterator = all.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, String> next = iterator.next();
//            if (next.getKey().equalsIgnoreCase(DEFAULT_WALLET_INDEX)) {
//                continue;
//            }
//            WalletIndex wallet = gson.fromJson(next.getValue(), WalletIndex.class);
//            walletIndices.add(wallet);
//        }
//        return walletIndices;
//    }


    /**
     * 获取所有的钱包
     */
//    public WalletIndex getWalletIndex(String walletName) {
//        String str = walletIndexSharedPreferences.getString(walletName, "");
//        WalletIndex wallet = gson.fromJson(str, WalletIndex.class);
//        return wallet;
//    }

    /**
     * 获取所有的钱包
     */
    public WalletDB getWalletDB(String walletName) {
        String str = walletIndexSharedPreferences.getString(walletName, "");
        WalletDB wallet = gson.fromJson(str, WalletDB.class);
        return wallet;
    }


    /**
     * 增加一个钱包
     *
     * @return true代表增加成功，false表示失败,或者已经存在此钱包
     */
    public boolean saveWallet(Wallet wallet) {
        try {
            boolean exist = Mobile.isExist(wallet.getWalletID());
            if (exist) {
//                SharedPreferences.Editor edit = walletSharedPreferences.edit();
//                edit.putString(wallet.getWalletID(), gson.toJson(wallet));
//                edit.commit();

                walletEditor.putString(wallet.getWalletID(), gson.toJson(wallet));
                walletEditor.commit();

                //通知监听器
                for (OnWalletChangeListener onWalletChangeListener : onWalletChangeListenerList) {
                    onWalletChangeListener.onSaveWallet(wallet);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return false;
    }


    /**
     * 删除一个钱包
     *
     * @return true代表删除成功，false表示失败,或者此钱包不存在
     */
    public boolean removeWallet(List<Wallet> wallet, WalletDB walletDb) {
        String walletJson = walletIndexSharedPreferences.getString(walletDb.name, "");
        if (TextUtils.isEmpty(walletJson)) {
            return false;
        }
        try {
            if (Mobile.isExist(wallet.get(0).getWalletID())) {
                for (int i = 0; i < wallet.size(); i++) {
                    Mobile.remove(wallet.get(i).getWalletID());
                }
                SharedPreferences.Editor edit = walletSharedPreferences.edit();
                edit.remove(wallet.get(0).getWalletName());
                edit.commit();
                edit = walletIndexSharedPreferences.edit();
                edit.remove(walletDb.name);
                //如果是默认钱包，多删除出一次
                if (walletDb.name.equals(
                        WalletManager.getInstance().restorDefaultWalletDB().name)) {
                    edit.remove(DEFAULT_WALLET_DB);
                }
                edit.commit();
                //通知监听器
                for (OnWalletChangeListener onWalletChangeListener : onWalletChangeListenerList) {
                    onWalletChangeListener.onRemoveWallet(walletDb);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }



    /**
     * 判断钱包名称是否已经存在
     */
    public boolean isExitWallet(String walletName) {
        Map<String, String> all = (Map<String, String>) walletSharedPreferences.getAll();
        Iterator<Map.Entry<String, String>> iterator = all.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            if (walletName.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取钱包已创建的所有地址
     */
    public List<Address> getAddressesByWalletId(String tokenName, String walletID) {
        List<Address> result = new ArrayList<>();
        try {
            String rawAddresses = Mobile.getAddresses(walletID);
            JSONObject jsonObject = new JSONObject(rawAddresses);
            JSONArray addressArray = jsonObject.optJSONArray("addresses");
            if (addressArray != null && addressArray.length() > 0) {
                int len = addressArray.length();
                for (int i = 0; i < len; i++) {
                    Address address = new Address();
                    address.setAddressId(String.valueOf(i + 1));
                    address.setAddress(String.valueOf(addressArray.get(i)));
                    address.setAddresBalance(String.valueOf(Wallet.getBalanceFromRawData(
                            Mobile.getBalance(tokenName, address.getAddress()))));
                    result.add(address);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    /**
     * 监听器
     */
    public interface OnWalletChangeListener {
        void onSaveWallet(Wallet wallet);

        void onWalletDBChanged(WalletDB walletDB);

        void onRemoveWallet(WalletDB wallets);

    }

    /**
     * 静态内部类单利
     */
    private static class WalletManagerHolder {
        private static final WalletManager minstance = new WalletManager();
    }

}
