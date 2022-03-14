package com.example.modeswitch;

/*
    user:王久铭
    date:2022-03-14
    purpose:将设置的常用的坐标记录在这个类中，方便后续进行调整时
 */

public class Coordinate {
    /*
        提示文字的坐标
     */
    //当前颜色提示文字坐标
    public float current_color_x = 0;
    public float current_color_y = 0;
    //当前像素提示文字坐标
    public float current_pixel_x = 0;
    public float current_pixel_y = 0;
    //目标颜色提示文字
    public float target_color_x = 0;
    public float target_color_y = 0;
    //目标像素提示文字
    public float target_pixel_x = 0;
    public float target_pixel_y = 0;

    /*
        提示颜色块和像素文字坐标
     */
    //当前颜色块坐标
    public float current_color_left = 0;
    public float current_color_top = 0;
    public float current_color_right = 0;
    public float current_color_bottom = 0;
    //当前像素文字坐标
    public float current_pixel_word_X = 0;
    public float current_pixel_word_y = 0;

    public Coordinate(float h, float w) {
        current_color_x = w / 3;
        current_color_y = h / 5;

        current_pixel_x = w / 3;
        current_pixel_y = h / 5 + 80;

        target_color_x = w / 3 + 500;
        target_color_y = h / 5;

        target_pixel_x = w / 3 + 500;
        target_pixel_y = h / 5 + 80;

        current_color_left = w / 3 + 230;
        current_color_top = h / 5 - 40;
        current_color_right = w / 3 + 350;
        current_color_bottom = h /5;

        current_pixel_word_X = w / 3 + 230;
        current_pixel_word_y = h / 5 + 80;

    }

}
