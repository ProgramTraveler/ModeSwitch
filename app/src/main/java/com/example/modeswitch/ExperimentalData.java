package com.example.modeswitch;

import android.content.Context;
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
    String name = "bb"; //文件名

    public ExperimentalData() {}

    public void saveInf() throws IOException {

        String temp = name + ".csv"; //添加csv文件后缀

        //保存实验数据的文件名
        File saveFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + temp); //存储的路劲和csv文件的名称

        System.out.println(Environment.getExternalStorageDirectory().getPath());

        csv = new RandomAccessFile(saveFile, "rw");

        int csvLine = (int) csv.length();
        String saveText = "";

        if (csvLine == 0) {
            System.out.println("1");
            saveText = "姓名" + "," + "学号" + "\n"; //第一排的命名名称
            csv.write(saveText.getBytes("GBK"));
        }
        csv.skipBytes(csvLine);

        System.out.println("2");
        saveText = "name" + "," + "number" + "\n"; //每排存储的数据记录
        csv.write(saveText.getBytes("GBK"));
        csv.close();
    }
}
