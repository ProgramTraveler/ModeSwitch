package com.example.modeswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

/*
    user:王久铭
    date:2022-01-24
    purpose:活动 主选择模式菜单
 */

public class MainMenu extends AppCompatActivity {
    private ExperimentalData experimentalData = new ExperimentalData(); //数据保存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //对传统模式按钮进行监听
        Button buttonTra = (Button) findViewById(R.id.Traditional);
        buttonTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("in");
                Intent intent = new Intent(MainMenu.this, traditional.class);
                startActivity(intent);
                Toast.makeText(MainMenu.this, "已选择传统对照模式", Toast.LENGTH_SHORT).show();
            }
        });

        //对单手主辅模式的按钮的监听
        Button buttonSingle = (Button) findViewById(R.id.SingleHand);
        buttonSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, SingleHandMAndA.class); //由当前模式跳往单手主辅模式
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

        //对测试者输入框进行监听
        EditText editText = (EditText) findViewById(R.id.edit_text_user);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                experimentalData.Set_user_name(editText.getText().toString()); //保存测试者姓名
            }
        });
    }
}