package com.gtappdevelopers.videoplayerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VideoRVAdapter extends RecyclerView.Adapter<VideoRVAdapter.ViewHolder> {
    private ArrayList<VideoModal> videoModalArrayList;
    private Context context;
    private VideoClickInterface videoClickInterface;

    public VideoRVAdapter(ArrayList<VideoModal> videoModalArrayList, Context context,VideoClickInterface videoClickInterface) {
        this.videoModalArrayList = videoModalArrayList;
        this.context = context;
        this.videoClickInterface = videoClickInterface;
    }

    @NonNull
    @Override
    public VideoRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoRVAdapter.ViewHolder holder, int position) {
        VideoModal modal = videoModalArrayList.get(position);
        holder.videoThumbIV.setImageBitmap(modal.getThumbnailBitMap());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoClickInterface.onVideoClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView videoThumbIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumbIV = itemView.findViewById(R.id.idIVVideoThumbNail);
        }
    }

    public interface VideoClickInterface{
        void onVideoClick(int position);
    }
}
