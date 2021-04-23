package com.xuhao.didi.core.client;

import com.xuhao.didi.core.iocore.interfaces.ISocketInputStream;
import com.xuhao.didi.core.iocore.interfaces.ISocketOutputStream;
import com.xuhao.didi.core.client.stream.TCPInputStream;
import com.xuhao.didi.core.client.stream.TCPOutputStream;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class TCPClient implements ISocketClient {
    private volatile Socket mSocket;

    public TCPClient() {
        mSocket = new Socket();
    }

    public TCPClient(Socket socket) {
        this.mSocket = socket;
    }

    @Override
    public void bind(SocketAddress bindpoint) throws IOException {
        mSocket.bind(bindpoint);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        mSocket.connect(endpoint, timeout);
        mSocket.setTcpNoDelay(true);
    }

    @Override
    public ISocketInputStream getInputStream() throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!isConnected())
            throw new SocketException("Socket is not connected");
        if (mSocket.isInputShutdown())
            throw new SocketException("Socket input is shutdown");
        return new TCPInputStream(mSocket.getInputStream());
    }

    @Override
    public ISocketOutputStream getOutputStream() throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!isConnected())
            throw new SocketException("Socket is not connected");
        if (mSocket.isOutputShutdown())
            throw new SocketException("Socket output is shutdown");
        return new TCPOutputStream(mSocket.getOutputStream());
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        return mSocket.getLocalSocketAddress();
    }

    @Override
    public InetAddress getInetAddress() {
        return mSocket.getInetAddress();
    }

    @Override
    public int getPort() {
        return mSocket.getPort();
    }

    @Override
    public boolean isConnected() {
        return mSocket.isConnected();
    }

    @Override
    public boolean isClosed() {
        return mSocket.isClosed();
    }

    @Override
    public void close() throws IOException {
        mSocket.close();
    }
}
