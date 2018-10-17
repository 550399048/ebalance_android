package com.eui.ebalance.business.home.tendency.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eui.ebalance.R;
import com.eui.ebalance.business.history.view.DataDetailActivity;
import com.eui.ebalance.business.home.tendency.model.ChangeDataEntity;
import com.eui.library.base.XFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class HomeTendencyFragment extends XFragment implements View.OnClickListener {

    @BindView(R.id.home_tendency_week)
    TextView mTvWeek;
    @BindView(R.id.home_tendency_month)
    TextView mTvMonth;
    @BindView(R.id.home_tendency_3_month)
    TextView mTv3Month;
    @BindView(R.id.home_tendency_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.home_tendency_lineCharView)
    LineChartView mLineChartView;
    @BindView(R.id.home_tendency_enter_detail)
    ImageView mIvEnterDetail;
    private List<ChangeDataEntity> changeDataEntities;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerViews();
        initLineChartView();
    }

    private void initLineChartView() {
        mLineChartView.setOnValueTouchListener(new ValueTouchListener());
        // Generate some random values.
        generateValues();

        generateData();

        // Disable viewport recalculations, see toggleCubic() method for more info.
        mLineChartView.setViewportCalculationEnabled(false);

        resetViewport();
    }
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    private boolean hasGradientToTransparent = false;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
    private void generateValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }
    }
    private LineChartData data;
    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
//            line.setHasGradientToTransparent(hasGradientToTransparent);
            if (pointsHaveDifferentColor){
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        mLineChartView.setLineChartData(data);

    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(mLineChartView.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        mLineChartView.setMaximumViewport(v);
        mLineChartView.setCurrentViewport(v);
    }

    private void initDatas() {

    }

    private void initRecyclerViews() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        HomeTendencyRecyclerViewAdapter adapter = new HomeTendencyRecyclerViewAdapter();
        adapter.setDatas(changeDataEntities);
        mRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.home_tendency_enter_detail)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_tendency_enter_detail:
                Toast.makeText(getContext(), "data detail...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), DataDetailActivity.class));
                break;
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        changeDataEntities = new ArrayList<>();
        changeDataEntities.add(new ChangeDataEntity("称重记录", "2", "天"));
        changeDataEntities.add(new ChangeDataEntity("体重变化", "-0.2", "%"));
        changeDataEntities.add(new ChangeDataEntity("脂肪变化", "-1.3", "%"));
        changeDataEntities.add(new ChangeDataEntity("肌肉变化", "+0.9", "%"));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_tendency;
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }

    }
}
