package com.ym.splash.network;

/**
 * Created by Administrator on 2017-10-09.
 */

public class Urls {

    public static String URL = "http://192.168.1.167:9804";

    public static final String WELCOME = "/api/auth/system/welcome";

    public static final String FILE_SHOW = "/file/";



    public static String getUrl(String... uris){
        for(String uri:uris){
            URL += uri;
        }
        return URL;
    }
}
