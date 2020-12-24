package com.example.copyhomet.Bluetooth.Bluetooth5;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.copyhomet.Bluetooth.Bluetooth5.BLE.BleProfileServiceReadyActivity_5;
import com.example.copyhomet.Bluetooth.Bluetooth5.BLE.BleProfileService_5;
import com.example.copyhomet.Bluetooth.Bluetooth5.UART.UARTInterface;
import com.example.copyhomet.Bluetooth.Bluetooth5.UART.UARTService5;
import com.example.copyhomet.R;

import java.util.UUID;

public class Ble5Activity extends BleProfileServiceReadyActivity_5<UARTService5.UARTBinder>
implements UARTInterface {

    private SharedPreferences preferences;
    private View container;
    private UARTService5.UARTBinder serviceBinder;
    private boolean editMode;

    public static Ble5Activity ble5Activity;
    @Override
    protected void onServiceBound(UARTService5.UARTBinder binder) {
        serviceBinder = binder;
    }

    @Override
    protected void onServiceUnbound() {
        serviceBinder = null;
    }

    @Override
    protected Class<? extends BleProfileService_5> getServiceClass() {
        return UARTService5.class;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ble5Activity =this;
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
    public static Ble5Activity getBle1Activity(){
        return ble5Activity;
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
