package io.samos.tests;

//import com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import org.junit.Test;

import io.samos.im.beans.CmdRegisterWithSign;
import io.samos.im.beans.ImRequest;
import io.samos.im.beans.ImResponse;
import io.samos.im.constants.Cmds;

public class EmailValidatorTest {
    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        String string = "awesome";
        assertThat(string).startsWith("awe");
        assertWithMessage("Without me, it's just aweso").that(string).contains("me");


    }

    @Test
    public void testGson() {

         CmdRegisterWithSign cmdRegisterWithSign = new CmdRegisterWithSign();
       //  cmdRegisterWithSign.setAddress();
        ImRequest request = new ImRequest();
        request.cmd = Cmds.CMD_REGISTER_WITH_SIGN;
        request.body = cmdRegisterWithSign;
        Gson gson= new Gson();
        String cmdstr = gson.toJson(request);
        System.out.println(cmdstr);
        assertThat(cmdstr).startsWith("{");


        String s  = "{\"cmd\":\"0002\",\"body\":{\"xxx\":1,\"ddd\":2}}";

        ImResponse response = gson.fromJson(s,ImResponse.class);
        System.out.println(response);
        System.out.println(response.cmd);
        System.out.println(response.body);




    }
}