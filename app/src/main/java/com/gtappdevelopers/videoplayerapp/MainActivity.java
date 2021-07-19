package com.gtappdevelopers.videoplayerapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements VideoRVAdapter.VideoClickInterface {

    private static final int STORAGE_PERMISSION_CODE = 101;
    private ArrayList<VideoModal> videoModalArrayList;
    private VideoRVAdapter videoRVAdapter;
    private RecyclerView videoRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoModalArrayList = new ArrayList<>();
        videoRV = findViewById(R.id.idRVVideos);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        videoRV.setLayoutManager(layoutManager);
        videoRVAdapter = new VideoRVAdapter(videoModalArrayList, this,this::onVideoClick);
        videoRV.setAdapter(videoRVAdapter);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            getVideos();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] per, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, per, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                getVideos();
            } else {
                Toast.makeText(MainActivity.this, "Sorry you cannot use this app without the permissions..", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void getVideos() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(data, MediaStore.Images.Thumbnails.MINI_KIND);

                videoModalArrayList.add(new VideoModal(data, thumb, title));

            } while (cursor.moveToNext());
        }
        videoRVAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVideoClick(int position) {
        Intent i = new Intent(MainActivity.this, VideoPlayerActivity.class);
        i.putExtra("videoUrl", videoModalArrayList.get(position).getVideoPath());
        i.putExtra("videoName", videoModalArrayList.get(position).getVideoName());
        startActivity(i);
    }
}