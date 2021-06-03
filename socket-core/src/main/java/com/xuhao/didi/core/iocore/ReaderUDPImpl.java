package com.xuhao.didi.core.iocore;

import com.xuhao.didi.core.exceptions.ReadException;
import com.xuhao.didi.core.iocore.interfaces.IOAction;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.core.utils.BytesUtils;
import com.xuhao.didi.core.utils.SLog;

import java.nio.ByteBuffer;

public class ReaderUDPImpl extends AbsReader {
    private byte[] buf = new byte[2048];
    @Override
    public void read() throws RuntimeException {
        OriginalData originalData = new OriginalData();
        IReaderProtocol headerProtocol = mOkOptions.getReaderProtocol();
        int headerLength = headerProtocol.getHeaderLength();
        ByteBuffer headBuf = ByteBuffer.allocate(headerLength);
        headBuf.order(mOkOptions.getReadByteOrder());
        try {
            int len = mInputStream.read(buf);
            headerProtocol.setPackLength(len);
            if (len < headerLength){
                throw new ReadException(
                        "read head is wrong,this socket input stream is end of file read " + len);
            }
            headBuf.put(buf, 0, headerLength);
            originalData.setHeadBytes(headBuf.array());
            if (SLog.isDebug()) {
                SLog.i("read head: " + BytesUtils.toHexStringForLog(headBuf.array()));
            }
            int bodyLength = headerProtocol.getBodyLength(originalData.getHeadBytes(), mOkOptions.getReadByteOrder());
            if (SLog.isDebug()) {
                SLog.i("need read body length: " + bodyLength);
            }
            if (bodyLength > 0) {
                if (bodyLength > mOkOptions.getMaxReadDataMB() * 1024 * 1024) {
                    throw new ReadException("Need to follow the transmission protocol.\r\n" +
                            "Please check the client/server code.\r\n" +
                            "According to the packet header data in the transport protocol, the package length is " + bodyLength + " Bytes.\r\n" +
                            "You need check your <ReaderProtocol> definition");
                }
                ByteBuffer byteBuffer = ByteBuffer.allocate(bodyLength);
                byteBuffer.order(mOkOptions.getReadByteOrder());
                if (len < bodyLength + headerLength){
                    throw new ReadException(
                            "read body is wrong,this socket input stream is end of file read " + bodyLength);
                }else{
                    byteBuffer.put(buf,headerLength, bodyLength);
                }
                if (SLog.isDebug()) {
                    SLog.i("read total bytes: " + BytesUtils.toHexStringForLog(byteBuffer.array()));
                    SLog.i("read total length:" + (byteBuffer.capacity() - byteBuffer.remaining()));
                }
                originalData.setBodyBytes(byteBuffer.array());
            } else if (bodyLength == 0) {
                originalData.setBodyBytes(new byte[0]);
            } else if (bodyLength < 0) {
                throw new ReadException(
                        "read body is wrong,this socket input stream is end of file read " + bodyLength + " ,that mean this socket is disconnected by server");
            }
            mStateSender.sendBroadcast(IOAction.ACTION_READ_COMPLETE, originalData);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
