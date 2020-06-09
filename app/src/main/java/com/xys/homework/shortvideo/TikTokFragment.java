package com.xys.homework.shortvideo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TikTokFragment extends Fragment {

    private SwipeRefreshLayout mSwipe;
    private ViewPager2 viewPager2;
    public TikTokAdapter adapter;
    public int pos = 0;
    public boolean isFirst = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_tiktok, container, false);

        mSwipe = view.findViewById(R.id.mswipeRefreshLayout);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新需执行的操作
                getDataFromNetwork(view, true);
            }
        });

        viewPager2 = view.findViewById(R.id.viewpager2);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        adapter = new TikTokAdapter(this.getActivity());
        viewPager2.setAdapter(adapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                System.out.println(position);
                pos = position;
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!isFirst){
                            adapter.notifyItemChanged(position, "play");
                            if (position > 0)
                                adapter.notifyItemChanged(position - 1, "stop");
                            if (position < adapter.getItemCount())
                                adapter.notifyItemChanged(position + 1, "stop");
                        }
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        getDataFromNetwork(view, false);

        return view;
    }

    private void getDataFromNetwork(View view, boolean isSwipe){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getVideos().enqueue(new Callback<List<VideoInfo>>() {
            @Override
            public void onResponse(Call<List<VideoInfo>> call, Response<List<VideoInfo>> response) {
                if (response.body() != null) {
                    List<VideoInfo> videos = response.body();
                    if (videos.size() != 0) {
                        adapter.notifyItemChanged(pos, "stop");
                        adapter.setData(videos);
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        if(isSwipe){
                            view.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyItemChanged(pos, "play");
                                }
                            },100);
                        }
                        mSwipe.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<VideoInfo>> call, Throwable t) {
                //无网络
                mSwipe.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onPause() {
        adapter.notifyItemChanged(pos, "stop");
        isFirst= true;
        super.onPause();
    }
}
