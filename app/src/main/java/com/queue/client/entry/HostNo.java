package com.queue.client.entry;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HostNo {
    @Id(autoincrement = true)
    private Long Id;

    @Keep
    private String IP;

    @Keep
    private String showNo;
    @Keep
    private String remark;

    public HostNo(String IP, String showNo) {
        this.IP = IP;
        this.showNo = showNo;
    }

    public HostNo(String IP, String showNo, String remark) {
        this.IP = IP;
        this.showNo = showNo;
        this.remark = remark;
    }

    @Generated(hash = 1360339581)
    public HostNo(Long Id, String IP, String showNo, String remark) {
        this.Id = Id;
        this.IP = IP;
        this.showNo = showNo;
        this.remark = remark;
    }

    @Generated(hash = 212175313)
    public HostNo() {
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getIP() {
        return this.IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShowNo() {
        return TextUtils.isEmpty(this.showNo) ? "" : this.showNo;
    }

    public void setShowNo(String showNo) {
        this.showNo = showNo;
    }
}
