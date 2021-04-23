package com.xuhao.didi.core.iocore;


import com.xuhao.didi.core.iocore.interfaces.IIOCoreOptions;
import com.xuhao.didi.core.iocore.interfaces.IReader;
import com.xuhao.didi.core.iocore.interfaces.ISocketInputStream;
import com.xuhao.didi.core.iocore.interfaces.IStateSender;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Tony on 2017/12/26.
 */

public abstract class AbsReader implements IReader<IIOCoreOptions> {

    protected volatile IIOCoreOptions mOkOptions;

    protected IStateSender mStateSender;

    protected ISocketInputStream mInputStream;

    public AbsReader() {
    }

    @Override
    public void initialize(ISocketInputStream inputStream, IStateSender stateSender) {
        mStateSender = stateSender;
        mInputStream = inputStream;
    }

    @Override
    public void setOption(IIOCoreOptions option) {
        mOkOptions = option;
    }


    @Override
    public void close() {
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
