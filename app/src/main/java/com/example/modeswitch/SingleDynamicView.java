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
    }

    //重写父类方法
    public SingleHandView(Context context) { //在new的时候调用
        super(context);
        MYinit();
    }
    public SingleHandView(Context context, AttributeSet attr) { //在布局中使用(layout)
        super(context, attr);
        MYinit();
    }
    public SingleHandView(Context context, AttributeSet attr, int defStyleAttr) { //会在layout中使用，但会有style
        super(context, attr, defStyleAttr);
        MYinit();
    }


    private void MYinit() {
        System.out.println("动态");
    }
}
