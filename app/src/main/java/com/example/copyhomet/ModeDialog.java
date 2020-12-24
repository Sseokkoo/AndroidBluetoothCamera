package com.example.copyhomet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

public class ModeDialog extends AppCompatActivity {

    Button Mode1,Mode2,Cancel;


    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mode);

         pref = getApplicationContext().getSharedPreferences("info",MODE_PRIVATE);
        editor = pref.edit();

        Mode1 = findViewById(R.id.mode_1);
        Mode1.setOnClickListener(n->{
//            editor.putString("mode","M");
//            editor.commit();
            Toast.makeText(ModeDialog.this,"근육 모드", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra("mode","M");
            setResult(1,intent);
            finish();
        });
        Mode2= findViewById(R.id.mode_2);
        Mode2.setOnClickListener(n->{

//            editor.putString("mode","S");
//            editor.commit();

            Toast.makeText(ModeDialog.this,"다이어트 모드", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent();
            intent.putExtra("mode","S");
            setResult(1,intent);
            finish();
        });

        Cancel = findViewById(R.id.cancel);
        Cancel.setOnClickListener(n->{
            finish();
        });
    }
}
