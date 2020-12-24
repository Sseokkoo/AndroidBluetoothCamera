package com.example.copyhomet.Main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;


public class DialogCustomExMode extends AppCompatActivity {

    TextView tvArm,tvABS,tvLEG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_ex_mode);

            tvArm = findViewById(R.id.tvArm);
            tvABS = findViewById(R.id.tvABS);
            tvLEG = findViewById(R.id.tvLEG);

            tvArm.setOnClickListener(n->{
                Intent intent = new Intent(DialogCustomExMode.this,CustomExActivity.class);
                startActivity(intent);
                finish();
            });
            tvABS.setOnClickListener(n->{
                Toast.makeText(this, "준비중인 기능입니다.", Toast.LENGTH_SHORT).show();
            });
            tvLEG.setOnClickListener(n->{
                Toast.makeText(this, "준비중인 기능입니다.", Toast.LENGTH_SHORT).show();
            });



    }
}
