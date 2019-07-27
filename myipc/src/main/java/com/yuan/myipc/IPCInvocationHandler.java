package com.yuan.myipc;

import com.google.gson.Gson;
import com.yuan.myipc.model.Request;
import com.yuan.myipc.model.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IPCInvocationHandler implements InvocationHandler {

    private final Class<? extends IPCService> mServiceClass;
    private final String mServiceId;
    Gson mGson = new Gson();

    public IPCInvocationHandler(Class<? extends IPCService> serviceClass, String serviceId) {
        mServiceClass = serviceClass;
        mServiceId = serviceId;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**
         * 向服务器发起执行method的请求
         */
        Response response = Channel.getInstance().send(Request.GET_METHOD, mServiceClass, mServiceId, method.getName(), args);
        if (response.isSuccess()) {
            // 方法返回值
            Class<?> returnType = method.getReturnType();
            if (returnType != void.class && returnType != Void.class) {
                // 方法执行后的返回值， json数据
                String source = response.getSource();
                return mGson.fromJson(source, returnType);
            }
        }
        return null;
    }
}
