package com.xys.homework.shortvideo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoActivity extends AppCompatActivity {

    private VideoPlayerTouchView videoTouchView;
    private VideoPlayerIJK ijkPlayer;
    private RelativeLayout rl_change_progress;
    private TextView tv_change_progress;
    private ImageView iv_change_progress;
    private boolean isFirst = true;//用于判断是否刚进入Activity

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏工具栏
        setContentView(R.layout.activity_video);
        //设为全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //获取参数
        Intent intent = getIntent();
        String videoUrl = intent.getStringExtra("VideoUrl");

        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }

        //获取控件
        ijkPlayer = findViewById(R.id.ijkPlayer);
        rl_change_progress = findViewById(R.id.rl_change_progress);
        tv_change_progress = findViewById(R.id.tv_change_progress);
        iv_change_progress = findViewById(R.id.iv_change_progress);

        //设置监听事件
        initTouchView();
        ijkPlayer.setListener(new VideoPlayerListener(){
            //根据视频宽高判断屏幕方向
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                super.onPrepared(iMediaPlayer);
                int width = iMediaPlayer.getVideoWidth();
                int height = iMediaPlayer.getVideoHeight();
                if(width > height)
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });
        //设置视频路径
        ijkPlayer.setVideoPath(videoUrl);

    }

    //设置TouchView的监听事件
    private void initTouchView() {
        videoTouchView = findViewById(R.id.videoPlayerTouchView);
        videoTouchView.setOnTouchListener(new VideoPlayerTouchView.MyTouchListener() {
            @Override
            public void onClick(){
                if(ijkPlayer.isPlaying())
                    ijkPlayer.pause();
                else
                    ijkPlayer.start();
            }

            @Override
            public void onSlide(float distant){
                if(ijkPlayer == null){
                    return;
                }
                if(!rl_change_progress.isShown()){
                    rl_change_progress.setVisibility(View.VISIBLE);
                    changeProgressTime = ijkPlayer.getCurrentPosition();
                }
                changeProgressText(distant);
            }
            @Override
            public void onUp(){
                if(rl_change_progress.isShown()){
                    rl_change_progress.setVisibility(View.GONE);
                }
                ijkPlayer.seekTo(changeProgressTime);
                ijkPlayer.start();
            }
        });
    }

    long changeProgressTime;
    private void changeProgressText(float distant){

        long duration = ijkPlayer.getDuration();
        long currentPosition = ijkPlayer.getCurrentPosition();
        float radio = distant/videoTouchView.getWidth();
        changeProgressTime += duration*radio;

        if(changeProgressTime < 0){
            changeProgressTime = 0;
        }
        if(changeProgressTime > duration){
            changeProgressTime = duration;
        }

        String changeTimeStr = Util.fromMMss(changeProgressTime);
        String rawTime = Util.fromMMss(duration);
        tv_change_progress.setText(new StringBuilder(changeTimeStr+" / "+rawTime));

        if(changeProgressTime > currentPosition){
            iv_change_progress.setImageResource(R.mipmap.video_fast_forward);
        }else{
            iv_change_progress.setImageResource(R.mipmap.video_fast_back);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ijkPlayer.isPlaying()) {
            ijkPlayer.stop();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isFirst){
            try {
                IjkMediaPlayer.loadLibrariesOnce(null);
                IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            } catch (Exception e) {
                this.finish();
            }
            ijkPlayer.load();
        }
        else{
            isFirst = false;
        }
    }

}
