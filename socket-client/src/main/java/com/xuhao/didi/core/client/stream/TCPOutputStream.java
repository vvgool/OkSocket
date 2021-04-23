package com.xuhao.didi.core.client.stream;

import com.xuhao.didi.core.iocore.interfaces.ISocketOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class TCPOutputStream implements ISocketOutputStream {
    private boolean mIsClose;
    private OutputStream mOutputStream;

    public TCPOutputStream(OutputStream outputStream) {
        this.mOutputStream = outputStream;
        mIsClose = false;
    }

    @Override
    public void write(byte[] b) throws IOException {
        mOutputStream.write(b);
    }

    @Override
    public boolean isClose() {
        return mIsClose;
    }

    @Override
    public void close() throws IOException {
        mOutputStream.close();
        mIsClose = true;
    }

    @Override
    public void flush() throws IOException {
        mOutputStream.flush();
    }
}
