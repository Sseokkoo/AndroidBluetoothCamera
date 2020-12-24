package com.example.copyhomet.Bluetooth.Bluetooth1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.copyhomet.Bluetooth.Bluetooth1.BLE.BleProfileService;
import com.example.copyhomet.Bluetooth.Bluetooth1.BLE.BleProfileServiceReadyActivity;
import com.example.copyhomet.Bluetooth.Bluetooth1.UART.UARTInterface;
import com.example.copyhomet.Bluetooth.Bluetooth1.UART.UARTService;
import com.example.copyhomet.R;

import java.util.UUID;

public class Ble1Activity extends BleProfileServiceReadyActivity<UARTService.UARTBinder>
implements UARTInterface {

    private SharedPreferences preferences;
    private View container;
    private UARTService.UARTBinder serviceBinder;
    private boolean editMode;

    public static Ble1Activity ble1Activity;
    @Override
    protected void onServiceBound(UARTService.UARTBinder binder) {
        serviceBinder = binder;
    }

    @Override
    protected void onServiceUnbound() {
        serviceBinder = null;
    }

    @Override
    protected Class<? extends BleProfileService> getServiceClass() {
        return UARTService.class;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ble1Activity =this;
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
    public static Ble1Activity getBle1Activity(){
        return ble1Activity;
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
