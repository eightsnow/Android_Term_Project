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

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private ListItemClickListener mOnClickListener;
    private List<VideoInfo> mDataset;
    private Context context;

    public ListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<VideoInfo> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.mTitleView.setText(mDataset.get(position).getDescripiton());
        holder.mNicknameView.setText(new StringBuilder("作者：" + mDataset.get(position).getNickname()));
        Glide.with(context)
                .load(mDataset.get(position).getFeedurl())
                .thumbnail(Glide.with(context).load(R.drawable.loading).fitCenter())
                .override(1080, 607)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTitleView;
        TextView mNicknameView;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleView = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.imageView);
            mNicknameView = itemView.findViewById(R.id.tv_nickname);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            if (mOnClickListener != null) {
                mOnClickListener.onListItemClick(clickedPosition, mDataset.get(clickedPosition).getFeedurl());
            }
        }

    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String videoUrl);
    }

    public void setOnItemClickListener(ListItemClickListener listItemClickListener) {
        this.mOnClickListener = listItemClickListener;
    }

}
