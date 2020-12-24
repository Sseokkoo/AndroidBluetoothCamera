package com.example.copyhomet.Bluetooth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.Bluetooth.Bluetooth1.BLE.BleProfileService;
import com.example.copyhomet.Bluetooth.Bluetooth1.Ble1Activity;
import com.example.copyhomet.Bluetooth.Bluetooth2.BLE.BleProfileService_2;
import com.example.copyhomet.Bluetooth.Bluetooth2.Ble2Activity;
import com.example.copyhomet.Bluetooth.Bluetooth3.BLE.BleProfileService_3;
import com.example.copyhomet.Bluetooth.Bluetooth3.Ble3Activity;
import com.example.copyhomet.Bluetooth.Bluetooth4.BLE.BleProfileService_4;
import com.example.copyhomet.Bluetooth.Bluetooth4.Ble4Activity;
import com.example.copyhomet.Bluetooth.Bluetooth5.BLE.BleProfileService_5;
import com.example.copyhomet.Bluetooth.Bluetooth5.Ble5Activity;
import com.example.copyhomet.R;

public class DialogBle extends AppCompatActivity {

    Button btnBLE1, btnBLE2, btnBLE3, btnBLE4, btnBLE5, Insert, Cancel;
    TextView tvBLE1, tvBLE2, tvBLE3, tvBLE4, tvBLE5;
    TextView Battery1,Battery2,Battery3,Battery4,Battery5;

