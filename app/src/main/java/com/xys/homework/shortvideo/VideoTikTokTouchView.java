package com.xys.homework.shortvideo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import java.util.Random;

public class VideoTikTokTouchView extends RelativeLayout {
    private Context mContext;
    private VideoTikTokTouchView.MyTouchListener onTouchListener;
    private float[] num = {-30, -20, 0, 20, 30};//随机心形图片角度
    private static int HEARTSIZE = 300;
    //记录上一次的点击时间
    private long lastClickTime = 0;
    //点击的时间间隔
    private long INTERVAL = 200;

    public VideoTikTokTouchView(Context context) {
        super(context);
        initView(context);
    }

    public VideoTikTokTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VideoTikTokTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取点击时间
                long currTime = System.currentTimeMillis();
                //判断点击之间的时间差
                long interval = currTime - lastClickTime;
                lastClickTime = currTime;
                if (interval < INTERVAL) {//小于1秒，拦截事件，并做处理
                    final ImageView imageView = new ImageView(mContext);
                    //设置展示的位置，需要在手指触摸的位置上方，即触摸点是心形的右下角的位置
                    LayoutParams params = new LayoutParams(HEARTSIZE, HEARTSIZE);
                    params.leftMargin = (int) event.getX() - HEARTSIZE / 2;
                    params.topMargin = (int) event.getY() - HEARTSIZE;
                    //设置图片资源
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.like));
                    imageView.setLayoutParams(params);
                    //把IV添加到父布局当中
                    addView(imageView);
                    //设置控件的动画
                    AnimatorSet animatorSet = new AnimatorSet();
                    //缩放动画，X轴2倍缩小至0.9倍
                    animatorSet.play(Util.scale(imageView, "scaleX", 2f, 0.9f, 100, 0))
                            //缩放动画，Y轴2倍缩放至0.9倍
                            .with(Util.scale(imageView, "scaleY", 2f, 0.9f, 100, 0))
                            //旋转动画，随机旋转角
                            .with(Util.rotation(imageView, 0, 0, num[new Random().nextInt(4)]))
                            //渐变透明动画，透明度从0-1
                            .with(Util.alpha(imageView, 0, 1, 100, 0))
                            //缩放动画，X轴0.9倍缩放至1倍
                            .with(Util.scale(imageView, "scaleX", 0.9f, 1, 50, 150))
                            //缩放动画，Y轴0.9倍缩放至1倍
                            .with(Util.scale(imageView, "scaleY", 0.9f, 1, 50, 150))
                            //位移动画，Y轴从0上移至600
                            .with(Util.translationY(imageView, 0, -600, 800, 400))
                            //透明动画，从1-0
                            .with(Util.alpha(imageView, 1, 0, 300, 400))
                            //缩放动画，X轴1至3倍
                            .with(Util.scale(imageView, "scaleX", 1, 3f, 700, 400))
                            //缩放动画，Y轴1至3倍
                            .with(Util.scale(imageView, "scaleY", 1, 3f, 700, 400));
                    //开始动画
                    animatorSet.start();
                    //设置动画结束监听
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //当动画结束以后，需要把控件从父布局移除
                            removeViewInLayout(imageView);
                        }
                    });
                    if(onTouchListener != null){
                        onTouchListener.doubleClick();
                    }
                }
                break;
            default:break;
        }
        return super.onTouchEvent(event);
    }

    //回调接口
    public interface MyTouchListener{
        void doubleClick();
    }

    //设置监听器
    public void setOnTouchListener(VideoTikTokTouchView.MyTouchListener onTouchListener){
        this.onTouchListener = onTouchListener;
    }
}
