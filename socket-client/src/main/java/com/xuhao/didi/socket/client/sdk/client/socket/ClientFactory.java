package com.xuhao.didi.socket.client.sdk.client.socket;

import com.xuhao.didi.core.client.ConnectMode;
import com.xuhao.didi.core.client.TCPClient;
import com.xuhao.didi.core.client.UDPClient;
import com.xuhao.didi.core.utils.SLog;
import com.xuhao.didi.core.client.ISocketClient;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.OkSocketSSLConfig;
import com.xuhao.didi.socket.common.interfaces.default_protocol.DefaultX509ProtocolTrustManager;
import com.xuhao.didi.socket.common.interfaces.utils.TextUtils;

import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class ClientFactory {

    public static synchronized ISocketClient createSocketClient(OkSocketOptions options, ConnectionInfo connectionInfo) throws Exception {
        //自定义socket操作
        if (options.getOkSocketFactory() != null) {
            return options.getOkSocketFactory().createSocket(connectionInfo, options);
        }

        if (options.getConnectMode() == ConnectMode.UDP){
            return new UDPClient();
        }
        return createTCP(options, connectionInfo);
    }

    private static ISocketClient createTCP(OkSocketOptions options, ConnectionInfo connectionInfo) throws Exception {
        return new TCPClient(getTCPSocket(options, connectionInfo));
    }

    private static Socket getTCPSocket(OkSocketOptions options, ConnectionInfo connectionInfo) throws Exception{

        //默认操作
        OkSocketSSLConfig config = options.getSSLConfig();
        if (config == null) {
            return new Socket();
        }

        SSLSocketFactory factory = config.getCustomSSLFactory();
        if (factory == null) {
            String protocol = "SSL";
            if (!TextUtils.isEmpty(config.getProtocol())) {
                protocol = config.getProtocol();
            }

            TrustManager[] trustManagers = config.getTrustManagers();
            if (trustManagers == null || trustManagers.length == 0) {
                //缺省信任所有证书
                trustManagers = new TrustManager[]{new DefaultX509ProtocolTrustManager()};
            }

            try {
                SSLContext sslContext = SSLContext.getInstance(protocol);
                sslContext.init(config.getKeyManagers(), trustManagers, new SecureRandom());
                return sslContext.getSocketFactory().createSocket();
            } catch (Exception e) {
                if (options.isDebug()) {
                    e.printStackTrace();
                }
                SLog.e(e.getMessage());
                return new Socket();
            }

        } else {
            try {
                return factory.createSocket();
            } catch (IOException e) {
                if (options.isDebug()) {
                    e.printStackTrace();
                }
                SLog.e(e.getMessage());
                return new Socket();
            }
        }
    }
}
