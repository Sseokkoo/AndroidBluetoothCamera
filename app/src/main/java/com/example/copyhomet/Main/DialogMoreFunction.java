package com.example.copyhomet.Main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.Camera.CameraActivity;
import com.example.copyhomet.R;
import com.example.copyhomet.WatchingVideoActivity;

public class DialogMoreFunction extends AppCompatActivity {

    TextView tvExVideo,tvCancel,tvCorrection,tvCustomEx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more_function);

        tvCustomEx = findViewById(R.id.tvCustomEx);
        tvCustomEx.setOnClickListener(n->{
            if(MainActivity.getMain2().getStartState()==1) {
                MainActivity.getMain2().PlayClick();
            }
//            Intent intent = new Intent(DialogMoreFunction.this,CustomExActivity.class);
//            startActivity(intent);
            Intent intent = new Intent(DialogMoreFunction.this,DialogCustomExMode.class);
            startActivity(intent);
            finish();
        });

        tvCancel = findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(n->{
            finish();
        });
        tvExVideo = findViewById(R.id.tvExVideo);
        tvExVideo.setOnClickListener(n->{
            Intent intent = new Intent(DialogMoreFunction.this, WatchingVideoActivity.class);
            startActivity(intent);
            finish();
        });
        tvCorrection = findViewById(R.id.tvCorrection);
        tvCorrection.setOnClickListener(n->{
            Intent intent = new Intent(DialogMoreFunction.this, CameraActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });


    }
}
