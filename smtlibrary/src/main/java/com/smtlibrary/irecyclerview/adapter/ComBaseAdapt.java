package com.smtlibrary.irecyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gbh on 17/4/20.
 * 通用适配器
 */

public abstract class ComBaseAdapt<T> extends BaseAdapter implements DataIO<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas = new ArrayList<>();
    protected LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;


    public ComBaseAdapt(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    public ComBaseAdapt(Context context, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        convert(viewHolder, mDatas.get(position),position);
        return viewHolder.getConvertView();
    }

    @Override
    public void add(T t) {
        mDatas.add(t);
        notifyDataSetChanged();
    }

    @Override
    public void addAt(int i, T t) {
        mDatas.add(i, t);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<T> list) {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void addAllAt(int i, List<T> list) {
        mDatas.addAll(i, list);
        notifyDataSetChanged();
    }

    @Override
    public void remove(T t) {
        mDatas.remove(t);
        notifyDataSetChanged();
    }

    @Override
    public void removeAll(List<T> list) {
        mDatas.removeAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void removeAt(int i) {
        mDatas.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        if (mDatas != null && mDatas.size() > 0) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void replace(T t, T t1) {
        replaceAt(mDatas.indexOf(t), t1);
    }

    @Override
    public void replaceAt(int i, T t) {
        mDatas.set(i, t);
        notifyDataSetChanged();
    }

    @Override
    public void replaceAll(List<T> list) {
        if (mDatas.size() > 0) {
            mDatas.clear();
        }
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public List<T> getAll() {
        return mDatas;
    }

    @Override
    public T get(int position) {
        if (position >= mDatas.size())
            return null;
        return mDatas.get(position);
    }

    @Override
    public int getSize() {
        return mDatas.size();
    }

    @Override
    public boolean contains(T t) {
        return mDatas.contains(t);
    }


    public abstract void convert(ViewHolder helper, T t, int position);


}
