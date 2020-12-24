package com.example.copyhomet.Bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.copyhomet.Bluetooth.Bluetooth1.BLE.BleProfileService;
import com.example.copyhomet.Bluetooth.Bluetooth2.BLE.BleProfileService_2;
import com.example.copyhomet.Bluetooth.Bluetooth3.BLE.BleProfileService_3;
import com.example.copyhomet.Bluetooth.Bluetooth4.BLE.BleProfileService_4;
import com.example.copyhomet.Bluetooth.Bluetooth5.BLE.BleProfileService_5;
import com.example.copyhomet.Main.MainActivity;
import com.example.copyhomet.R;

public class DialogBleNext extends AppCompatActivity {

    RecyclerView ConnectList;
    Button Insert,Close;
    ListViewAdapter adapter;
    public static DialogBleNext dialogBle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ble_next);
        getSupportActionBar().hide();

        dialogBle = this;

        Insert = findViewById(R.id.Insert);
        Insert.setOnClickListener(n->{
            Intent intent = new Intent(DialogBleNext.this, MainActivity.class);
            startActivity(intent);
        });
        Close = findViewById(R.id.close);
        Close.setOnClickListener(n->{
            onBackPressed();
        });
        ConnectList = findViewById(R.id.ConnectList);



        /*SET THE ADAPTER TO LISTVIEW*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ConnectList.setLayoutManager(linearLayoutManager);
        adapter = new ListViewAdapter();
        ConnectList.setAdapter(adapter);


        if(BleProfileService.getBleProfile()!=null) {
            if (BleProfileService.getBleProfile().getConnectionState() == 2) {
                ListViewAdapter.DataList data = new ListViewAdapter.DataList();
                data.setDeviceName("EMS HomeT 1(배)");
                adapter.addItem(data);

            }
        }
        if(BleProfileService_2.getBleProfile()!=null) {
            if (BleProfileService_2.getBleProfile().getConnectionState() == 2) {
                ListViewAdapter.DataList data = new ListViewAdapter.DataList();
                data.setDeviceName("EMS HomeT 2(팔1)");
                adapter.addItem(data);
            }
        }
        if(BleProfileService_3.getBleProfile()!=null) {
            if (BleProfileService_3.getBleProfile().getConnectionState() == 2) {
                ListViewAdapter.DataList data = new ListViewAdapter.DataList();
                data.setDeviceName("EMS HomeT 3(팔2)");
                adapter.addItem(data);
            }
        }
        if(BleProfileService_4.getBleProfile()!=null) {
            if (BleProfileService_4.getBleProfile().getConnectionState() == 2) {
                ListViewAdapter.DataList data = new ListViewAdapter.DataList();
                data.setDeviceName("EMS HomeT 4(다리1)");
                adapter.addItem(data);
            }
        }
        if(BleProfileService_5.getBleProfile()!=null) {
            if (BleProfileService_5.getBleProfile().getConnectionState() == 2) {
                ListViewAdapter.DataList data = new ListViewAdapter.DataList();
                data.setDeviceName("EMS HomeT 5(다리2)");
                adapter.addItem(data);
            }
        }
        if(adapter.getItemCount()==0){
            ListViewAdapter.DataList data = new ListViewAdapter.DataList();
            data.setDeviceName("현재 연결된 장비가 없습니다");
            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(DialogBleNext.this,BleConnectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
    public static DialogBleNext getDialogBle(){
        return dialogBle;
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();

    }
}
