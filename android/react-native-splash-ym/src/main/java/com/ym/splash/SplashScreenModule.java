package com.ym.splash;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by Administrator on 2017-09-20.
 */

public class SplashScreenModule extends ReactContextBaseJavaModule {

    public SplashScreenModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "SplashYM";
    }

    /**
     * 打开启动屏
     */
    @ReactMethod
    public void show(String url) {
        SplashScreen.show(getCurrentActivity(),url);
    }

    /**
     * 关闭启动屏
     */
    @ReactMethod
    public void hide() {
        SplashScreen.hide(getCurrentActivity());
    }
}
