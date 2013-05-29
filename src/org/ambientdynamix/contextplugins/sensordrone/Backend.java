package org.ambientdynamix.contextplugins.sensordrone;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sensorcon.sensordrone.Drone;

public class Backend 
{
	private static Drone drone; //the Sensordrone
	private BroadcastReceiver mBluetoothReceiver;
	private BluetoothAdapter mBluetoothAdapter;
	
	public Backend()
	{
		Log.i("Sensordrone", "starting backend process");
		drone = new Drone();
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
	
	public void scanToConnect(Drone drone)
	{

		// Is Bluetooth on?
	    if (!mBluetoothAdapter.isEnabled()) 
	    {
			// Don't proceed until the user turns Bluetooth on.
			return;
		}
	    
	    mBluetoothReceiver = new BroadcastReceiver() 
	    {

			@Override
			public void onReceive(Context arg0, Intent arg1) 
			{
				Log.i("Sensordrone", "found Bloutooth Device");
				
			}
	    	
	    };

	}

}
