package com.eui.ebalance.business.home.tendency.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eui.ebalance.R;
import com.eui.ebalance.business.home.tendency.model.ChangeDataEntity;

import java.util.ArrayList;
import java.util.List;

public class HomeTendencyRecyclerViewAdapter extends RecyclerView.Adapter<HomeTendencyRecyclerViewAdapter.ViewHolder> {

    private List<ChangeDataEntity> mValues = new ArrayList<>();

    public void setDatas(List<ChangeDataEntity> datas) {
        this.mValues = datas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tendency_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ChangeDataEntity data = mValues.get(position);
        holder.mTvName.setText(data.getName() + "(" + data.getUnit() + ")");
        holder.mTvValue.setText(data.getValue());
    }


    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvName;
        public TextView mTvValue;


        public ViewHolder(View view) {
            super(view);
            mTvName = (TextView) view.findViewById(R.id.fragment_tendency_item_name_unit);
            mTvValue = (TextView) view.findViewById(R.id.fragment_tendency_item_value);
        }
    }

    public interface HomeTendencyItemListener {
        void onItemClick(View view, ChangeDataEntity entity);
    }
}
