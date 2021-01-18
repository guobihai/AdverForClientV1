package com.queue.client.greendao.ctrls;

import com.queue.client.entry.RoomsInfo;

import java.util.List;

public class RoomCtrls {
    /**
     * 添加房间管理
     *
     * @param roomName
     * @return
     */
    public static long addRoomsInfo(String roomName) {
        return -1;
    }

    /**
     * 删除房间
     *
     * @param id
     */
    public static void deleteRoomsInfoById(long id) {
    }

    /**
     * 获取所有在用的房间
     *
     * @return
     */
    public static List<RoomsInfo> loadAllRoomInfos() {
//        return MyApp.getDaoSession().getRoomsInfoDao().queryBuilder().orderDesc(RoomsInfoDao.Properties.Type).build().list();
        return null;
    }

    /**
     * 修改信息
     *
     * @param roomsInfo
     */
    public static boolean updateRoomInfoByEntry(RoomsInfo roomsInfo) {
        if (null == roomsInfo) return false;
//        MyApp.getDaoSession().getRoomsInfoDao().update(roomsInfo);
        return true;
    }


}
