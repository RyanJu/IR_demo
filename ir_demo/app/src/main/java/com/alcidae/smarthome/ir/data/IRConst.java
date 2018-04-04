package com.alcidae.smarthome.ir.data;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/2 18:06 1.0
 * @time 2018/4/2 18:06
 * @project ir_demo com.alcidae.smarthome.ir.data
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/2 18:06
 */

public class IRConst {

    //stb type
    public static final int NORMAL_STB = 0;
    public static final int IPTV = 1;


    //remote key fid
    public interface KEY {
        int power = 1;
        int ok = 42;
        int channel_up = 43;
        int channel_down = 44;
        int menu = 45;
        int navigate_up = 46;
        int navigate_down = 47;
        int navigate_left = 48;
        int navigate_right = 49;
        int volume_up = 50;
        int volume_down = 51;
        int num_0 = 56;
        int num_1 = 61;
        int num_2 = 66;
        int num_3 = 71;
        int num_4 = 76;
        int num_5 = 81;
        int num_6 = 86;
        int num_7 = 91;
        int num_8 = 96;
        int num_9 = 101;

        //静音
        int mute = 106;

        //返回
        int back = 116;

        int homepage = 136;

        //快退
        int rewind = 141;

        int play = 146;

        //快进
        int fast_forward = 151;

        int pause = 166;

        //上页
        int page_up = 171;

        //下页
        int page_down = 176;

        //设置
        int set = 437;

        //删除
        int delete = 1697;

        //回看
        int look_back = 2267;

        int sleep = 22;

        //信号源
        int input = 111;

        //退出
        int exit = 121;

        //交替
        int last = 126;

        //屏显
        int display = 131;

        //0-9 number
        int number = 457;


    }

}
