package com.queue.client.greendao.ctrls;

import android.text.TextUtils;

import com.queue.client.base.MyApp;
import com.queue.client.entry.HostNo;

import java.util.List;

/**
 * 主机控制类
 */
public class HostCtrls {
    /**
     * 添加主机
     *
     * @param ip
     * @return
     */
    public static long addHostInfo(String ip,String no,String machineNo) {
        if (TextUtils.isEmpty(ip)) return -1;
        return MyApp.getDaoSession().getHostNoDao().insertOrReplace(new HostNo(ip,no,machineNo));
    }

    /**
     * 删除主机
     *
     * @param key
     */
    public static void deleteHostInfoById(long key) {
        MyApp.getDaoSession().getHostNoDao().deleteByKey(key);
    }

    /**
     * 查找所有的主机
     *
     * @return
     */
    public static List<HostNo> loadListHostInfo() {
        return MyApp.getDaoSession().getHostNoDao().loadAll();
    }
}
