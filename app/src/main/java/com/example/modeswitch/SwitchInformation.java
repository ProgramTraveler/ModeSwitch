package com.example.modeswitch;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*
    user:王久铭
    date:2022-03-14
    purpose:切换信息和当前信息的显示
 */

public class SwitchInformation {
    String current_pixel = "1PX"; //当前像素
    String target_pixel = ""; //目标像素

    /*
        颜色的识别用数字作为识别条件，识别代码如下
        -1:白色  0:黑色  1:红色  2:黄色  3:蓝色
     */
    int current_color = 0;
    int target_color = -1; //目标初始为白色和背景一个颜色

    String current_color_inf = "当前颜色：";
    String current_pixel_inf = "当前像素：";

    String target_color_inf = "目标颜色：";
    String target_pixel_inf = "目标像素：";

    public SwitchInformation() {}

    public Paint getWordInf() {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
        return paint;
    }

    public String getCurrent_color_inf() {
        return current_color_inf;
    }

    public String getCurrent_pixel_inf() {
        return current_pixel_inf;
    }

    public String getTarget_color_inf() {
        return target_color_inf;
    }

    public String getTarget_pixel_inf() {
        return target_pixel_inf;
    }

    public void setCurrent_color(int n) {
        current_color = n;
    }
    public Paint getCurrent_color() { //当前颜色画笔
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        paint.setColor(Color.BLACK); //默认为黑色

        //根据当前不同的颜色情况设置不同的画笔颜色
        if (current_color == 1) {
            paint.setColor(Color.RED);
        }
        if (current_color == 2) {
            paint.setColor(Color.YELLOW);
        }
        if (current_color == 3) {
            paint.setColor(Color.BLUE);
        }
        return paint;
    }

    public void setCurrent_pixel(String s) {
        current_pixel = s;
    }
    public String getCurrent_pixel() {
        return current_pixel;
    }

    public void setTarget_color(int n) { target_color = n; }
    public Paint getTarget_color() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        paint.setColor(Color.WHITE); //默认为白色（背景色）

        if (target_color == 1) paint.setColor(Color.RED);
        if (target_color == 2) paint.setColor(Color.YELLOW);
        if (target_color == 3) paint.setColor(Color.BLUE);

        return paint;
    }

    public void setTarget_pixel(int  n) {
        if (n == 1) target_pixel = "4PX";
        if (n == 2) target_pixel = "8PX";
        if (n == 3) target_pixel = "16PX";
    }
    public String getTarget_pixel() { return target_pixel; }

}
