package com.xys.homework.shortvideo;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class Util {

    public static ObjectAnimator scale(View view, String propertyName, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , propertyName
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationX(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationX"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationY(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationY"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator alpha(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "alpha"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator rotation(View view, long time, long delayTime, float... values) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", values);
        rotation.setDuration(time);
        rotation.setStartDelay(delayTime);
        rotation.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        return rotation;
    }

    public static String fromMMss(long time) {
        if (time < 0) {
            return "00:00";
        }

        int ss = (int) (time / 1000);
        int mm = ss / 60;
        int s = ss % 60;
        int m = mm % 60;
        String strM = String.valueOf(m);
        String strS = String.valueOf(s);
        if (m < 10) {
            strM = "0" + strM;
        }
        if (s < 10) {
            strS = "0" + strS;
        }
        return strM + ":" + strS;
    }

    public static String IntToString(int count){
        if(count / 10000 == 0)
            return count + "";
        else{
            if(count / 10000000 == 0)
                return count / 10000 + "." + count / 1000 % 10 + "w";
            else
                return count / 10000000 + "." + count / 1000000 % 10 + "kw";
        }
    }
}
