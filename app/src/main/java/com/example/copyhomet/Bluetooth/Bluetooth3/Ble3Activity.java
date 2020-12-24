package com.example.copyhomet.Bluetooth.Bluetooth3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.copyhomet.Bluetooth.Bluetooth3.BLE.BleProfileServiceReadyActivity_3;
import com.example.copyhomet.Bluetooth.Bluetooth3.BLE.BleProfileService_3;
import com.example.copyhomet.Bluetooth.Bluetooth3.UART.UARTInterface;
import com.example.copyhomet.Bluetooth.Bluetooth3.UART.UARTService3;
import com.example.copyhomet.R;

import java.util.UUID;

public class Ble3Activity extends BleProfileServiceReadyActivity_3<UARTService3.UARTBinder>
implements UARTInterface {

    private SharedPreferences preferences;
    private View container;
    private UARTService3.UARTBinder serviceBinder;
    private boolean editMode;

    public static Ble3Activity ble3Activity;
    @Override
    protected void onServiceBound(UARTService3.UARTBinder binder) {
        serviceBinder = binder;
    }

    @Override
    protected void onServiceUnbound() {
        serviceBinder = null;
    }

    @Override
    protected Class<? extends BleProfileService_3> getServiceClass() {
        return UARTService3.class;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ble3Activity =this;
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
    public static Ble3Activity getBle1Activity(){
        return ble3Activity;
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
