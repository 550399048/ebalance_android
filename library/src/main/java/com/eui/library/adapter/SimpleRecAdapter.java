package com.eui.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eui.library.adapter.RecyclerAdapter;

/**
 * Created by edisonz on 17-11-16.
 */

public abstract class SimpleRecAdapter<T, F extends RecyclerView.ViewHolder> extends RecyclerAdapter<T,F> {
    public SimpleRecAdapter(Context context) {
        super(context);
    }

    @Override
    public F onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(),parent,false);
        return newViewHolder(view);
    }

    protected abstract F newViewHolder(View itemView);

    public abstract int getLayoutId();
}
