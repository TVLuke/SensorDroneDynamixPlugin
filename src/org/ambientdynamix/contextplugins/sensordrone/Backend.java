package org.ambientdynamix.contextplugins.sensordrone;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sensorcon.sensordrone.Drone.DroneEventListener;
import com.sensorcon.sensordrone.Drone.DroneStatusListener;
import com.sensorcon.sdhelper.ConnectionBlinker;
import com.sensorcon.sdhelper.SDStreamer;
import com.sensorcon.sensordrone.Drone;

public class Backend
{
	private static final String TAG = "Sensordrone";
	
	private static HashMap<String, Drone> drones; //the Sensordrone
	private static HashMap<String, DroneStatusListener> statusListeners; 
	private static HashMap<String, DroneEventListener> eventListeners;
	private static HashMap<String, ArrayList<SDStreamer>> streamers;
	private BroadcastReceiver mBluetoothReceiver;
	private BluetoothAdapter mBluetoothAdapter;
	BackendRunner backendrunnable;
	private IntentFilter btFilter;
	Context ctx;
	private static boolean running=false;
	int[] sensortypes;
	
	public Backend(Context context)
	{
		ctx = context;
		Log.i(TAG, "starting backend process");
		drones = new HashMap<String, Drone>(); 
		streamers = new HashMap<String, ArrayList<SDStreamer>>();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	    if (!mBluetoothAdapter.isEnabled()) 
	    {
	    	Log.i(TAG, "Bluetooth was off, will now be turned on");
	    	mBluetoothAdapter.enable();
	    }
	    else
	    { 
	        //is enabled
	    } 
	    if(!running)
	    {
			scanToConnect();
			Thread t1 = new Thread( new BackendRunner() );
			t1.start();
	    }
	}
	
