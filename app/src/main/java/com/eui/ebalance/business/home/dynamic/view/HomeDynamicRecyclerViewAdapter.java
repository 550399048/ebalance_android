package com.eui.ebalance.business.home.dynamic.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eui.ebalance.R;
import com.eui.ebalance.business.home.dynamic.model.Entity;

import java.util.ArrayList;
import java.util.List;

public class HomeDynamicRecyclerViewAdapter extends RecyclerView.Adapter<HomeDynamicRecyclerViewAdapter.ViewHolder> {

    private List<Entity> mValues = new ArrayList<>();
    private HomeDynamicItemListener homeDynamicItemListener;

    public void setDatas(List<Entity> datas) {
        this.mValues = datas;
    }

    public void setHomeDynamicItemListener(HomeDynamicItemListener homeDynamicItemListener) {
        this.homeDynamicItemListener = homeDynamicItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dynamic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Entity data = mValues.get(position);
        holder.mIvIcon.setImageResource(data.getIcon());
        holder.mTvName.setText(data.getName() + "(" + data.getValue() + ")");
        holder.mTvType.setText(data.getType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeDynamicItemListener != null) {
                    homeDynamicItemListener.onItemClick(v, data);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvIcon;
        public TextView mTvName;
        public TextView mTvType;


        public ViewHolder(View view) {
            super(view);
            mIvIcon = (ImageView) view.findViewById(R.id.fragment_item_icon);
            mTvName = (TextView) view.findViewById(R.id.fragment_item_name_value);
            mTvType = (TextView) view.findViewById(R.id.fragment_item_type);
        }
    }

    public interface HomeDynamicItemListener {
        void onItemClick(View view, Entity entity);
    }

}
