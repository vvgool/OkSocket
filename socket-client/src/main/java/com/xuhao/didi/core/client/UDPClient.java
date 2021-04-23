package com.xuhao.didi.core.client;

import com.xuhao.didi.core.iocore.interfaces.ISocketInputStream;
import com.xuhao.didi.core.iocore.interfaces.ISocketOutputStream;
import com.xuhao.didi.core.client.stream.UDPInputStream;
import com.xuhao.didi.core.client.stream.UDPOutputStream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public class UDPClient implements ISocketClient {
    private volatile DatagramSocket mSocket;
    private DatagramPacket mReaderPacket;

    public UDPClient() {
    }

    public UDPClient(DatagramSocket socket) {
        this.mSocket = socket;
    }

    public DatagramSocket getSocket() throws IOException{
        if (mSocket == null) mSocket = new DatagramSocket();
        return mSocket;
    }
    public void setReaderPacket(DatagramPacket packet){
        mReaderPacket = packet;
    }

    public DatagramPacket getReaderPacket(){
        return mReaderPacket;
    }

    @Override
    public void bind(SocketAddress bindpoint) throws IOException {
        getSocket().bind(bindpoint);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        getSocket().connect(endpoint);
    }

    @Override
    public ISocketInputStream getInputStream() throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!mSocket.isBound())
            throw new SocketException("Socket is not bound");
        return new UDPInputStream(this);
    }

    @Override
    public ISocketOutputStream getOutputStream() throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!mSocket.isBound())
            throw new SocketException("Socket is not bound");
        return new UDPOutputStream(this);
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        if (mSocket == null) return null;
        return mSocket.getLocalSocketAddress();
    }

    @Override
    public InetAddress getInetAddress() {
        if (mSocket == null) return null;
        return mSocket.getInetAddress();
    }

    @Override
    public int getPort() {
        if (mSocket == null) return 0;
        return mSocket.getPort();
    }

    @Override
    public boolean isConnected() {
        return mSocket != null && mSocket.isConnected();
    }

    @Override
    public boolean isClosed() {
        return mSocket == null || mSocket.isClosed();
    }

    @Override
    public void close() throws IOException {
        if (mSocket != null) mSocket.close();
    }
}
