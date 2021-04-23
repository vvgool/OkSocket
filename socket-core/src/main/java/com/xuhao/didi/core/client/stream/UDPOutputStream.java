package com.xuhao.didi.core.client.stream;

import com.xuhao.didi.core.client.UDPClient;
import com.xuhao.didi.core.iocore.interfaces.ISocketOutputStream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPOutputStream implements ISocketOutputStream {
    private UDPClient mUdpClient;
    private DatagramSocket mDatagramSocket;
    private boolean mIsClose;

    public UDPOutputStream(UDPClient udpClient) throws IOException{
        this.mUdpClient = udpClient;
        this.mDatagramSocket = mUdpClient.getSocket();
        mIsClose = false;
    }

    @Override
    public void write(byte[] d) throws IOException {
        if (mUdpClient.isConnected()){
            mDatagramSocket.send(new DatagramPacket(d, d.length));
        }else{
            DatagramPacket readerPacket = mUdpClient.getReaderPacket();
            mDatagramSocket.send(new DatagramPacket(d, d.length, readerPacket.getAddress(), readerPacket.getPort()));
        }
    }

    @Override
    public boolean isClose() {
        return !mUdpClient.isConnected() || mIsClose;
    }

    @Override
    public void close() throws IOException {
        mDatagramSocket.close();
        mIsClose = true;
    }

    @Override
    public void flush() throws IOException {
    }
}
