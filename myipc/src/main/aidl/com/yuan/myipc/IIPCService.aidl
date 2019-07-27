// IPCService.aidl
package com.yuan.myipc;

// Declare any non-default types here with import statements
import com.yuan.myipc.model.Request;
import com.yuan.myipc.model.Response;

interface IIPCService {
   Response send(in Request request);
}
