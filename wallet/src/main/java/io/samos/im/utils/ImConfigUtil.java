package io.samos.im.utils;

import io.samos.im.beans.ImServerConfig;

public class ImConfigUtil {

    public static ImServerConfig getWsConfig() {

        ImServerConfig config = new ImServerConfig();
        config.ctrUrl = "https://yqkkn.com";
        config.wsUrl = "ws://127";
        return config;
    }

    public static String getWsUrl() {
        return "ws://172.21.78.103:6060/v1/channel?apikey=AQEAAAABAAD_rAp4DJh05a1HAwFT3A6K";
    }
}
