package com.xys.homework.shortvideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TikTokAdapter extends RecyclerView.Adapter<TikTokAdapter.ViewHolder> {

    private List<VideoInfo> mDataset;
    private boolean[] isLike;
    private Context context;

    public TikTokAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<VideoInfo> mDataset) {
        this.mDataset = mDataset;
        isLike = new boolean[mDataset.size()];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_tiktok_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, List<Object> payloads) {
        if(payloads.isEmpty())
            onBindViewHolder(holder, position);
        else{
            switch ((String)payloads.get(0)){
                case "like":
                    holder.likeView.setImageResource(R.mipmap.like);
                    holder.likeCountView.setText(Util.IntToString(mDataset.get(position).getLikecount()));
                    break;
                case "click":
                    if(holder.ijkPlayer.isPlaying())
                        holder.ijkPlayer.pause();
                    else
                        holder.ijkPlayer.start();
                    break;
                case "leave":
                    holder.ijkPlayer.pause();
                    break;
                case "comeback":
                    holder.ijkPlayer.start();
                    break;
                case "play":
                    holder.ijkPlayer.setVideoPath(mDataset.get(position).getFeedurl());
                    holder.ijkPlayer.resumeSurfaceView();
                    break;
                case "stop":
                    holder.ijkPlayer.reset();
                    holder.ijkPlayer.deleteSurfaceView();
                    break;
                default:break;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoInfo videoInfo = mDataset.get(position);
        holder.mTvTitle.setText(videoInfo.getDescripiton());
        holder.mTvName.setText(new StringBuilder("@"+videoInfo.getNickname()));
        if(isLike[position])
            holder.likeView.setImageResource(R.mipmap.like);
        else
            holder.likeView.setImageResource(R.mipmap.like_before);
        holder.likeCountView.setText(Util.IntToString(videoInfo.getLikecount()));
        Glide.with(context)
                .load(mDataset.get(position).getAvatar())
                .placeholder(R.drawable.akkarin)
                .thumbnail(0.2f)
                .into(holder.avatarView);
        holder.videoTikTokTouchView.setOnTouchListener(new VideoTikTokTouchView.MyTouchListener() {
            @Override
            public void doubleClick(){
                if(!isLike[position])
                    mDataset.get(position).setLikecount(videoInfo.getLikecount() + 1);
                isLike[position] = true;
                notifyItemChanged(position, "like");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle;
        private TextView mTvName;
        private CircleImageView avatarView;
        private ImageView likeView;
        private TextView likeCountView;
        private VideoTikTokTouchView videoTikTokTouchView;
        private VideoPlayerIJK ijkPlayer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvName = itemView.findViewById(R.id.tv_nickname);
            avatarView = itemView.findViewById(R.id.iv_avatar);
            likeView = itemView.findViewById(R.id.iv_like);
            likeCountView = itemView.findViewById(R.id.tv_likecount);
            videoTikTokTouchView = itemView.findViewById(R.id.videoTikTokTouchView);
            ijkPlayer = itemView.findViewById(R.id.ijkPlayer);
        }
    }

}
