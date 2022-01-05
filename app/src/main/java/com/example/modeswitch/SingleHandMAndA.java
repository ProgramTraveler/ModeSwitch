package com.example.modeswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

/*
    user:王久铭
    date:2022-01-05
    purpose:单手主辅模式
 */
public class SingleHandMAndA extends AppCompatActivity {
    /*Resources resources = this.getResources();
    DisplayMetrics dm = resources.getDisplayMetrics();
    float density = dm.density;
    int width = dm.widthPixels; //获取当前屏幕宽度
    int height = dm.heightPixels; //获取当前屏幕高度
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //System.out.print(width + " " + height);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_hand_mand);
        //SingleHandView sv = new SingleHandView(this);
    }
}