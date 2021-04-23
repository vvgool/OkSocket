package com.xuhao.didi.socket.client.sdk.client;



import com.xuhao.didi.core.client.ISocketClient;


public abstract class OkSocketFactory {

    public abstract ISocketClient createSocket(ConnectionInfo info, OkSocketOptions options) throws Exception;

}
