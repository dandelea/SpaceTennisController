package tennis.android;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import tennis.SpaceTennisController;
import tennis.managers.Log;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.badlogic.gdx.Gdx;

/**
 * Bluetooth Client implementation for Space Tennis Controller. Sends
 * accelerometer data to a server device.
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class BluetoothClient {
	private static BluetoothDevice preferedDevice = null;
	private static BluetoothConnector btConnector;
	private static BluetoothSocket btSocket;
	private static DataOutputStream outStream;
	public static boolean connected;
	public static int connection_attempts = 0;
	public static final int MAX_CONNECTION_ATTEMPTS = 50;
	public final static float MESSAGE_END = Float.POSITIVE_INFINITY;
	public final static float MESSAGE_PAUSE = Float.MAX_VALUE;

	// CONNECTION

	/**
	 * Start a bluetooth connection with a prefered device
	 */
	public static void connect() {
		try {

			if (preferedDevice == null) {
				// IF NOT SELECTED, PREFERED DEVICE IS FIRST
				Set<BluetoothDevice> pairedDevices = BluetoothAdapter
						.getDefaultAdapter().getBondedDevices();
				for (BluetoothDevice btDevice : pairedDevices) {
					preferedDevice = btDevice;
					Log.info("\nPrefered device: "
							+ preferedDevice.getAddress());
					break;
				}
			}

			btConnector = new BluetoothConnector(preferedDevice, true,
					BluetoothAdapter.getDefaultAdapter(), null);
			// ESTABLISH THE CONNECTION. THIS WILL BLOCK THE THREAD UNTIL IT
			// CONNECTS
			btSocket = btConnector.connect();
			Log.info("\n...Connection established and data link opened...");
			connected = true;
		} catch (IOException e) {
			Log.error("\nCouldnt connect btSocket. " + e.getMessage());
		}

	}

	// SEND

	/**
	 * Sends the actual accelerometer coordinates through the bluetooth channel
	 */
	public static void sendAccelerometer() {
		float x = Gdx.input.getAccelerometerX();
		float y = Gdx.input.getAccelerometerY();
		float z = Gdx.input.getAccelerometerZ();

		// CREATE A DATA STREAM SO WE CAN TALK TO SERVER
		try {
			outStream = new DataOutputStream(btSocket.getOutputStream());
		} catch (IOException e) {
			Log.error("Fatal Error. Output stream creation failed.  "
					+ e.getMessage());
		}

		try {
			// SEND DATA
			outStream.writeFloat(x);
			outStream.writeFloat(y);
			outStream.writeFloat(z);
		} catch (IOException e) {
			Log.error("Check that the SPP UUID: "
					+ BluetoothConnector.BLUETOOTH_SPP_UUID.toString()
					+ " exists on server.\n\n");
			connected = false;
		}
		SpaceTennisController.movement.addValue(z);
	}

	public static void specialMessage(float messageCode) {
		if (connected) {
			// CREATE A DATA STREAM SO WE CAN TALK TO SERVER
			try {
				outStream = new DataOutputStream(btSocket.getOutputStream());
			} catch (IOException e) {
				Log.error("\nFatal Error. Output stream creation failed.  "
						+ e.getMessage());
			}

			try {
				// SEND DATA
				outStream.writeFloat(messageCode);
			} catch (IOException e) {
				Log.error("Check that the SPP UUID: "
						+ BluetoothConnector.BLUETOOTH_SPP_UUID.toString()
						+ " exists on server.\n\n");
			}

			if (messageCode == MESSAGE_END) {
				connected = false;
			}
		}
	}

	// REQUEST

	/**
	 * @return if the device actually supports Bluetooth
	 */
	public static boolean supportsBluetooth() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		return adapter != null;
	}

	/**
	 * Switch offs Bluetooth. Called on exit.
	 */
	public static void requestTurnOff() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter != null) {
			adapter.disable();
		}
	}

	/**
	 * @return List of the names of the paired devices
	 */
	public static List<String> pairedDevices() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
		List<String> list = new ArrayList<String>();
		for (BluetoothDevice bt : pairedDevices)
			list.add(bt.getName());
		return list;
	}

	/**
	 * @param deviceName
	 *            Name of the requested device
	 * @return The {@link android.bluetooth.BluetoothDevice} requested by name.
	 */
	public static BluetoothDevice getBTDevice(String deviceName) {
		connection_attempts = 0;
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice device = null;
		for (BluetoothDevice btDevice : adapter.getBondedDevices()) {
			if (btDevice.getName().equals(deviceName)) {
				device = btDevice;
				break;
			}
		}
		return device;
	}

	/**
	 * Sets the prefered device
	 * 
	 * @param btDevice
	 *            The prefered {@link android.bluetooth.BluetoothDevice}
	 */
	public static void setPreferedDevice(BluetoothDevice btDevice) {
		preferedDevice = btDevice;
	}

}
