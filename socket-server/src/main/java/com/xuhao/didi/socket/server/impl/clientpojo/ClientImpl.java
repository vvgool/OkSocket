package com.xuhao.didi.socket.server.impl.clientpojo;

import com.xuhao.didi.core.client.ConnectMode;
import com.xuhao.didi.core.client.ISocketClient;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.iocore.interfaces.ISocketInputStream;
import com.xuhao.didi.core.iocore.interfaces.ISocketOutputStream;
import com.xuhao.didi.core.iocore.interfaces.IStateSender;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.IIOManager;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClient;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.xuhao.didi.socket.server.action.ClientActionDispatcher;
import com.xuhao.didi.socket.server.action.IAction;
import com.xuhao.didi.socket.server.exceptions.CacheException;
import com.xuhao.didi.socket.server.impl.OkServerOptions;
import com.xuhao.didi.socket.server.impl.iocore.tcp.TCPClientIOManager;
import com.xuhao.didi.socket.server.impl.iocore.udp.UDPClientIOManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl extends AbsClient {

    private volatile boolean isDead;

    private IServerIOManager mIOManager;

    private IStateSender mActionDispatcher;

    private volatile ClientPoolImpl mClientPool;

    private IStateSender mServerStateSender;

    private volatile boolean isReadThreadStarted;

    private volatile List<IClientIOCallback> mCallbackList = new ArrayList<>();

    public ClientImpl(ISocketClient socket,
                      OkServerOptions okServerOptions) {
        super(socket, okServerOptions);
        mActionDispatcher = new ClientActionDispatcher(this);

        try {
            initIOManager();
        } catch (IOException e) {
            disconnect(e);
        }
    }

    public void setClientPool(ClientPoolImpl clientPool) {
        mClientPool = clientPool;
    }

    public void setServerStateSender(IStateSender serverStateSender) {
        mServerStateSender = serverStateSender;
    }

    private void initIOManager() throws IOException {
        ISocketInputStream inputStream = mSocket.getInputStream();
        ISocketOutputStream outputStream = mSocket.getOutputStream();
        if (mOkServerOptions.getConnectMode() == ConnectMode.TCP){
            mIOManager = new TCPClientIOManager(inputStream, outputStream, mOkServerOptions, mActionDispatcher);
        }else{
            mIOManager = new UDPClientIOManager(inputStream, outputStream, mOkServerOptions, mActionDispatcher);
        }

    }

    public void startIOEngine() {
        if (mIOManager != null) {
            synchronized (mIOManager) {
                mIOManager.startEngine();
            }
        }
    }

    @Override
    public void disconnect(Exception e) {
        if (mIOManager != null) {
            synchronized (mIOManager) {
                mIOManager.close(e);
            }
        } else {
            onClientDead(e);
        }
        try {
            synchronized (mSocket) {
                mSocket.close();
            }
        } catch (IOException e1) {
        }
        removeAllIOCallback();
        isReadThreadStarted = false;
    }

    @Override
    public void disconnect() {
        if (mIOManager != null) {
            synchronized (mIOManager) {
                mIOManager.close();
            }
        } else {
            onClientDead(null);
        }
        try {
            synchronized (mSocket) {
                mSocket.close();
            }
        } catch (IOException e1) {
        }
        removeAllIOCallback();
        isReadThreadStarted = false;
    }

    @Override
    public IClient send(ISendable sendable) {
        if (mIOManager != null) {
            mIOManager.send(sendable);
        }
        return this;
    }

    @Override
    protected void onClientReady() {
        if (isDead) {
            return;
        }
        mClientPool.cache(this);
        mServerStateSender.sendBroadcast(IAction.Server.ACTION_CLIENT_CONNECTED, this);
    }

    @Override
    protected void onClientDead(Exception e) {
        if (isDead) {
            return;
        }
        if (!(e instanceof CacheException)) {
            mClientPool.unCache(this);
        }
        if (e != null) {
            if (mOkServerOptions.isDebug()) {
                e.printStackTrace();
            }
        }
        disconnect(e);
        mServerStateSender.sendBroadcast(IAction.Server.ACTION_CLIENT_DISCONNECTED, this);
        synchronized (this) {
            isDead = true;
        }
    }

    @Override
    public void setReaderProtocol(IReaderProtocol protocol) {
        if (mIOManager != null) {
            synchronized (mIOManager) {
                OkServerOptions.Builder builder = new OkServerOptions.Builder(mOkServerOptions);
                builder.setReaderProtocol(protocol);
                mOkServerOptions = builder.build();
                mIOManager.setOkOptions(mOkServerOptions);
            }
        }
    }

    @Override
    public void addIOCallback(IClientIOCallback clientIOCallback) {
        if (isDead) {
            return;
        }
        synchronized (mCallbackList) {
            mCallbackList.add(clientIOCallback);
        }
        synchronized (mIOManager) {
            if (!isReadThreadStarted) {
                isReadThreadStarted = true;
                mIOManager.startReadEngine();
            }
        }
    }

    @Override
    public void removeIOCallback(IClientIOCallback clientIOCallback) {
        synchronized (mCallbackList) {
            mCallbackList.remove(clientIOCallback);
        }
    }

    @Override
    public void removeAllIOCallback() {
        synchronized (mCallbackList) {
            mCallbackList.clear();
        }
    }

    @Override
    public void onClientRead(OriginalData originalData) {
        List<IClientIOCallback> list = new ArrayList<>();
        list.addAll(mCallbackList);

        for (IClientIOCallback clientIOCallback : list) {
            try {
                clientIOCallback.onClientRead(originalData, this, mClientPool);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClientWrite(ISendable sendable) {
        List<IClientIOCallback> list = new ArrayList<>();
        list.addAll(mCallbackList);

        for (IClientIOCallback clientIOCallback : list) {
            try {
                clientIOCallback.onClientWrite(sendable, this, mClientPool);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
