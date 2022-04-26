package com.example.modeswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
    user:王久铭
    date:2022-01-24
    purpose:活动 单手动态模式
 */
public class SingleDynamic extends AppCompatActivity {

    private String user_name = "";
    private String group = "";
    private String hand_mode = "";

    public String get_user_name () {
        return user_name;
    }

    public String get_group () {
        return group;
    }

    public String get_hand_mode () {
        return hand_mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取输入的测试者的姓名和组数
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        group = intent.getStringExtra("group");
        hand_mode = intent.getStringExtra("hand");
        //System.out.println(hand_mode);

        setContentView(R.layout.activity_single_dynamic);

        //对返回按钮的监听
        Button buttonBack = (Button) findViewById(R.id.Back_single_dynamic);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleDynamic.super.onBackPressed();
            }
        });
    }
}