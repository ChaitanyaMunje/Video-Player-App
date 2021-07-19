package com.gtappdevelopers.videoplayerapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class VideoPlayerActivity extends AppCompatActivity {

    VideoView videoView;
    RelativeLayout zoomLayout;
    boolean isOpen = true;
    RelativeLayout controlsCL;
    ImageButton  rewind, forward, playPause;
    TextView title, endTime;
    SeekBar videoSeekBar;
    private String videoPath, videoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoPath = getIntent().getStringExtra("videoUrl");
        videoName = getIntent().getStringExtra("videoName");
        videoView = findViewById(R.id.idVideoview);
        controlsCL = findViewById(R.id.idCLController);
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoSeekBar.setMax(videoView.getDuration());
                videoView.start();
            }
        });

        title = findViewById(R.id.videoView_title);
        rewind = findViewById(R.id.videoView_rewind);
        playPause = findViewById(R.id.videoView_play_pause_btn);
        forward = findViewById(R.id.videoView_forward);
        endTime = findViewById(R.id.videoView_endtime);
        videoSeekBar = findViewById(R.id.videoView_seekbar);

        title.setText(videoName);
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition() - 10000);
            }
        });
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    playPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                } else {
                    videoView.start();
                    playPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                }
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition() + 10000);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                videoView.setVideoURI(Uri.parse(videoPath));
            }
        });
        zoomLayout = findViewById(R.id.zoom_layout);
        zoomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    hideDefaultControls();
                    isOpen = false;
                } else {
                    showDefaultControls();
                    isOpen = true;
                }
            }
        });

        setHandler();
        initializeSeekBars();
    }

    private String convertIntoTime(int ms) {
        String time;
        int x, seconds, minutes, hours;
        x = ms / 1000;
        seconds = x % 60;
        x /= 60;
        minutes = x % 60;
        x /= 60;
        hours = x % 24;
        if (hours != 0)
            time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        else time = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        return time;
    }

    private void setHandler() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.getDuration() > 0) {
                    int currentPosition = videoView.getCurrentPosition();
                    videoSeekBar.setProgress(currentPosition);
                    endTime.setText("" + convertIntoTime(videoView.getDuration() - currentPosition));
                }
                handler.postDelayed(this, 0);
            }
        };
        handler.postDelayed(runnable, 500);
    }

    private void initializeSeekBars() {
        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (videoSeekBar.getId() == R.id.videoView_seekbar) {
                    if (fromUser) {
                        videoView.seekTo(progress);
                        videoView.start();
                        int currentPosition = videoView.getCurrentPosition();
                        endTime.setText("" + convertIntoTime(videoView.getDuration() - currentPosition));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void hideDefaultControls() {
        controlsCL.setVisibility(View.GONE);

        //Todo this function will hide status and navigation when we click on screen
        final Window window = this.getWindow();
        if (window == null) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final View decorView = window.getDecorView();
        if (decorView != null) {
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= 14) {
                uiOption |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT >= 16) {
                uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    private void showDefaultControls() {
        controlsCL.setVisibility(View.VISIBLE);

        //todo this function will show status and navigation when we click on screen
        final Window window = this.getWindow();
        if (window == null) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        final View decorView = window.getDecorView();
        if (decorView != null) {
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= 14) {
                uiOption &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT >= 16) {
                uiOption &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOption &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }
    
}