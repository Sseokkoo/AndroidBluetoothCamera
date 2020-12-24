package com.example.copyhomet.Graph;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

public class DialogSelectGraphValue extends AppCompatActivity {


    TextView tvDay,tvYear,tvCancel;

    /**그래프 값 설정하기**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_value_menu);


        tvDay = findViewById(R.id.tvDay);
        tvYear = findViewById(R.id.tvYear);
        tvCancel = findViewById(R.id.tvCancel);

        tvDay.setOnClickListener(n->{
            Intent intent = new Intent(DialogSelectGraphValue.this,CalendarRange.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            finish();
        });
        tvYear.setOnClickListener(n->{
            Intent intent = new Intent(DialogSelectGraphValue.this,DialogSelectYear.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            finish();
        });
        tvCancel.setOnClickListener(n->{
            finish();
        });
    }
}
