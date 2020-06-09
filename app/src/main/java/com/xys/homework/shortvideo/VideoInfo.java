package com.xys.homework.shortvideo;

import com.google.gson.annotations.SerializedName;

public class VideoInfo {
    @SerializedName("errorCode")
    public int errorCode;
    @SerializedName("errorMsg")
    public String errorMsg;
    @SerializedName("_id")
    private String id;
    @SerializedName("feedurl")
    private String feedurl;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("description")
    private String descripiton;
    @SerializedName("likecount")
    private int likecount;
    @SerializedName("avatar")
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedurl() {
        return feedurl;
    }

    public void setFeedurl(String feedurl) {
        this.feedurl = feedurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescripiton() {
        return descripiton;
    }

    public void setDescripiton(String descripiton) {
        this.descripiton = descripiton;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
