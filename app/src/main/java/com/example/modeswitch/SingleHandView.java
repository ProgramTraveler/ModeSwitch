package com.example.modeswitch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/*
    user:王久铭
    date:2022-01-05
    purpose:单手主辅模式绘制界面
 */

public class SingleHandView extends View {
    private Paint paint; //绘制的画笔
    private int width = 0; //屏幕的宽度
    private int high = 0; //屏幕的高度
    private Path path; //记录用户绘制的Path
    private Canvas canvas; //内存中创建的Canvas
    private Bitmap bitmap; //缓存绘制的内容

    private float LastX;
    private float LastY;

    //刚进入环内的起点坐标
    private float StartX;
    private float StartY;
    private boolean index = false; //表示当前状态未进入环

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

    //对误差的判断
    private boolean error = false; //当状态变为true时说明是第二次遇到符合误差的状态
    private float errorNum = 10.0F;

    //重写父类方法
    public SingleHandView(Context context) { //在new的时候调用
        super(context);
        init();
    }
    public SingleHandView(Context context, AttributeSet attr) { //在布局中使用(layout)
        super(context, attr);
        init();
    }
    public SingleHandView(Context context, AttributeSet attr, int defStyleAttr) { //会在layout中使用，但会有style
        super(context, attr, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();

        paint = new Paint(); //初始化画笔
        paint.setAntiAlias(true); //抗锯齿
        paint.setColor(getResources().getColor(R.color.black)); //画笔颜色
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE); //画笔风格，这里仅是画出边界，不填充
        paint.setStrokeJoin(Paint.Join.ROUND); //结合处为圆角
        paint.setStrokeCap(Paint.Cap.ROUND); //设置转弯处为圆角
        paint.setTextSize(36); //绘制文字大小，单位px
        paint.setStrokeWidth(3); //画笔粗细
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int highMeasureSpec) {
        super.onMeasure(widthMeasureSpec, highMeasureSpec);
        //布局的宽高都是由该方法指定的
        //但指定快件的宽高需要测量
        width = MeasureSpec.getSize(widthMeasureSpec); //获取屏幕宽度
        high = MeasureSpec.getSize(highMeasureSpec); //获取屏幕高度

        //根据屏幕大小来设置大小半径
        SmallCircleR = high / 12;
        BigCircleR = high / 6;
        //根据屏幕大小来设置每个圆环的圆心位置
        Circle1_X = high / 6 * 3 / 2;
        Circle1_Y = width / 3;

        Circle2_X = high / 6 * 11 / 2;
        Circle2_Y = width / 3;

        Circle3_X = high / 6 * 10;
        Circle3_Y = width / 3;

        //width = getMeasuredWidth();
        //high = getMeasuredHeight();
        //初始化bitmap和canvas
        bitmap = Bitmap.createBitmap(width, high, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    //重写该方法，在这里绘图
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制第一个环
        canvas.drawCircle(Circle1_X, Circle1_Y, BigCircleR, paint);
        canvas.drawCircle(Circle1_X, Circle1_Y, SmallCircleR, paint);

        if (ring2) { //显示第二个环
            canvas.drawCircle(Circle2_X, Circle2_Y, BigCircleR, paint);
            canvas.drawCircle(Circle2_X, Circle2_Y, SmallCircleR, paint);
        }
        if (ring3) { //显示第三个环
            canvas.drawCircle(Circle3_X, Circle3_Y, BigCircleR, paint);
            canvas.drawCircle(Circle3_X, Circle3_Y, SmallCircleR, paint);
        }
        drawPath();
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
    //绘制线条
    private void drawPath() {
        canvas.drawPath(path, paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        if (ring1 && !ring2) { //当前只显示第一个圆环
            //求出当前位置与圆心坐标差值的平方
            double xr = Math.pow((x - Circle1_X), 2);
            double yr = Math.pow((y - Circle1_Y), 2);
            double ra = xr + yr;

            if (ra >= Math.pow((SmallCircleR), 2) && ra <= Math.pow((BigCircleR), 2)) { //如果落在圆环内

                if (!index) { //如果之前没有进入过
                    index = true; //表示已经进入
                    //初次进入的坐标
                    StartX = x;
                    StartY = y;
                    //System.out.println("come in" + " " + x + " " + y);
                }
                else { //已经进入，这个用来检测是否是一个闭环

                    if (!error && (Math.abs(x - StartX) > SmallCircleR * 2)) {
                        error = true;
                    }

                    if ((Math.abs(x - StartX) <= errorNum) && (Math.abs(y - StartY) <= errorNum) && error) {
                        ring2 = true; //当误差满足条件的时候就当做是一个闭环
                        //重新初始化条件
                        index = false;
                        error = false;
                    }
                }
            }else { //要是画出环的处理，暂时还没有想法

            }

        }
        if (ring2 && !ring3) { //第二个圆环已经显示，与环1的判断一样
            //System.out.println("bingo");
            double xr = Math.pow((x - Circle2_X), 2);
            double yr = Math.pow((y - Circle2_Y), 2);
            double ra = xr + yr;

            if (ra >= Math.pow((SmallCircleR), 2) && ra <= Math.pow((BigCircleR), 2)) { //如果落在圆环内

                if (!index) { //如果之前没有进入过
                    index = true; //表示已经进入
                    //初次进入的坐标
                    StartX = x;
                    StartY = y;
                    //System.out.println("come in" + " " + x + " " + y);
                }
                else { //已经进入，这个用来检测是否是一个闭环

                    if (!error && (Math.abs(x - StartX) > SmallCircleR * 2)) {
                        error = true;
                    }
                    if ((Math.abs(x - StartX) <= errorNum) && (Math.abs(y - StartY) <= errorNum) && error) {
                        System.out.println("come in");
                        ring3 = true; //当误差满足条件的时候就当做是一个闭环

                    }
                }
            }else { //要是画出环的处理，暂时还没有想法

            }
        }
        //获取坐标进行画线
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                LastX = x;
                LastY = y;
                path.moveTo(LastX, LastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - LastX);
                float dy = Math.abs(y - LastY);
                if (dx > 3 || dy > 3)
                    path.lineTo(x, y);
                LastX = x;
                LastY = y;
                break;
        }
        invalidate();
        return true;
    }
}
