package com.ym.leancloud;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.avos.avoscloud.AVOSCloud;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeanCloudReceiver extends BroadcastReceiver {

     @Override
    public void onReceive(Context context, Intent intent) {

        try{
            JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
            String type = json.getString("msgType");
            String title = json.getString("title");
            String msg = json.getString("alert");
            String objectId = json.getString("objectId");
            com.liangmayong.text2speech.Text2Speech.speech(context, msg, true);
            int mNotificationId = (int) (Math.random() * 10000);
            Intent clickIntent = new Intent(AVOSCloud.applicationContext, ReceiverOnPress.class);
            clickIntent.putExtra("id",objectId);
            clickIntent.putExtra("type",type);
            clickIntent.putExtra("title",title);
            clickIntent.putExtra("alert",msg);
            PendingIntent contentIntent = PendingIntent.getBroadcast(AVOSCloud.applicationContext, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(AVOSCloud.applicationContext)
                                .setSmallIcon(R.drawable.push_icon)
                                .setContentTitle(msg)
                                .setContentText(title)
                                .setContentIntent(contentIntent)
                                .setAutoCancel(true);
            Notification notification = mBuilder.build();
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_SOUND;
            NotificationManager mNotifyMgr = (NotificationManager) AVOSCloud.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, notification);
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", type);
            map.put("title", title);
            map.put("alert", msg);
            map.put("id", objectId);
            LeanCloudPushModule.onReceive(map);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class ReceiverOnPress extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent mainIntent;
            if (isApplicationRunningBackground(context)) {
                mainIntent = new Intent();
                mainIntent.setClassName(context.getPackageName(), context.getPackageName() + ".MainActivity");
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            else{
                mainIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            context.startActivity(mainIntent);
            Map<String, String> map = new HashMap();
            map.put("id",intent.getExtras().get("id").toString());
            map.put("type",intent.getExtras().get("type").toString());
            map.put("alert",intent.getExtras().get("alert").toString());
            LeanCloudPushModule.emitJS(map);
        }
    }

    private static boolean isApplicationRunningBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}