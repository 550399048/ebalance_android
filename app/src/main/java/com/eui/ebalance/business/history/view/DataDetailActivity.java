package com.eui.ebalance.business.history.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.eui.ebalance.R;
import com.eui.ebalance.business.history.model.DataDetailEntity;
import com.eui.ebalance.business.home.dynamic.model.Entity;

import java.util.ArrayList;
import java.util.List;

public class DataDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ImageView mIvBack;
    private List<DataDetailEntity> entities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);
        initView();
        initDatas();
        initRecyclerView();
    }

    private void initView() {
        mRecyclerView = $(R.id.data_detail_recyclerView);
        mIvBack = $(R.id.data_detail_back);
        mIvBack.setOnClickListener(this);
    }

    private void initDatas() {
        entities = new ArrayList<>();
        entities.add(new DataDetailEntity(R.mipmap.ic_launcher_round, "11月10日", "16:02", getEntitys()));
        entities.add(new DataDetailEntity(R.mipmap.ic_launcher_round, "11月10日", "16:33", getEntitys()));
        entities.add(new DataDetailEntity(R.mipmap.ic_launcher_round, "11月10日", "16:53", getEntitys()));
        entities.add(new DataDetailEntity(R.mipmap.ic_launcher_round, "11月11日", "16:13", getEntitys()));
        entities.add(new DataDetailEntity(R.mipmap.ic_launcher_round, "11月12日", "16:13", getEntitys()));
        entities.add(new DataDetailEntity(R.mipmap.ic_launcher_round, "11月15日", "10:13", getEntitys()));
    }

    private List<Entity> getEntitys() {
        List<Entity> datas = new ArrayList<>();
        datas.add(new Entity(R.mipmap.ic_launcher_round, "体重", "60", "标准"));
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
        return datas;
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DataDetailRecyclerViewAdapter adapter = new DataDetailRecyclerViewAdapter(this, entities);
        mRecyclerView.setAdapter(adapter);
    }

    private <T> T $(int resId) {
        return (T) findViewById(resId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.data_detail_back:
                finish();
                break;
        }
    }
}
