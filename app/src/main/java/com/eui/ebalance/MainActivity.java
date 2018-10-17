package com.eui.ebalance;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.eui.ebalance.business.home.dynamic.view.HomeDynamicFragment;
import com.eui.ebalance.business.home.mime.view.HomeMineFragment;
import com.eui.ebalance.business.home.tendency.view.HomeTendencyFragment;
import com.eui.ebalance.widget.LeFragmentTabHost;
import com.eui.ebalance.widget.LeTabWidget;
import com.eui.library.base.XActivity;

import butterknife.BindView;

public class MainActivity extends XActivity {

    private static final String TAG = "MainActivity";

    public static final String TAG_FRAGMENT_LATEST = "latest";
    public static final String TAG_FRAGMENT_TREND = "trend";
    public static final String TAG_FRAGMENT_MINE = "mine";

    @BindView(android.R.id.tabhost)
    LeFragmentTabHost mTabHost;
    @BindView(R.id.main_realtabcontent)
    FrameLayout mRealTabContent;
    LeTabWidget mTabWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();
    }

    private void findViews() {
        mTabHost = (LeFragmentTabHost) findViewById(android.R.id.tabhost);
        mRealTabContent = (FrameLayout) findViewById(R.id.main_realtabcontent);
        mTabWidget = (LeTabWidget) mTabHost.getTabWidget();
    }

    private void initViews() {
        mTabHost.setFragmentHiddenEnabled(true);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_realtabcontent, mRealTabContent);

        // removing divider
        mTabHost.getTabWidget().setDividerDrawable(null);


        View indicatorView;
        Bundle b;

        // 最新 页面
        b = new Bundle();
        b.putString("text", "gallery");
        indicatorView = mTabHost.createIndicatorView(R.drawable.ic_tab_like,
                getString(R.string.tab_latest));

        mTabHost.addTab(mTabHost.newTabSpec(TAG_FRAGMENT_LATEST).setIndicator(indicatorView),
                HomeDynamicFragment.class, b);

        // 趋势 页面
        b = new Bundle();
        b.putString("text", "gallery");
        indicatorView = mTabHost.createIndicatorView(R.drawable.ic_tab_like,
                getString(R.string.tab_trend));

        mTabHost.addTab(mTabHost.newTabSpec(TAG_FRAGMENT_TREND).setIndicator(indicatorView),
                HomeTendencyFragment.class, b);

        // 我的 页面
        b = new Bundle();
        b.putString("text", "gallery");
        indicatorView = mTabHost.createIndicatorView(R.drawable.ic_tab_like,
                getString(R.string.tab_mine));

        mTabHost.addTab(mTabHost.newTabSpec(TAG_FRAGMENT_MINE).setIndicator(indicatorView),
                HomeMineFragment.class, b);

        // 调整 LeTabWidget 的布局
        //LeTabWidgetUtils.setTabWidgetLayout(this, mTabWidget);
        mTabHost.setTabWidgetLayout(this, true);


    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }
}
