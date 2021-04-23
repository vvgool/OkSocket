package com.xuhao.didi.socket.server.impl.server;

import com.xuhao.didi.core.client.ISocketClient;
import com.xuhao.didi.core.client.UDPClient;

import java.io.IOException;
import java.net.DatagramSocket;

public class UDPServerClient extends IServerClient{
    private DatagramSocket mDatagramSocket;
    public UDPServerClient(int mPort) throws IOException {
        super(mPort);
        mDatagramSocket = new DatagramSocket(mPort);
    }

    @Override
    public ISocketClient accept() throws IOException {
        return new UDPClient(mDatagramSocket);
    }

    @Override
    public void close() throws IOException {
        mDatagramSocket.close();
    }

    @Override
    public boolean isClosed() {
        return mDatagramSocket.isClosed();
    }
}
