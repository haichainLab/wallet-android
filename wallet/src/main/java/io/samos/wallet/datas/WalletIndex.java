//package io.samos.wallet.datas;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * 1，可以有多个钱包
// * 2，每个钱包里面有多个币种
// * 3，每个币种里面有多个地址
// *
// */
//public class WalletIndex implements Serializable {
//    private static final long serialVersionUID = 4L;
//    public String name;
//    public int avatarResourceId;
//    boolean selected; //是不是当前的
//    int showNum =10;
//
//
//    public HashMap<String, String> coinTypeWalletIDMap;
//
//    //其实是选择的币种，一个币种是一个wallet
//    public List<String> selectWallets = new ArrayList<String>();
//
//    HashMap<String, Integer> coinTypeDepth; //每一种币有几个地址
//
//    public WalletIndex() {
//
//    }
//    public WalletIndex(String name, int resourceId) {
//        this.name = name;
//        avatarResourceId = resourceId;
//
//        selectWallets.add("samos"); //默认选择的
//       // selectWallets.add("shihu"); //默认选择的,创建的时候默认只创建一个
//       // selectWallets.add("yongbang"); //默认选择的
//
//        coinTypeWalletIDMap = new HashMap<>();
//
//        coinTypeDepth = new HashMap<>(); //skycion:5  samos:6
////
////        coinTypeDepth.put("shihu", 3);
////        coinTypeDepth.put("yongbang", 2);
////        coinTypeDepth.put("samos", 1);
////        coinTypeDepth.put("sky", 0);
//
//
//
//    }
//
//    //钱包名字
//    public static WalletIndex restoreFromLocal(String walletName) {
//        return WalletManager.getInstance().restorWalletIndex(walletName);
//    }
//
//    public int getShowNum() {
//        return showNum;
//    }
//
//    public void setShowNum(int showNum) {
//        this.showNum = showNum;
//    }
//
//    public boolean isSelected() {
//        return selected;
//    }
//
//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }
//
//
//    /**
//     * 设置一个coin对应的walletID
//     * @param coinType
//     * @param walleID
//     */
//    public void setCoinWalletID(String coinType, String walleID) {
//        coinTypeWalletIDMap.put(coinType, walleID);
//    }
//
////    public void addSuppotCoinType(@NonNull String coinType, @NonNull String id) {
////        coinTypeDepth.put(coinType, 1);
////        coinTypeWalletIDMap.put(coinType, id);
////    }
//
//    //设置有几个地址，samos：5
//    public void setDepth(String coinType, int depth) {
//        coinTypeDepth.put(coinType, depth);
//    }
//
////    public void addAddress(String coinType) {
////        if (!coinTypeDepth.containsKey(coinType)) {
////            coinTypeDepth.put(coinType, 1);
////        } else {
////            int depth = coinTypeDepth.get(coinType);
////            coinTypeDepth.put(coinType, ++depth);
////        }
////    }
//
//    public void save() {
//        WalletManager.getInstance().saveWalletIndex(this);
//    }
//}
