package com.xys.homework.shortvideo;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    // https://beiyou.bytedance.com/api/invoke/video/invoke/video
    @GET("api/invoke/video/invoke/video")
    Call<List<VideoInfo>> getVideos();

}
