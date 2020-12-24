package com.example.copyhomet.Graph;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogSelectYear extends AppCompatActivity {

    NumberPicker pickerYear;
    Button btnSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_graph_year_picker);

        SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        pickerYear = findViewById(R.id.picker_year);
        btnSelect = findViewById(R.id.btnSelectYear);

        long now = System.currentTimeMillis();
        Date Todate = new Date(now);
        SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy");
        String DATE = sdfNOW.format(Todate);

        pickerYear.setMaxValue(Integer.parseInt(DATE));
        pickerYear.setMinValue(1980);
        pickerYear.setValue(Integer.parseInt(pref.getString("SelectYear",DATE)));
//        pickerYear.setClickable(false);
        pickerYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        btnSelect.setOnClickListener(n->{
            editor.putString("SelectYear","2020");
            editor.commit();

            Intent intent = new Intent();
            intent.putExtra("SelectYear",String.valueOf(pickerYear.getValue()));
            setResult(3001,intent);
            finish();
        });


    }
}
