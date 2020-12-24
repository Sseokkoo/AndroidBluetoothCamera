package com.example.copyhomet.Main;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

public class DialogReset extends AppCompatActivity {

    Button btnReset,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reset);

        btnReset = findViewById(R.id.btnReset);
        btnCancel = findViewById(R.id.Cancel);

        btnReset.setOnClickListener(n->{
            setResult(1);
            finish();
        });
        btnCancel.setOnClickListener(n->{
            setResult(2);
            finish();
        });

    }
}
