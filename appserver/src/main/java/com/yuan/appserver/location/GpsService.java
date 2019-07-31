package com.yuan.appserver.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.yuan.myipc.IPC;

public class GpsService extends Service {
    private static final String TAG = "GpsService";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        LocationManager.getDefault().setLocation(new Location("天府新区", 110, 119));
        IPC.register(LocationManager.class);
    }
}