    public static DialogBle dialogBle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ble);


        init();

    }
    public void init(){
        dialogBle = this;

        Insert = findViewById(R.id.Insert);
        Insert.setOnClickListener(n->{
            Intent intent = new Intent(DialogBle.this,DialogBleNext.class);
            startActivity(intent);
        });
        Cancel = findViewById(R.id.Cancel);
        Cancel.setOnClickListener(n->{
//            onBackPressed();
            finish();
        });
        btnBLE1 = findViewById(R.id.btnBLE1);
        btnBLE1.setOnClickListener(n -> {
            Intent intent = new Intent(DialogBle.this, Ble1Activity.class);
            startActivity(intent);
        });
        btnBLE2 = findViewById(R.id.btnBLE2);
        btnBLE2.setOnClickListener(n -> {
            Intent intent = new Intent(DialogBle.this, Ble2Activity.class);
            startActivity(intent);
        });
        btnBLE3 = findViewById(R.id.btnBLE3);
        btnBLE3.setOnClickListener(n -> {
            Intent intent = new Intent(DialogBle.this, Ble3Activity.class);
            startActivity(intent);
        });
        btnBLE4 = findViewById(R.id.btnBLE4);
        btnBLE4.setOnClickListener(n -> {
            Intent intent = new Intent(DialogBle.this, Ble4Activity.class);
            startActivity(intent);
        });
        btnBLE5 = findViewById(R.id.btnBLE5);
        btnBLE5.setOnClickListener(n -> {
            Intent intent = new Intent(DialogBle.this, Ble5Activity.class);
            startActivity(intent);
        });
        Battery1 = findViewById(R.id.Battery1);
        Battery2 = findViewById(R.id.Battery2);
        Battery3 = findViewById(R.id.Battery3);
        Battery4 = findViewById(R.id.Battery4);
        Battery5 = findViewById(R.id.Battery5);

        setBle();
    }

    public static DialogBle getDialogBle(){
        return dialogBle;
    }



    public void setBle(){



        if(BleProfileService.getBleProfile()!=null) {
            int ConnectionState =  BleProfileService.getBleProfile().getConnectionState();
            if(ConnectionState == 2){
                btnBLE1.setText("");
                btnBLE1.setTextColor(Color.parseColor("#ffffff"));
                btnBLE1.setBackground(getResources().getDrawable(R.drawable.btn_connect_touch));
                Battery1.setVisibility(View.VISIBLE);
                btnBLE1.setOnClickListener(n->{
                    BleProfileService.getBleProfile().disconnect();
//                    setBle();
                });
            }else{
                btnBLE1.setText("기기 연결");
                btnBLE1.setTextColor(Color.parseColor("#ffffff"));
                btnBLE1.setBackground(getResources().getDrawable(R.drawable.button_back_pinkred));
                btnBLE1.setOnClickListener(n->{
                    Intent intent = new Intent(DialogBle.this, Ble1Activity.class);
                    startActivity(intent);
                });
                Battery1.setVisibility(View.GONE);

            }
        } else{
            btnBLE1.setText("기기 연결");
            btnBLE1.setTextColor(Color.parseColor("#ffffff"));
            btnBLE1.setBackground(getResources().getDrawable(R.drawable.button_back_pinkred));
            btnBLE1.setOnClickListener(n->{
                Intent intent = new Intent(DialogBle.this, Ble1Activity.class);
                startActivity(intent);
            });
            Battery1.setVisibility(View.GONE);
        }





        if(BleProfileService_2.getBleProfile()!=null) {
            int ConnectionState =  BleProfileService_2.getBleProfile().getConnectionState();
            if(ConnectionState == 2){
                btnBLE2.setText("");
                btnBLE2.setTextColor(Color.parseColor("#ffffff"));
                btnBLE2.setBackground(getResources().getDrawable(R.drawable.btn_connect_touch));
                btnBLE2.setOnClickListener(n->{
                    BleProfileService_2.getBleProfile().disconnect();
//                    setBle();
                });
                Battery2.setVisibility(View.VISIBLE);
            }else{
                btnBLE2.setText("기기 연결");
                btnBLE2.setTextColor(Color.parseColor("#ffffff"));
                btnBLE2.setBackground(getResources().getDrawable(R.drawable.button_back_pinkred));
                btnBLE2.setOnClickListener(n->{
                    Intent intent = new Intent(DialogBle.this, Ble2Activity.class);
                    startActivity(intent);
                });
                Battery2.setVisibility(View.GONE);
            }
        }else{
            btnBLE2.setText("기기 연결");
            btnBLE2.setTextColor(Color.parseColor("#ffffff"));
            btnBLE2.setBackground(getResources().getDrawable(R.drawable.button_back_pinkred));
            btnBLE2.setOnClickListener(n->{
                Intent intent = new Intent(DialogBle.this, Ble2Activity.class);
                startActivity(intent);
            });
            Battery2.setVisibility(View.GONE);
        }
        if(BleProfileService_3.getBleProfile()!=null) {
            int ConnectionState =  BleProfileService_3.getBleProfile().getConnectionState();
            if(ConnectionState == 2){
                btnBLE3.setText("");
                btnBLE3.setTextColor(Color.parseColor("#ffffff"));
                btnBLE3.setBackground(getResources().getDrawable(R.drawable.btn_connect_touch));
                btnBLE3.setOnClickListener(n->{
                    BleProfileService_3.getBleProfile().disconnect();
//                    setBle();
                });
                Battery3.setVisibility(View.VISIBLE);
            }else{
                btnBLE3.setText("기기 연결");
                btnBLE3.setTextColor(Color.parseColor("#ffffff"));
                btnBLE3.setBackground(getResources().getDrawable(R.drawable.button_back_pinkred));
                btnBLE3.setOnClickListener(n->{
                    Intent intent = new Intent(DialogBle.this, Ble3Activity.class);
                    startActivity(intent);
                });
                Battery3.setVisibility(View.GONE);
            }
        }else{
            btnBLE3.setText("기기 연결");
            btnBLE3.setTextColor(Color.parseColor("#ffffff"));
            btnBLE3.setBackground(getResources().getDrawable(R.drawable.button_back_pinkred));
            btnBLE3.setOnClickListener(n->{
                Intent intent = new Intent(DialogBle.this, Ble3Activity.class);
                startActivity(intent);
            });
            Battery3.setVisibility(View.GONE);
        }
        if(BleProfileService_4.getBleProfile()!=null) {
            int ConnectionState =  BleProfileService_4.getBleProfile().getConnectionState();
            if(ConnectionState == 2){
                btnBLE4.setText("");
                btnBLE4.setTextColor(Color.parseColor("#ffffff"));
                btnBLE4.setBackground(getResources().getDrawable(R.drawable.btn_connect_touch));
                btnBLE4.setOnClickListener(n->{
                    BleProfileService_4.getBleProfile().disconnect();
//                    setBle();
                });
                Battery4.setVisibility(View.VISIBLE);
            }else{
                btnBLE4.setText("기기 연결");
                btnBLE4.setTextColor(Color.parseColor("#ffffff"));
                btnBLE4.setBackground(getResources().getDrawable(R.drawable.button_back_pinkred));
                btnBLE4.setOnClickListener(n->{
                    Intent intent = new Intent(DialogBle.this, Ble4Activity.class);
                    startActivity(intent);
                });
                Battery4.setVisibility(View.GONE);
            }
        }else{
            btnBLE4.setText("기기 연결");
            btnBLE4.setTextColor(Color.parseColor("#ffffff"));
            btnBLE4.setBackground(getResources().getDrawable(R.drawable.button_back_pinkred));
            btnBLE4.setOnClickListener(n->{
                Intent intent = new Intent(DialogBle.this, Ble4Activity.class);
                startActivity(intent);
            });
            Battery4.setVisibility(View.GONE);
        }
        if(BleProfileService_5.getBleProfile()!=null) {
            int ConnectionState =  BleProfileService_5.getBleProfile().getConnectionState();
            if(ConnectionState == 2){
                btnBLE5.setText("");
                btnBLE5.setTextColor(Color.parseColor("#ffffff"));
                btnBLE5.setBackground(getResources().getDrawable(R.drawable.btn_connect_touch));
                btnBLE5.setOnClickListener(n->{
                    BleProfileService_5.getBleProfile().disconnect();
//                    setBle();
                });
                Battery5.setVisibility(View.VISIBLE);
            }else{
                btnBLE5.setText("기기 연결");
                btnBLE5.setTextColor(Color.parseColor("#ffffff"));
                btnBLE5.setBackground(getResources().getDrawable(R.drawable.button_back_pinkred));
                btnBLE5.setOnClickListener(n->{
                    Intent intent = new Intent(DialogBle.this, Ble5Activity.class);
                    startActivity(intent);
                });
                Battery5.setVisibility(View.GONE);
            }
        }else{
            btnBLE5.setText("기기 연결");
            btnBLE5.setTextColor(Color.parseColor("#ffffff"));
            btnBLE5.setBackground(getResources().getDrawable(R.drawable.button_back_pinkred));
            btnBLE5.setOnClickListener(n->{
                Intent intent = new Intent(DialogBle.this, Ble5Activity.class);
                startActivity(intent);
            });
            Battery5.setVisibility(View.GONE);
        }


    }

    public void setBattery1(int value){
        if(value<=20){
            Battery1.setBackground(getResources().getDrawable(R.drawable.icon_battery1));
        }else if(value<=40){
            Battery1.setBackground(getResources().getDrawable(R.drawable.icon_battery2));
        }else if(value<=60){
            Battery1.setBackground(getResources().getDrawable(R.drawable.icon_battery3));
        }else if(value<=80){
            Battery1.setBackground(getResources().getDrawable(R.drawable.icon_battery4));
        }else if(value<=100){
            Battery1.setBackground(getResources().getDrawable(R.drawable.icon_battery5));
        }
    }
    public void setBattery2(int value){
        if(value<=20){
            Battery2.setBackground(getResources().getDrawable(R.drawable.icon_battery1));
        }else if(value<=40){
            Battery2.setBackground(getResources().getDrawable(R.drawable.icon_battery2));
        }else if(value<=60){
            Battery2.setBackground(getResources().getDrawable(R.drawable.icon_battery3));
        }else if(value<=80){
            Battery2.setBackground(getResources().getDrawable(R.drawable.icon_battery4));
        }else if(value<=100){
            Battery2.setBackground(getResources().getDrawable(R.drawable.icon_battery5));
        }
    }
    public void setBattery3(int value){
        if(value<=20){
            Battery3.setBackground(getResources().getDrawable(R.drawable.icon_battery1));
        }else if(value<=40){
            Battery3.setBackground(getResources().getDrawable(R.drawable.icon_battery2));
        }else if(value<=60){
            Battery3.setBackground(getResources().getDrawable(R.drawable.icon_battery3));
        }else if(value<=80){
            Battery3.setBackground(getResources().getDrawable(R.drawable.icon_battery4));
        }else if(value<=100){
            Battery3.setBackground(getResources().getDrawable(R.drawable.icon_battery5));
        }
    }
    public void setBattery4(int value){
        if(value<=20){
            Battery4.setBackground(getResources().getDrawable(R.drawable.icon_battery1));
        }else if(value<=40){
            Battery4.setBackground(getResources().getDrawable(R.drawable.icon_battery2));
        }else if(value<=60){
            Battery4.setBackground(getResources().getDrawable(R.drawable.icon_battery3));
        }else if(value<=80){
            Battery4.setBackground(getResources().getDrawable(R.drawable.icon_battery4));
        }else if(value<=100){
            Battery4.setBackground(getResources().getDrawable(R.drawable.icon_battery5));
        }
    }
    public void setBattery5(int value){
        if(value<=20){
            Battery5.setBackground(getResources().getDrawable(R.drawable.icon_battery1));
        }else if(value<=40){
            Battery5.setBackground(getResources().getDrawable(R.drawable.icon_battery2));
        }else if(value<=60){
            Battery5.setBackground(getResources().getDrawable(R.drawable.icon_battery3));
        }else if(value<=80){
            Battery5.setBackground(getResources().getDrawable(R.drawable.icon_battery4));
        }else if(value<=100){
            Battery5.setBackground(getResources().getDrawable(R.drawable.icon_battery5));
        }
    }
}
