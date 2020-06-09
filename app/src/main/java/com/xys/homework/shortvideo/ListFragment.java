package com.xys.homework.shortvideo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListFragment extends Fragment {

    private SwipeRefreshLayout mSwipe;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private LinearLayout bar;
    private int barHeight;

    ListFragment(int barHeight){
        super();
        this.barHeight = barHeight;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_list, container, false);
        bar = view.findViewById(R.id.bar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bar.getLayoutParams();
        params.height = barHeight;
        bar.setLayoutParams(params);

        mSwipe = view.findViewById(R.id.mswipeRefreshLayout);
        //mSwipe.setColorSchemeResources(R.color.swipeColor1,R.color.swipeColor2,R.color.swipeColor3,R.color.swipeColor4,R.color.swipeColor5);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新需执行的操作
                getDataFromNetwork();
            }
        });

        recyclerView = view.findViewById(R.id.list_video);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ListAdapter(this.getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ListAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex, String videoUrl) {
                gotoVideoActivity(videoUrl);
            }
        });

        getDataFromNetwork();

        return view;
    }

    private void getDataFromNetwork(){
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
                        adapter.setData(videos);
                        adapter.notifyDataSetChanged();
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

    private void gotoVideoActivity(String videoUrl){
        Intent intent = new Intent(this.getActivity(), VideoActivity.class);
        intent.putExtra("VideoUrl", videoUrl);
        startActivity(intent);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}
