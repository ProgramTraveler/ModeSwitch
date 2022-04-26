package com.example.modeswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;

/*
    user:王久铭
    date:2022-01-05
    purpose:活动 单手主辅模式
 */
public class SingleHandMAndA extends AppCompatActivity {

    private String user_name = "";
    private String group = "";
    private String hand_mode = "";
    private String pra = "";

    public String get_user_name () {
        return user_name;
    }

    public String get_group () {
        return group;
    }

    public String get_hand_mode () {
        return hand_mode;
    }

    public String get_pra () {
        return pra;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取输入的姓名和组数
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        group = intent.getStringExtra("group");
        hand_mode = intent.getStringExtra("hand");
        pra = intent.getStringExtra("pra");

        setContentView(R.layout.activity_single_hand_mand);

        Button buttonBack = (Button) findViewById(R.id.Back_single_Hand);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                SingleHandMAndA.super.onBackPressed();
            }
        });
    }
}