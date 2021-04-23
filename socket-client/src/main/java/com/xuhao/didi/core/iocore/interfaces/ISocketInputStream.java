package com.xuhao.didi.core.iocore.interfaces;

import java.io.Closeable;
import java.io.IOException;

public interface ISocketInputStream extends Closeable {
    int read(byte b[]) throws IOException;

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream.
     *
     * <p> The <code>close</code> method of <code>InputStream</code> does
     * nothing.
     *
     * @throws IOException if an I/O error occurs.
     */
    void close() throws IOException;

    boolean isClose();
}
