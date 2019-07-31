package com.yuan.ipc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yuan.ipc.location.ILocationManager;
import com.yuan.ipc.location.Location;
import com.yuan.myipc.IPC;
import com.yuan.myipc.IPCService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AppServer只启动了IPCService0
        IPC.connect(this, "com.yuan.appserver", IPCService.IPCService1.class);// TODO: 2019/7/27 出错二
    }

    // 客户端通过动态代理获得接口类对象实例
    public void location(View view) {
        //指定获得单例的方法
        ILocationManager locationManager = IPC.getInstanceWithName(IPCService.IPCService1.class, ILocationManager.class, "getDefault");
        Log.d(TAG, "location: locationManager=" + locationManager);
        Location location = locationManager.getLocation();
        Toast.makeText(getApplicationContext(), "位置是：" + location, Toast.LENGTH_SHORT).show();
    }

    public void location2(View view) {
        ILocationManager locationManager = IPC.getInstanceWithName(IPCService.IPCService1.class, ILocationManager.class, "getDefault");
        Log.d(TAG, "location2: locationManager=" + locationManager);
        Location locate = locationManager.getLocation(2);
        Toast.makeText(getApplicationContext(), "位置2是：" + locate, Toast.LENGTH_SHORT).show();
    }
}
