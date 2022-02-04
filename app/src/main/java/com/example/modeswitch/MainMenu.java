package com.example.modeswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
    user:王久铭
    date:2022-01-24
    purpose:活动 主选择模式菜单
 */

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //对单手主辅模式的按钮的监听
        Button buttonSingle = (Button) findViewById(R.id.SingleHand);
        buttonSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, SingleHandMAndA.class); //由当前模式朓往单手主辅模式
                startActivity(intent);
                //对选择的模式进行提示
                Toast.makeText(MainMenu.this, "已选择单手主辅模式", Toast.LENGTH_SHORT).show();
            }
        });
        //对单手动态模式按钮的监听
        Button buttonSingleD = (Button) findViewById(R.id.SingDynamic);
        buttonSingleD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, SingleDynamic.class);
                startActivity(intent);
                Toast.makeText(MainMenu.this, "已选择单手动态模式", Toast.LENGTH_SHORT).show();
            }
        });

    }
}