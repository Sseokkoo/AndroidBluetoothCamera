package com.example.copyhomet.SignDialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

public class DialogWeight extends AppCompatActivity {


    Button Insert,Cancel;
    EditText edtWeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_weight);

        edtWeight =findViewById(R.id.edtWeight);
        Insert = findViewById(R.id.Insert);
        Cancel =findViewById(R.id.Cancel);

        Cancel.setOnClickListener(n->{
            setResult(0);
            finish();
        });
        Insert.setOnClickListener(n->{
            String Weight = edtWeight.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("Weight",Weight);
            setResult(1,intent);
            finish();
        });


    }
}
