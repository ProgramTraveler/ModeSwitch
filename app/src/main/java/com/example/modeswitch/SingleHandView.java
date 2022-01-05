package com.example.modeswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SingleHandView extends View {
    private Paint paint;
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
        paint = new Paint();
        paint.setAntiAlias(true); //抗锯齿
        paint.setColor(getResources().getColor(R.color.purple_200)); //画笔颜色
        paint.setStyle(Paint.Style.FILL); //画笔风格
        paint.setTextSize(36); //绘制文字大小，单位px
        paint.setStrokeWidth(5); //画笔粗细
    }
    //重写该方法，在这里绘图
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int highMeasureSpec) {
        super.onMeasure(widthMeasureSpec, highMeasureSpec);
        //布局的宽高都是由该方法指定的
        //但指定快件的宽高需要测量
        //获取宽高的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int highMode = MeasureSpec.getMode((highMeasureSpec));
    }
}
