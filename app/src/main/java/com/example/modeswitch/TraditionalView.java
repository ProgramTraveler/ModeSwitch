package com.example.modeswitch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/*
    user:王久铭
    date:2022-03-22
    purpose:传统模式的绘制界面
 */

public class TraditionalView extends View {
    private ArrayList<PathInf> pathInfArrayList; //记录路径的信息
    private int PathInfNum = 0; //当前下标

    private Paint MyPaint; //绘制图形的画笔
    private Paint MyMenu; //绘制菜单的画笔

    private Hoop hoop; //环
    //进入环内的坐标
    private float StartX = 0;
    private float StartY = 0;
    private boolean index = false; //对入环状态的判断，false表示未入环

    private float LastX;
    private float LastY;

    //对误差的判断
    private boolean error = false; //当状态变为true时说明是第二次遇到符合误差的状态
    private float errorNum = 20.0F;

    private float width = 0; //屏幕的宽度
    private float high = 0; //屏幕的高度
    private Canvas canvas; //内存中创建的Canvas
    private Bitmap bitmap; //缓存绘制的内容

    /*
    一级菜单变量
     */
    private float MenuLen = 0; //菜单长度
    private float MenuWith = 0; //菜单宽度

    //一级菜单出现的位置
    private float Menu_X = 0;
    private float Menu_Y = 0;

    //一级菜单的中心位置
    private float center_x = 0;
    private float center_y = 0;



    private int colorNow = Color.BLACK;
    private int pixNow = 2;

    private SwitchInformation switchInformation = new SwitchInformation();

    private Coordinate coordinate;

    private RandomNumber randomNumber = new RandomNumber();

    private int tips = 0;

    private ExperimentalData experimentalData = new ExperimentalData();


