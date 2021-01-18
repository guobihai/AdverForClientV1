package com.queue.client.tcp;

import android.text.TextUtils;

import com.queue.client.utils.SendFileUtils;
import com.smtlibrary.utils.FileUtil;
import com.smtlibrary.utils.LogUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by gbh on 17/11/4  10:30.
 *
 * @describe 客户端
 */

public class SmartClientSocket {
    private String ip;
    private int port;

    private Socket mSocket;
    private InputStream input;
    private boolean isRun;
    private byte[] mUcRecvData;
    private int len;
    private CallBackInterface mCallBackInterface;

    public SmartClientSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
        isRun = true;
    }


    /**
     * 关闭
     *
     * @throws Exception
     */
    public void close() throws Exception {
        setRun(false);
        if (null == mSocket) return;
        isRun = false;
        if (!mSocket.isClosed()) {
            mSocket.getInputStream().close();
            mSocket.getOutputStream().close();
            mSocket.close();
            mSocket = null;
        }
    }


    /**
     * 发送消息
     *
     * @param msg
     * @throws Exception
     */
    public void loadData(final String msg, CallBackInterface callBackInterface) {
        this.mCallBackInterface = callBackInterface;
        isRun = true;
        mUcRecvData = new byte[1024 * 1024];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(ip, port);
                    mSocket.setSoTimeout(10000);
//                    mSocket.setKeepAlive(true);
                    if (mSocket.isConnected()) {
                        mSocket.getOutputStream().write(msg.getBytes());
                        revData();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendFile(final String path, CallBackInterface callBackInterface) {
        this.mCallBackInterface = callBackInterface;
        isRun = true;
        mUcRecvData = new byte[1024 * 1024];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(ip, port);
                    mSocket.setSoTimeout(10000);
//                    mSocket.setKeepAlive(true);
                    if (mSocket.isConnected()) {
                        sendFile(mSocket, path);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int sendFile(Socket socket, String path) {
        try {
            if (TextUtils.isEmpty(path)) return -1;
            OutputStream outputData = socket.getOutputStream();
            FileInputStream fileInput = new FileInputStream(path);
            int size = -1;
            byte[] buffer = new byte[1024];
            while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                outputData.write(buffer, 0, size);
            }
            outputData.close();
            fileInput.close();
            socket.close();
            if (null != mCallBackInterface)
                mCallBackInterface.onCallBack("file");
            return 1;
        } catch (Exception e) {
            if (null != mCallBackInterface)
                mCallBackInterface.onFaile(e);
            return -1;
        }
    }

    private void revData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    LogUtils.sysout("=======run==========", Thread.currentThread());
                    try {
                        input = mSocket.getInputStream();
                        if (null == input) continue;
                        len = input.read(mUcRecvData);
                        if (len == -1) {
                            System.out.println("远程服务已关闭");
                            return;
                        }
                        String data = new String(mUcRecvData).trim();
                        System.out.println("接收的数据" + data);
                        if (null != mCallBackInterface)
                            mCallBackInterface.onCallBack(data);

                        close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (null != mCallBackInterface)
                            mCallBackInterface.onFaile(e);
                    }
                    break;
                }
            }
        }).start();
    }

    public void setRun(boolean run) {
        isRun = run;
    }


    public interface CallBackInterface {
        void onCallBack(String data);

        void onFaile(Exception e);
    }
}
