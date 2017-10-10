package com.ym.leancloud;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tshen on 16/8/16.
 */
public class LeanCloudPush extends ReactContextBaseJavaModule {

    public static String MODULE_NAME = "LeanCloudPush";

    private static LeanCloudPush singleton;
    private static Map<String, String> backgroundNotificationCache = null;
    private static String ON_RECEIVE = "leancloudPushOnReceive";
    private static String ON_ERROR = "leancloudPushOnError";
    private static String ON_PRESS = "onPress";

    public LeanCloudPush(ReactApplicationContext reactContext) {
        super(reactContext);
        singleton = this;
        // subscribeChannel("15950450693");
    }

    protected static void onError(Exception e) {
        if (singleton != null) {
            WritableMap error = Arguments.createMap();
            error.putString("message", e.getLocalizedMessage());
            RCTDeviceEventEmitter emitter = singleton.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class);
            emitter.emit(ON_ERROR, error);
        }
    }

    private static WritableMap getWritableMap(Map<String, String> map) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("type", map.get("type"));
        writableMap.putString("title", map.get("title"));
        writableMap.putString("alert", map.get("alert"));
        return writableMap;
    }

    protected static void onReceive(Map<String, String> map) {
        Log.i("notification","notification onReceive");
        if (singleton != null) {
            WritableMap pushNotification = getWritableMap(map);
            RCTDeviceEventEmitter emitter = singleton.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class);
            emitter.emit(ON_RECEIVE, pushNotification);
        }
    }

    protected static void driverPush(Map<String, String> map){
        if (singleton != null) {
            WritableMap pushNotification = getWritableMap(map);
            RCTDeviceEventEmitter emitter = singleton.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class);
            emitter.emit(ON_PRESS, pushNotification);
        }
    }

    @ReactMethod
    public void subscribes(ReadableArray channels) {
        if(channels != null && channels.size() > 0) {
            for(int i = 0; i < channels.size(); i++) {
                String channel = channels.getString(i);
                this.subscribeChannel(channel);
            }
        }
    }

    @ReactMethod
    public void subscribe(String channel) {
        this.subscribeChannel(channel);
    }

    private void subscribeChannel(String channel) {
        PushService.subscribe(AVOSCloud.applicationContext, channel, null);
    }

    @ReactMethod
    public void getInstallationId(final Promise promise) {
        try {
            AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                        Log.i(MODULE_NAME, "installationId = " + installationId);
                        promise.resolve(installationId);
                    } else {
                        promise.reject(e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(MODULE_NAME, "fail to get installationId");
            promise.reject(e);
        }
    }


    protected static boolean isActive() {
        return singleton != null;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("ON_RECEIVE", ON_RECEIVE);
        constants.put("ON_ERROR", ON_ERROR);
        constants.put("ON_PRESS", ON_PRESS);
        return constants;
    }
}
