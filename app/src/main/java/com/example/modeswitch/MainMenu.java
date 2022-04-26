package com.example.modeswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

/*
    user:王久铭
    date:2022-01-24
    purpose:活动 主选择模式菜单
 */

public class MainMenu extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String group_context = ""; //记录选择的组数
    private String hand_mode = ""; //记录选择的单双手的模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        EditText edit_user = (EditText) findViewById(R.id.edit_text_user); //获取输入的用户名

        Spinner spinner_group = (Spinner) findViewById(R.id.spinner_group); //获取选中的组数
        spinner_group.setOnItemSelectedListener(this); //为组数设置监听器

        Spinner spinner_hand = (Spinner) findViewById(R.id.spinner_hand); //获取选择的单双手模式
        spinner_hand.setOnItemSelectedListener(this);

        //对传统模式按钮进行监听
        Button buttonTra = (Button) findViewById(R.id.Traditional);
        buttonTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //主菜单输入的测试者的姓名
                Intent intent = new Intent(MainMenu.this, traditional.class);
                intent.putExtra("user_name", edit_user.getText().toString());
                //主菜单选择的测试组数
                intent.putExtra("group", group_context);
                //主菜单选择的单双手模式
                intent.putExtra("hand", hand_mode);

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
                intent.putExtra("user_name", edit_user.getText().toString());
                intent.putExtra("group", group_context);
                intent.putExtra("hand", hand_mode);

                startActivity(intent);
                //对选择的模式进行提示
                Toast.makeText(MainMenu.this, "已选择主辅选择模式", Toast.LENGTH_SHORT).show();
            }
        });

        //对单手动态模式按钮的监听
        Button buttonSingleD = (Button) findViewById(R.id.SingDynamic);
        buttonSingleD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, SingleDynamic.class);
                intent.putExtra("user_name", edit_user.getText().toString());
                intent.putExtra("group", group_context);
                //System.out.println(hand_mode);
                intent.putExtra("hand", hand_mode);

                startActivity(intent);
                Toast.makeText(MainMenu.this, "已选择动态响应模式", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //对选择的组数和单双手模式进行监听
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //System.out.println(adapterView.getItemAtPosition(i).toString());
        switch (adapterView.getItemAtPosition(i).toString()) {
            case "1组" :
                //System.out.println("come 1");
                group_context = "1";
                break;
            case "2组" :
                //System.out.println("come 2");
                group_context = "2";
                break;
            case "3组" :
                //System.out.println("come 3");
                group_context = "3";
                break;
            case "4组" :
                //System.out.println("come 4");
                group_context = "4";
                break;
            case "5组" :
                //System.out.println("come 5");
                group_context = "5";
                break;
            case "6组" :
                //System.out.println("come 6");
                group_context = "6";
                break;
            case "7组" :
                //System.out.println("come 7");
                group_context = "7";
                break;
            case "8组" :
                //System.out.println("come 8");
                group_context = "8";
                break;
            case "9组" :
                //System.out.println("come 9");
                group_context = "9";
                break;
            case "单手" :
                //System.out.println("单手");
                hand_mode = "单手";
                break;
            case "双手" :
                //System.out.println("双手");
                hand_mode = "双手";
            default: break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}