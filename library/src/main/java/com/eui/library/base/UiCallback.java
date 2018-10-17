package com.eui.library.base;

import android.os.Bundle;

/**
 * 为了Activity & Fragment 保持一致,抽取为接口UiCallback
 */

public interface UiCallback {
    void initData(Bundle savedInstanceState);
    void setListener();
    int getLayoutId();
    boolean useEventBus();//是否使用eventbus
}
