package com.eui.ebalance.business.history.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eui.ebalance.R;
import com.eui.ebalance.business.history.model.DataDetailEntity;

import java.util.List;

public class DataDetailRecyclerViewAdapter extends RecyclerView.Adapter<DataDetailRecyclerViewAdapter.ViewHolder> {

    private List<DataDetailEntity> mValues;
    private Context mContext;

    public DataDetailRecyclerViewAdapter(Context context, List<DataDetailEntity> datas) {
        this.mContext = context;
        this.mValues = datas;
    }

    public void setDatas(List<DataDetailEntity> datas) {
        this.mValues = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DataDetailEntity data = mValues.get(position);
        holder.mIvIcon.setImageResource(data.getIcon());
        holder.mTvDate.setText(data.getDate());
        if (getPositionForDate(data.getDate()) == position) {
            holder.mTvDate.setVisibility(View.VISIBLE);
        } else {
            holder.mTvDate.setVisibility(View.GONE);
        }
        holder.mTvTime.setText(data.getTime());
        if (data.getEntities() != null) {
            holder.mTvParamName1.setText(data.getEntities().get(0).getName());
            holder.mTvParamValue1.setText(data.getEntities().get(0).getValue().toString());
            holder.mTvParamName2.setText(data.getEntities().get(1).getName());
            holder.mTvParamValue2.setText(data.getEntities().get(1).getValue().toString());
            holder.mTvParamType2.setText(data.getEntities().get(1).getType());
        }
        holder.mIvExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mIvExpand.getTag() == null) {
                    holder.mLinearLayout.setVisibility(View.VISIBLE);
                    holder.mIvExpand.setTag("expand");
                    holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    DataDetailExpandRecyclerViewAdapter adapter = new DataDetailExpandRecyclerViewAdapter(data.getEntities());
                    holder.mRecyclerView.setAdapter(adapter);
                } else {
                    holder.mLinearLayout.setVisibility(View.GONE);
                    holder.mIvExpand.setTag(null);
                }
            }
        });
    }

    public int getPositionForDate(String date) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mValues.get(i).getDate();
            if (date.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        }
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvDate;
        public ImageView mIvIcon;
        public TextView mTvTime;
        public TextView mTvParamName1;
        public TextView mTvParamValue1;
        public TextView mTvParamName2;
        public TextView mTvParamValue2;
        public TextView mTvParamType2;
        public ImageView mIvExpand;
        private LinearLayout mLinearLayout;
        public RecyclerView mRecyclerView;

        public ViewHolder(View view) {
            super(view);
            mTvDate = $(view, R.id.data_detail_item_date);
            mIvIcon = $(view, R.id.data_detail_item_icon);
            mTvTime = $(view, R.id.data_detail_item_time);
            mTvParamName1 = $(view, R.id.data_detail_item_param_name_1);
            mTvParamValue1 = $(view, R.id.data_detail_item_param_value_1);
            mTvParamName2 = $(view, R.id.data_detail_item_param_value_2);
            mTvParamValue2 = $(view, R.id.data_detail_item_param_value_2);
            mTvParamType2 = $(view, R.id.data_detail_item_param_type_2);
            mIvExpand = $(view, R.id.data_detail_item_expand);
            mLinearLayout = $(view, R.id.data_detail_item_expand_layout);
            mRecyclerView = $(view, R.id.data_detail_item_expand_recyclerView);
        }
    }

    private <T> T $(View view, int id) {
        return (T) view.findViewById(id);
    }

}
