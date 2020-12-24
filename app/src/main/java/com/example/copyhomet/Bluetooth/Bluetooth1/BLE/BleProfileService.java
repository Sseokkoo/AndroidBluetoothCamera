/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.example.copyhomet.Bluetooth.Bluetooth1.BLE;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.copyhomet.Bluetooth.DialogBle;
import com.example.copyhomet.Bluetooth.DialogBleToMain;

import no.nordicsemi.android.ble.BleManagerCallbacks;


@SuppressWarnings("unused")
public abstract class BleProfileService extends Service implements BleManagerCallbacks {    /**nrf tool box 참고**/
    @SuppressWarnings("unused")
    private static final String TAG = "BleProfileService";

    public static final String BROADCAST_CONNECTION_STATE = "no.nordicsemi.android.nrftoolbox.BROADCAST_CONNECTION_STATE";
    public static final String BROADCAST_SERVICES_DISCOVERED = "no.nordicsemi.android.nrftoolbox.BROADCAST_SERVICES_DISCOVERED";
    public static final String BROADCAST_DEVICE_READY = "no.nordicsemi.android.nrftoolbox.DEVICE_READY";
    public static final String BROADCAST_BOND_STATE = "no.nordicsemi.android.nrftoolbox.BROADCAST_BOND_STATE";
    @Deprecated
    public static final String BROADCAST_BATTERY_LEVEL = "no.nordicsemi.android.nrftoolbox.BROADCAST_BATTERY_LEVEL";
    public static final String BROADCAST_ERROR = "no.nordicsemi.android.nrftoolbox.BROADCAST_ERROR";

    public static final String EXTRA_DEVICE_ADDRESS = "no.nordicsemi.android.nrftoolbox.EXTRA_DEVICE_ADDRESS";
    public static final String EXTRA_DEVICE_NAME = "no.nordicsemi.android.nrftoolbox.EXTRA_DEVICE_NAME";
    public static final String EXTRA_DEVICE = "no.nordicsemi.android.nrftoolbox.EXTRA_DEVICE";
    public static final String EXTRA_LOG_URI = "no.nordicsemi.android.nrftoolbox.EXTRA_LOG_URI";
    public static final String EXTRA_CONNECTION_STATE = "no.nordicsemi.android.nrftoolbox.EXTRA_CONNECTION_STATE";
    public static final String EXTRA_BOND_STATE = "no.nordicsemi.android.nrftoolbox.EXTRA_BOND_STATE";
    public static final String EXTRA_SERVICE_PRIMARY = "no.nordicsemi.android.nrftoolbox.EXTRA_SERVICE_PRIMARY";
    public static final String EXTRA_SERVICE_SECONDARY = "no.nordicsemi.android.nrftoolbox.EXTRA_SERVICE_SECONDARY";
    @Deprecated
    public static final String EXTRA_BATTERY_LEVEL = "no.nordicsemi.android.nrftoolbox.EXTRA_BATTERY_LEVEL";
    public static final String EXTRA_ERROR_MESSAGE = "no.nordicsemi.android.nrftoolbox.EXTRA_ERROR_MESSAGE";
    public static final String EXTRA_ERROR_CODE = "no.nordicsemi.android.nrftoolbox.EXTRA_ERROR_CODE";

    public static final int STATE_LINK_LOSS = -1;
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTED = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_DISCONNECTING = 3;

    private LoggableBleManager<BleManagerCallbacks> bleManager;
    private Handler handler;

    protected boolean bound;
    private boolean activityIsChangingConfiguration;
    private BluetoothDevice bluetoothDevice;
    private String deviceName;
//    private ILogSession logSession;

    public static BleProfileService bleProfile;
    private final BroadcastReceiver bluetoothStateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
//            final ILogger logger = getBinder();




