package com.example.copyhomet.SignDialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

public class DialogBirth extends AppCompatActivity {

    EditText edtAge;
    Button Insert,Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_birth);

        edtAge = findViewById(R.id.edtAge);
        Insert = findViewById(R.id.Insert);
        Cancel = findViewById(R.id.Cancel);

        Cancel.setOnClickListener(n->{
            setResult(0);
            finish();
        });
        Insert.setOnClickListener(n->{
//            String year = String.valueOf(datePicker.getYear());
//            String Month = String.valueOf(datePicker.getMonth());
//            String day = String.valueOf(datePicker.getDayOfMonth());
            String Age = edtAge.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("birth",Age);
            setResult(1,intent);
            finish();
        });

    }
}
