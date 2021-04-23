package com.xuhao.didi.socket.server.impl.server;

import com.xuhao.didi.core.client.ISocketClient;
import com.xuhao.didi.core.client.TCPClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class TCPServerClient extends IServerClient{
    private ServerSocket mServerSocket;
    public TCPServerClient(int mPort) throws IOException {
        super(mPort);
        mServerSocket = new ServerSocket(mPort);
    }

    @Override
    public ISocketClient accept() throws IOException{
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!mServerSocket.isBound())
            throw new SocketException("Socket is not bound yet");
        return new TCPClient(mServerSocket.accept());
    }

    @Override
    public void close() throws IOException{
        if (mServerSocket != null) mServerSocket.close();
    }

    @Override
    public boolean isClosed() {
        return mServerSocket == null || mServerSocket.isClosed();
    }
}
