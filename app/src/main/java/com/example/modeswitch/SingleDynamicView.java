package com.example.modeswitch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/*
    user:王久铭
    date:2022-01-24
    purpose:单手动态模式的绘制界面
 */

public class SingleDynamicView extends View {

    public SingleDynamicView(Context context) {
        super(context);
        init();
        System.out.println("1");
    }
    public SingleDynamicView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        System.out.println("2");
        init();
    }
    public SingleDynamicView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        System.out.println("3");
        init();
    }

    private void init() {
        System.out.println("动态");
    }
}
