package com.example.copyhomet.Bluetooth.Bluetooth2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.copyhomet.Bluetooth.Bluetooth2.BLE.BleProfileServiceReadyActivity_2;
import com.example.copyhomet.Bluetooth.Bluetooth2.BLE.BleProfileService_2;
import com.example.copyhomet.Bluetooth.Bluetooth2.UART.UARTInterface;
import com.example.copyhomet.Bluetooth.Bluetooth2.UART.UARTService2;
import com.example.copyhomet.R;

import java.util.UUID;

public class Ble2Activity extends BleProfileServiceReadyActivity_2<UARTService2.UARTBinder>
implements UARTInterface {

    private SharedPreferences preferences;
    private View container;
    private UARTService2.UARTBinder serviceBinder;
    private boolean editMode;

    public static Ble2Activity ble2Activity;
    @Override
    protected void onServiceBound(UARTService2.UARTBinder binder) {
        serviceBinder = binder;
    }

    @Override
    protected void onServiceUnbound() {
        serviceBinder = null;
    }

    @Override
    protected Class<? extends BleProfileService_2> getServiceClass() {
        return UARTService2.class;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ble2Activity =this;
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
    public static Ble2Activity getBle1Activity(){
        return ble2Activity;
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
