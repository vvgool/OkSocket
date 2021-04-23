package com.xuhao.didi.core.client;

import com.xuhao.didi.core.iocore.interfaces.ISocketInputStream;
import com.xuhao.didi.core.iocore.interfaces.ISocketOutputStream;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;

public interface ISocketClient {
    void bind(SocketAddress bindpoint) throws IOException;
    void connect(SocketAddress endpoint, int timeout) throws IOException;
    ISocketInputStream getInputStream() throws IOException;
    ISocketOutputStream getOutputStream() throws IOException;
    SocketAddress getLocalSocketAddress();
    InetAddress getInetAddress();
    int getPort();
    boolean isConnected();
    boolean isClosed();
    void close() throws IOException;
}
