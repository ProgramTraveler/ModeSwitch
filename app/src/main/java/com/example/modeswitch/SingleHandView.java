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

    private int LastX;
    private int LastY;

    //刚进入环内的起点坐标
    private int StartX;
    private int StartY;
    private boolean index = false; //表示当前状态未进入环

    //每个操作环的状态，true表示已经出现
    private boolean ring1 = true; //第一个环是一个出现的，所以只能是true
    private boolean ring2 = false;
    private boolean ring3 = false;

    //对误差的判断
    private boolean error = false; //当状态变为true时说明是第二次遇到符合误差的状态


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
        canvas.drawCircle(high / 6 * 3 / 2, width / 3, high / 6, paint);
        canvas.drawCircle(high / 6 * 3 / 2, width / 3, high / 12, paint);

        if (ring2) { //显示第二个环
            canvas.drawCircle(high / 6 * 11 / 2, width / 3, high / 6, paint);
            canvas.drawCircle(high / 6 * 11 / 2, width / 3, high / 12, paint);
        }

        if (ring3) { //显示第三个环
            canvas.drawCircle(high / 6 * 10, width / 3, high / 6, paint);
            canvas.drawCircle(high / 6 * 10, width / 3, high / 12, paint);
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
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (ring1 && !ring2) { //当前只显示第一个圆环
            boolean indexRing1 = false;
            int xr = (int) Math.pow((x - high / 6), 2);
            int yr = (int) Math.pow((y - width / 3), 2);
            int ra = xr + yr;
            System.out.println(index);
            System.out.println("开始位置" + StartX + " " + StartY);
            System.out.println("当前位置" + x + " " + y);
            if (ra >= Math.pow((high / 12), 2) && ra <= Math.pow((high / 6), 2)) { //如果落在圆环内

                /*System.out.println("开始位置" + StartX + " " + StartY);
                System.out.println("当前位置" + x + " " + y) ;*/

                if (!index) { //如果之前没有进入过
                    index = true; //表示已经进入
                    //初次进入的坐标
                    StartX = x;
                    StartY = y;
                    //System.out.println("come in" + " " + x + " " + y);
                }
                else { //已经进入，这个用来检测是否是一个闭环
                    /*System.out.println("开始位置" + StartX + " " + StartY);
                    System.out.println("当前位置" + x + " " + y) ;*/
                    if (!error && (Math.abs(x - StartX) > high / 3)) {
                        error = true;
                    }
                    //System.out.println(error);
                    if ((Math.abs(x - StartX) <= 10) && (Math.abs(y - StartY) <= 10) && error) {
                        ring2 = true; //当误差小于10的时候就当做是一个闭环
                    }
                }
            }else { //要是画出环的处理，暂时还没有想法

            }

        }
        //获取坐标进行画圆
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                LastX = x;
                LastY = y;
                path.moveTo(LastX, LastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = Math.abs(x - LastX);
                int dy = Math.abs(y - LastY);
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
