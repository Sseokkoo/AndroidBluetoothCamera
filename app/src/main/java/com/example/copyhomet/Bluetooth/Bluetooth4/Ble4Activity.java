package com.example.copyhomet.Bluetooth.Bluetooth4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.copyhomet.Bluetooth.Bluetooth4.BLE.BleProfileServiceReadyActivity_4;
import com.example.copyhomet.Bluetooth.Bluetooth4.BLE.BleProfileService_4;
import com.example.copyhomet.Bluetooth.Bluetooth4.UART.UARTInterface;
import com.example.copyhomet.Bluetooth.Bluetooth4.UART.UARTService4;
import com.example.copyhomet.R;

import java.util.UUID;

public class Ble4Activity extends BleProfileServiceReadyActivity_4<UARTService4.UARTBinder>
implements UARTInterface {

    private SharedPreferences preferences;
    private View container;
    private UARTService4.UARTBinder serviceBinder;
    private boolean editMode;

    public static Ble4Activity ble4Activity;
    @Override
    protected void onServiceBound(UARTService4.UARTBinder binder) {
        serviceBinder = binder;
    }

    @Override
    protected void onServiceUnbound() {
        serviceBinder = null;
    }

    @Override
    protected Class<? extends BleProfileService_4> getServiceClass() {
        return UARTService4.class;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ble4Activity =this;
    }

    @Override
    protected void setDefaultUI() {

    }

    @Override
    protected int getDefaultDeviceName() {
        return 0;
    }

    @Override
    protected int getAboutTextId() {
        return 0;
    }

    @Override
    protected UUID getFilterUUID() {
        return null;
    }

    @Override
    public void send(final String text) {
        if (serviceBinder != null)
            serviceBinder.send(text);
    }
    public static Ble4Activity getBle1Activity(){
        return ble4Activity;
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
