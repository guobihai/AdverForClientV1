package com.queue.client.entry;

import java.util.List;

/**
 * Created by gbh on 2018/8/30  21:12.
 *
 * @describe
 */

public class OrderInfo {

    /**
     * tag : C0FF04
     * value : [{"ID":1,"time":"20180902","fullTime":"20180902151256","name":"张三46","orderNo":2,"setOrderNo":0,"showOrderNo":801,"age":0,"type":0},{"ID":2,"time":"20180902","fullTime":"20180902151300","name":"张三76","orderNo":2,"setOrderNo":0,"showOrderNo":802,"age":0,"type":0}]
     */

    private String tag;
    private List<ValueBean> value;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<ValueBean> getValue() {
        return value;
    }

    public void setValue(List<ValueBean> value) {
        this.value = value;
    }

    public static class ValueBean {
        /**
         * ID : 1
         * time : 20180902
         * fullTime : 20180902151256
         * name : 张三46
         * orderNo : 2
         * setOrderNo : 0
         * showOrderNo : 801
         * age : 0
         * type : 0
         */

        private int ID;
        private String time;
        private String fullTime;
        private String name;
        private int orderNo;
        private int setOrderNo;
        private int showOrderNo;
        private int age;
        private int type;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFullTime() {
            return fullTime;
        }

        public void setFullTime(String fullTime) {
            this.fullTime = fullTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public int getSetOrderNo() {
            return setOrderNo;
        }

        public void setSetOrderNo(int setOrderNo) {
            this.setOrderNo = setOrderNo;
        }

        public int getShowOrderNo() {
            return showOrderNo;
        }

        public void setShowOrderNo(int showOrderNo) {
            this.showOrderNo = showOrderNo;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
