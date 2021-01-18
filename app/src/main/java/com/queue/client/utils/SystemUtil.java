package com.queue.client.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class SystemUtil {
    /**
     * @return
     */
    public static String getLocalHostIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        }
        try {
//            Enumeration<NetworkInterface> en2 = NetworkInterface.getNetworkInterfaces();
//            while (en2.hasMoreElements()) {
//                NetworkInterface networkInterface = (NetworkInterface) en2.nextElement();
//                Enumeration<InetAddress> inet = networkInterface.getInetAddresses();
//                while (inet.hasMoreElements()) {
//                    InetAddress inetAddress = (InetAddress) inet.nextElement();
//                    if (!inetAddress.isLoopbackAddress()) {
//                        return inetAddress.getAddress().toString();
//                    }
//                }
//
//            }

            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") ||
                        intf.getName().toLowerCase().equals("wlan0")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                         enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::")) {//ipV6的地址
                                return ipaddress;
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            return "0.0.0.0";
        }
        return "0.0.0.0";
    }


    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    /**
     * @return
     */
    public static int getVersionCode(Context context) {
        int a = 0;
        try {
            a = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return a;
    }

    /**
     * @return
     */
    public static String getVersionName(Context context) {
        String a = "";
        try {
            a = "V" + context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return a;
    }


}
