package tennis.android;

import tennis.SpaceTennisController;
import tennis.managers.Log;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Android Launcher. The launcher of the game.
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class AndroidLauncher extends AndroidApplication {
	private static final int REQUEST_ENABLE_BT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// GET LOCAL BLUETOOTH ADAPTER
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

		// IF THE ADAPTER NOT FOUND, BLUETOOTH NOT SUPPORTED
		if (btAdapter == null) {
			Toast.makeText(this, "ERROR: Bluetooth not available",
					Toast.LENGTH_LONG).show();
			Log.error("ERROR: Bluetooth not available");
			return;
		}

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		// WE WILL USE THE ACCELEROMETER SENSOR
		config.useAccelerometer = true;
		initialize(new SpaceTennisController(), config);
		// REQUEST TO TURN ON THE BLUETOOTH
		requestEnable();
	}

	// REQUESTS

	/**
	 * If not on, request the user to activate Bluetooth
	 */
	public void requestEnable() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (!adapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}

}
