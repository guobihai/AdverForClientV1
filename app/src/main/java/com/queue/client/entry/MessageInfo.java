package com.queue.client.entry;

/**
 * Created by gbh on 2018/8/28  20:04.
 *
 * @describe 消息
 */

public class MessageInfo<T> {
    private String tag;
    private T value;

    public MessageInfo(String tag, T value) {
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
