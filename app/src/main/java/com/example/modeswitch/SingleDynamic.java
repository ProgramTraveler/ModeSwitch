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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_dynamic);
        //对返回按钮的监听
        Button buttonBack = (Button) findViewById(R.id.Back_single_dynamic);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleDynamic.this, MainMenu.class);
                startActivity(intent);
            }
        });
    }
}