package com.example.copyhomet.Bluetooth.Bluetooth3.BLE;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.Bluetooth.Bluetooth3.UART.UARTManager3;
import com.example.copyhomet.R;

public class DialogDevice3Droped extends AppCompatActivity {

    Button btnContinue, btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device_droped);

        btnContinue = findViewById(R.id.btnContinue);
        btnFinish = findViewById(R.id.btnFinish);


        btnContinue.setOnClickListener(n->{
            if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF0F0001FE");
            }
            setResult(81);
            finish();
        });
        btnFinish.setOnClickListener(n->{
            if(UARTManager3.getManager()!=null){
                UARTManager3.getManager().send("FF100001FE");
            }
            setResult(80);
            finish();
        });


    }
}
