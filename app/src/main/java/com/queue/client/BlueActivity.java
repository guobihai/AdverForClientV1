package com.queue.client;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.queue.client.utils.PrintFormatUtils;
import com.queue.client.utils.PrintUtils;
import com.queue.client.utils.Tool;
import com.smtlibrary.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.queue.client.R;

public class BlueActivity extends BaseActivity {
    private List<BluetoothDevice> bondedDevicesList;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final UUID uuid = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static BluetoothDevice mdevice = null;
    public static String mDeviceAddress = "";

    private Button button;

    /**
     * 打印纸一行最大的字节
     */
    private static final int LINE_BYTE_SIZE = 32;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btnGet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMsgInfo();
//                printText();
            }
        });

        // 注册用以接收到已搜索到的蓝牙设备的receiver
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(receiver, mFilter);

        if (null != bluetoothAdapter) {
            //得到所有已经配对的蓝牙适配器对象
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
            if (devices.size() > 0) {
                //用迭代
                for (Iterator iterator = devices.iterator(); iterator.hasNext(); ) {
                    //得到BluetoothDevice对象,也就是说得到配对的蓝牙适配器
                    BluetoothDevice device = (BluetoothDevice) iterator.next();
                    //得到远程蓝牙设备的地址
                    LogUtils.sysout("=============name", device.getName());
                    LogUtils.sysout("=============uuid", device.getUuids());
                    LogUtils.sysout("=============getAddress", device.getAddress());
                    if (device.getName().startsWith("Printer")) {
                        connect(device);
                    }
//                    searchDevicesList.add(new BluethBean(device, true));
//                    mSearchAdapter.notifyDataSetChanged();
                }
            }

        }
    }


    private void printText() {
        PrintUtils.setOutputStream(outputStream);
        PrintUtils.selectCommand(PrintUtils.RESET);
        PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.printText("美食餐厅\n\n");
        PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
        PrintUtils.printText("桌号：1号桌\n\n");
        PrintUtils.selectCommand(PrintUtils.NORMAL);
        PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
        PrintUtils.printText(PrintUtils.printTwoData("订单编号", "201507161515\n"));
        PrintUtils.printText(PrintUtils.printTwoData("点菜时间", "2016-02-16 10:46\n"));
        PrintUtils.printText(PrintUtils.printTwoData("上菜时间", "2016-02-16 11:46\n"));
        PrintUtils.printText(PrintUtils.printTwoData("人数：2人", "收银员：张三\n"));

        PrintUtils.printText("--------------------------------\n");
        PrintUtils.selectCommand(PrintUtils.BOLD);
        PrintUtils.printText(PrintUtils.printThreeData("项目", "数量", "金额\n"));
        PrintUtils.printText("--------------------------------\n");
        PrintUtils.selectCommand(PrintUtils.BOLD_CANCEL);
        PrintUtils.printText(PrintUtils.printThreeData("面", "1", "0.00\n"));
        PrintUtils.printText(PrintUtils.printThreeData("米饭", "1", "6.00\n"));
        PrintUtils.printText(PrintUtils.printThreeData("铁板烧", "1", "26.00\n"));
        PrintUtils.printText(PrintUtils.printThreeData("一个测试", "1", "226.00\n"));
        PrintUtils.printText(PrintUtils.printThreeData("牛肉面啊啊", "1", "2226.00\n"));
        PrintUtils.printText(PrintUtils.printThreeData("牛肉面啊啊啊牛肉面啊啊啊", "888", "98886.00\n"));

        PrintUtils.printText("--------------------------------\n");
        PrintUtils.printText(PrintUtils.printTwoData("合计", "53.50\n"));
        PrintUtils.printText(PrintUtils.printTwoData("抹零", "3.50\n"));
        PrintUtils.printText("--------------------------------\n");
        PrintUtils.printText(PrintUtils.printTwoData("应收", "50.00\n"));
        PrintUtils.printText("--------------------------------\n");

        PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
        PrintUtils.printText("备注：不要辣、不要香菜");
        PrintUtils.printText("\n\n\n\n\n");


    }

    private void getMsgInfo() {

//        PrintUtils.setOutputStream(outputStream);
//        PrintUtils.selectCommand(PrintUtils.RESET);
//        PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
//        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
//        PrintUtils.selectCommand(PrintUtils.BOLD);
//        PrintUtils.printText("育儿诊所\n\n");
//        PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
//        PrintUtils.printText("801\n\n");
//        PrintUtils.selectCommand(PrintUtils.NORMAL);
//        PrintUtils.printText("\n\n\n\n\n");

        StringBuffer prinshowinfo = new StringBuffer();
        prinshowinfo.append("\n\n\n");
        //设置大号字体以及加粗
        prinshowinfo.append(PrintFormatUtils.getFontSizeCmd(PrintFormatUtils.FONT_BIG));
        prinshowinfo.append(PrintFormatUtils.getAlignCmd(PrintFormatUtils.ALIGN_CENTER));
        prinshowinfo.append(PrintFormatUtils.getFontBoldCmd(PrintFormatUtils.FONT_BOLD));

        prinshowinfo.append("育儿诊所");
        prinshowinfo.append("\n\n");
        //设置普通字体大小、不加粗

//
        prinshowinfo.append("801");
        prinshowinfo.append("\n\n");
        prinshowinfo.append(PrintFormatUtils.getFontSizeCmd(PrintFormatUtils.FONT_MIDDLE));
        prinshowinfo.append("张三");
        prinshowinfo.append("\n\n");
        //取消字体加粗
        prinshowinfo.append(PrintFormatUtils.getFontBoldCmd(PrintFormatUtils.FONT_BOLD_CANCEL));
        prinshowinfo.append(PrintFormatUtils.getFontSizeCmd(PrintFormatUtils.FONT_NORMAL));
        prinshowinfo.append(Tool.getDealListTime());
        prinshowinfo.append("\n\n");
        prinshowinfo.append(PrintFormatUtils.getFontSizeCmd(PrintFormatUtils.FONT_MIDDLE));
        prinshowinfo.append(PrintFormatUtils.getFontBoldCmd(PrintFormatUtils.FONT_BOLD));
        prinshowinfo.append("有效时间: 60分钟");
        prinshowinfo.append("\n\n");
        prinshowinfo.append(PrintFormatUtils.getFontBoldCmd(PrintFormatUtils.FONT_BOLD));
//        prinshowinfo.append(PrintFormatUtils.getFontBoldCmd(PrintFormatUtils.FONT_BOLD_CANCEL));
        prinshowinfo.append(PrintFormatUtils.getFontSizeCmd(PrintFormatUtils.FONT_BIG));
        prinshowinfo.append("欢迎光临");
        prinshowinfo.append("\n\n\n");
        //切纸
        prinshowinfo.append(PrintFormatUtils.getCutPaperCmd());

        printMsg(prinshowinfo.toString());
    }

    /**
     * 打印两列
     *
     * @param leftText  左侧文字
     * @param rightText 右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);

        // 计算两侧文字中间的空格
        int marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        return sb.toString();
    }

    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }


    private BluetoothDevice device;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("onReceiveonReceive");
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 搜索到的不是已经绑定的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 防止重复添加
//                    if (searchDevicesList.indexOf(device) == -1)
//                        searchDevicesList.add(new BluethBean(device, false));
//                    mSearchAdapter.notifyDataSetChanged();
                }
                // 搜索完成
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                // 状态改变的广播
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int connectState = device.getBondState();
                switch (connectState) {
                    case BluetoothDevice.BOND_NONE:  //10
                        Toast.makeText(BlueActivity.this, "取消配对：" + device.getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_BONDING:  //11
                        Toast.makeText(BlueActivity.this, "正在配对：" + device.getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_BONDED:   //12
                        Toast.makeText(BlueActivity.this, "完成配对：" + device.getName(), Toast.LENGTH_SHORT).show();
                        try {
                            // 连接
                            connect(device);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    };

    private OutputStream outputStream;
    private InputStream inputStream;
    private BluetoothSocket socket;

    //蓝牙设备的连接（客户端）
    private void connect(BluetoothDevice device) {
        // 固定的UUID
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        UUID uuid = UUID.fromString(SPP_UUID);
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印信息
     *
     * @param prinshowinfo 要打印的信息
     * @return
     */
    private boolean printMsg(String prinshowinfo) {
        if (null == outputStream) return false;
        try {
            outputStream.write(prinshowinfo.getBytes("GBK"));
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void close() {
        if (null == socket)
            return;
        if (socket.isConnected()) {
            try {
                outputStream.close();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        close();
    }
}
