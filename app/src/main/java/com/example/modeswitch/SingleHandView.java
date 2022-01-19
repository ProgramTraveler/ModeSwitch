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

    private int width = 0; //屏幕的宽度
    private int high = 0; //屏幕的高度

    private Canvas canvas; //内存中创建的Canvas
    private Bitmap bitmap; //缓存绘制的内容

    private float LastX;
    private float LastY;

    //刚进入环内的起点坐标
    private float StartX = 0;
    private float StartY = 0;
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

        //设置菜单的半径
        MenuRa = high / 6;

        //初始化bitmap和canvas
        bitmap = Bitmap.createBitmap(width, high, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    //重写该方法，在这里绘图
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制第一个环
        canvas.drawCircle(Circle1_X, Circle1_Y, BigCircleR, MyPaint);
        canvas.drawCircle(Circle1_X, Circle1_Y, SmallCircleR, MyPaint);

        if (ring2) { //显示第二个环
            canvas.drawCircle(Circle2_X, Circle2_Y, BigCircleR, MyPaint);
            canvas.drawCircle(Circle2_X, Circle2_Y, SmallCircleR, MyPaint);
        }
        if (ring3) { //显示第三个环
            canvas.drawCircle(Circle3_X, Circle3_Y, BigCircleR, MyPaint);
            canvas.drawCircle(Circle3_X, Circle3_Y, SmallCircleR, MyPaint);
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
                }
                else { //已经进入，这个用来检测是否是一个闭环

                    if (!error && (Math.sqrt(Math.pow(x - StartX, 2) + Math.pow(y - StartY, 2)) >= SmallCircleR * 2)) { //根据两点距离判断
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

            double xr = Math.pow((x - Circle2_X), 2);
            double yr = Math.pow((y - Circle2_Y), 2);
            double ra = xr + yr;

            if (ra >= Math.pow((SmallCircleR), 2) && ra <= Math.pow((BigCircleR), 2)) { //如果落在圆环内

                if (!index) { //如果之前没有进入过
                    index = true; //表示已经进入
                    //初次进入的坐标
                    StartX = x;
                    StartY = y;
                }
                else { //已经进入，这个用来检测是否是一个闭环
                    if (!error && (Math.sqrt(Math.pow(x - StartX, 2) + Math.pow(y - StartY, 2)) >= SmallCircleR * 2)) {
                        error = true;
                    }
                    if ((Math.abs(x - StartX) <= errorNum) && (Math.abs(y - StartY) <= errorNum) && error) {
                        ring3 = true; //当误差满足条件的时候就当做是一个闭环
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

                //System.out.println(PathInfNum + " " + pathInfArrayList.size());


                pathInfArrayList.get(PathInfNum).paint.setColor(colorNow);
                pathInfArrayList.get(PathInfNum).paint.setStrokeWidth(pixNow);


                pathInfArrayList.get(PathInfNum).path.moveTo(event.getX(0), event.getY(0)); //第一根手指可能会有移动，更新一下位置，不然会出现直接将两点连线

                MenuIn = false;

                //pathInfArrayList.get(PathInfNum).paint = pathInfArrayList.get(PathInfNum - 1).paint;

                //颜色控制
                if ((Tou_y <= (Math.tan(Math.PI * 30 / 180)) * (Tou_x - MenuX) + MenuY) && Tou_x <= MenuX && MenuSeCol && !MenuSePix) { //选择红色
                    pathInfArrayList.get(PathInfNum).paint.setColor(Color.RED);
                    colorNow = Color.RED;
                } else if ((Tou_y >= (Math.tan(Math.PI * 30 / 180)) * (Tou_x - MenuX) + MenuY) && (Tou_y <= (Math.tan(Math.PI * 150 /180)) * (Tou_x - MenuX) + MenuY) && MenuSeCol && !MenuSePix) { //选择黄色
                    pathInfArrayList.get(PathInfNum).paint.setColor(Color.YELLOW);
                    colorNow = Color.YELLOW;
                } else if ((Tou_y >= (Math.tan(Math.PI * 150 / 180)) * (Tou_x - MenuX) + MenuY) && Tou_x <= MenuX && MenuSeCol && !MenuSePix) { //选择蓝色
                    pathInfArrayList.get(PathInfNum).paint.setColor(Color.BLUE);
                    colorNow = Color.BLUE;
                } else if ((Tou_y >= (Math.tan(Math.PI * 30 / 180)) * (Tou_x - MenuX) + MenuY) && Tou_x >= MenuX && !MenuSeCol && MenuSePix) { //最大像素区域高亮
                    pathInfArrayList.get(PathInfNum).paint.setStrokeWidth(16);
                    pixNow = 16;
                } else if ((Tou_y <= (Math.tan(Math.PI * 30 / 180)) * (Tou_x - MenuX) + MenuY) && (Tou_x >= (Math.tan(Math.PI * 150 /180)) * (Tou_x - MenuX) + MenuY) && !MenuSeCol && MenuSePix) { //中等像素区域高亮
                    pathInfArrayList.get(PathInfNum).paint.setStrokeWidth(8);
                    pixNow = 8;
                } else if ((Tou_y <= (Math.tan(Math.PI * 150 / 180)) * (Tou_x - MenuX) + MenuY) && Tou_x >= MenuX && !MenuSeCol && MenuSePix) { //最小像素区域高亮
                    pathInfArrayList.get(PathInfNum).paint.setStrokeWidth(4);
                    pixNow = 4;
                }else { //否则就和之前保持一致

                    //pathInfArrayList.get(PathInfNum).paint.setColor(colorNow);
                    //pathInfArrayList.get(PathInfNum).paint.setStrokeWidth(pixNow);

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
        MenuY = Click_y - MenuRa * 3 / 2;

        Paint MenuP = new Paint();
        MenuP.setTextSize(100); //文字大小
        MenuP.setStyle(Paint.Style.FILL); //画笔风格为填充
        MenuP.setTypeface(Typeface.DEFAULT_BOLD); //粗体

        if (status) { //展开菜单
            canvas.drawCircle(MenuX, MenuY, MenuRa, MyPaint); //设置菜单的图形数据
            canvas.drawLine(MenuX, MenuY - MenuRa, MenuX, MenuY + MenuRa, MyPaint); //菜单的左右分割线

            //设置文字的位置
            canvas.drawText("颜",Click_x - MenuRa / 2 - MenuRa / 4, Click_y - MenuRa  / 2 * 3 - MenuRa / 4, MenuP);
            canvas.drawText("色",Click_x - MenuRa / 2 - MenuRa / 4, Click_y - MenuRa  / 2 * 3 + MenuRa / 4, MenuP);

        }else { //关闭菜单
            canvas.drawColor(0,PorterDuff.Mode.CLEAR); //其实就是清除画布
        }
    }
    //像素一级菜单
    public void ShowPixMenu(boolean status) {
        //菜单圆心
        MenuX = Click_x;
        MenuY = Click_y - MenuRa * 3 / 2;

        Paint MenuP = new Paint();
        MenuP.setTextSize(100); //文字大小
        MenuP.setStyle(Paint.Style.FILL); //画笔风格为填充
        MenuP.setTypeface(Typeface.DEFAULT_BOLD); //粗体


        if (status) { //展开菜单
            canvas.drawCircle(MenuX, MenuY, MenuRa, MyPaint); //设置菜单的图形数据
            canvas.drawLine(MenuX, MenuY - MenuRa, MenuX, MenuY + MenuRa, MyPaint); //菜单的左右分割线

            //设置文字的位置
            canvas.drawText("粗",Click_x - MenuRa / 2 + MenuRa / 3 * 2, Click_y - MenuRa  / 2 * 3 - MenuRa / 4, MenuP);
            canvas.drawText("细",Click_x - MenuRa / 2 + MenuRa / 3 * 2, Click_y - MenuRa  / 2 * 3 + MenuRa / 4, MenuP);

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

        Paint ArcP = new Paint();
        ArcP.setColor(Color.LTGRAY); //圆弧颜色
        ArcP.setStyle(Paint.Style.FILL); //效果为填充
        ArcP.setStrokeWidth(5); //设置大小

        //canvas.drawColor(0,PorterDuff.Mode.CLEAR); //画之前将画布清除

        if (r <= Math.pow(MenuRa, 2) && MenuIn && (in_x < MenuX)) { //当手指的坐标在园内，一级菜单已经展开，而且位置在左
            //颜色菜单高亮
            //canvas.drawArc(rectF, 90, 180, true, ArcP); //从90度开始，画180度，连接圆心
            ColMenu = false; //一级颜色菜单关闭
            MenuSeCol = true; //二级颜色菜单打开
            MenuSePix = false; //二级像素菜单关闭

            showSeMenu(rectF, true, false, in_x, in_y);

        }else if (r <= Math.pow(MenuRa, 2) && MenuIn && (in_x > MenuX)) { //像素菜单高亮

            PixMenu = false; //一级像素菜单关闭
            MenuSeCol = false; //二级颜色菜单不显示
            MenuSePix = true; //二级像素菜单显示

            showSeMenu(rectF, false, true, in_x, in_y);

        }else {
            ColMenu = true;
            PixMenu = true;

            MenuSeCol = false;
            MenuSePix = false;
        }
    }
    //二级菜单显示
    public void showSeMenu(RectF rectF, boolean Col, boolean Pix, float inf_x, float inf_y) {

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
            SeCHLPaint.setStyle(Paint.Style.STROKE);
            SeCHLPaint.setColor(Color.BLACK);
            SeCHLPaint.setStrokeWidth(10);

            if ((inf_y <= (Math.tan(Math.PI * 30 / 180)) * (inf_x - MenuX) + MenuY) && inf_x <= MenuX) { //红色区域高亮
                canvas.drawArc(rectF,210, 60, true, SeCHLPaint);
            }
            if ((inf_y >= (Math.tan(Math.PI * 30 / 180)) * (inf_x - MenuX) + MenuY) && (inf_y <= (Math.tan(Math.PI * 150 /180)) * (inf_x - MenuX) + MenuY)) { //黄色区域高亮
                canvas.drawArc(rectF, 150, 60, true, SeCHLPaint);
            }
            if ((inf_y >= (Math.tan(Math.PI * 150 / 180)) * (inf_x - MenuX) + MenuY) && inf_x <= MenuX) { //蓝色区域高亮
                canvas.drawArc(rectF, 90, 60, true, SeCHLPaint);
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
            SePHLPaint.setStyle(Paint.Style.STROKE);
            SePHLPaint.setColor(Color.BLACK);
            SePHLPaint.setStrokeWidth(10);

            if ((inf_y >= (Math.tan(Math.PI * 30 / 180)) * (inf_x - MenuX) + MenuY) && inf_x >= MenuX) { //最大像素区域高亮
                canvas.drawArc(rectF,30, 60, true, SePHLPaint);
            }
            if ((inf_y <= (Math.tan(Math.PI * 30 / 180)) * (inf_x - MenuX) + MenuY) && (inf_y >= (Math.tan(Math.PI * 150 /180)) * (inf_x - MenuX) + MenuY)) { //中等像素区域高亮
                canvas.drawArc(rectF, 30, -60, true, SePHLPaint);
            }
            if ((inf_y <= (Math.tan(Math.PI * 150 / 180)) * (inf_x - MenuX) + MenuY) && inf_x >= MenuX) { //最小像素区域高亮
                canvas.drawArc(rectF, 270, 60, true, SePHLPaint);
            }

        }
    }
}
