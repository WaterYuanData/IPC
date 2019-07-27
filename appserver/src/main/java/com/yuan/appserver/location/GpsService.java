package com.yuan.appserver.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yuan.myipc.IPC;

public class GpsService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocationManager.getDefault().setLocation(new Location("天府新区", 110, 119));
        IPC.register(LocationManager.class);
    }
}
