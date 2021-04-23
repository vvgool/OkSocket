package com.xuhao.didi.core.iocore.interfaces;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IReader<T extends IIOCoreOptions> {

    void initialize(ISocketInputStream inputStream, IStateSender stateSender);

    void read() throws RuntimeException;

    void setOption(T option);

    void close();
}
