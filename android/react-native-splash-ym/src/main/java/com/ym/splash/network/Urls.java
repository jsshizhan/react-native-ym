package com.ym.splash.network;

/**
 * Created by Administrator on 2017-10-09.
 */

public class Urls {

    public static final String WELCOME = "/api/auth/system/welcome";

    public static final String FILE_SHOW = "/file/";



    public static String getRequestUrl(String url,String api){
        return url + api;
    }

    public static String getFileShow(String url,String fileName){
        return url + FILE_SHOW + fileName;
    }
}
