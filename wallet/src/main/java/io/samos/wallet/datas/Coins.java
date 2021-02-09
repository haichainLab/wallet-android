package io.samos.wallet.datas;

import java.util.ArrayList;



/**
 * Created by jinyang.zheng on 2018/2/22.
 */

public class Coins {
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    private String name;
    private String url;

    public static ArrayList<Coins> GetAllCoins(){
        ArrayList<Coins> result = new ArrayList<>();
        result.add(BuildSkyInfo());
        result.add(BuildSamosInfo());
        result.add(BuildYbcInfo());
        result.add(BuildShcInfo());


        return result;
    }

    private static Coins BuildSkyInfo(){
        Coins coins = new Coins();
        coins.name = "skycoin";
      //  coins.url = Const.SKYIP;
        return coins;
    }

    private static Coins BuildShcInfo(){
        Coins coins = new Coins();
        coins.name = "shihu";
     //   coins.url = Const.SHCIP;
        return coins;
    }

    private static Coins BuildSamosInfo(){
        Coins coins = new Coins();
        coins.name = "samos";
       // coins.url =  Const.SAMIP;
        return coins;
    }

    private static Coins BuildYbcInfo(){
        Coins coins = new Coins();
        coins.name = "yongbang";
       // coins.url = Const.YBCIP;
        return coins;
    }

    @Override
    public String toString() {
        return name;
    }
}
