package com.example.modeswitch;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/*
    user:王久铭
    date:2022-01-18
    purpose:用于解决切换选择会导致将之前的线条覆盖的问题
 */

public class PathInf {

    public Path path; //画线的路径
    public Paint paint; //画笔

    public PathInf () {

        path = new Path();
        paint = new Paint();

        paint.setAntiAlias(true); //抗锯齿
        paint.setColor(Color.BLACK); //画笔颜色
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE); //画笔风格，这里仅是画出边界，不填充
        paint.setStrokeJoin(Paint.Join.ROUND); //结合处为圆角
        paint.setStrokeCap(Paint.Cap.ROUND); //设置转弯处为圆角
        paint.setTextSize(36); //绘制文字大小，单位px
        paint.setStrokeWidth(2); //画笔粗细
    }

}
