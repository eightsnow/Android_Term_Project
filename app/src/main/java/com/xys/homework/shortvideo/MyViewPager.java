package com.xys.homework.shortvideo;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {

    private long lastClickTime = 0;
    private long INTERVAL = 200;
    private boolean timeup;
    float downX;
    float downY;
    boolean isSlideing;
    boolean dontSlide;
    private float slideMove;
    private float slideClick;

    public MyViewPager(@NonNull Context context) {
        super(context);
        init();
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        slideMove = getResources().getDimension(R.dimen.move);
        slideClick = getResources().getDimension(R.dimen.click);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                //获取点击时间
                long currTime = System.currentTimeMillis();
                //判断点击之间的时间差
                long interval = currTime - lastClickTime;
                lastClickTime = currTime;
                if (interval < INTERVAL)
                    timeup = false;
                else
                    timeup = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(listener!=null){
                    float moveX = event.getRawX();
                    float moveY = event.getRawY();
                    float slideX = moveX-downX;
                    float slideY = moveY-downY;
                    if(isSlideing){
                        downX = moveX;
                    }else{
                        if(Math.abs(slideX) > slideMove && !dontSlide){
                            isSlideing = true;
                            downX = moveX;
                        }else if(Math.abs(slideY) > slideMove){
                            dontSlide = true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!isSlideing){
                    float upX = event.getRawX();
                    float upY = event.getRawY();

                    if (Math.abs(upX - downX) < slideClick && Math.abs(upY - downY) < slideClick) {
                        //单击事件
                        if(listener != null){
                            if(timeup){
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(timeup){
                                            listener.onClick();
                                        }
                                    }
                                },INTERVAL + 10);
                            }
                        }
                    }
                }
                isSlideing = false;
                dontSlide = false;
                break;
            default: break;
        }
        return super.dispatchTouchEvent(event);
    }

    private OnViewPagerTouchEvent listener;

    public void setOnViewPagerTouchEventListener(OnViewPagerTouchEvent l){
        listener = l;
    }

    public interface OnViewPagerTouchEvent{
        void onClick();
    }
}
