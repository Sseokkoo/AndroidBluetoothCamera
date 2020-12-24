package com.example.copyhomet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

public class DialogExTimeSet extends AppCompatActivity {


    Button Plus,Minus,Insert,Cancel;
    TextView set21,set26,set31;
    TextView setTime;

    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_extime_set);


        pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);
        editor = pref.edit();

        Plus= findViewById(R.id.plus);
        Minus = findViewById(R.id.minus);


        set21 = findViewById(R.id.SetMin21);
        set26 = findViewById(R.id.SetMin26);
        set31 = findViewById(R.id.SetMin31);

//        Insert = findViewById(R.id.Success);
        Cancel = findViewById(R.id.cancel);
        Cancel.setOnClickListener(n->{
            finish();
        });

//        setTime = findViewById(R.id.tvsetTime);


//        Plus.setOnClickListener(n ->{
//            int time = Integer.parseInt(setTime.getText().toString());
//            time++;
//            setTime.setText(String.valueOf(time));
//        });
//        Minus.setOnClickListener(n->{
//            if(!setTime.getText().toString().equals("0")) {
//                int time = Integer.parseInt(setTime.getText().toString());
//                time--;
//                setTime.setText(String.valueOf(time));
//            }else{
//            }
//        });

        set21.setOnClickListener(n->{
            editor.putString("ExTime","21:00");
            editor.putString("ExTimeMode","21");
            editor.commit();
            Intent intent = new Intent();
            intent.putExtra("exTime","21");
            intent.putExtra("exTimeMode","21");
            setResult(1010,intent);
            finish();
        });
        set26.setOnClickListener(n->{
            editor.putString("ExTime","26:00");
            editor.putString("ExTimeMode","26");
            editor.commit();
            Intent intent = new Intent();
            intent.putExtra("exTime","26");
            intent.putExtra("exTimeMode","26");
            setResult(1010,intent);
            finish();
        });
        set31.setOnClickListener(n->{
            editor.putString("ExTime","31:00");
            editor.putString("ExTimeMode","31");
            editor.commit();
            Intent intent = new Intent();
            intent.putExtra("exTime","31");
            intent.putExtra("exTimeMode","31");
            setResult(1010,intent);
            finish();
        });
//        Insert.setOnClickListener(n->{
//            Intent intent = new Intent();
//            intent.putExtra("exTime",setTime.getText().toString());
//            setResult(1010,intent);
//            finish();
//        });
    }
}
