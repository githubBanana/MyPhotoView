package com.zf.photoview.images;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

abstract class BAdapter<T> extends BaseAdapter implements Filterable {
    public Activity mAct;
    public LayoutInflater mInflater;
    public List<T> mListData;

    public BAdapter(Activity act) {
        this.mAct = act;
        this.init(this.mAct);
    }

    private void init(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mListData = new ArrayList();
    }

    public void addListData(T t, boolean isNotify) {
        this.mListData.add(t);
        if(isNotify) {
            this.notifyDataSetChanged();
        }

    }

    public void addListData(int location, T t, boolean isNotify) {
        this.mListData.add(location, t);
        if(isNotify) {
            this.notifyDataSetChanged();
        }

    }

    public void addListData(T t, boolean isNotify, int position) {
        this.mListData.add(position, t);
        if(isNotify) {
            this.notifyDataSetChanged();
        }

    }

    public void setListData(Collection<T> c) {
        if(this.mListData != null) {
            this.mListData.clear();
            this.mListData.addAll(c);
            this.notifyDataSetChanged();
        }

    }

    public void setListData(Iterator<T> iterator) {
        if(this.mListData != null) {
            this.mListData.clear();

            while(iterator.hasNext()) {
                this.mListData.add(iterator.next());
            }

            this.notifyDataSetChanged();
        }

    }

    public void addListData(Collection<T> c) {
        if(c != null) {
            this.mListData.addAll(c);
            this.notifyDataSetChanged();
        }

    }

    public void addListData(int location, Collection<T> c) {
        if(c != null) {
            this.mListData.addAll(location, c);
            this.notifyDataSetChanged();
        }

    }

    public void clearListData() {
        this.mListData.clear();
        this.notifyDataSetChanged();
    }

    public void removeListData(int location) {
        this.mListData.remove(location);
        this.notifyDataSetChanged();
    }

    public void removeListData(T t) {
        this.mListData.remove(t);
        this.notifyDataSetChanged();
    }

    public void setChangeData(int position, T t) {
        this.mListData.set(position, t);
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return this.mListData.size();
    }

    public T getItem(int position) {
        return this.mListData.get(position);
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public abstract View getView(int var1, View var2, ViewGroup var3);

    public Filter getFilter() {
        return null;
    }

    public abstract static class ViewHolder<T> {
        View mView;

        public ViewHolder(View view) {
            this.mView = view;
        }

        public void setData(int position, T t) {
        }
    }
}

