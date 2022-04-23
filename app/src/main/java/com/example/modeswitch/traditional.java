package com.example.modeswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
    user:王久铭
    date:2022-03-22
    purpose:传统模式
 */

public class traditional extends AppCompatActivity {

    private String user_name = "";
    private String group = "";

    public String get_user_name () {
        return user_name;
    }

    public String get_group () {
        return group;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取主菜单的输入的测试者的姓名
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        //获取主菜单的输入的测试组数
        group = intent.getStringExtra("group");

        setContentView(R.layout.activity_traditional);

        //对返回按钮进行监听
        Button buttonBack = (Button) findViewById(R.id.Back_traditional);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                traditional.super.onBackPressed();
            }
        });
    }
}