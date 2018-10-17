package com.eui.ebalance.business.history.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eui.ebalance.R;
import com.eui.ebalance.business.home.dynamic.model.Entity;

import java.util.List;

public class DataDetailExpandRecyclerViewAdapter extends RecyclerView.Adapter<DataDetailExpandRecyclerViewAdapter.ViewHolder> {

    private List<Entity> mValues;

    public DataDetailExpandRecyclerViewAdapter(List<Entity> datas) {
        this.mValues = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_detail_expand_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Entity data = mValues.get(position);
        holder.mIvIcon.setImageResource(data.getIcon());
        holder.mTvName.setText(data.getName());
        holder.mTvValue.setText(data.getValue().toString());
        holder.mTvType.setText(data.getType());
    }

    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        }
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvIcon;
        public TextView mTvName;
        public TextView mTvValue;
        public TextView mTvType;

        public ViewHolder(View view) {
            super(view);
            mIvIcon = $(view, R.id.data_detail_expand_item_icon);
            mTvName = $(view, R.id.data_detail_expand_item_name);
            mTvValue = $(view, R.id.data_detail_expand_item_value);
            mTvType = $(view, R.id.data_detail_expand_item_type);
        }
    }

    private <T> T $(View view, int id) {
        return (T) view.findViewById(id);
    }

}
