package com.ym.splash;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.AsyncRequestExecutor;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.ym.splash.model.WelcomeAbout;
import com.ym.splash.network.JavaBeanRequest;
import com.ym.splash.network.Urls;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by Administrator on 2017-09-20.
 */

public class SplashScreen {

    private static Dialog mSplashDialog;
    private static WeakReference<Activity> mActivity;
    private static ImageView iv;

    private static Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bmp=(Bitmap)msg.obj;
                    iv.setImageBitmap(bmp);
                    break;
            }
        };
    };

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity, final boolean fullScreen,final String url) {
        if (activity == null) return;

        mActivity = new WeakReference<Activity>(activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    iv = new ImageView(activity);
                    mSplashDialog = new Dialog(activity, fullScreen ? R.style.SplashScreen_Fullscreen : R.style.SplashScreen_SplashTheme);
                    mSplashDialog.setCancelable(false);
                    JavaBeanRequest<WelcomeAbout> request = new JavaBeanRequest<>(Urls.getRequestUrl(url,Urls.WELCOME), RequestMethod.GET, WelcomeAbout.class);
                    AsyncRequestExecutor.INSTANCE.execute(0, request, new SimpleResponseListener<WelcomeAbout>() {
                        @Override
                        public void onSucceed(int what, Response<WelcomeAbout> response) {
                            final WelcomeAbout welcomeAbout = response.get();
                            if(welcomeAbout == null || "".equals(welcomeAbout.getPicture())){
                                iv.setImageResource(R.mipmap.ic_launcher);
                            }
                            else {
//                                Bitmap bm = BitmapFactory.decodeFile(Urls.getFileShow(welcomeAbout.getPicture()));
//                                iv.setImageBitmap(bm);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bitmap bmp = getURLimage(Urls.getFileShow(url,welcomeAbout.getPicture()));
                                        Message msg = new Message();
                                        msg.what = 0;
                                        msg.obj = bmp;
                                        handle.sendMessage(msg);
                                    }
                                }).start();
                            }
                            mSplashDialog.setContentView(iv);
                            if (!mSplashDialog.isShowing()) {
                                mSplashDialog.show();
                            }
                        }

                        @Override
                        public void onFailed(int what, Response<WelcomeAbout> response) {
                            // 请求失败。
                        }
                    });

                }
            }
        });
    }

    public static Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            InputStream is = new URL(url).openStream();
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }


    /**
     * 打开启动屏
     */
    public static void show(final Activity activity,String url) {
        show(activity, true,url);
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
