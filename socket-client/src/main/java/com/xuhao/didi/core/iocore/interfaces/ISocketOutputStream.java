package com.xuhao.didi.core.iocore.interfaces;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public interface ISocketOutputStream extends Closeable, Flushable {
    void write(byte b[]) throws IOException;
    boolean isClose();
}
