package com.queue.client.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class SendFileUtils {

    /**
     * 发送文件
     * Socket name = new Socket(ipAddress, port);
     * OutputStream outputName = name.getOutputStream();
     * OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
     * BufferedWriter bwName = new BufferedWriter(outputWriter);
     * bwName.write(fileName);
     * bwName.close();
     * outputWriter.close();
     * outputName.close();
     * name.close();
     * <p>
     * Socket data = new Socket(ipAddress, port);
     *
     * @return
     */
    public static int sendFile(Socket socket, String path) {
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
//            socket.close();
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    // 文件接收方法
    public int ReceiveFile(ServerSocket server) {
        try {
//            // 接收文件名
//            Socket name = server.accept();
//            InputStream nameStream = name.getInputStream();
//            InputStreamReader streamReader = new InputStreamReader(nameStream);
//            BufferedReader br = new BufferedReader(streamReader);
//            String fileName = br.readLine();
//            br.close();
//            streamReader.close();
//            nameStream.close();
//            name.close();

            // 接收文件数据
            Socket data = server.accept();
            InputStream dataStream = data.getInputStream();
            File dir = new File("/sdcard/smartinfo/images"); // 创建文件的存储路径
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String savePath = "/sdcard/smartinfo/images" + System.currentTimeMillis() + ".jpg"; // 定义完整的存储路径
            FileOutputStream file = new FileOutputStream(savePath, false);
            byte[] buffer = new byte[1024];
            int size = -1;
            while ((size = dataStream.read(buffer)) != -1) {
                file.write(buffer, 0, size);
            }
            file.close();
            dataStream.close();
            data.close();
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

}
