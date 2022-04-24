package com.example.modeswitch;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*
    user:王久铭
    date:2022-03-15
    purpose:用来记录实验数据，并导出csv文件
 */

public class ExperimentalData {
    private RandomAccessFile csv; // 存实验数据的文件
    private String name = "information"; //文件名

    private String user_Name = ""; //测试者的姓名

    private int group = 0; //实验的组数
    private int num = 1; //当前组的第几次

    private String mode = ""; //切换模式技术

    private String target_Col = ""; //切换的目标颜色
    private String target_Pix = ""; //切换的目标像素

    private int false_tig = 0; //误触发次数
    private boolean false_tig_index = false; //误触发的变量约束 false 表示不允许切换

    private int false_Col = 0; //颜色切换错误次数
    private int false_Pix = 0; //像素切换错误次数
    private int false_All = 0; //切换的总的错误次数

    /*
        绘制第一个圆环的时间
     */
    private long start_hoop_1 = 0;
    private long end_hoop_1 = 0;

    /*
        绘制第二个圆环的时间
     */
    private long start_hoop_2 = 0;
    private long end_hoop_2 = 0;

    /*
        绘制第三个圆环的时间
     */
    private long start_hoop_3 = 0;
    private long end_hoop_3 = 0;

    /*
        模式切换时间
     */
    //颜色切换时间
    private long swi_start_color = 0;
    private long swi_end_color = 0;
    private boolean swi_color_index_s = false; //第一次开始切换标志
    private boolean swi_color_index_e = false; //第一次结束切换标志

    //像素切换时间
    private long swi_start_pixel = 0;
    private long swi_end_pixel = 0;
    private boolean swi_pixel_index_s = false; //第一次开始切换标志
    private boolean swi_pixel_index_e = false; //第一次结束切换标志

    /*
        整个操作时间
     */
    private long whole_start = 0;
    private long whole_end = 0;
    private boolean whole_index = false;


    private boolean save = false; //是否存储过 false 表示没有储存

    public ExperimentalData () {}

    //测试者姓名
    public void Set_user_name (String s) {
        user_Name = s;
    }

    //实验组数
    public void Set_group (String s) {
        group = Integer.parseInt(s);
    }
    public int Get_group () {return group;}
    public void Update_group () {group --;}

    //当前组数的第几次
    public void Init_num () {
        num = 1;
    }
    public void Add_num () {
        num ++;
    }

    //切换模式
    public void Set_mode (String s) {
        mode = s;
    }

    //目标颜色
    public void Set_Tar_Col (int n) { //保存目标颜色
        if (n == 1) target_Col = "红色";
        if (n == 2) target_Col = "黄色";
        if (n == 3) target_Col = "蓝色";
    }
    //目标像素
    public void Set_Tar_Pix (int n) { //保存目标像素
        if (n == 1) target_Pix = "4PX";
        if (n == 2) target_Pix = "8PX";
        if (n == 3) target_Pix = "16PX";
    }
    //误触发错误数
    public void Add_Tig () { //误触发次数
        false_tig ++;
    }
    public void Init_Tig () {false_tig = 0;}
    public void set_tig_index (boolean b) {
        false_tig_index = b;
    }
    public boolean get_tig_index () {
        return false_tig_index;
    }

    public void Init_Col () {
        false_Col = 0; //初始化
    }
    public void Add_Col () { //颜色切换错误次数
        false_Col ++;
        false_All ++;
    }

    public void Init_Pix () {
        false_Pix = 0;
    }
    public void Add_Pix () { //像素切换错误次数
        false_Pix ++;
        false_All ++;
    }

    //初始化切换总错误
    public void Init_false_all () {
        false_All = 0;
    }

    //当前测试是否存储过
    public void set_Save (boolean b) {
        save = b;
    }
    public boolean get_Save () {
        return save;
    }

    //记录环一时间
    public void set_start_hoop_1 (long l) {
        start_hoop_1 = l;
    }
    public void set_end_hoop_1 (long l) {
        end_hoop_1 = l;
    }

