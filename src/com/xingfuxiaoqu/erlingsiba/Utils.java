package com.xingfuxiaoqu.erlingsiba;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class Utils {
    /**
     * 这里使用位的移动来计算2的N次方法
     */
    static int toBinary(int value) {
        int i = 0;
        do {
            value = (value >> 1);
            i++;
        } while (value != 1);
        return i - 1;

    }

    /**
     * 翻译特制版
     */
    static String translation2other(Context context, int num, String model) {
        String str = "";
        if (isNullOrEmpty(model)) {
            str = Utils.getFromAssets(context, "properties1.txt");
        } else {
            str = Utils.getFromAssets(context, model);
        }
        String strlist[] = str.split(",");
        return strlist[Utils.toBinary(num)];
    }

    /**
     * 读取assets文件
     */
    static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                result += line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 检测字符串是否为空或无内容
     * 
     * @param srcString
     * @return
     */
    static boolean isNullOrEmpty(String srcString) {
        if (TextUtils.isEmpty(srcString)) {
            return true;
        }
        return srcString.toLowerCase().equals("null");
    }

    /**
     * 获得屏幕宽度单位px
     * 
     * @param context
     * @return
     */
    static int getWith(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;// 屏幕的宽px
    }

    /**
     * 创建渐变尺寸缩放动画
     */
    static void createScaleAnimation(View v) {
        ScaleAnimation ani = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ani.setDuration(500);
        v.startAnimation(ani);
    }

    /**
     * 创建旋转动画
     */
    static void createRotateAnimation(View v) {
        RotateAnimation ani = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ani.setDuration(500);
        v.startAnimation(ani);
    }

    /**
     * 创建移动动画
     */
    static void createTranslateAnimatio(View v, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        // 初始化
        Animation translateAnimation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        // 设置动画时间
        translateAnimation.setDuration(1000);
        v.startAnimation(translateAnimation);
    }

    /**
     * 渐变透明度动画
     */
    static void createAlphaAnimation(View v) {
        // 初始化
        Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        // 设置动画时间
        alphaAnimation.setDuration(800);
        v.startAnimation(alphaAnimation);
    }

    /**
     * 创建抖动动画
     */
    static void createJitterAnimation(Context context, View v) {
        Animation cycleAnim = AnimationUtils.loadAnimation(context, R.anim.anim);
        v.startAnimation(cycleAnim);
    }
}
