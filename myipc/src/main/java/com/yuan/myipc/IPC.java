package com.yuan.myipc;

import android.content.Context;

import com.yuan.myipc.model.Request;
import com.yuan.myipc.model.Response;

import java.lang.reflect.Proxy;

public class IPC {
    //=================================================

    /**
     * 注册接口
     * 服务端需要暴露出去的服务 注册！！！
     */
    public static void register(Class<?> clazz) {
        Registry.getInstance().register(clazz);
    }
    //===================================================

    /**
     * 客户端 方法
     */
    public static void connect(Context context, Class<? extends IPCService> service) {
        connect(context, null, service);
    }

    public static void connect(Context context, String packageName, Class<? extends IPCService> service) {
        Channel.getInstance().bind(context, packageName, service);
    }


    // 动态代理获得接口类对象实例
    public static <T> T getInstanceWithName(Class<? extends IPCService> serviceClass, Class<T> interfaceClass, String methodName, Object... parameters) {
        if (!interfaceClass.isInterface()) {
            throw new RuntimeException("必须传入接口类");
        }
        ServiceId annotation = interfaceClass.getAnnotation(ServiceId.class);
        Response response = Channel.getInstance().send(Request.GET_INSTANCE, serviceClass, annotation.value(), methodName, parameters);
        if (response.isSuccess()) {
            return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new IPCInvocationHandler(serviceClass, annotation.value()));
        }
        return null;
    }
}
