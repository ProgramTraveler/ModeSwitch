package com.example.modeswitch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;

import java.io.IOException;
import java.sql.Ref;
import java.util.ArrayList;

/*
    user:王久铭
    date:2022-01-05
    purpose:单手主辅模式绘制界面
 */

public class SingleHandView extends View {
    private ArrayList<PathInf> pathInfArrayList;
    private int PathInfNum = 0; //当前下标指示

    private Paint MyPaint; //绘制图形的画笔

    private float width = 0; //屏幕的宽度
    private float high = 0; //屏幕的高度

    private Canvas canvas; //内存中创建的Canvas
    private Bitmap bitmap; //缓存绘制的内容

    //环
    private Hoop hoop;
    //刚进入环内的起点坐标
    private float StartX = 0;
    private float StartY = 0;
    private boolean index = false; //表示当前状态未进入环

    private float LastX;
    private float LastY;

    //对误差的判断
    private boolean error = false; //当状态变为true时说明是第二次遇到符合误差的状态
    private float errorNum = 20.0F;

    //记录按下的当前毫秒数
    private ArrayList<Long> TimeArr = new ArrayList<Long>();

    /*
    一级菜单变量
     */
    //绘制的菜单半径
    private float MenuRa = 0;
    //菜单的圆心
    private float MenuX = 0;
    private float MenuY = 0;
    //一级菜单是否展开默认为false
    private boolean MenuIn = false;
    //菜单双击位置
    private float Click_x = 0;
    private float Click_y = 0;

    /*
    一级菜单展开的依据
     */
    //当前手指位置
    private float Tou_x = 0;
    private float Tou_y = 0;
    //选择的菜单
    private boolean ColMenu = true;
    private boolean PixMenu = true;

    /*
    二级菜单的高亮变量
     */
    private boolean MenuSeCol = false; //一级菜单还未选择颜色区域
    private boolean MenuSePix = false; //一级菜单还未选择像素区域

    private int colorNow = Color.BLACK;
    private int pixNow = 2;

    private SwitchInformation switchInformation = new SwitchInformation();

    private Coordinate coordinate;

    private RandomNumber randomNumber = new RandomNumber();

    private int tips = 0; //记录菜单切换信息

    private ExperimentalData experimentalData = new ExperimentalData(); //数据记录

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

        MyPaint = new Paint();
        MyPaint.setColor(Color.BLACK);
        MyPaint.setStrokeWidth(3);
        MyPaint.setStyle(Paint.Style.STROKE);

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
        hoop.setSmallCircleR(high / 12);
        //BigCircleR = high / 6;
        hoop.setBigCircleR(high / 6);


        //根据屏幕大小来设置每个圆环的圆心位置
        hoop.setCircle(1, high / 6 * 3 / 2, width / 3);
        hoop.setCircle(2, high / 6 * 11 / 2, width / 3);
        hoop.setCircle(3, high / 6 * 10, width / 3);

        //设置菜单的半径
        MenuRa = high / 9;

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

        if (MenuIn) { //展开一级菜单

            if (ColMenu) ShowColMenu(true);
            else ShowColMenu(false);

            if (PixMenu) ShowPixMenu(true);
            else ShowPixMenu(false);
        }else {
            ShowColMenu(false);
            ShowPixMenu(false);
        }

