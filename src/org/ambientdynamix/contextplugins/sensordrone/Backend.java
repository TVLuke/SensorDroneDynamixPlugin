package org.ambientdynamix.contextplugins.sensordrone;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sensorcon.sensordrone.Drone;

public class Backend 
{
	private static Drone drone; //the Sensordrone
	private BroadcastReceiver mBluetoothReceiver;
	private BluetoothAdapter mBluetoothAdapter;
	private IntentFilter btFilter;
	Context ctx;
	
	public Backend(Context context)
	{
		ctx = context;
		Log.i("Sensordrone", "starting backend process");
		drone = new Drone(); //THIS IS A PROBLM BECAUSE WE GET A CLASSDEFNOTFOUNDERROR
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
		scanToConnect(drone);
	}
	
	//This is addapted from the SDHelper Library 
	public void scanToConnect(final Drone drone)
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
						drone.btConnect(device.getAddress());
						mBluetoothAdapter.startDiscovery();
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
