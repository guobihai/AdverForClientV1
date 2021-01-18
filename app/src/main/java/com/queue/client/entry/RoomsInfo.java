package com.queue.client.entry;

import android.text.TextUtils;

import java.io.Serializable;


/**
 * 房间信息
 */
public class RoomsInfo implements Serializable{
    private long Id;
    private String roomName;
    private String roomTitle;
    private String roomDesc;
    private int type;//使用类型 0未使用，1，已使用
    private String remark;//备注


    public RoomsInfo(String roomName, String roomTitle, String roomDesc, int type) {
        this.roomName = roomName;
        this.roomTitle = roomTitle;
        this.roomDesc = roomDesc;
        this.type = type;
    }


    public RoomsInfo() {

    }


    public String getRoomName() {
        return TextUtils.isEmpty(this.roomName) ? "" : this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomTitle() {
        return TextUtils.isEmpty(this.roomTitle) ? "" : this.roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getRoomDesc() {
        return TextUtils.isEmpty(this.roomDesc) ? "" : this.roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
