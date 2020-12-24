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

package com.example.copyhomet.Bluetooth.Bluetooth2.UART;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.copyhomet.Bluetooth.Bluetooth2.BLE.LoggableBleManager_2;
import com.example.copyhomet.Bluetooth.DialogBle;
import com.example.copyhomet.Main.MainActivity;

import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.WriteRequest;
//import no.nordicsemi.android.log.LogContract;

public class UARTManager2 extends LoggableBleManager_2<UARTManagerCallbacks> {
	/** Nordic UART Service UUID */
	private final static UUID UART_SERVICE_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
	/** RX characteristic UUID */
	private final static UUID UART_RX_CHARACTERISTIC_UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
	/** TX characteristic UUID */
	private final static UUID UART_TX_CHARACTERISTIC_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");

	private BluetoothGattCharacteristic rxCharacteristic, txCharacteristic;

	private static UARTManager2 Manager = null;

	int a = 1;
	/**
	 * A flag indicating whether Long Write can be used. It's set to false if the UART RX
	 * characteristic has only PROPERTY_WRITE_NO_RESPONSE property and no PROPERTY_WRITE.
	 * If you set it to false here, it will never use Long Write.
	 *
	 * TODO change this flag if you don't want to use Long Write even with Write Request.
	 */
	private boolean useLongWrite = true;

	UARTManager2(final Context context) {
		super(context);
		Manager = UARTManager2.this;

	}

	@NonNull
	@Override
	protected BleManager.BleManagerGattCallback getGattCallback() {
		return new UARTManagerGattCallback();
	}

	/**
	 * BluetoothGatt callbacks for connection/disconnection, service discovery,
	 * receiving indication, etc.
	 */

	int AppKilled = 0;
	private class UARTManagerGattCallback extends BleManager.BleManagerGattCallback {

		@Override
		protected void initialize() {


			setNotificationCallback(txCharacteristic)
					.with((device, data) -> {
						final String text = data.getStringValue(0);

//						log(LogContract.Log.Level.APPLICATION, "\"" + data + "\" received");
//						ArrayListStri a = a+"\n"+data;
//						MainActivity2.getMain().setToast(String.valueOf(data));
						mCallbacks.onDataReceived(device, text);

						//패드 부착유무
						if(MainActivity.getMain()!=null) {
							if (String.valueOf(data).equals("(0x) FF-0E-00-01-FE")) {
//							Intent intent = new Intent(getContext(), DialogDeviceDroped.class);
								MainActivity.getMain().Device2Drop();
							}
						}
						//운동 Block
						if(MainActivity.getMain()!=null){
							Log.d("a=", String.valueOf(a));
//							if(String.valueOf(data).contains("FF-12-00") && !String.valueOf(data).equals("(0x) FF-12-00-00-FE")){
//								if(a ==1) {
//									MainActivity.getMain().getWarmUp(String.valueOf(data));
//									a=2;
//								}
//							}else if(String.valueOf(data).equals("(0x) FF-12-00-00-FE")){
//								a=1;
//							}

							if (AppKilled == 0) {
								if (String.valueOf(data).contains("FF-12-00")) {
									if (!String.valueOf(data).contains("FF-12-00-00-FE")) {
//										send("FF0B0001FE");
										if (String.valueOf(data).contains("FF-12-00-01-FE")) {
											MainActivity.getMain().DeviceIsAlreadyStated(1);
										}else if(String.valueOf(data).contains("FF-12-00-02-FE")) {
											MainActivity.getMain().DeviceIsAlreadyStated(2);
										}else if(String.valueOf(data).contains("FF-12-00-03-FE")) {
											MainActivity.getMain().DeviceIsAlreadyStated(3);
										}else if(String.valueOf(data).contains("FF-12-00-04-FE")) {
											MainActivity.getMain().DeviceIsAlreadyStated(4);
										}else if(String.valueOf(data).contains("FF-12-00-05-FE")) {
											MainActivity.getMain().DeviceIsAlreadyStated(5);
										}else if(String.valueOf(data).contains("FF-12-00-06-FE")) {
											MainActivity.getMain().DeviceIsAlreadyStated(6);
										}else if(String.valueOf(data).contains("FF-12-00-07-FE")) {
											MainActivity.getMain().DeviceIsAlreadyStated(7);
										}
										AppKilled = 1;
									}else{
										AppKilled = 1;
									}
								}
							}
						}
						//배터리 잔량
						if(DialogBle.getDialogBle()!=null) {
							if(String.valueOf(data).contains("FF-14")){
								int value;
								String val = String.valueOf(data).substring(11,16).replace("-","");
								value = Integer.parseInt(val,16);
								DialogBle.getDialogBle().setBattery2(value);
							}
						}
						//기기 세기
						if(MainActivity.getMain()!=null){
							if(String.valueOf(data).contains("FF-15")){
								String Count = String.valueOf(data).substring(11,16);
								Count = Count.replace("-","");
								int toInt = Integer.parseInt(Count,16);
								MainActivity.getMain().SetedtCount2(String.valueOf(toInt),2);
							}
						}







					});
			requestMtu(260).enqueue();
			enableNotifications(txCharacteristic).enqueue();
		}

