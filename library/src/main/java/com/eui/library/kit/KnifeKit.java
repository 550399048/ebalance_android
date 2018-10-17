package com.eui.library.kit;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class KnifeKit {

    static {
        ButterKnife.setDebug(true);
    }
    public static Unbinder bind(Object target) {
        if (target instanceof Activity) {
            return ButterKnife.bind((Activity) target);
        }else if (target instanceof Dialog) {
            return ButterKnife.bind((Dialog) target);
        }else if (target instanceof View) {
            return ButterKnife.bind((View) target);
        }
        return Unbinder.EMPTY;
    }

    public static Unbinder bind(Object target, Object source) {
        if (source instanceof Activity) {
            ButterKnife.bind(target,(Activity) source);
        }else if (source instanceof Dialog) {
            ButterKnife.bind(target, (Dialog) source);
        }else if (source instanceof View) {
            ButterKnife.bind(target, (View) source);
        }
        return Unbinder.EMPTY;
    }

    public static void unbind(Unbinder unbinder){
        if (unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }
    }
}
