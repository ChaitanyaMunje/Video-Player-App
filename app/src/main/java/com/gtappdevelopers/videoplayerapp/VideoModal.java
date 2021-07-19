package com.gtappdevelopers.videoplayerapp;

import android.graphics.Bitmap;

public class VideoModal {

    private String videoPath;
    private Bitmap thumbnailBitMap;
    private String videoName;

    public VideoModal(String videoPath, Bitmap thumbnailBitMap, String videoName) {
        this.videoPath = videoPath;
        this.thumbnailBitMap = thumbnailBitMap;
        this.videoName = videoName;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Bitmap getThumbnailBitMap() {
        return thumbnailBitMap;
    }

    public void setThumbnailBitMap(Bitmap thumbnailBitMap) {
        this.thumbnailBitMap = thumbnailBitMap;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }


}
