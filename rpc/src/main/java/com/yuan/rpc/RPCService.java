package com.yuan.rpc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class IPCService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class IPCService0 extends IPCService {
    }

    public static class IPCService1 extends IPCService {
    }

    public static class IPCService2 extends IPCService {
    }
}
