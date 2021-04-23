package com.xuhao.didi.socket.server.impl.server;

import com.xuhao.didi.core.client.ISocketClient;

import java.io.IOException;

public abstract class IServerClient {
    protected int mPort;

    public IServerClient(int mPort) throws IOException {
        this.mPort = mPort;
    }

    public abstract ISocketClient accept() throws IOException;
    public abstract void close() throws IOException;
    public abstract boolean isClosed();
}
