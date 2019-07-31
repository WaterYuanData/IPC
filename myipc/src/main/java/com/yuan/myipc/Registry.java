package com.yuan.myipc;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Registry {
    private static final String TAG = "Registry";
    private static Registry sInstance = new Registry();

    // 服务表
    // TODO: 2019/7/27 Class<?> 的意思是？？？
    private ConcurrentHashMap<String, Class<?>> mServices = new ConcurrentHashMap();
    // 方法表
    private ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mMethods = new ConcurrentHashMap();

    private ConcurrentHashMap<String, Object> mObjects = new ConcurrentHashMap();

    public static Registry getInstance() {
        return sInstance;
    }

    public void register(Class<?> service) {
        Log.d(TAG, "register: ");
        // 1.服务id 与 class的表
        ServiceId annotation = service.getAnnotation(ServiceId.class);
        if (annotation == null) {
            throw new RuntimeException("必须使用ServiceId注解的服务才能注册！");
        }
        String serviceId = annotation.value();
        mServices.put(serviceId, service);

        // 2.class与方法的表
        ConcurrentHashMap<String, Method> methods = mMethods.get(service);
        if (methods == null) {
            methods = new ConcurrentHashMap<String, Method>();
            mMethods.put(service, methods);
        }
        // TODO: 2019/7/27 getDeclaredMethods()与getMethods()用哪个？？？
        for (Method method : service.getDeclaredMethods()) {
            // 因为有重载方法的存在，所有不能以方法名 作为key！ 带上参数作为key！
            StringBuilder builder = new StringBuilder(method.getName());// TODO: 2019/7/27 出错一
            builder.append("(");
            // 方法的参数类型数组
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 0) {
                builder.append(parameterTypes[0].getName());
            }
            for (int i = 1; i < parameterTypes.length; i++) {
                builder.append(",").append(parameterTypes[i].getName());
            }
            builder.append(")");
            methods.put(builder.toString(), method);
        }

        // 注解详情打印
        for (Map.Entry<String, Class<?>> entry : mServices.entrySet()) {
            System.out.println("服务表：" + entry.getKey() + "=" + entry.getValue());
        }
        /*
2019-07-29 23:39:56.175 12581-12581/com.yuan.appserver:gps I/System.out: 服务表：LocationManager=class com.yuan.appserver.location.LocationManager
2019-07-29 23:39:56.176 12581-12581/com.yuan.appserver:gps I/System.out: 方法表：class com.yuan.appserver.location.LocationManager
2019-07-29 23:39:56.176 12581-12581/com.yuan.appserver:gps I/System.out: 参数：access$super(com.yuan.appserver.location.LocationManager,java.lang.String,[Ljava.lang.Object;)=public static java.lang.Object com.yuan.appserver.location.LocationManager.access$super(com.yuan.appserver.location.LocationManager,java.lang.String,java.lang.Object[])
2019-07-29 23:39:56.176 12581-12581/com.yuan.appserver:gps I/System.out: 参数：getDefault()=public static com.yuan.appserver.location.LocationManager com.yuan.appserver.location.LocationManager.getDefault()
2019-07-29 23:39:56.176 12581-12581/com.yuan.appserver:gps I/System.out: 参数：getLocation(int)=public com.yuan.appserver.location.Location com.yuan.appserver.location.LocationManager.getLocation(int)
2019-07-29 23:39:56.178 12581-12581/com.yuan.appserver:gps I/System.out: 参数：getLocation()=public com.yuan.appserver.location.Location com.yuan.appserver.location.LocationManager.getLocation()
2019-07-29 23:39:56.178 12581-12581/com.yuan.appserver:gps I/System.out: 参数：setLocation(com.yuan.appserver.location.Location)=public void com.yuan.appserver.location.LocationManager.setLocation(com.yuan.appserver.location.Location)
         */
        for (Map.Entry<Class<?>, ConcurrentHashMap<String, Method>> entry : mMethods.entrySet()) {
            System.out.println("方法表：" + entry.getKey());
            for (Map.Entry<String, Method> methodEntry : entry.getValue().entrySet()) {
                System.out.println("参数：" + methodEntry.getKey() + "=" + methodEntry.getValue());
            }
        }
    }

    public Method findMethod(String serviceId, String methodName, Object[] objects) {
        Class<?> service = mServices.get(serviceId);
        ConcurrentHashMap<String, Method> methods = mMethods.get(service);
        StringBuilder builder = new StringBuilder(methodName);

        builder.append("(");
        if (objects.length != 0) {
            builder.append(objects[0].getClass().getName());
        }
        for (int i = 1; i < objects.length; i++) {
            builder.append(",").append(objects[0].getClass().getName());
        }
        builder.append(")");

        System.out.println("findMethod:" + builder.toString());
        return methods.get(builder.toString());
    }

    public void putInstanceObject(String serviceId, Object object) {
        mObjects.put(serviceId, object);
    }

    public Object getInstanceObject(String serviceId) {
        return mObjects.get(serviceId);
    }
}
