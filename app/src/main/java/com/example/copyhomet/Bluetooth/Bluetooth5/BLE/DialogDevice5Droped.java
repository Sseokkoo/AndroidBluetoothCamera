package com.example.copyhomet.Bluetooth.Bluetooth5.BLE;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.Bluetooth.Bluetooth5.UART.UARTManager5;
import com.example.copyhomet.R;

public class DialogDevice5Droped extends AppCompatActivity {

    Button btnContinue, btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device_droped);

        btnContinue = findViewById(R.id.btnContinue);
        btnFinish = findViewById(R.id.btnFinish);


        btnContinue.setOnClickListener(n->{
            if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF0F0001FE");
            }
            setResult(81);
            finish();
        });
        btnFinish.setOnClickListener(n->{
            if(UARTManager5.getManager()!=null){
                UARTManager5.getManager().send("FF100001FE");
            }
            setResult(80);
            finish();
        });


    }
}
