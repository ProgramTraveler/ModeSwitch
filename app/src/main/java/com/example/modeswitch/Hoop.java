package com.example.modeswitch;
/*
    user:王久铭
    date:2022-01-23
    purpose:环的相关信息，主要是控制环的一些动作和相关参数
 */
public class Hoop {
    //每个操作环的状态，true表示已经出现
    private boolean ring1 = true; //第一个环是一个出现的，所以只能是true
    private boolean ring2 = false;
    private boolean ring3 = false;


    //圆环中大小圆的半径
    private float SmallCircleR = 0;
    private float BigCircleR = 0;
    //第一个圆环的圆心坐标
    private float Circle1_X = 0;
    private float Circle1_Y = 0;
    //第二个圆环的圆心坐标
    private float Circle2_X = 0;
    private float Circle2_Y = 0;
    //第三个圆环的圆心坐标
    private float Circle3_X = 0;
    private float Circle3_Y = 0;

    /*
        圆环的时间记录
            boolean 用来约束第一次落下的时间和抬起的时间
     */
    //第一个圆环的时间变量
    private boolean ring1_start = false;
    //第二个圆环的时间变量
    private boolean ring2_start = false;
    //第三个圆环的时间变量
    private boolean ring3_start = false;

    public Hoop() {}

    //设置和获取环一的状态
    public void setRing_1 (boolean b) {
        ring1 = b;
    }
    public boolean getRing_1 () {
        return  ring1;
    }
    public void set_Ring_1_start (boolean b) {ring1_start = b;}
    public boolean get_Ring_1_start () {
        return ring1_start;
    }
    //设置和获取环二的状态
    public void setRing_2 (boolean b) {
        ring2 = b;
    }
    public boolean getRing_2 () {
        return ring2;
    }
    public void set_Ring_2_start (boolean b) {ring2_start = b;}
    public boolean get_Ring_2_start () {
        return ring2_start;
    }
    //设置和获取环三的状态
    public void setRing_3 (boolean b) {
        ring3 = b;
    }
    public boolean getRing_3 () {
        return ring3;
    }
    public void set_Ring_3_start (boolean b) {ring3_start = b;}
    public boolean get_Ring_3_start () {
        return ring3_start;
    }
    //设置和获取大小圆半径
    public void setBigCircleR (float f) {
        BigCircleR = f;
    }
    public float getBigCircleR () {
        return BigCircleR;
    }
    public void setSmallCircleR (float f) {
        SmallCircleR = f;
    }
    public float getSmallCircleR () {
        return SmallCircleR;
    }
    //设置每个圆的圆心坐标
    public void setCircle (int n, float x, float y) {
        if (n == 1) { //如果是环一
            Circle1_X = x;
            Circle1_Y = y;
        }

        if (n == 2) { //如果是环二
            Circle2_X = x;
            Circle2_Y = y;
        }

        if (n == 3) { //如果是环三
            Circle3_X = x;
            Circle3_Y = y;
        }
    }
    //获取每个圆的圆心x方向的位置
    public float getCircle_X (int n) {
        if (n == 1) {
            return Circle1_X;
        }
        if (n == 2) {
            return Circle2_X;
        }
        if (n == 3) {
            return Circle3_X;
        }
        return 0;
    }
    //获取每个圆的圆心y方向的位置
    public float getCircle_Y (int n) {
        if (n == 1) {
            return Circle1_Y;
        }
        if (n == 2) {
            return Circle2_Y;
        }
        if (n == 3) {
            return Circle3_Y;
        }
        return  0;
    }
}
