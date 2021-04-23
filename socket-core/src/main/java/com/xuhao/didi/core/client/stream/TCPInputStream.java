package com.xuhao.didi.core.client.stream;

import com.xuhao.didi.core.iocore.interfaces.ISocketInputStream;

import java.io.IOException;
import java.io.InputStream;

public class TCPInputStream implements ISocketInputStream {
    private boolean mIsClose;
    private InputStream mInputStream;
    public TCPInputStream(InputStream inputStream) {
        this.mInputStream = inputStream;
        mIsClose = false;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return mInputStream.read(b);
    }

    @Override
    public void close() throws IOException {
        mInputStream.close();
        mIsClose = true;
    }

    @Override
    public boolean isClose() {
        return mIsClose;
    }
}
