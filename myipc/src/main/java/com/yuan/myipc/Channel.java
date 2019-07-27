package com.yuan.myipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.yuan.myipc.model.Parameters;
import com.yuan.myipc.model.Request;
import com.yuan.myipc.model.Response;

import java.util.concurrent.ConcurrentHashMap;

public class Channel {
    private static final String TAG = "Channel";
    private static final Channel ourInstance = new Channel();
    private Gson mGson = new Gson();
    private ConcurrentHashMap<Class<? extends IPCService>, IIPCService> binders = new ConcurrentHashMap<>();

    public static Channel getInstance() {
        return ourInstance;
    }

    private Channel() {
    }

    /**
     * bind 服务 同APP绑定成功
     * 不同APP呢？
     * todo 1、已经绑定的就别bind了
     * 2、正在bind的也别bind了!
     *
     * @param context
     * @param service
     */
    public void bind(Context context, String packageName, Class<? extends IPCService> service) {
        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            // TODO: 2019/7/27 跨App的绑定
            intent.setClassName(packageName, service.getName());
        }
        context.bindService(intent, new IPCServiceConnection(service), Context.BIND_AUTO_CREATE);
    }

    class IPCServiceConnection implements ServiceConnection {
        private final Class<? extends IPCService> mService;

        public IPCServiceConnection(Class<? extends IPCService> service) {
            mService = service;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IIPCService binder = IIPCService.Stub.asInterface(service);
            Log.i(TAG, "onServiceConnected: ");
            binders.put(mService, binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binders.remove(mService);
        }
    }

    public Response send(int type, Class<?> serviceClass, String serviceId, String methodName, Object[] parameters) {
        Request request = new Request(type, serviceId, methodName, makeParameters(parameters));
        IIPCService iipcService = binders.get(serviceClass);
        try {
            return iipcService.send(request);// TODO: 2019/7/27 若服务器没起来，客户端调用此处进入send()方法后会报错
        } catch (RemoteException e) {
            e.printStackTrace();
            // 也可以把null变成错误信息
            return new Response(null, false);
        }
    }

    private Parameters[] makeParameters(Object[] objects) {
        Parameters[] parameters;
        if (objects != null) {
            parameters = new Parameters[objects.length];
            for (int i = 0; i < objects.length; i++) {
                // TODO: 2019/7/27 makeParameters
                System.out.println("参数：" + objects[i].getClass().getName() + "=" + objects[i]);
                parameters[i] = new Parameters(objects[i].getClass().getName(), mGson.toJson(objects[i]));
            }
        } else {
            parameters = new Parameters[0];
        }
        return parameters;
    }
}
