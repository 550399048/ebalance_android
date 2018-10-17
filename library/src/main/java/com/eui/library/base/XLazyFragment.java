package com.eui.library.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eui.library.event.BusFactory;
import com.eui.library.kit.KnifeKit;

import butterknife.Unbinder;
import cn.droidlover.xdroidbase.base.LazyFragment;

public abstract class XLazyFragment extends LazyFragment implements UiCallback{
    protected View rootView;
    protected Activity context;
    protected Unbinder unbinder;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            unbinder = KnifeKit.bind(this, getRealRootView());
        }
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }
        setListener();
        initData(savedInstanceState);
    }

    @Override
    protected void onDestoryLazy() {
        super.onDestoryLazy();
        if (useEventBus()) {
            BusFactory.getBus().unregister(this);
        }
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void setListener() {

    }
}
