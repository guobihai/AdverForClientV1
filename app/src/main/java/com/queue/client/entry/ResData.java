package com.queue.client.entry;

/**
 * Created by gbh on 2018/8/30  13:33.
 *
 * @describe
 */

public class ResData {


    /**
     * tag : C0FF01
     * value : {"tag":"33333"}
     */

    private String tag;
    private ValueBean value;

    public ResData() {
    }

    public ResData(ValueBean value) {
        this.value = value;
    }

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

    public static class ValueBean {

        public ValueBean(String tag, String value) {
            this.tag = tag;
            this.value = value;
        }

        public ValueBean(String tag, String value, int type) {
            this.tag = tag;
            this.value = value;
            this.type = type;
        }

        private String tag;
        private String value;
        private int type;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
