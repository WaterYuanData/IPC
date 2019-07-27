package com.yuan.ipc.location;

import com.yuan.myipc.ServiceId;

@ServiceId("LocationManager")
public interface ILocationManager {
    Location getLocation();

    Location getLocation(int num);
}
