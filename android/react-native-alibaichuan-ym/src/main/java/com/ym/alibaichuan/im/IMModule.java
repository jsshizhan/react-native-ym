package com.ym.alibaichuan.im;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by Administrator on 2017-10-17.
 */

public class IMModule extends ReactContextBaseJavaModule {
    public IMModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "alibaichuanIM";
    }

    @ReactMethod
    public void toChatCustomer(String accountName){
        final YWIMKit mIMKit = YWAPI.getIMKitInstance(accountName, IMTool.IM_KEY);
        IYWLoginService loginService = mIMKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(accountName, IMTool.IM_PASSWORD);
        loginService.login(loginParam, new IWxCallback() {
            @Override
            public void onSuccess(Object... arg0) {
                Activity currentActivity = getCurrentActivity();
                EServiceContact contact = new EServiceContact("蒙a货的:售前张雪辉", 0);
                Intent intent = mIMKit.getChattingActivityIntent(contact);
                currentActivity.startActivity(intent);
            }

            @Override
            public void onProgress(int arg0) {
            }

            @Override
            public void onError(int errCode, String description) {
                //如果登录失败，errCode为错误码,description是错误的具体描述信息
            }
        });
    }

    @ReactMethod
    public void toChatPerson( String id) {
        Activity context = getCurrentActivity();
        YWIMKit mIMKit = IMTool.getInstance().getIMKit();
        String loginUserId = IMTool.getInstance().getIMKit().getIMCore().getLoginUserId();
        String appKey = IMTool.getInstance().getIMKit().getIMCore().getAppKey();
        if (TextUtils.isEmpty(loginUserId) || TextUtils.isEmpty(appKey)) {
            return;
        }
        Intent intent = mIMKit.getChattingActivityIntent(id, IMTool.IM_KEY);
        context.startActivity(intent);
    }

    @ReactMethod
    public void initIMKit(String accountName){
        IMTool.getInstance().initIMKit(accountName, IMTool.IM_KEY);
    }

    @ReactMethod
    public void getChatList(){
        Activity context = getCurrentActivity();
        YWIMKit mIMKit = IMTool.getInstance().getIMKit();
        Intent intent = mIMKit.getConversationActivityIntent();
        context.startActivity(intent);
    }

}
