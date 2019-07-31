package com.yuan.appserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yuan.appserver.location.GpsService;
import com.yuan.appserver.location.ILocationManager;
import com.yuan.myipc.IPC;
import com.yuan.myipc.IPCService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);

        startService(new Intent(this, GpsService.class));
        // 同一个App内跨进程
        IPC.connect(this, IPCService.IPCService1.class); // 出错三：应与manifest中匹配，否则找不到
    }

    public void location(View view) {
        //代理对象
        ILocationManager location = IPC.getInstanceWithName(IPCService.IPCService1.class, ILocationManager.class, "getDefault");
        Toast.makeText(this, "当前位置:" + location.getLocation(), Toast.LENGTH_LONG).show();
    }
}
