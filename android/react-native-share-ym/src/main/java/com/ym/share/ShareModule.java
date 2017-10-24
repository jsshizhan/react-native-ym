package com.ym.share;

import android.app.Activity;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Created by Administrator on 2017-10-18.
 */

public class ShareModule extends ReactContextBaseJavaModule {
    public ShareModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private ShareModel shareModel = new ShareModel();

    @Override
    public String getName() {
        return "ShareYM";
    }

    @ReactMethod
    public void share(ReadableMap data){
        shareModel.setNetUrl(data.getString("netUrl"));
        shareModel.setShareIcon(data.getString("shareIcon"));
        shareModel.setShareLink(data.getString("shareLink"));
        shareModel.setShareTitle(data.getString("shareTitle"));
        showShare(shareModel);
    }

    private void showShare(ShareModel bean) {
        Activity context = getCurrentActivity();
        OnekeyShare oks = new OnekeyShare();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(bean.getShareTitle());
        oks.setTitleUrl(bean.getShareLink());
        oks.setText("分享");
//        oks.setImagePath("/storage/6261-3034/DCIM/Camera/test.jpg");  //分享sdcard目录下的图片
        oks.setImageUrl(bean.getNetUrl()+"/file/"+bean.getShareIcon());
        oks.setUrl(bean.getShareLink()); //微信不绕过审核分享链接
        //oks.setFilePath("/sdcard/test-pic.jpg");  //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
        //oks.setComment("分享"); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
        oks.setSite(bean.getShareTitle());  //QZone分享完之后返回应用时提示框上显示的名称
        oks.setSiteUrl(bean.getShareLink());//QZone分享参数;

        // 启动分享GUI
        oks.show(context);
    }
}
