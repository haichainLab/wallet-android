package io.samos.wallet.utils;

import com.google.gson.Gson;

/**
 * Created by hanyouhong on 2018/10/12.
 */

public class DoubleTest {

    static class Data {
        Double a;
        Double b;
        Data() {}
    }
    public static  void main(String[] args) {
        Gson gson = new Gson();
        Data d = new Data();
        d.a = 0.111;
        d.b = 0.22d;
        String str = gson.toJson(d);
        System.out.println(str);

    }
}