    //记录环二时间
    public void set_start_hoop_2 (long l) {
        start_hoop_2 = l;
    }
    public void set_end_hoop_2 (long l) {
        end_hoop_2 = l;
    }

    //记录环三时间
    public void set_start_hoop_3 (long l) {
        start_hoop_3 = l;
    }
    public void set_end_hoop_3 (long l) {
        end_hoop_3 = l;
    }

    /*
        模式切换时间
     */
    //颜色
    public void set_start_color (long l) {
        swi_start_color = l;
    }
    public void set_end_color (long l) {
        swi_end_color = l;
    }

    public void set_color_index_s (boolean b) {
        swi_color_index_s = b;
    }
    public boolean get_color_index_s () {
        return swi_color_index_s;
    }

    public void set_color_index_e (boolean b) {
        swi_color_index_e = b;
    }
    public boolean get_color_index_e () {
        return swi_color_index_e;
    }

    //像素
    public void set_start_pixel (long l) {
        swi_start_pixel = l;
    }
    public void set_end_pixel (long l) {
        swi_end_pixel = l;
    }

    public void set_pixel_index_s (boolean b) {
        swi_pixel_index_s = b;
    }
    public boolean get_pixel_index_s () {
        return swi_pixel_index_s;
    }

    public void set_pixel_index_e (boolean b) {
        swi_pixel_index_e = b;
    }
    public boolean get_pixel_index_e () {
        return swi_pixel_index_e;
    }
    //整个操作时间
    public void set_start_whole (long l) {
        whole_start = l;
    }
    public void set_end_whole (long l) {
        whole_end = l;
    }
    public void set_whole_index (boolean b) {
        whole_index = b;
    }
    public boolean get_whole_index () {
        return whole_index;
    }

    public void saveInf () throws IOException {

        String temp = name + ".csv"; //添加csv文件后缀

        //保存实验数据的文件名
        File saveFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + temp); //存储的路劲和csv文件的名称

        System.out.println(Environment.getExternalStorageDirectory().getPath());

        csv = new RandomAccessFile(saveFile, "rw");

        int csvLine = (int) csv.length();
        String saveText = "";

        if (csvLine == 0) {
            saveText = "姓名" + ","
                    + "实验组数" + ","
                    + "实验组编号" + ","
                    + "切换模式技术"  + ","
                    + "目标颜色" + ","
                    + "目标像素" + ","
                    + "误触发错误总数" + ","
                    + "颜色切换错误数" + ","
                    + "像素切换错误数" + ","
                    + "模式切换总错误数" + ","
                    + "第一个圆环绘制时间" + ","
                    + "第二个圆环绘制时间" + ","
                    + "第三个圆环绘制时间" + ","
                    + "颜色菜单切换时间" + ","
                    + "像素菜单切换时间" + ","
                    + "模式切换总时间" + ","
                    + "总操作时间" + ","
                    + "\n"; //第一排的命名名称
            csv.write(saveText.getBytes("GBK"));
        }
        csv.skipBytes(csvLine);

        //注意和第一行数据的文字对应
        System.out.println("save: " + user_Name);
        saveText = user_Name + ","
                + group + ","
                + num + ","
                + mode + ","
                + target_Col + ","
                + target_Pix + ","
                + false_tig + ","
                + false_Col + ","
                + false_Pix + ","
                + false_All + ","
                + (end_hoop_1 - start_hoop_1) + ","
                + (end_hoop_2 - start_hoop_2) + ","
                + (end_hoop_3 - start_hoop_3) + ","
                + (swi_end_color - swi_start_color) + ","
                + (swi_end_pixel - swi_start_pixel) + ","
                + (swi_end_color + swi_end_pixel - swi_start_color - swi_start_pixel) + ","
                + (whole_end - whole_start) + ","
                + "\n"; //每排存储的数据记录
        csv.write(saveText.getBytes("GBK"));
        csv.close();
    }
}