        infShowMenu(Tou_x, Tou_y);

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
        for (int i = 0; i < pathInfArrayList.size(); i ++) { canvas.drawPath(pathInfArrayList.get(i).path, pathInfArrayList.get(i).paint); }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

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
                }
                else { //已经进入，这个用来检测是否是一个闭环

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
            }else { //要是画出环的处理，暂时还没有想法

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
                }
                else { //已经进入，这个用来检测是否是一个闭环
                    if (!error && (Math.sqrt(Math.pow(x - StartX, 2) + Math.pow(y - StartY, 2)) >= hoop.getSmallCircleR() * 2)) {
                        error = true;
                    }
                    if ((Math.abs(x - StartX) <= errorNum) && (Math.abs(y - StartY) <= errorNum) && error) {
                        hoop.setRing_3(true);//当误差满足条件的时候就当做是一个闭环

                        //像素提示
                        switchInformation.setTarget_pixel(tips % 10);
                    }
                }
            }else { //要是画出环的处理，暂时还没有想法

            }
        }

        //根据不同状态，获取坐标进行画线
        switch (action & MotionEvent.ACTION_MASK) { //

            case MotionEvent.ACTION_DOWN: //单指按下触发
                LastX = x;
                LastY = y;
                pathInfArrayList.get(PathInfNum).path.moveTo(LastX, LastY);
                break;

            case MotionEvent.ACTION_MOVE:

                if (event.getPointerCount() > 1) {
                    //第二根手指的位置
                    Tou_x = event.getX(1);
                    Tou_y = event.getY(1);
                }

                if (event.getPointerId(event.getActionIndex()) == 0 && event.getPointerCount() == 1) { //判断是否是第一个手指（释放后，再次按下会默认是0）
                    float dx = Math.abs(x - LastX);
                    float dy = Math.abs(y - LastY);
                    if (dx > 3 || dy > 3)
                        pathInfArrayList.get(PathInfNum).path.lineTo(x, y);
                    LastX = x;
                    LastY = y;
                }
                break;

            case MotionEvent.ACTION_POINTER_UP: //非第一根手指抬起触发
                pathInfArrayList.get(PathInfNum).paint.setColor(colorNow);
                pathInfArrayList.get(PathInfNum).paint.setStrokeWidth(pixNow);
                pathInfArrayList.get(PathInfNum).path.moveTo(event.getX(0), event.getY(0)); //第一根手指可能会有移动，更新一下位置，不然会出现直接将两点连线

                MenuIn = false;

                //颜色控制
                if ((Tou_y <= (Math.tan(Math.PI * 30 / 180)) * (Tou_x - MenuX) + MenuY) && Tou_x <= MenuX && MenuSeCol && !MenuSePix) { //选择红色
                    pathInfArrayList.get(PathInfNum).paint.setColor(Color.RED);
                    colorNow = Color.RED;

                    switchInformation.setCurrent_color(1); //设置当前颜色

                } else if ((Tou_y >= (Math.tan(Math.PI * 30 / 180)) * (Tou_x - MenuX) + MenuY) && (Tou_y <= (Math.tan(Math.PI * 150 /180)) * (Tou_x - MenuX) + MenuY) && MenuSeCol && !MenuSePix) { //选择黄色
                    pathInfArrayList.get(PathInfNum).paint.setColor(Color.YELLOW);
                    colorNow = Color.YELLOW;

                    switchInformation.setCurrent_color(2);

                } else if ((Tou_y >= (Math.tan(Math.PI * 150 / 180)) * (Tou_x - MenuX) + MenuY) && Tou_x <= MenuX && MenuSeCol && !MenuSePix) { //选择蓝色
                    pathInfArrayList.get(PathInfNum).paint.setColor(Color.BLUE);
                    colorNow = Color.BLUE;

                    switchInformation.setCurrent_color(3);

                } else if ((Tou_y >= (Math.tan(Math.PI * 30 / 180)) * (Tou_x - MenuX) + MenuY) && Tou_x >= MenuX && !MenuSeCol && MenuSePix) { //最大像素区域高亮
                    pathInfArrayList.get(PathInfNum).paint.setStrokeWidth(16);
                    pixNow = 16;

                    switchInformation.setCurrent_pixel("16PX");

                } else if ((Tou_y <= (Math.tan(Math.PI * 30 / 180)) * (Tou_x - MenuX) + MenuY) && (Tou_y >= (Math.tan(Math.PI * 150 /180)) * (Tou_x - MenuX) + MenuY) && Tou_x >= MenuX && !MenuSeCol && MenuSePix) { //中等像素区域高亮
                    pathInfArrayList.get(PathInfNum).paint.setStrokeWidth(8);
                    pixNow = 8;
                    //System.out.println("come in 8PX");
                    switchInformation.setCurrent_pixel("8PX");

                } else if ((Tou_y <= (Math.tan(Math.PI * 150 / 180)) * (Tou_x - MenuX) + MenuY) && Tou_x >= MenuX && !MenuSeCol && MenuSePix) { //最小像素区域高亮
                    pathInfArrayList.get(PathInfNum).paint.setStrokeWidth(4);
                    pixNow = 4;
                    //System.out.println("come in 4PX");
                    switchInformation.setCurrent_pixel("4PX");

                }else { //否则就和之前保持一致
                }
                //将二级菜单重新关闭
                MenuSeCol = false;
                MenuSePix = false;

                break;
            case MotionEvent.ACTION_POINTER_DOWN: //多指按下时触发
                TimeArr.add(System.currentTimeMillis()); //获取当前毫秒数
                if (TimeArr.size() > 1) {
                    if (event.getPointerCount() > 1 && (TimeArr.get(TimeArr.size() - 1) - TimeArr.get(TimeArr.size() - 2)) <= 500) { //如果前后间隔的差值在500以内，而且有一个以上触摸点，那么就是双击
                        //显示菜单，并传入菜单出现的位置
                        MenuIn = true;
                        Click_x = event.getX(1); //双击位置
                        Click_y = event.getY(1);

                        //每次双击产生一个新的对象
                        PathInfNum ++;
                        pathInfArrayList.add(new PathInf());

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //当最后一根手指抬起时，清除所有元素（其实清不清都行，也没节省多少空间）
                while (TimeArr.size() > 0) { TimeArr.remove(0); }
                break;
        }
        invalidate();
        return true;
    }
    //颜色一级菜单显示
    public void ShowColMenu(boolean status) { //根据双击位置进行调整
        //菜单圆心
        MenuX = Click_x;
        MenuY = Click_y;
        Paint MenuP = new Paint();
        MenuP.setTextSize(75); //文字大小
        MenuP.setStyle(Paint.Style.FILL); //画笔风格为填充
        MenuP.setTypeface(Typeface.DEFAULT_BOLD); //粗体

        if (status) { //展开菜单
            canvas.drawCircle(MenuX, MenuY, MenuRa, MyPaint); //设置菜单的图形数据
            canvas.drawLine(MenuX, MenuY - MenuRa, MenuX, MenuY + MenuRa, MyPaint); //菜单的左右分割线

            //设置文字的位置
            canvas.drawText("颜",MenuX - MenuRa / 2 - MenuRa / 4, MenuY - MenuRa / 4, MenuP);
            canvas.drawText("色",MenuX - MenuRa / 2 - MenuRa / 4, MenuY + MenuRa / 4, MenuP);

        }else { //关闭菜单
            canvas.drawColor(0,PorterDuff.Mode.CLEAR); //其实就是清除画布
        }
    }
    //像素一级菜单
    public void ShowPixMenu(boolean status) {

        //菜单圆心
        MenuX = Click_x;
        MenuY = Click_y;

        Paint MenuP = new Paint();
        MenuP.setTextSize(75); //文字大小
        MenuP.setStyle(Paint.Style.FILL); //画笔风格为填充
        MenuP.setTypeface(Typeface.DEFAULT_BOLD); //粗体


        if (status) { //展开菜单
            canvas.drawCircle(MenuX, MenuY, MenuRa, MyPaint); //设置菜单的图形数据
            canvas.drawLine(MenuX, MenuY - MenuRa, MenuX, MenuY + MenuRa, MyPaint); //菜单的左右分割线

            //设置文字的位置
            canvas.drawText("粗",MenuX - MenuRa / 2 + MenuRa / 3 * 2, MenuY - MenuRa / 4, MenuP);
            canvas.drawText("细",MenuX - MenuRa / 2 + MenuRa / 3 * 2, MenuY + MenuRa / 4, MenuP);

        }else { //关闭菜单
            canvas.drawColor(0,PorterDuff.Mode.CLEAR); //其实就是清除画布
        }
    }
    //一级菜单展开二级菜单
    public void infShowMenu(float in_x, float in_y) { //传入当前第二根手指的位置，用来判断是否在菜单范围内
        //求出当前以菜单为圆心，第二根手指位置为边界的圆的方程
        double x = Math.pow((MenuX - in_x), 2);
        double y = Math.pow((MenuY - in_y), 2);
        double r = x + y;

        //设置圆弧范围
        RectF rectF = new RectF(MenuX - MenuRa, MenuY - MenuRa, MenuX + MenuRa, MenuY + MenuRa);
        //设置高亮区域的圆弧范围
        RectF rectH = new RectF(MenuX - MenuRa / 2 , MenuY - MenuRa / 2, MenuX + MenuRa / 2, MenuY + MenuRa / 2);

        Paint ArcP = new Paint();
        ArcP.setColor(Color.LTGRAY); //圆弧颜色
        ArcP.setStyle(Paint.Style.FILL); //效果为填充
        ArcP.setStrokeWidth(5); //设置大小

        if (r <= Math.pow(MenuRa, 2) && MenuIn && (in_x < MenuX)) { //当手指的坐标在园内，一级菜单已经展开，而且位置在左
            //颜色菜单高亮
            ColMenu = false; //一级颜色菜单关闭
            MenuSeCol = true; //二级颜色菜单打开
            MenuSePix = false; //二级像素菜单关闭

            showSeMenu(rectF, true, false, in_x, in_y, rectH);

        }else if (r <= Math.pow(MenuRa, 2) && MenuIn && (in_x > MenuX)) { //像素菜单高亮

            PixMenu = false; //一级像素菜单关闭
            MenuSeCol = false; //二级颜色菜单不显示
            MenuSePix = true; //二级像素菜单显示

            showSeMenu(rectF, false, true, in_x, in_y, rectH);

        }else {
            ColMenu = true;
            PixMenu = true;

            MenuSeCol = false;
            MenuSePix = false;
        }
    }
    //二级菜单显示
    public void showSeMenu(RectF rectF, boolean Col, boolean Pix, float inf_x, float inf_y, RectF rectH) {

        Paint SePaint = new Paint();
        SePaint.setStrokeWidth(5);
        SePaint.setStyle(Paint.Style.FILL);
        SePaint.setTextSize(50);

        if (Col && !Pix) { //展开二级颜色菜单

            ShowPixMenu(true); //像素一级菜单正常显示

            SePaint.setColor(Color.BLUE);
            canvas.drawArc(rectF, 90, 60, true, SePaint); //从90度开始，画180度，连接圆心
            SePaint.setColor(Color.YELLOW);
            canvas.drawArc(rectF, 150, 60, true, SePaint);
            SePaint.setColor(Color.RED);
            canvas.drawArc(rectF, 210, 60, true, SePaint);

            //颜色二级菜单高亮显示
            Paint SeCHLPaint = new Paint();
            SeCHLPaint.setStyle(Paint.Style.FILL); //效果为填充
            SeCHLPaint.setColor(Color.GREEN); //颜色为绿色
            SeCHLPaint.setStrokeWidth(10);

            if ((inf_y <= (Math.tan(Math.PI * 30 / 180)) * (inf_x - MenuX) + MenuY) && inf_x <= MenuX) { //红色区域高亮
                canvas.drawArc(rectH,210, 60, true, SeCHLPaint);
            }
            if ((inf_y >= (Math.tan(Math.PI * 30 / 180)) * (inf_x - MenuX) + MenuY) && (inf_y <= (Math.tan(Math.PI * 150 /180)) * (inf_x - MenuX) + MenuY)) { //黄色区域高亮
                canvas.drawArc(rectH, 150, 60, true, SeCHLPaint);
            }
            if ((inf_y >= (Math.tan(Math.PI * 150 / 180)) * (inf_x - MenuX) + MenuY) && inf_x <= MenuX) { //蓝色区域高亮
                canvas.drawArc(rectH, 90, 60, true, SeCHLPaint);
            }
        }
        if (!Col && Pix) {

            ShowColMenu(true); //颜色一级菜单正常显示

            //对提示文字进行区域划分
            canvas.drawLine(MenuX, MenuY, (float) (MenuX + Math.cos(Math.PI * 30 / 180) * MenuRa), (float) (MenuY - Math.sin(Math.PI * 30 / 180) * MenuRa), SePaint);
            canvas.drawLine(MenuX, MenuY, (float) (MenuX + Math.cos(Math.PI * 30 / 180) * MenuRa), (float) (MenuY + Math.sin(Math.PI * 30 / 180) * MenuRa), SePaint);
            //提示文字
            canvas.drawText("4px", MenuX + MenuRa / 4, MenuY - MenuRa / 2, SePaint);
            canvas.drawText("8px", MenuX + MenuRa / 2, MenuY, SePaint);
            canvas.drawText("16px", MenuX + MenuRa / 4, MenuY + MenuRa / 3 * 2,SePaint);

            //像素二级菜单高亮显示
            Paint SePHLPaint = new Paint();
            SePHLPaint.setStyle(Paint.Style.FILL);
            SePHLPaint.setColor(Color.GREEN);
            SePHLPaint.setStrokeWidth(10);

            if ((inf_y >= (Math.tan(Math.PI * 30 / 180)) * (inf_x - MenuX) + MenuY) && inf_x >= MenuX) { //最大像素区域高亮
                canvas.drawArc(rectH,30, 60, true, SePHLPaint);
            }
            if ((inf_y <= (Math.tan(Math.PI * 30 / 180)) * (inf_x - MenuX) + MenuY) && (inf_y >= (Math.tan(Math.PI * 150 /180)) * (inf_x - MenuX) + MenuY)) { //中等像素区域高亮
                canvas.drawArc(rectH, 30, -60, true, SePHLPaint);
            }
            if ((inf_y <= (Math.tan(Math.PI * 150 / 180)) * (inf_x - MenuX) + MenuY) && inf_x >= MenuX) { //最小像素区域高亮
                canvas.drawArc(rectH, 270, 60, true, SePHLPaint);
            }

        }
    }
}