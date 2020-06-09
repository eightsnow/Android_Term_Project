package com.xys.homework.shortvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final int PAGE_COUNT = 2;
    private LinearLayout statusBar;
    private int pos;
    private MainActivity mainActivity = this;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        final float statusBarHight = getStatusBarHeight();
        setHight(statusBarHight);

        final int tabHeight = dip2px(this, 40);

        MyViewPager pager = findViewById(R.id.view_pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0)
                    return new ListFragment((int) statusBarHight + tabHeight);
                else
                    return new TikTokFragment();
            }

            @Override
            public int getCount() {
                return PAGE_COUNT;
            }

        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                TikTokFragment tikTokFragment = (TikTokFragment)mainActivity.getSupportFragmentManager()
                        .findFragmentByTag("android:switcher:" + R.id.view_pager + ":1");
                if(tikTokFragment != null && tikTokFragment.adapter != null){
                    if(pos == 0)
                        tikTokFragment.adapter.notifyItemChanged(tikTokFragment.pos, "leave");
                    else{
                        if(tikTokFragment.isFirst){
                            tikTokFragment.isFirst = false;
                            tikTokFragment.adapter.notifyItemChanged(tikTokFragment.pos, "play");
                        }
                        else
                            tikTokFragment.adapter.notifyItemChanged(tikTokFragment.pos, "comeback");
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pager.setOnViewPagerTouchEventListener(new MyViewPager.OnViewPagerTouchEvent() {
            @Override
            public void onClick() {
                if(pos == 1){
                    TikTokFragment tikTokFragment = (TikTokFragment)mainActivity.getSupportFragmentManager()
                            .findFragmentByTag("android:switcher:" + R.id.view_pager + ":1");
                    if(tikTokFragment != null && tikTokFragment.adapter != null)
                        tikTokFragment.adapter.notifyItemChanged(tikTokFragment.pos, "click");
                }
            }
        });

        // 添加 TabLayout 支持 Tab
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0)
                    return new ListFragment((int) statusBarHight + tabHeight);
                else
                    return new TikTokFragment();
            }

            @Override
            public int getCount() {
                return PAGE_COUNT;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0)
                    return "列表模式";
                else
                    return "全屏模式";
            }
        });
        tabLayout.setupWithViewPager(pager);
    }

    public float getStatusBarHeight() {
        float result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = getResources().getDimension(resourceId);
        return result;
    }

    private void setHight(float statusBarHight) {
        statusBar = findViewById(R.id.statusBar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) statusBar.getLayoutParams();
        params.height = (int) statusBarHight;
        statusBar.setLayoutParams(params);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isFirst){
            TikTokFragment tikTokFragment = (TikTokFragment)mainActivity.getSupportFragmentManager()
                    .findFragmentByTag("android:switcher:" + R.id.view_pager + ":1");
            if(pos == 1 && tikTokFragment != null && tikTokFragment.adapter != null) {
                tikTokFragment.adapter.notifyItemChanged(tikTokFragment.pos, "play");
                tikTokFragment.isFirst = false;
            }
        }
        else{
            isFirst = false;
        }
    }
}