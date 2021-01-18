package com.queue.client.entry;

public class TjInfo {

    /**
     * tag : C0FF24
     * value : {"tjAll":13,"tjYk":4,"tjWk":9}
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

    public static class ValueBean {
        /**
         * tjAll : 13
         * tjYk : 4
         * tjWk : 9
         */

        private int tjAll;
        private int tjYk;
        private int tjWk;

        public int getTjAll() {
            return tjAll;
        }

        public void setTjAll(int tjAll) {
            this.tjAll = tjAll;
        }

        public int getTjYk() {
            return tjYk;
        }

        public void setTjYk(int tjYk) {
            this.tjYk = tjYk;
        }

        public int getTjWk() {
            return tjWk;
        }

        public void setTjWk(int tjWk) {
            this.tjWk = tjWk;
        }
    }
}
