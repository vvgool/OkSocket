package com.xuhao.didi.socket.server.impl.clientpojo;

import com.xuhao.didi.core.iocore.interfaces.IIOCoreOptions;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.IIOManager;

public interface IServerIOManager<E extends IIOCoreOptions> extends IIOManager<E> {
    void startReadEngine();
}
