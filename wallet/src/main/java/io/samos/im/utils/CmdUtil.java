package io.samos.im.utils;

import com.google.gson.Gson;

import io.samos.im.beans.CmdRegisterWithPhone;
import io.samos.im.beans.CmdRegisterWithSign;
import io.samos.im.beans.ImRequest;
import io.samos.im.constants.Cmds;
import io.samos.wallet.common.Constant;
import io.samos.wallet.utils.SamosWalletUtils;
import io.samos.wallet.utils.StringUtils;

public class CmdUtil {


    //0001
    public static String getRegisterWithSignCmd(String pwd) {

        pwd = SamosWalletUtils.getPin16();
        String addr = WalletUtil.getDefautlWalletAddress();
        String appid = Constant.APP_ID;
        String coin = WalletUtil.getDefaultWallet().getToken();
        String pubkey = WalletUtil.getPubkey(pwd,addr);
        String hash = StringUtils.sha256(addr+appid+coin+pubkey);
        String sign = WalletUtil.signData(hash,pwd,addr);

        CmdRegisterWithSign cmdRegisterWithSign = new CmdRegisterWithSign();

        cmdRegisterWithSign.setAddress(addr);
        cmdRegisterWithSign.setCoin(coin);
        cmdRegisterWithSign.setAppid(appid);
        cmdRegisterWithSign.setPubkey(pubkey);
        cmdRegisterWithSign.setSign(sign);


        ImRequest request = new ImRequest();
        request.cmd = Cmds.CMD_REGISTER_WITH_SIGN;
        request.body = cmdRegisterWithSign;

        return toJson(request);
    }

    //0003
    public static String getRegisterWithPhoneCmd(String phone,String code) {

        String appid = Constant.APP_ID;
        String coin = WalletUtil.getDefaultWallet().getToken();


        CmdRegisterWithPhone cmdRegisterWithPhone = new CmdRegisterWithPhone();

        cmdRegisterWithPhone.setCoin(coin);
        cmdRegisterWithPhone.setAppid(appid);
        cmdRegisterWithPhone.setPhone(phone);
        cmdRegisterWithPhone.setCode(code);



        ImRequest request = new ImRequest();
        request.cmd = Cmds.CMD_REGISTER_WITH_PHONE;
        request.body = cmdRegisterWithPhone;

        return toJson(request);
    }



    public static String toJson(Object o ) {
        Gson gson= new Gson();
        return gson.toJson(o);
    }
}
