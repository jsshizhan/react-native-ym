package com.ym.alibaichuan.im;

import android.app.Application;
import android.text.TextUtils;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWConstants;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.LoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.login.YWPwdType;
import com.alibaba.wxlib.util.SysUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-10-17.
 */

public class IMTool {

    private static IMTool sInstance = new IMTool();

    public static IMTool getInstance() {
        return sInstance;
    }

    // openIM UI解决方案提供的相关API，创建成功后，保存为全局变量使用
    private YWIMKit mIMKit;

    private Application mApp;

    private List<Map<YWTribe, YWTribeMember>> mTribeInviteMessages = new ArrayList<Map<YWTribe, YWTribeMember>>();

    public YWIMKit getIMKit() {
        return mIMKit;
    }

    public void setIMKit(YWIMKit imkit) {
        mIMKit = imkit;
    }

    public void initIMKit(String userId, String appKey) {
        mIMKit = YWAPI.getIMKitInstance(userId, appKey);
    }

    /**
     * 登录操作
     *
     * @param userId   用户id
     * @param password 密码
     * @param callback 登录操作结果的回调
     */
    //------------------请特别注意，OpenIMSDK会自动对所有输入的用户ID转成小写处理-------------------
    //所以开发者在注册用户信息时，尽量用小写
    public void login_Sample(String userId, String password, String appKey,
                             IWxCallback callback) {

        if (mIMKit == null) {
            return;
        }

        SysUtil.setCnTaobaoInit(true);
        YWLoginParam loginParam = YWLoginParam.createLoginParam(userId,
                password);
        if (TextUtils.isEmpty(appKey) || appKey.equals(YWConstants.YWSDKAppKey)
                || appKey.equals(YWConstants.YWSDKAppKeyCnHupan)) {
            loginParam.setServerType(LoginParam.ACCOUNTTYPE_WANGXIN);
            loginParam.setPwdType(YWPwdType.pwd);
        }
        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = mIMKit.getLoginService();

        mLoginService.login(loginParam, callback);
    }


    /**
     * 登出
     */
    public void loginOut_Sample() {
        if (mIMKit == null) {
            return;
        }


        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = mIMKit.getLoginService();
        mLoginService.logout(new IWxCallback() {

            @Override
            public void onSuccess(Object... arg0) {

            }

            @Override
            public void onProgress(int arg0) {

            }

            @Override
            public void onError(int arg0, String arg1) {

            }
        });
    }

    public List<Map<YWTribe, YWTribeMember>> getTribeInviteMessages() {
        return mTribeInviteMessages;
    }
}
