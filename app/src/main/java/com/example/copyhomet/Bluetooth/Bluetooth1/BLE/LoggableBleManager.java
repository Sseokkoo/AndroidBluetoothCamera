package com.example.copyhomet.Bluetooth.Bluetooth1.BLE;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.copyhomet.Bluetooth.BleConnectionActivity;
import com.example.copyhomet.Main.MainActivity;
import com.example.copyhomet.TotalActivity;

import no.nordicsemi.android.ble.BleManagerCallbacks;
import no.nordicsemi.android.ble.LegacyBleManager;
//import no.nordicsemi.android.log.ILogSession;
//import no.nordicsemi.android.log.LogContract;
//import no.nordicsemi.android.log.Logger;

/**
 * The manager that logs to nRF Logger. If nRF Logger is not installed, logs are ignored.
 *
 * @param <T> the callbacks class.
 */
public abstract class LoggableBleManager<T extends BleManagerCallbacks> extends LegacyBleManager<T> {
//	private ILogSession logSession;

	/**
	 * The manager constructor.
	 * <p>
	 * After constructing the manager, the callbacks object must be set with
	 * {@link #setGattCallbacks(BleManagerCallbacks)}.
	 *
	 * @param context the context.
	 */
	public LoggableBleManager(@NonNull final Context context) {
		super(context);
	}

	/**
	 * Sets the log session to log into.
	 *
//	 * @param session nRF Logger log session to log inti, or null, if nRF Logger is not installed.
	 */
//	public void setLogger(@Nullable final ILogSession session) {
//		logSession = session;
//	}

	@Override
	public void log(final int priority, @NonNull final String message) {
//		Logger.log(logSession, LogContract.Log.Level.fromPriority(priority), message);
		Log.println(priority, "BleManager", message);
		if(message.contains("DISCONNECTED")){
//			Toast.makeText(getContext(),"연결 끊김",Toast.LENGTH_SHORT).show();
			BleConnectionActivity.getBleConnectionActivity().setToast("'배' 에 장착되어 있는 기기와의 연결이 끊어졌습니다.");


			if(BleProfileService.getBleProfile().getConnectionState()==2) {
			BleProfileService.getBleProfile().disconnect();
			}

			if(MainActivity.getMain() !=null) {
				MainActivity.getMain().device1Vis();
			}else if(TotalActivity.getTotalActivity()!=null){
				TotalActivity.getTotalActivity().setDialog();
			}


		}

	}
}
