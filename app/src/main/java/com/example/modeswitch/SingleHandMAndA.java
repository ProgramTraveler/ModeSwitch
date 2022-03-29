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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_hand_mand);

        Button buttonBack = (Button) findViewById(R.id.Back_single_Hand);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    Intent intent = new Intent(SingleHandMAndA.this, MainMenu.class);
                    startActivity(intent);
                */
                SingleHandMAndA.super.onBackPressed();
            }
        });
    }
}