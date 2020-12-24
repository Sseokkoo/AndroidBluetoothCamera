package com.example.copyhomet.SignDialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

public class DialogNickname extends AppCompatActivity {

    EditText edtNickname;
    Button Insert, Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_nickname);

        edtNickname =findViewById(R.id.edtNickname);
        Insert = findViewById(R.id.Insert);
        Cancel =findViewById(R.id.Cancel);

        Cancel.setOnClickListener(n->{
            setResult(0);
            finish();
        });
        Insert.setOnClickListener(n->{
            String Nickname = edtNickname.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("Nickname",Nickname);
            setResult(1,intent);
            finish();
        });

    }
}