		@Override
		public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
			final BluetoothGattService service = gatt.getService(UART_SERVICE_UUID);
			if (service != null) {
				rxCharacteristic = service.getCharacteristic(UART_RX_CHARACTERISTIC_UUID);
				txCharacteristic = service.getCharacteristic(UART_TX_CHARACTERISTIC_UUID);
			}

			boolean writeRequest = false;
			boolean writeCommand = false;
			if (rxCharacteristic != null) {
				final int rxProperties = rxCharacteristic.getProperties();
				writeRequest = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
				writeCommand = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0;

				// Set the WRITE REQUEST type when the characteristic supports it.
				// This will allow to send long write (also if the characteristic support it).
				// In case there is no WRITE REQUEST property, this manager will divide texts
				// longer then MTU-3 bytes into up to MTU-3 bytes chunks.
				if (writeRequest)
					rxCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
				else
					useLongWrite = false;
			}

			return rxCharacteristic != null && txCharacteristic != null && (writeRequest || writeCommand);
		}

		@Override
		protected void onDeviceDisconnected() {
			rxCharacteristic = null;
			txCharacteristic = null;
			useLongWrite = true;
		}
	}

	// This has been moved to the service in BleManager v2.0.
	/*@Override
	protected boolean shouldAutoConnect() {
		// We want the connection to be kept
		return true;
	}*/



	public static UARTManager2 getManager(){
		return Manager;
	}
	public void send2(String text){
		send(text);
	}

	public void send(final String text) {
		try {
			// Are we connected?
			if (rxCharacteristic == null) {
//			Toast.makeText(getContext(), "블루투스 연결 오류_1", Toast.LENGTH_SHORT).show();
				return;
			} else {
				if (!TextUtils.isEmpty(text)) {
					final WriteRequest request = writeCharacteristic(rxCharacteristic, hexStringToByteArray(text));
//							.with((device, data) -> log(LogContract.Log.Level.APPLICATION,
//									"\"" + text + "\" sent"));
					if (!useLongWrite) {
						// This will automatically split the long data into MTU-3-byte long packets.
						request.split();
					}
					request.enqueue();
				}
			}
		}catch (Exception e){
			Toast.makeText(getContext(), "데이터 오류", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public static byte[] hexStringToByteArray(String s) {
		try {//바이너리 스트링을 바이트 배열로 변환
			int len = s.length();
			byte[] data = new byte[len / 2];
			for (int i = 0; i < len; i += 2) {
				data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
						+ Character.digit(s.charAt(i + 1), 16));
			}
			return data;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public void setAppKilled(){
		AppKilled = 0;
	}
}
