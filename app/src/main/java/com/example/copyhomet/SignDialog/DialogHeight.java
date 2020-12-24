package com.example.copyhomet.SignDialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

public class DialogHeight extends AppCompatActivity {


    EditText edtHeight;
    Button Insert,Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_height);

        edtHeight =findViewById(R.id.edtHeight);
        Insert = findViewById(R.id.Insert);
        Cancel =findViewById(R.id.Cancel);

        Cancel.setOnClickListener(n->{
            setResult(0);
            finish();
        });
        Insert.setOnClickListener(n->{

            String Height = edtHeight.getText().toString();

            Intent intent = new Intent();
            intent.putExtra("height",Height);
            setResult(1,intent);
            finish();
        });

    }
}
