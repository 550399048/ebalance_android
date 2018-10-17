package com.eui.ebalance.business.home.dynamic.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eui.ebalance.R;
import com.eui.ebalance.business.home.dynamic.model.Entity;
import com.eui.library.base.XFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static java.security.AccessController.getContext;


public class HomeDynamicFragment extends XFragment {

    @BindView(R.id.home_dynamic_title)
    TextView mTvTitle;
    @BindView(R.id.home_dynamic_user)
    ImageButton mIbUser;
    @BindView(R.id.home_dynamic_shared)
    ImageButton mIbShared;
    @BindView(R.id.home_dynamic_recyclerview)
    RecyclerView mRecyclerView;
    private List<Entity> datas;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        HomeDynamicRecyclerViewAdapter adapter = new HomeDynamicRecyclerViewAdapter();
        adapter.setDatas(datas);
        adapter.setHomeDynamicItemListener(new HomeDynamicRecyclerViewAdapter.HomeDynamicItemListener() {
            @Override
            public void onItemClick(View view, Entity entity) {
                Toast.makeText(getContext(), "" + entity.getName() + "click...", Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.home_dynamic_title)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_dynamic_title:
                Toast.makeText(getContext(), "点击开始连接", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        datas = new ArrayList<>();
        datas.add(new Entity(R.mipmap.ic_launcher_round, "BMI", "21.0", "正常"));
        datas.add(new Entity(R.mipmap.ic_launcher_round, "脂肪", "21.6%", "正常"));
        datas.add(new Entity(R.mipmap.ic_launcher_round, "肌肉", "48.5%", "正常"));
        datas.add(new Entity(R.mipmap.ic_launcher_round, "水分", "56.4%", "正常"));
        datas.add(new Entity(R.mipmap.ic_launcher_round, "蛋白质", "20%", "标准"));
        datas.add(new Entity(R.mipmap.ic_launcher_round, "内脏脂肪", "5", "标准"));
        datas.add(new Entity(R.mipmap.ic_launcher_round, "脂肪重量", "13.4KG", "正常"));
        datas.add(new Entity(R.mipmap.ic_launcher_round, "基础代谢率", "1129", "达标"));
        datas.add(new Entity(R.mipmap.ic_launcher_round, "身体年龄", "27", "年轻"));
        datas.add(new Entity(R.mipmap.ic_launcher_round, "其他", "--", "--"));

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_dynamic;
    }


    public class MyItemDecoration extends RecyclerView.ItemDecoration {

        /**
         * 复写onDraw方法，从而达到在每隔条目的被绘制之前（或之后），让他先帮我们画上去几根线吧
         *
         * @param c
         * @param parent
         * @param state
         */
        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            //先初始化一个Paint来简单指定一下Canvas的颜色，就黑的吧！
            Paint paint = new Paint();
            paint.setColor(parent.getContext().getResources().getColor(android.R.color.black));

            //获得RecyclerView中总条目数量
            int childCount = parent.getChildCount();

            //遍历一下
            for (int i = 0; i < childCount; i++) {
//                if (i == 0) {
//                    //如果是第一个条目，那么我们就不画边框了
//                    continue;
//                }
                //获得子View，也就是一个条目的View，准备给他画上边框
                View childView = parent.getChildAt(i);

                //先获得子View的长宽，以及在屏幕上的位置，方便我们得到边框的具体坐标
                float x = childView.getX();
                float y = childView.getY();
                int width = childView.getWidth();
                int height = childView.getHeight();

                //根据这些点画条目的四周的线
                c.drawLine(x, y, x + width, y, paint);
                c.drawLine(x, y, x, y + height, paint);
                c.drawLine(x + width, y, x + width, y + height, paint);
                c.drawLine(x, y + height, x + width, y + height, paint);

            }
            super.onDraw(c, parent, state);
        }
    }

}
