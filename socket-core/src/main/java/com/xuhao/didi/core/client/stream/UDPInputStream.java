package com.xuhao.didi.core.client.stream;

import com.xuhao.didi.core.client.UDPClient;
import com.xuhao.didi.core.iocore.interfaces.ISocketInputStream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

public class UDPInputStream implements ISocketInputStream {
    private UDPClient mUdpClient;
    private DatagramSocket mDatagramSocket;
    private DatagramPacket mDatagramPacket;
    private byte[] buf = new byte[2048];
    private boolean mIsClose = false;

    public UDPInputStream(UDPClient udpClient) throws IOException{
        this.mUdpClient = udpClient;
        this.mDatagramSocket = udpClient.getSocket();
        this.mDatagramPacket = new DatagramPacket(buf, buf.length);
        udpClient.setReaderPacket(mDatagramPacket);
        mIsClose = false;
    }

    @Override
    public int read(byte[] b) throws IOException {
        mDatagramSocket.receive(mDatagramPacket);
        System.arraycopy(buf, 0, b, 0, mDatagramPacket.getLength());
        return mDatagramPacket.getLength();
    }

    @Override
    public void close() throws IOException {
        mDatagramSocket.close();
        mIsClose = true;
    }

    @Override
    public boolean isClose() {
        return !mUdpClient.isConnected() || mIsClose;
    }
}