    public TraditionalView(Context context) {
        super(context);
        init();
    }
    public TraditionalView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }
    public TraditionalView(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        init();
    }

    public void init() {
        System.out.println("come in");

        MyPaint = new Paint();
        MyPaint.setColor(Color.BLACK);
        MyPaint.setStrokeWidth(3);
        MyPaint.setStyle(Paint.Style.STROKE);

        MyMenu = new Paint();
        MyMenu.setColor(Color.BLACK);
        MyMenu.setStrokeWidth(5);
        MyMenu.setStyle(Paint.Style.STROKE);

        pathInfArrayList = new ArrayList<>(); //记录不同情况的路径

        pathInfArrayList.add(new PathInf());

        hoop = new Hoop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int highMeasureSpec) {
        super.onMeasure(widthMeasureSpec, highMeasureSpec);
        //布局的宽高都是由该方法指定的

        //但指定快件的宽高需要测量
        width = MeasureSpec.getSize(widthMeasureSpec); //获取屏幕宽度
        high = MeasureSpec.getSize(highMeasureSpec); //获取屏幕高度

        //根据屏幕大小来设置大小半径
        hoop.setSmallCircleR((float) high / 12);
        //BigCircleR = high / 6;
        hoop.setBigCircleR((float) high / 6);


        //根据屏幕大小来设置每个圆环的圆心位置
        hoop.setCircle(1, high / 6 * 3 / 2, width / 3);
        hoop.setCircle(2, high / 6 * 11 / 2, width / 3);
        hoop.setCircle(3, high / 6 * 10, width / 3);

        //设置菜单的长和宽
        MenuLen = high * 10 / 35;
        MenuWith = width / 30;

        //设置菜单的起始位置（左上角坐标）
        Menu_X = 0;
        Menu_Y = 0;

        //菜单的中心位置
        center_x = Menu_X + MenuLen;
        center_y = Menu_Y + MenuWith / 2;

        coordinate = new Coordinate(high, width);

        //初始化bitmap和canvas
        bitmap = Bitmap.createBitmap((int)width, (int)high, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }
    //重写该方法，在这里绘图
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制第一个环
        canvas.drawCircle(hoop.getCircle_X(1), hoop.getCircle_Y(1), hoop.getBigCircleR(), MyPaint);
        canvas.drawCircle(hoop.getCircle_X(1), hoop.getCircle_Y(1), hoop.getSmallCircleR(), MyPaint);


        if (hoop.getRing_2()) { //显示第二个环
            canvas.drawCircle(hoop.getCircle_X(2), hoop.getCircle_Y(2), hoop.getBigCircleR(), MyPaint);
            canvas.drawCircle(hoop.getCircle_X(2), hoop.getCircle_Y(2), hoop.getSmallCircleR(), MyPaint);
        }
        if (hoop.getRing_3()) { //显示第三个环
            canvas.drawCircle(hoop.getCircle_X(3), hoop.getCircle_Y(3), hoop.getBigCircleR(), MyPaint);
            canvas.drawCircle(hoop.getCircle_X(3), hoop.getCircle_Y(3), hoop.getSmallCircleR(), MyPaint);

        }
        // 放在这里会导致一级菜单一直处于最上层（但好像没什么好的解决办法）
        showColMenu(); //显示颜色一级菜单
        showPixMenu(); //显示像素一级菜单

        //提示文字显示
        canvas.drawText(switchInformation.getCurrent_color_inf(), coordinate.current_color_x, coordinate.current_color_y, switchInformation.getWordInf());
        canvas.drawText(switchInformation.getCurrent_pixel_inf(), coordinate.current_pixel_x,  coordinate.current_pixel_y, switchInformation.getWordInf());

        canvas.drawText(switchInformation.getTarget_color_inf(), coordinate.target_color_x, coordinate.target_color_y, switchInformation.getWordInf());
        canvas.drawText(switchInformation.getTarget_pixel_inf(), coordinate.target_pixel_x, coordinate.target_pixel_y, switchInformation.getWordInf());

        //显示颜色和像素提示
        canvas.drawRect(coordinate.current_color_left, coordinate.current_color_top, coordinate.current_color_right, coordinate.current_color_bottom, switchInformation.getCurrent_color());
        canvas.drawText(switchInformation.getCurrent_pixel(), coordinate.current_pixel_word_X, coordinate.current_pixel_word_y, switchInformation.getWordInf());

        //显示颜色切换目标和像素切换目标
        canvas.drawRect(coordinate.target_color_left, coordinate.target_color_top, coordinate.target_color_right, coordinate.target_color_bottom, switchInformation.getTarget_color());
        canvas.drawText(switchInformation.getTarget_pixel(), coordinate.target_pixel_word_x, coordinate.target_pixel_word_y, switchInformation.getWordInf());


        drawPath();
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    //绘制线条
    private void drawPath() {
        for (int i = 0; i < pathInfArrayList.size(); i++) { canvas.drawPath(pathInfArrayList.get(i).path, pathInfArrayList.get(i).paint); }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int  action = event.getAction();

        float x = event.getX();
        float y = event.getY();

        if (hoop.getRing_1() && !hoop.getRing_2()) { //当前只显示第一个圆环
            //求出当前位置与圆心坐标差值的平方
            double xr = Math.pow((x - hoop.getCircle_X(1)), 2);
            double yr = Math.pow((y - hoop.getCircle_Y(1)), 2);

            double ra = xr + yr;

            if ((ra >= Math.pow((hoop.getSmallCircleR()), 2)) && (ra <= Math.pow((hoop.getBigCircleR()), 2))) { //如果落在圆环内

                if (!index) { //如果之前没有进入过
                    index = true; //表示已经进入
                    //初次进入的坐标
                    StartX = x;
                    StartY = y;
                } else { //已经进入，这个用来检测是否是一个闭环

                    if (!error && (Math.sqrt(Math.pow(x - StartX, 2) + Math.pow(y - StartY, 2)) >= hoop.getSmallCircleR() * 2)) { //根据两点距离判断
                        error = true;
                    }
                    if ((Math.abs(x - StartX) <= errorNum) && (Math.abs(y - StartY) <= errorNum) && error) {
                        hoop.setRing_2(true);// 当误差满足条件的时候就当做是一个闭环
                        //重新初始化条件
                        index = false;
                        error = false;

                        //获得颜色提示
                        tips = randomNumber.getRandom();
                        switchInformation.setTarget_color(tips / 10);
                    }
                }
            } else { //要是画出环的处理，暂时还没有想法

            }
        }
        if (hoop.getRing_2() && !hoop.getRing_3()) { //第二个圆环已经显示，与环1的判断一样

            double xr = Math.pow((x - hoop.getCircle_X(2)), 2);
            double yr = Math.pow((y - hoop.getCircle_Y(2)), 2);
            double ra = xr + yr;

            if (ra >= Math.pow((hoop.getSmallCircleR()), 2) && ra <= Math.pow((hoop.getBigCircleR()), 2)) { //如果落在圆环内

                if (!index) { //如果之前没有进入过
                    index = true; //表示已经进入
                    //初次进入的坐标
                    StartX = x;
                    StartY = y;
                } else { //已经进入，这个用来检测是否是一个闭环
                    if (!error && (Math.sqrt(Math.pow(x - StartX, 2) + Math.pow(y - StartY, 2)) >= hoop.getSmallCircleR() * 2)) {
                        error = true;
                    }
                    if ((Math.abs(x - StartX) <= errorNum) && (Math.abs(y - StartY) <= errorNum) && error) {
                        hoop.setRing_3(true);//当误差满足条件的时候就当做是一个闭环

                        //像素提示
                        switchInformation.setTarget_pixel(tips % 10);
                    }
                }
            } else { //要是画出环的处理，暂时还没有想法

            }
        }

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: //单指按下时触发
                LastX = x;
                LastY = y;
                pathInfArrayList.get(PathInfNum).path.moveTo(LastX, LastY);
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - LastX);
                float dy = Math.abs(y - LastY);
                if (dx > 3 || dy > 3)
                    pathInfArrayList.get(PathInfNum).path.lineTo(x, y);
                LastX = x;
                LastY = y;

            case MotionEvent.ACTION_UP: //当手指抬起时（选择的结果在这里实现）


        }
        invalidate();
        return true;
    }

    //颜色一级菜单的显示
    public void showColMenu() {
        //控制文字的画笔
        Paint MenuP = new Paint();
        MenuP.setTextSize(MenuWith); //文字大小
        MenuP.setStyle(Paint.Style.FILL); //画笔风格为填充
        //MenuP.setTypeface(Typeface.DEFAULT_BOLD); //粗体（字体没必要）

        //显示菜单
        canvas.drawRect(Menu_X, Menu_Y, Menu_X + MenuLen, Menu_Y + MenuWith,MyMenu);

        //绘制文字
        canvas.drawText("颜", Menu_X + MenuLen / 3, Menu_Y + MenuWith - 10, MenuP);
        canvas.drawText("色", Menu_X + MenuLen / 3 + MenuWith, Menu_Y + MenuWith - 10, MenuP);

    }
    //像素一级菜单的显示
    public void showPixMenu() {
        //控制文字的画笔
        Paint MenuP = new Paint();
        MenuP.setTextSize(MenuWith); //文字大小
        MenuP.setStyle(Paint.Style.FILL); //画笔风格为填充
        //MenuP.setTypeface(Typeface.DEFAULT_BOLD); //粗体

        //显示菜单
        canvas.drawRect(Menu_X + MenuLen,  Menu_Y, Menu_X + MenuLen * 2, Menu_Y + MenuWith, MyMenu);
        //绘制文字
        canvas.drawText("粗", Menu_X + MenuLen + MenuLen / 3, Menu_Y + MenuWith - 10, MenuP);
        canvas.drawText("细", Menu_X + MenuLen + MenuLen / 3 + MenuWith, Menu_Y + MenuWith - 10, MenuP);
    }
}
