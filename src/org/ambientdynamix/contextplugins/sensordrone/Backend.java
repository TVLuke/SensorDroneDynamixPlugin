package org.ambientdynamix.contextplugins.sensordrone;

import java.util.ArrayList;
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

import com.sensorcon.sdhelper.ConnectionBlinker;
import com.sensorcon.sensordrone.Drone;

public class Backend
{
	private static HashMap<String, Drone> drones; //the Sensordrone
	private BroadcastReceiver mBluetoothReceiver;
	private BluetoothAdapter mBluetoothAdapter;
	private IntentFilter btFilter;
	ConnectionBlinker myBlinker;
	Context ctx;
	
	public Backend(Context context)
	{
		ctx = context;
		Log.i("Sensordrone", "starting backend process");
		drones = new HashMap<String, Drone>(); 
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    
	    if (!mBluetoothAdapter.isEnabled()) 
	    {
	    	Log.i("Sensordrone", "Bluetooth was off, will now be turned on");
	    	mBluetoothAdapter.enable();
	    }
	    else
	    { 
	        //is enabled
	    } 
		scanToConnect();
	}
	
	//This is addapted from the SDHelper Library 
	public void scanToConnect()
	{

		// Is Bluetooth on?
	    if (!mBluetoothAdapter.isEnabled()) 
	    {
			// Don't proceed until the user turns Bluetooth on.
	    	Log.i("Sensordrone", "wasn't on...");
			return;
		}
	    
	    mBluetoothReceiver = new BroadcastReceiver() 
	    {

			@Override
			public void onReceive(Context arg0, Intent arg1) 
			{
				Log.i("Sensordrone", "found Bloutooth Device");
				String action = arg1.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) 
				{
					BluetoothDevice device = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Log.i("Sensordrone", "name="+device.getName());
					Log.i("Sensordrone", "deviceClass="+device.getBluetoothClass());
					Log.i("Sensordrone", "BondState="+device.getBondState());
					Log.i("Sensordrone", "Adress="+device.getAddress());
					Log.i("Sensordrone", "HAShCode="+device.hashCode());
					Log.i("Sensordrone", "toString="+device.toString());
					if(device.getName().contains("Sensordrone"))
					{
						Drone drone = new Drone();
						if(drones.containsKey(device.getAddress()))
						{
							//the device is already in the hashmap
						}
						else
						{
							drones.put(device.getAddress(), drone);
						}

						mBluetoothAdapter.cancelDiscovery();
						ctx.unregisterReceiver(mBluetoothReceiver);
						Log.i("Sensordrone", "-------->try to connect");

					}
					
					//TODO: Long term this has to be moved out of the receiver
					Set<Entry<String, Drone>> droneset = drones.entrySet();
					Iterator<Entry<String, Drone>> it = droneset.iterator();
					while(it.hasNext())
					{
						Entry<String, Drone> dentry = it.next();
						Drone d = dentry.getValue();
						if(!d.isConnected)
						{
							if(!d.btConnect(device.getAddress()))
							{
								Log.i("Sensordrone", "could not connect");
							}
							else
							{
								//connected.
							}
						}
						myBlinker = new ConnectionBlinker(d, 1000, 0, 0, 255);
						Log.i("Sensordrone", "is it connected? "+d.isConnected);
					}
				}
			}
	    	
	    };
	    Log.i("Sensordrone", "ok");
	    btFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	    ctx.registerReceiver(mBluetoothReceiver, btFilter);
	    mBluetoothAdapter.startDiscovery();


	}

	

}
