package com.yuan.appserver.location;

import com.yuan.myipc.ServiceId;

/**
 * 服务中提供暴露给其他进程使用的方法并提供一个`ServiceId`注解标记，
 * 而服务实现中必须给到相同的`ServiceId`与方法实现，
 * 不强制要求`LocationManager`一定需要继承`ILocationManager`j接口，
 * 但是为了保证方法签名统一建议继承
 */
//public class LocationManager implements ILocationManager {
@ServiceId("LocationManager")
public class LocationManager {

    private static final LocationManager sInstance = new LocationManager();

    public static LocationManager getDefault() {
        return sInstance;
    }

    private LocationManager() {
    }

    private Location location;

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public Location getLocation(int num) {
        return new Location("高新区" + num, 0, 0);
    }


}