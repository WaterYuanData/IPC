package com.yuan.myipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.yuan.myipc.model.Parameters;
import com.yuan.myipc.model.Request;
import com.yuan.myipc.model.Response;

import java.lang.reflect.Method;

public abstract class IPCService extends Service {

    private Gson mGson = new Gson();

    @Override
    public IBinder onBind(Intent intent) {
        return new IIPCService.Stub() {
            @Override
            public Response send(Request request) throws RemoteException {
                String methodName = request.getMethodName();
                Parameters[] parameters = request.getParameters();
                String serviceId = request.getServiceId();
                Object[] objects = restoreParameters(parameters);

                Method method = Registry.getInstance().findMethod(serviceId, methodName, objects);
                switch (request.getType()) {
                    //执行单例方法 静态方法
                    case Request.GET_INSTANCE:
                        try {
                            Object instanceObject = Registry.getInstance().getInstanceObject(serviceId);
                            //getInstance
                            Object result = method.invoke(instanceObject, objects);
                            Registry.getInstance().putInstanceObject(serviceId, result);
                            return new Response(null, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new Response(null, false);
                        }
                        //执行普通方法
                    case Request.GET_METHOD:
                        try {
                            Object instanceObject = Registry.getInstance().getInstanceObject(serviceId);
                            //获得结果
                            Object result = method.invoke(instanceObject, objects);// TODO: 2019/7/27 带参调用此处报错 
                            return new Response(mGson.toJson(result), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new Response(null, false);
                        }
                }
                return null;
            }
        };
    }

    private Object[] restoreParameters(Parameters[] parameters) {
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameters parameter = parameters[i];
            //还原
            try {
                // TODO: 2019/7/27 restoreParameters
                System.out.println("restoreParameters:" + parameter.getType() + "=" + parameter.getValue());
                objects[i] = mGson.fromJson(parameter.getValue(), Class.forName(parameter.getType()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }

    // TODO: 2019/7/27 IPCService0
    public static class IPCService0 extends IPCService {
    }

    public static class IPCService1 extends IPCService {
    }

    public static class IPCService2 extends IPCService {
    }
}