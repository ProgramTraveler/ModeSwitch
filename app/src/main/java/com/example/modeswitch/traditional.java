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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traditional);

        //对返回按钮进行监听
        Button buttonBack = (Button) findViewById(R.id.Back_traditional);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                traditional.super.onBackPressed();
            }
        });
    }
}