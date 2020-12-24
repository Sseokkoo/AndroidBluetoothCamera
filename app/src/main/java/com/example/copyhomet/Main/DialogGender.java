package com.example.copyhomet.Main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

public class DialogGender extends AppCompatActivity {


    CheckBox chkMale,chkFeMale;
    Button Insert,Cancel;
    String isMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ismale);

        Intent get = getIntent();
        String gender = get.getStringExtra("Gender");

        chkFeMale = findViewById(R.id.chkFemale);
        chkFeMale.setOnClickListener(n->{
            chkMale.setChecked(false);
        });
        chkMale =findViewById(R.id.chkMale);
        chkMale.setOnClickListener(n->{
            chkFeMale.setChecked(false);
        });
        Insert = findViewById(R.id.Insert);
        Cancel =findViewById(R.id.Cancel);


        if(gender.equals("M")) {
            chkMale.setChecked(true);
        }else{
            chkFeMale.setChecked(true);
        }


        Cancel.setOnClickListener(n->{
            setResult(0);
            finish();
        });
        isMale = "male";
        Insert.setOnClickListener(n->{
            if(chkMale.isChecked()) {
                isMale = "male";
            }else {
                isMale = "female";
            }
            Intent intent = new Intent();
            intent.putExtra("gender",isMale);
            setResult(1,intent);
            finish();
        });

}
}
