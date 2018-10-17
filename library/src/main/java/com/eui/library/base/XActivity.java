package com.eui.library.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eui.library.event.BusFactory;
import com.eui.library.kit.KnifeKit;

import butterknife.Unbinder;

/**
 * Created by edisonz on 17-11-15.
 */

public abstract class XActivity extends AppCompatActivity implements UiCallback{
    protected Activity context;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            unbinder = KnifeKit.bind(this);
        }
        setListener();
        initData(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            BusFactory.getBus().unregister(this);
        }
        if (unbinder != null) {
            KnifeKit.unbind(unbinder);
        }
    }

    protected  <T> T $(int resId) {
        return (T) findViewById(resId);
    }

    protected  <T> T $(View view, int resId) {
        return (T) view.findViewById(resId);
    }
}
