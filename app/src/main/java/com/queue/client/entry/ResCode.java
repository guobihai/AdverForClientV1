package com.queue.client.entry;

import android.text.TextUtils;

public class ResCode {

    /**
     * tag : C0FF02
     * value : {"msg":"NO","code":"0000"}
     */

    private String tag;
    private ValueBean value;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ValueBean getValue() {
        return value;
    }

    public void setValue(ValueBean value) {
        this.value = value;
    }

    public boolean isSuccess(){
        if(null == value)return false;
        if(TextUtils.isEmpty(value.code))return false;
        if (value.code.equals("0000"))return true;
        return false;
    }

    public static class ValueBean {
        /**
         * msg : NO
         * code : 0000
         */

        private String msg;
        private String code;
        private String time;
        private String righttime;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getRighttime() {
            return righttime;
        }

        public void setRighttime(String righttime) {
            this.righttime = righttime;
        }
    }
}
