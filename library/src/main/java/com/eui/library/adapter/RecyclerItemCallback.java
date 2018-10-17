package com.eui.library.adapter;

/**
 * Created by edisonz on 17-11-16.
 */

public abstract class RecyclerItemCallback<T, F> {
    public void onItemClick(int position, T model, int tag, F holder) {
    }
    public void onItemLongClick(int position, T model, int tag, F holder) {
    }
}
