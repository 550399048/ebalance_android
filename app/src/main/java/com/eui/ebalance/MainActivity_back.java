package com.eui.ebalance;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eui.ebalance.business.home.dynamic.view.HomeDynamicFragment;
import com.eui.ebalance.business.home.mime.view.HomeMineFragment;
import com.eui.ebalance.business.home.tendency.view.HomeTendencyFragment;
import com.eui.ebalance.widget.NoScrollViewPager;
import com.eui.library.base.XActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity_back extends XActivity {

    @BindView(R.id.home_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.home_viewpager)
    NoScrollViewPager mViewPager;

    private List<Fragment> mFragmentLists;
    private MainPagerAdapter adapter;
    private String[] titles = {"动态", "趋势", "我的"};
    private int images[] = {R.drawable.home_dynamic_selector, R.drawable.home_tendency_selector, R.drawable.home_mine_selector};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_back);
        this.initViewPager();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //页面，数据源
        mFragmentLists = new ArrayList<>();
        mFragmentLists.add(new HomeDynamicFragment());
        mFragmentLists.add(new HomeTendencyFragment());
        mFragmentLists.add(new HomeMineFragment());
    }

    private void initViewPager() {
        //ViewPager的适配器
        adapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setNoScroll(true);
        mViewPager.setAdapter(adapter);
        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //设置自定义视图
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    class MainPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        public MainPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentLists.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentLists.size();
        }

        /**
         * 自定义方法，提供自定义Tab
         *
         * @param position 位置
         * @return 返回Tab的View
         */
        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.tab_custom, null);
            TextView textView = (TextView) v.findViewById(R.id.home_tabelayout_title);
            ImageView imageView = (ImageView) v.findViewById(R.id.home_tabelayout_icon);
            textView.setText(titles[position]);
            imageView.setImageResource(images[position]);
            //添加一行，设置颜色
            textView.setTextColor(mTabLayout.getTabTextColors());//
            return v;
        }
    }
}
