package io.samos.wallet.beans;

/**
 * @author: lh on 2018/5/18 17:53.
 * Email:luchefg@gmail.com
 * Description:
 */
public class ExchangeBean {


    /**
     * ok : 1
     * data : {"bitcoin":{"name":"Bitcoin","price_usd":"8133.68","price_btc":"1.0",
     * "price_cny":"51789.580664"},"samos":{"name":"samos","price_usd":"0.1708",
     * "price_btc":"0.000021","price_cny":"1.0876"},"skycoin":{"name":"Skycoin",
     * "price_usd":"23.2976","price_btc":"0.00287104","price_cny":"148.34280848"}}
     */

    private int ok;
    private DataBean data;


    /**
     * 或得价格
     * @param coin
     * @param type
     * @return
     */
    public Double getPrice(String coin,String type) {
        String mcoin = coin.toLowerCase();
        CoinBean bean = null;
        String price = "0.0";
        if("shihu".equals(mcoin)) {
            bean = data.getShihu();
        } else if("yongbang".equals(mcoin)) {
            bean  = data.getYongbang();
        } else if("samos".equals(mcoin)) {
            bean  = data.getSamos();
        }else if("skycoin".equals(mcoin)) {
            bean  = data.getSkycoin();
        }
        if(bean != null) {
            if("cny".equals(type)) {
                price =  bean.getPrice_cny();
            } else if("usd".equals(type)) {
                price =  bean.getPrice_usd();
            }
        }
        return  Double.parseDouble(price);


    }



    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bitcoin : {"name":"Bitcoin","price_usd":"8133.68","price_btc":"1.0",
         * "price_cny":"51789.580664"}
         * samos : {"name":"samos","price_usd":"0.1708","price_btc":"0.000021","price_cny":"1.0876"}
         * skycoin : {"name":"Skycoin","price_usd":"23.2976","price_btc":"0.00287104",
         * "price_cny":"148.34280848"}
         */

        private BitcoinBean bitcoin;
        private SamosBean samos;
        private SkycoinBean skycoin;
        private YongbangBean yongbang;//永邦币
        private ShihuBean shihu;//石斛



        //石斛Token
        public ShihuBean getShihu() {
            return shihu;
        }

        public void setShihu(ShihuBean shihu) {
            this.shihu = shihu;
        }


        //石斛Token
        public YongbangBean getYongbang() {
            return yongbang;
        }

        public void setYongbang(YongbangBean yongbang) {
            this.yongbang = yongbang;
        }

        public BitcoinBean getBitcoin() {
            return bitcoin;
        }

        public void setBitcoin(BitcoinBean bitcoin) {
            this.bitcoin = bitcoin;
        }

        public SamosBean getSamos() {
            return samos;
        }

        public void setSamos(SamosBean samos) {
            this.samos = samos;
        }




        public SkycoinBean getSkycoin() {
            return skycoin;
        }

        public void setSkycoin(SkycoinBean skycoin) {
            this.skycoin = skycoin;
        }

        public static class BitcoinBean extends CoinBean{

        }

        public static class SamosBean extends CoinBean{

        }

        public static class YongbangBean extends CoinBean{

        }

        public static class ShihuBean  extends CoinBean{

        }

        public static class SkycoinBean extends CoinBean{

        }
    }

    public static class CoinBean {
        /**
         * name : Bitcoin
         * price_usd : 8133.68
         * price_btc : 1.0
         * price_cny : 51789.580664
         */

        private String name;
        private String price_usd;
        private String price_btc;
        private String price_cny;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice_usd() {
            return price_usd;
        }

        public void setPrice_usd(String price_usd) {
            this.price_usd = price_usd;
        }

        public String getPrice_btc() {
            return price_btc;
        }

        public void setPrice_btc(String price_btc) {
            this.price_btc = price_btc;
        }

        public String getPrice_cny() {
            return price_cny;
        }

        public void setPrice_cny(String price_cny) {
            this.price_cny = price_cny;
        }
    }
}
