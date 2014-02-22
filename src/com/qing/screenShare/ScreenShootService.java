package com.qing.screenShare;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by tan on 14-2-17.
 */
public class ScreenShootService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //这儿来实现一些功能
        System.out.println("flags---->" + flags);
        System.out.println("startId---->" + startId);
        System.out.println("Service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service onDestroy");
    }
}
