package com.example.copyhomet;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.Main.MainTestActivity;
import com.example.copyhomet.R;

public class WatchingVideoActivity extends AppCompatActivity {
    VideoView vv;

    LinearLayout VideoViewLayout,SelectVideoLayout;
    ImageView Image1,Image2;
    LinearLayout close;
    Button ExButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching_video);

        getSupportActionBar().hide();

        close = findViewById(R.id.close);
        close.setOnClickListener(n->{
            onBackPressed();
        });
        VideoViewLayout = findViewById(R.id.VideoViewLayout);
        SelectVideoLayout = findViewById(R.id.SelectVideoLayout);
        VideoViewLayout.setVisibility(View.GONE);

        Image1=findViewById(R.id.thumb_nail1);

        GradientDrawable drawable=
                (GradientDrawable) this.getDrawable(R.drawable.imageview_rounding);
        Image1.setBackground(drawable);
        Image1.setClipToOutline(true);
        Image1.setImageDrawable(getResources().getDrawable(R.drawable.thumb_nail1));

        Image2=findViewById(R.id.thumb_nail2);
        Image2.setBackground(drawable);
        Image2.setClipToOutline(true);
        Image2.setImageDrawable(getResources().getDrawable(R.drawable.thumb_nail2));


        Image1.setOnClickListener(n->{
            VideoViewLayout.setVisibility(View.VISIBLE);
            SelectVideoLayout.setVisibility(View.GONE);
            vv= findViewById(R.id.vv);
            Uri videoUri = Uri.parse("android.resource://"+getPackageName() + "/" + R.raw.video1);
            vv.setMediaController(new MediaController(this));
            vv.setVideoURI(videoUri);

            vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    //비디오 시작
                    vv.start();
                }
            });
            vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    SelectVideoLayout.setVisibility(View.VISIBLE);
                    VideoViewLayout.setVisibility(View.GONE);

                }
            });
        });
        Image2.setOnClickListener(n->{
            VideoViewLayout.setVisibility(View.VISIBLE);
            SelectVideoLayout.setVisibility(View.GONE);
            vv= findViewById(R.id.vv);
            Uri videoUri = Uri.parse("android.resource://"+getPackageName() + "/" + R.raw.video2);
            vv.setMediaController(new MediaController(this));
            vv.setVideoURI(videoUri);

            vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    //비디오 시작
                    vv.start();
                }
            });

            vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    SelectVideoLayout.setVisibility(View.VISIBLE);
                    VideoViewLayout.setVisibility(View.GONE);

                }
            });
        });

        ExButton = findViewById(R.id.ExButton);
        ExButton.setOnClickListener(n->{

            Intent intent = new Intent(WatchingVideoActivity.this, MainTestActivity.class);
            startActivity(intent);

        });


    }//onCreate ..

    //화면에 안보일때...
    @Override
    protected void onPause() {
        super.onPause();

        //비디오 일시 정지
        if(vv!=null && vv.isPlaying()) vv.pause();
    }
    //액티비티가 메모리에서 사라질때..
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //
        if(vv!=null) vv.stopPlayback();
    }

    @Override
    public void onBackPressed() {
        if(vv!=null && vv.isPlaying()) vv.pause();

        if(VideoViewLayout.getVisibility() == View.VISIBLE){
            VideoViewLayout.setVisibility(View.GONE);
            SelectVideoLayout.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }


    }
}