	//This is addapted from the SDHelper Library 
	public void scanToConnect()
	{

		// Is Bluetooth on?
	    if (!mBluetoothAdapter.isEnabled()) 
	    {
			// Don't proceed until the user turns Bluetooth on.
	    	Log.i(TAG, "wasn't on...");
			return;
		}
	    
	    mBluetoothReceiver = new BroadcastReceiver() 
	    {

			@Override
			public void onReceive(Context arg0, Intent arg1) 
			{
				Log.i(TAG, "found Bloutooth Device");
				String action = arg1.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) 
				{
					BluetoothDevice device = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Log.i(TAG, "name="+device.getName());
					Log.i(TAG, "deviceClass="+device.getBluetoothClass());
					Log.i(TAG, "BondState="+device.getBondState());
					Log.i(TAG, "Adress="+device.getAddress());
					Log.i(TAG, "HAShCode="+device.hashCode());
					Log.i(TAG, "toString="+device.toString());
					if(device.getName().contains("Sensordrone"))
					{
						if(drones.containsKey(device.getAddress()))
						{
							//the device is already in the hashmap
						}
						else
						{
							addDroneToTheRaster(device);
							
						}

						//mBluetoothAdapter.cancelDiscovery();
						//ctx.unregisterReceiver(mBluetoothReceiver);
					}
				}
			}
	    	
	    };
	    Log.i(TAG, "ok");
	    btFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	    ctx.registerReceiver(mBluetoothReceiver, btFilter);
	    mBluetoothAdapter.startDiscovery();


	}

	private void addDroneToTheRaster(BluetoothDevice device)
	{
		Log.i(TAG, "addtoRaster");
		final Drone drone = new Drone();
		Log.i(TAG, "create Drone object");
		drones.put(device.getAddress(), drone);
		Log.i(TAG, "connection Blinker");
		final ConnectionBlinker myBlinker = new ConnectionBlinker(drone, 1000, 0, 0, 255);
		Log.i(TAG, "streamer array erzeugen");
		ArrayList<SDStreamer> streamerArray = new ArrayList<SDStreamer>();
		Log.i(TAG, "das array fuellen");
		sensortypes = new int[] { 
				drone.QS_TYPE_TEMPERATURE,
				drone.QS_TYPE_HUMIDITY,
				drone.QS_TYPE_PRESSURE,
				drone.QS_TYPE_IR_TEMPERATURE,
				drone.QS_TYPE_RGBC,
				drone.QS_TYPE_PRECISION_GAS,
				drone.QS_TYPE_CAPACITANCE,
				drone.QS_TYPE_ADC, 
				drone.QS_TYPE_ALTITUDE };
		Log.i(TAG, "und nu for-schleife");
		for(int i=0; i<sensortypes.length; i++)
		{
			Log.i(TAG, "->");
			streamerArray.add(new SDStreamer(drone, sensortypes[i]));
		}
		Log.i(TAG, "and into the streamers hashmap");
		streamers.put(device.getAddress(), streamerArray);
		Log.i(TAG, "so far so good");
		
		DroneStatusListener dsListener = new DroneStatusListener() 
        {

			@Override
			public void adcStatus(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void altitudeStatus(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void batteryVoltageStatus(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone battery voltage status event "+drone.batteryVoltage_Volts);
			}

			@Override
			public void capacitanceStatus(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void chargingStatus(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone charging status");
				// TODO Auto-generated method stub
				
			}

			@Override
			public void customStatus(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void humidityStatus(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone humidity status");
				// TODO Auto-generated method stub
				
			}

			@Override
			public void irStatus(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void lowBatteryStatus(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone low battery status");
				// TODO Auto-generated method stub
				
			}

			@Override
			public void oxidizingGasStatus(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone oxydising gas status");
				// TODO Auto-generated method stub
				
			}

			@Override
			public void precisionGasStatus(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone precisionGas status");
				Log.i(TAG, "sensordrone precisionGas ppm Carbon Monoxide "+drone.precisionGas_ppmCarbonMonoxide);
				// TODO Auto-generated method stub
				
			}

			@Override
			public void pressureStatus(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void reducingGasStatus(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void rgbcStatus(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void temperatureStatus(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone temp measured");
				Log.i(TAG, "sensordrone Temperature "+drone.lastMAC+" "+drone.temperature_Celcius);
				try 
				{
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(drone.temperatureStatus)
				{
					ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
					SDStreamer s = sarray.get(0);
					s.run();
				}
			}

			@Override
			public void unknownStatus(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}
        	
        };
        
        DroneEventListener deListener = new DroneEventListener() 
        {

			@Override
			public void adcMeasured(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void altitudeMeasured(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void capacitanceMeasured(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectEvent(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone connection event");
				drone.enableTemperature();
				drone.quickEnable(drone.QS_TYPE_TEMPERATURE);
				drone.measureTemperature();
				myBlinker.enable();
				myBlinker.run();
				
				ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
				for(int i=0; i<sensortypes.length; i++)
				{
					sarray.get(i).enable();
				}
				
			}

			@Override
			public void connectionLostEvent(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone connection lost event");
				
				//nnectionBlinker myBlinker = new ConnectionBlinker(drone, 1000, 0, 0, 255);
				//myBlinker.disable();
				
			}

			@Override
			public void customEvent(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void disconnectEvent(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone disconnect lost event");
				
			}

			@Override
			public void humidityMeasured(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void i2cRead(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void irTemperatureMeasured(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone ir temp");
				
			}

			@Override
			public void oxidizingGasMeasured(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void precisionGasMeasured(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone precisionGas measured");
				Log.i(TAG, "sensordrone precisionGas ppm Carbon Monoxide "+drone.precisionGas_ppmCarbonMonoxide);
				
			}

			@Override
			public void pressureMeasured(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void reducingGasMeasured(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone reducingGas measured");
				
			}

			@Override
			public void rgbcMeasured(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void temperatureMeasured(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone temp measured");
				Log.i(TAG, "sensordrone Temperature "+drone.lastMAC+" "+drone.temperature_Celcius);
				ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
				SDStreamer s = sarray.get(0);
				s.streamHandler.postDelayed(s, 1000);				
			}

			@Override
			public void uartRead(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void unknown(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void usbUartRead(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}
        	
        };
        
        drone.registerDroneEventListener(deListener);
   		drone.registerDroneStatusListener(dsListener);
	}

	class BackendRunner implements Runnable
	{
		@Override
		public void run() 
		{
			running=true;
			while(running)
			{
				Log.i(TAG, "drones size"+drones.size());	
				//TODO: Try to connect
				Set<Entry<String, Drone>> droneset = drones.entrySet();
				Iterator<Entry<String, Drone>> it = droneset.iterator();
				while(it.hasNext())
				{
					Entry<String, Drone> dentry = it.next();
					Drone d = dentry.getValue();
					if(!d.isConnected)
					{
						if(!d.btConnect(dentry.getKey()))
						{
							Log.i(TAG, "could not connect");
						}
						else
						{
							//connected.
						}
					}
					Log.i(TAG, "is it connected? "+d.isConnected);
				}
				try 
				{
					Thread.sleep(2000);
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void disable()
	{
		Log.i(TAG, "Disable the runner");
		Set<Entry<String, Drone>> droneset = drones.entrySet();
		Iterator<Entry<String, Drone>> it = droneset.iterator();
		while(it.hasNext())
		{
			Entry<String, Drone> dentry = it.next();
			Drone d = dentry.getValue();
			d.disconnect();
			Log.i(TAG, "is it connected? "+d.isConnected);
		}
		running=false;
	}
	

}
