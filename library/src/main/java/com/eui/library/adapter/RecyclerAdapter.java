package com.eui.library.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by edisonz on 17-11-16.
 */

public abstract class RecyclerAdapter<T, F extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<F>{
    protected Context mContext;
    protected List<T> data = new ArrayList<>();
    private RecyclerItemCallback<T, F> recItemClick;

    public RecyclerAdapter(Context context){
        this.mContext = context;
    }
    public void setData(List<T> data) {
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }
    public void setData(T[] data) {
        if (data != null && data.length > 0) {
            setData(Arrays.asList(data));
        }
    }
    public void addData(List<T> data) {
        int preSize = this.data.size();
        if (data != null && data.size() > 0) {
            if (this.data == null) {
                this.data = new ArrayList<>();
            }
            this.data.addAll(data);
            notifyItemRangeInserted(preSize, this.data.size());
        }
        notifyDataSetChanged();
    }
    public void addData(T[] data) {
        addData(Arrays.asList(data));
    }
    public void addElement(T element) {
        if (element != null) {
            if (this.data == null) {
                this.data = new ArrayList<T>();
            }
            data.add(element);
            notifyItemInserted(this.data.size());
        }
    }

    public void addElement(int position, T element) {
        if (element != null) {
            if (this.data == null) {
                this.data = new ArrayList<T>();
            }
            data.add(position, element);
            notifyItemInserted(position);
        }
    }

    public void removeElement(T element) {
        if (data.contains(element)) {
            int position = data.indexOf(element);
            data.remove(element);
            notifyItemRemoved(position);
            notifyItemChanged(position);
        }
    }
    public void removeElement(int position) {
        if (data != null && data.size() > position) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position);
        }
    }
    public void removeElements(List<T> elements) {
        if (data != null && elements != null && elements.size() > 0
                && data.size() >= elements.size()) {

            for (T element : elements) {
                if (data.contains(element)) {
                    data.remove(element);
                }
            }

            notifyDataSetChanged();
        }
    }
    public void removeElements(T[] elements) {
        if (elements != null && elements.length > 0) {
            removeElements(Arrays.asList(elements));
        }
    }
    public void updateElement(T element, int position) {
        if (position >= 0 && data.size() > position) {
            data.remove(position);
            data.add(position, element);
            notifyItemChanged(position);
        }
    }
    public void clearData() {
        if (this.data != null) {
            this.data.clear();
            notifyDataSetChanged();
        }
    }
    protected void setVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }
    protected void setGone(View view) {
        view.setVisibility(View.GONE);
    }
    protected void setInvisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }
    protected void setEnable(View view) {
        view.setEnabled(true);
    }
    protected void setDisable(View view) {
        view.setEnabled(false);
    }
    protected Drawable getDrawable(int resId) {
        return mContext.getResources().getDrawable(resId);
    }
    protected String getString(int resId) {
        return mContext.getResources().getString(resId);
    }
    protected int getColor(int resId) {
        return mContext.getResources().getColor(resId);
    }
    public List<T> getDataSource() {
        return data;
    }
    public void setRecItemClick(RecyclerItemCallback<T, F> recItemClick) {
        this.recItemClick = recItemClick;
    }

    public RecyclerItemCallback<T, F> getRecItemClick() {
        return recItemClick;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public abstract F onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(F holder, int position);

    @Override
    public int getItemCount() {
        return data == null || data.isEmpty() ? 0 : data.size();
    }
}
