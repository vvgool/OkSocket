package com.xuhao.didi.core.iocore;

import com.xuhao.didi.core.client.ConnectMode;

/**
 * Created by xuhao on 2017/5/31.
 */

public class ReaderImpl {
    public static AbsReader newReader(ConnectMode mode){
        if (mode == ConnectMode.UDP) return new ReaderUDPImpl();
        return new ReaderTCPImpl();
    }
}
