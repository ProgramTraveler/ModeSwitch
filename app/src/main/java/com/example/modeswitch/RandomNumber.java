package com.example.modeswitch;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
    user:王久铭
    date:2022-03-14
    purpose:用于随机生成切换的颜色块和像素
 */
public class RandomNumber {
    /*
        随机生成数定义为
        颜色:
            1:红色 2:黄色 3:蓝色
        像素:
            1:4Px 2:8Px 3:16Px
        例如`11`就表示 红色 4Px
     */
    private ArrayList<Integer> randomN = new ArrayList<>(); // 随机生成数

    public RandomNumber() {
        randomN.add(11);
        randomN.add(12);
        randomN.add(13);
        for (int i = 0; i < 6; i ++) {
            randomN.add(randomN.get(i) + 10);
        }

        Random random = new Random();
        int index = -1;
        //将顺序数打乱以达到随机出现的效果
        for (int i = 0; i < 9; i ++) {
            index = random.nextInt(9);
            Collections.swap(randomN, index, randomN.size() - 1 - i);

        }


    }


}
