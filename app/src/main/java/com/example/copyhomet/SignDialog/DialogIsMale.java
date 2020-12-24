package com.example.copyhomet.SignDialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.R;

public class DialogIsMale extends AppCompatActivity {


    CheckBox chkMale,chkFeMale;
    Button Insert,Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ismale);



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

        Cancel.setOnClickListener(n->{
            setResult(0);
            finish();
        });
        Insert.setOnClickListener(n->{
            String isMale;
            if(chkMale.isChecked()) {
                isMale = "Male";
            }else {
                isMale = "FeMale";
            }
            Intent intent = new Intent();
            intent.putExtra("isMale",isMale);
            setResult(1,intent);
            finish();
        });

}
}
