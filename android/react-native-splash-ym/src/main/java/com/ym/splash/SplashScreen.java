package com.ym.splash;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Response;
import com.ym.splash.model.WelcomeAbout;
import com.ym.splash.network.JavaBeanRequest;
import com.ym.splash.network.Urls;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017-09-20.
 */

public class SplashScreen {

    private static Dialog mSplashDialog;
    private static WeakReference<Activity> mActivity;

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity, final boolean fullScreen) {
        if (activity == null) return;
        mActivity = new WeakReference<Activity>(activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    ImageView iv = new ImageView(activity);
                    mSplashDialog = new Dialog(activity, fullScreen ? R.style.SplashScreen_Fullscreen : R.style.SplashScreen_SplashTheme);
                    mSplashDialog.setCancelable(false);
                    JavaBeanRequest<WelcomeAbout> request = new JavaBeanRequest<>(Urls.getUrl(Urls.WELCOME), WelcomeAbout.class);
                    Response<WelcomeAbout> response = NoHttp.startRequestSync(request);
                    
                    WelcomeAbout welcomeAbout = response.get();
                    if(welcomeAbout == null || "".equals(welcomeAbout.getPicture())){
                        iv.setImageResource(R.mipmap.ic_launcher);
                    }
                    else {
                        Bitmap bm = BitmapFactory.decodeFile(Urls.getUrl(Urls.FILE_SHOW,welcomeAbout.getPicture()));
                        iv.setImageBitmap(bm);
                    }
                    mSplashDialog.setContentView(iv);
                    if (!mSplashDialog.isShowing()) {
                        mSplashDialog.show();
                    }
                }
            }
        });
    }

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity) {
        show(activity, true);
    }

    /**
     * 关闭启动屏
     */
    public static void hide(Activity activity) {
        if (activity == null) {
            if (mActivity == null) {
                return;
            }
            activity = mActivity.get();
        }
        if (activity == null) return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSplashDialog != null && mSplashDialog.isShowing()) {
                    mSplashDialog.dismiss();
                    mSplashDialog = null;
                }
            }
        });
    }
}