            switch (state) {
                case BluetoothAdapter.STATE_ON:
                    onBluetoothEnabled();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                case BluetoothAdapter.STATE_OFF:
                    onBluetoothDisabled();
                    break;
            }
        }

        private String state2String(final int state) {
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    return "TURNING ON";
                case BluetoothAdapter.STATE_ON:
                    return "ON";
                case BluetoothAdapter.STATE_TURNING_OFF:
                    return "TURNING OFF";
                case BluetoothAdapter.STATE_OFF:
                    return "OFF";
                default:
                    return "UNKNOWN (" + state + ")";
            }
        }
    };

    public final void disconnect() {
        if(bleManager!=null) {
            final int state = bleManager.getConnectionState();
            if (state == BluetoothGatt.STATE_DISCONNECTED || state == BluetoothGatt.STATE_DISCONNECTING) {
                bleManager.close();
                onDeviceDisconnected(bluetoothDevice);
                return;
            }
            bleManager.disconnect().enqueue();
        }
    }
        public class LocalBinder extends Binder {

        public final void disconnect() {
            final int state = bleManager.getConnectionState();
            if (state == BluetoothGatt.STATE_DISCONNECTED || state == BluetoothGatt.STATE_DISCONNECTING) {
                bleManager.close();
                onDeviceDisconnected(bluetoothDevice);
                return;
            }

            bleManager.disconnect().enqueue();
        }


        public void setActivityIsChangingConfiguration(final boolean changing) {
            activityIsChangingConfiguration = changing;
        }


        public String getDeviceAddress() {
            return bluetoothDevice.getAddress();
        }

        public String getDeviceName() {
            return deviceName;
        }


        public BluetoothDevice getBluetoothDevice() {
            return bluetoothDevice;
        }


        public boolean isConnected() {
            return bleManager.isConnected();
        }



    }
    public int getConnectionState() {
        if(bleManager!=null) {
            return bleManager.getConnectionState();
        }else{
            return 1;
        }
    }

    protected Handler getHandler() {
        return handler;
    }

    protected LocalBinder getBinder() {
        // default implementation returns the basic binder. You can overwrite the LocalBinder with your own, wider implementation
        return new LocalBinder();
    }

    @Override
    public IBinder onBind(final Intent intent) {
        bound = true;
        return getBinder();
    }

    @Override
    public final void onRebind(final Intent intent) {
        bound = true;

        if (!activityIsChangingConfiguration)
            onRebind();
    }

    protected void onRebind() {
        // empty default implementation
    }

    @Override
    public final boolean onUnbind(final Intent intent) {
        bound = false;

        if (!activityIsChangingConfiguration)
            onUnbind();

        // We want the onRebind method be called if anything else binds to it again
        return true;
    }

    protected void onUnbind() {
        // empty default implementation
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate() {
        super.onCreate();

        bleProfile = this;
        handler = new Handler();

        // Initialize the manager
        bleManager = initializeManager();
        bleManager.setGattCallbacks(this);

        // Register broadcast receivers
        registerReceiver(bluetoothStateBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

        // Service has now been created
        onServiceCreated();

        // Call onBluetoothEnabled if Bluetooth enabled
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {
            onBluetoothEnabled();
        }
    }


    public static BleProfileService getBleProfile(){

        return bleProfile;
    }
    protected void onServiceCreated() {
        // empty default implementation
    }

    @SuppressWarnings("rawtypes")
    protected abstract LoggableBleManager initializeManager();

    protected boolean shouldAutoConnect() {
        return false;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (intent == null || !intent.hasExtra(EXTRA_DEVICE_ADDRESS))
            throw new UnsupportedOperationException("No device address at EXTRA_DEVICE_ADDRESS key");

        final Uri logUri = intent.getParcelableExtra(EXTRA_LOG_URI);
        deviceName = intent.getStringExtra(EXTRA_DEVICE_NAME);


        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        final String deviceAddress = intent.getStringExtra(EXTRA_DEVICE_ADDRESS);
        bluetoothDevice = adapter.getRemoteDevice(deviceAddress);

        onServiceStarted();
        bleManager.connect(bluetoothDevice)
                .useAutoConnect(shouldAutoConnect())
                .retry(3, 100)
                .enqueue();
        return START_REDELIVER_INTENT;
    }


    protected void onServiceStarted() {
    }

    @Override
    public void onTaskRemoved(final Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister broadcast receivers
        unregisterReceiver(bluetoothStateBroadcastReceiver);

        // shutdown the manager
        bleManager.close();
//        Logger.i(logSession, "Service destroyed");
        bleManager = null;
        bluetoothDevice = null;
        deviceName = null;
//        logSession = null;
        handler = null;
    }


    protected void onBluetoothDisabled() {
    }


    protected void onBluetoothEnabled() {
    }

    @Override
    public void onDeviceConnecting(@NonNull final BluetoothDevice device) {
        final Intent broadcast = new Intent(BROADCAST_CONNECTION_STATE);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_CONNECTION_STATE, STATE_CONNECTING);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    @Override
    public void onDeviceConnected(@NonNull final BluetoothDevice device) {
        final Intent broadcast = new Intent(BROADCAST_CONNECTION_STATE);
        broadcast.putExtra(EXTRA_CONNECTION_STATE, STATE_CONNECTED);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_DEVICE_NAME, deviceName);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);

        if(deviceName.contains("BBT") || deviceName.contains("EMS") || deviceName.contains("ABS")) {
            if(DialogBle.getDialogBle()!=null){
                DialogBle.getDialogBle().setBle();
            }
            if(DialogBleToMain.getDialogBle()!=null){
                DialogBleToMain.getDialogBle().setBle();
            }
        }else{
//            Toast.makeText(this,"지원되지 않는 기기입니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeviceDisconnecting(@NonNull final BluetoothDevice device) {
        // Notify user about changing the state to DISCONNECTING
        final Intent broadcast = new Intent(BROADCAST_CONNECTION_STATE);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_CONNECTION_STATE, STATE_DISCONNECTING);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    /**
     * This method should return false if the service needs to do some asynchronous work after if has disconnected from the device.
     * In that case the {@link #stopService()} method must be called when done.
     *
     * @return true (default) to automatically stop the service when device is disconnected. False otherwise.
     */
    protected boolean stopWhenDisconnected() {
        return true;
    }

    @Override
    public void onDeviceDisconnected(@NonNull final BluetoothDevice device) {
        // Note 1: Do not use the device argument here unless you change calling onDeviceDisconnected from the binder above

        // Note 2: if BleManager#shouldAutoConnect() for this device returned true, this callback will be
        // invoked ONLY when user requested disconnection (using Disconnect button). If the device
        // disconnects due to a link loss, the onLinkLossOccurred(BluetoothDevice) method will be called instead.

        final Intent broadcast = new Intent(BROADCAST_CONNECTION_STATE);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_CONNECTION_STATE, STATE_DISCONNECTED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
        if (stopWhenDisconnected())
            stopService();

        if(DialogBle.getDialogBle()!=null){
            DialogBle.getDialogBle().setBle();
        }
        if(DialogBleToMain.getDialogBle()!=null){
            DialogBleToMain.getDialogBle().setBle();
        }

//        Toast.makeText(this," ''배'' 연결 끊김",Toast.LENGTH_SHORT).show();

    }

    public void setBleManagerConnect(){
        bleManager.disconnect();
//        bleProfile.disconnect();
    }
    protected void stopService() {
        // user requested disconnection. We must stop the service
//        Logger.v(logSession, "Stopping service...");
        stopSelf();
    }

    @Override
    public void onLinkLossOccurred(@NonNull final BluetoothDevice device) {
        final Intent broadcast = new Intent(BROADCAST_CONNECTION_STATE);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_CONNECTION_STATE, STATE_LINK_LOSS);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    @Override
    public void onServicesDiscovered(@NonNull final BluetoothDevice device, final boolean optionalServicesFound) {
        final Intent broadcast = new Intent(BROADCAST_SERVICES_DISCOVERED);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_SERVICE_PRIMARY, true);
        broadcast.putExtra(EXTRA_SERVICE_SECONDARY, optionalServicesFound);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    @Override
    public void onDeviceReady(@NonNull final BluetoothDevice device) {
        final Intent broadcast = new Intent(BROADCAST_DEVICE_READY);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    @Override
    public void onDeviceNotSupported(@NonNull final BluetoothDevice device) {
        final Intent broadcast = new Intent(BROADCAST_SERVICES_DISCOVERED);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_SERVICE_PRIMARY, false);
        broadcast.putExtra(EXTRA_SERVICE_SECONDARY, false);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);

        // no need for disconnecting, it will be disconnected by the manager automatically
    }

    @Override
    public void onBatteryValueReceived(@NonNull final BluetoothDevice device, final int value) {
        final Intent broadcast = new Intent(BROADCAST_BATTERY_LEVEL);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_BATTERY_LEVEL, value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    @Override
    public void onBondingRequired(@NonNull final BluetoothDevice device) {
//        showToast("Bonding with the device");

        final Intent broadcast = new Intent(BROADCAST_BOND_STATE);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_BOND_STATE, BluetoothDevice.BOND_BONDING);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    @Override
    public void onBonded(@NonNull final BluetoothDevice device) {
//        showToast("The device is now bonded.");

        final Intent broadcast = new Intent(BROADCAST_BOND_STATE);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_BOND_STATE, BluetoothDevice.BOND_BONDED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    @Override
    public void onBondingFailed(@NonNull final BluetoothDevice device) {
//        showToast("Bonding failed");

        final Intent broadcast = new Intent(BROADCAST_BOND_STATE);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    @Override
    public void onError(@NonNull final BluetoothDevice device, @NonNull final String message, final int errorCode) {
        final Intent broadcast = new Intent(BROADCAST_ERROR);
        broadcast.putExtra(EXTRA_DEVICE, bluetoothDevice);
        broadcast.putExtra(EXTRA_ERROR_MESSAGE, message);
        broadcast.putExtra(EXTRA_ERROR_CODE, errorCode);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    /**
     * Shows a message as a Toast notification. This method is thread safe, you can call it from any thread
     *
     * @param messageResId an resource id of the message to be shown
     */
    protected void showToast(final int messageResId) {
        handler.post(() -> Toast.makeText(BleProfileService.this, messageResId, Toast.LENGTH_SHORT).show());
    }

    /**
     * Shows a message as a Toast notification. This method is thread safe, you can call it from any thread
     *
     * @param message a message to be shown
     */
    protected void showToast(final String message) {
        handler.post(() -> Toast.makeText(BleProfileService.this, message, Toast.LENGTH_SHORT).show());
    }

    /**
     * Returns the log session that can be used to append log entries. The method returns <code>null</code> if the nRF Logger app was not installed. It is safe to use logger when
     * {@link #onServiceStarted()} has been called.
     *
     * @return the log session
     */
//    protected ILogSession getLogSession() {
//        return logSession;
//    }

    /**
     * Returns the device address
     *
     * @return device address
     */
    protected String getDeviceAddress() {
        return bluetoothDevice.getAddress();
    }

    /**
     * Returns the Bluetooth device object
     *
     * @return bluetooth device
     */
    protected BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    /**
     * Returns the device name
     *
     * @return the device name
     */
    protected String getDeviceName() {
        return deviceName;
    }

    /**
     * Returns <code>true</code> if the device is connected to the sensor.
     *
     * @return <code>true</code> if device is connected to the sensor, <code>false</code> otherwise
     */
    protected boolean isConnected() {
        return bleManager != null && bleManager.isConnected();
    }
}
