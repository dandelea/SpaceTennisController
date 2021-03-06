package tennis.android;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import tennis.managers.Log;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Bluetooth Connector manages the connection and UUID discovery in the
 * requested device.
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class BluetoothConnector {
	public static final UUID BLUETOOTH_SPP_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private BluetoothSocket bluetoothSocket;
	private BluetoothDevice device;
	private boolean secure;
	private BluetoothAdapter adapter;
	private List<UUID> uuidCandidates;
	private int candidate;

	// CONNECTION

	/**
	 * @param device
	 *            the device
	 * @param secure
	 *            if connection should be done via a secure socket
	 * @param adapter
	 *            the Android BT adapter
	 * @param uuidCandidates
	 *            a list of UUIDs. if null or empty, the Serial PP id is used
	 */
	public BluetoothConnector(BluetoothDevice device, boolean secure,
			BluetoothAdapter adapter, List<UUID> uuidCandidates) {
		this.device = device;
		this.secure = secure;
		this.adapter = adapter;
		this.uuidCandidates = uuidCandidates;

		if (this.uuidCandidates == null || this.uuidCandidates.isEmpty()) {
			this.uuidCandidates = new ArrayList<UUID>();
			this.uuidCandidates.add(BLUETOOTH_SPP_UUID);
		}
	}

	public BluetoothSocket connect() throws IOException {
		boolean success = false;

		while (selectSocket()) {
			adapter.cancelDiscovery();

			try {
				bluetoothSocket.connect();
				success = true;
				break;
			} catch (IOException e) {
				// try the fallback
				try {
					adapter.cancelDiscovery();
					bluetoothSocket.connect();
					success = true;
					break;
				} catch (IOException e1) {
					success = false;
					BluetoothClient.connection_attempts++;
					Log.error("BT. Fallback failed. Cancelling. " + e1);
				}
			}
		}

		if (!success) {
			throw new IOException("Could not connect to device: "
					+ device.getAddress() + ". Attempts: "
					+ BluetoothClient.connection_attempts);
		}

		return bluetoothSocket;
	}

	private boolean selectSocket() throws IOException {
		if (candidate >= uuidCandidates.size()) {
			return false;
		}

		BluetoothSocket tmp = null;
		UUID uuid = uuidCandidates.get(candidate++);

		Log.info("BT. Attempting to connect to Protocol: " + uuid);
		if (secure) {
			// tmp = device.createRfcommSocketToServiceRecord(uuid);
			try {
				tmp = (BluetoothSocket) device
						.getClass()
						.getMethod("createRfcommSocket",
								new Class[] { int.class }).invoke(device, 1);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			tmp = device.createInsecureRfcommSocketToServiceRecord(uuid);
		}
		bluetoothSocket = tmp;

		return true;
	}
}