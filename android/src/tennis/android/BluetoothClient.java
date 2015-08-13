package tennis.android;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import tennis.SpaceTennisController;
import tennis.managers.Log;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.badlogic.gdx.Gdx;

public class BluetoothClient {
	private static BluetoothDevice preferedDevice = null;
	private static BluetoothConnector btConnector;
	private static BluetoothSocket btSocket;
	private static DataOutputStream outStream;
	public static boolean connected;
	public static int connection_attempts = 0;
	public static final int MAX_CONNECTION_ATTEMPTS = 50;
	// "00:15:83:0C:BF:EB";
	private static final UUID BLUETOOTH_SPP_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	// CONNECTION
	
	public static void connect() {
		try {
			
			if (preferedDevice==null){
				// Prefered device: First
				Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
				for (BluetoothDevice btDevice: pairedDevices){
					preferedDevice = btDevice;
					Log.info("BT " + preferedDevice.getAddress());
					break;
				}
			}
			
			btConnector = new BluetoothConnector(preferedDevice, true, BluetoothAdapter.getDefaultAdapter(), null);
			// Establish the connection. This will block until it connects.
			btSocket = btConnector.connect();
			Log.info("\n...Connection established and data link opened...");
			connected = true;
		} catch (IOException e) {
			Log.error("Couldnt connect btSocket. " + e.getMessage());
		}
		
		
	}
	
	// SEND ACCELEROMETER
	
	public static void endConnection(){
		if (connected) {
			float message = Float.MAX_VALUE;
			try {
				outStream = new DataOutputStream(btSocket.getOutputStream());
			} catch (IOException e) {
				Log.error("Fatal Error. Output stream creation failed.  " + e.getMessage());
			}
					
			try {
				outStream.writeFloat(message);
			} catch (IOException e) {
				Log.error("Check that the SPP UUID: "
						+ BLUETOOTH_SPP_UUID.toString() + " exists on server.\n\n");
			}
			connected = false;
		}
	}
	
	public static void sendAccelerometer(){
		//Assert.assertNotNull("Fatal error. btSocket null", btSocket);
		//Assert.assertTrue("Fatal error. btSocket not connected", btSocket.isConnected());
				
		// Create a data stream so we can talk to server.
		float curX = Gdx.input.getAccelerometerX();
		float curY = Gdx.input.getAccelerometerY();
		float curZ = Gdx.input.getAccelerometerZ();
		//curX = (Gdx.input.getAccelerometerX() - 10) < 0 ? 0:(Gdx.input.getAccelerometerX() - 10);
		//curY = (Gdx.input.getAccelerometerY() - 10) < 0 ? 0:(Gdx.input.getAccelerometerY() - 10);
		//curZ = (Gdx.input.getAccelerometerZ() - 10) < 0 ? 0:(Gdx.input.getAccelerometerZ() - 10);
		//curX = (Math.abs(Gdx.input.getAccelerometerX()) > 10) ? Math.abs(Gdx.input.getAccelerometerX()) : 0;
		//curY = (Math.abs(Gdx.input.getAccelerometerY()) > 10) ? Math.abs(Gdx.input.getAccelerometerY()) : 0;
		//curZ = (Math.abs(Gdx.input.getAccelerometerZ()) > 10) ? Math.abs(Gdx.input.getAccelerometerZ()) : 0;
		/*curX = (Gdx.input.getAccelerometerX() > 5) ? Gdx.input.getAccelerometerX()-5 : 0;
		curX = (curX > 20) ? 12 : curX;
		curY = (Gdx.input.getAccelerometerY() < - 5) ? Gdx.input.getAccelerometerY() + 5 : 0;
		curY = (curY > 20) ? 12 : curY;
		curZ = (Math.abs(Gdx.input.getAccelerometerZ()) > 10) ? Math.abs(Gdx.input.getAccelerometerZ())-10 : 0;
		curZ = (curZ < 0.001) ? 0 : curZ;
		curZ = (curZ > 20) ? 12 : curZ;*/	
						
		try {
			outStream = new DataOutputStream(btSocket.getOutputStream());
		} catch (IOException e) {
			Log.error("Fatal Error. Output stream creation failed.  " + e.getMessage());
		}
				
		try {
			outStream.writeFloat(curX);
			outStream.writeFloat(curY);
			outStream.writeFloat(curZ);
		} catch (IOException e) {
			Log.error("Check that the SPP UUID: "
					+ BLUETOOTH_SPP_UUID.toString() + " exists on server.\n\n");
			connected = false;
		}
		SpaceTennisController.movement.addValue(curZ);
	}
	
	// REQUEST
    
    public static boolean supportsBluetooth() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		return adapter != null;
	}
    
    public static void requestTurnOff() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter != null) {
			adapter.disable();
		}
	}

	public static List<String> pairedDevices() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
		List<String> list = new ArrayList<String>();
		for (BluetoothDevice bt : pairedDevices)
			list.add(bt.getName());
		return list;
	}
	
	public static BluetoothDevice getBTDevice(String deviceName) {
		connection_attempts = 0;
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice device = null;
		for (BluetoothDevice btDevice: adapter.getBondedDevices()){
			if (btDevice.getName().equals(deviceName)){
				device = btDevice;
				break;
			}
		}
		return device;
	}
	
	public static void setPreferedDevice(BluetoothDevice btDevice){
		preferedDevice = btDevice;
	}

}
