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
import android.os.Handler;
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
	private static HashMap<String, ConnectionBlinker> blinkerarray; 
	private static HashMap<String, ArrayList<SDStreamer>> streamers;
	private BroadcastReceiver mBluetoothReceiver;
	private BluetoothAdapter mBluetoothAdapter;
	private IntentFilter btFilter;
	Context ctx;
	private static boolean running=false;
	int[] sensortypes;
	private static int FREQ = 60000;
	
	public Backend(Context context)
	{
		ctx = context;
		running=true;
		Log.i(TAG, "starting backend process");
		drones = new HashMap<String, Drone>(); 
		streamers = new HashMap<String, ArrayList<SDStreamer>>();
		blinkerarray = new HashMap<String, ConnectionBlinker>();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	    if (!mBluetoothAdapter.isEnabled()) 
	    {
	    	Log.i(TAG, "Bluetooth was off, will now be turned on");
	    	mBluetoothAdapter.enable();
	    	try 
	    	{
				Thread.sleep(2000);//so that its on by the time we go to scanning... there might be other, better ways of doing this. probably.
			} 
	    	catch (InterruptedException e) 
	    	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    else
	    { 
	        //is enabled
	    } 
	    if(running)
	    {
			scanToConnect();
			Thread t1 = new Thread( new BackendRunner());
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
	    	try 
	    	{
				Thread.sleep(1000);//so that its on by the time we go to scanning... there might be other, better ways of doing this. probably.
			} 
	    	catch (InterruptedException e) 
	    	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	scanToConnect();
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

						mBluetoothAdapter.cancelDiscovery();
						ctx.unregisterReceiver(mBluetoothReceiver);
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
		blinkerarray.put(device.getAddress(), new ConnectionBlinker(drone, 1000, 0, 0, 255));
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
				//Log.i(TAG, "sensordrone Temperature "+drone.lastMAC+" "+drone.temperature_Celcius);
				if(drone.humidityStatus)
				{
					ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
					SDStreamer s = sarray.get(1);
					s.run();
				}
				
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
				Log.i(TAG, "sensordrone prssure status");
				if(drone.pressureStatus)
				{
					ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
					SDStreamer s = sarray.get(2);
					s.run();
				}
				
			}

			@Override
			public void reducingGasStatus(EventObject arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void rgbcStatus(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone rgbc status");
				// TODO Auto-generated method stub
				if(drone.rgbcStatus)
				{
					ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
					SDStreamer s = sarray.get(4);
					s.run();
				}
			}

			@Override
			public void temperatureStatus(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone temp status");
				//Log.i(TAG, "sensordrone Temperature "+drone.lastMAC+" "+drone.temperature_Celcius);
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
				//drone.quickEnable(drone.QS_TYPE_TEMPERATURE);
				drone.measureTemperature();
				
				drone.enablePressure();
				//drone.quickEnable(drone.QS_TYPE_PRESSURE);
				drone.measurePressure();
				
				drone.enableRGBC();
				//drone.quickEnable(drone.QS_TYPE_RGBC);
				drone.measureRGBC();
				
				drone.enableHumidity();
				//drone.quickEnable(drone.QS_TYPE_HUMIDITY);
				drone.measureHumidity();
				
				drone.enableADC();
				//drone.quickEnable(drone.QS_TYPE_ADC);
				drone.measureExternalADC();
				
				blinkerarray.get(drone.lastMAC).enable();
				blinkerarray.get(drone.lastMAC).run();
				
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
				//disable temperature
				drone.disableTemperature();
				drone.quickDisable(drone.QS_TYPE_TEMPERATURE);
				
				drone.disablePressure();
				drone.quickDisable(drone.QS_TYPE_PRESSURE);
				
				drone.disableRGBC();
				drone.quickDisable(drone.QS_TYPE_RGBC);
				
				drone.disableHumidity();
				drone.quickDisable(drone.QS_TYPE_HUMIDITY);	
				
				drone.disableADC();
				drone.quickDisable(drone.QS_TYPE_ADC);

				blinkerarray.get(drone.lastMAC).disable();
				blinkerarray.put(drone.lastMAC, new ConnectionBlinker(drone, 1000, 0, 0, 000));
				blinkerarray.get(drone.lastMAC).enable();
				blinkerarray.get(drone.lastMAC).run();
				blinkerarray.get(drone.lastMAC).disable();
				
				ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
				for(int i=0; i<sensortypes.length; i++)
				{
					sarray.get(i).disable();
				}
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
				
				drone.disableTemperature();
				drone.quickDisable(drone.QS_TYPE_TEMPERATURE);
				
				drone.disablePressure();
				drone.quickDisable(drone.QS_TYPE_PRESSURE);
				
				drone.disableRGBC();
				drone.quickDisable(drone.QS_TYPE_RGBC);
				
				drone.disableHumidity();
				drone.quickDisable(drone.QS_TYPE_HUMIDITY);	
				
				drone.disableADC();
				drone.quickDisable(drone.QS_TYPE_ADC);
				
				blinkerarray.get(drone.lastMAC).disable();
				blinkerarray.put(drone.lastMAC, new ConnectionBlinker(drone, 1000, 0, 0, 000));
				blinkerarray.get(drone.lastMAC).enable();
				blinkerarray.get(drone.lastMAC).run();
				blinkerarray.get(drone.lastMAC).disable();
				
				ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
				for(int i=0; i<sensortypes.length; i++)
				{
					sarray.get(i).disable();
				}
			}

			@Override
			public void humidityMeasured(EventObject arg0) 
			{
				ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
				SDStreamer s = sarray.get(1);
				s.streamHandler.postDelayed(s, FREQ);	
				Log.i(TAG, "sensordrone Humidity % "+drone.humidity_Percent);
			
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
				ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
				SDStreamer s = sarray.get(2);
				s.streamHandler.postDelayed(s, FREQ);	
				Log.i(TAG, "sensordrone pressure level in Pa "+drone.pressure_Pascals);
				
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
				ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
				SDStreamer s = sarray.get(4);
				s.streamHandler.postDelayed(s, FREQ);
				Log.i(TAG, "sensordrone lux "+drone.rgbcLux);
			}

			@Override
			public void temperatureMeasured(EventObject arg0) 
			{
				Log.i(TAG, "sensordrone temp measured");
				Log.i(TAG, "sensordrone Temperature "+drone.lastMAC+" "+drone.temperature_Celcius);
				ArrayList<SDStreamer> sarray =streamers.get(""+drone.lastMAC);
				SDStreamer s = sarray.get(0);
				s.streamHandler.postDelayed(s, FREQ);				
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
		private Handler handler = new Handler();
		private int delay=1000;
		long counter=0;
		
		@Override
		public void run() 
		{
				Log.i(TAG, "drones size"+drones.size());	
				//TODO: Try to connect
				if(!running)
				{
					handler.removeCallbacks(this);
					return;
				}
				if(counter%100==0)
				{
				    ctx.registerReceiver(mBluetoothReceiver, btFilter);
				    mBluetoothAdapter.startDiscovery();
				}
				if(counter%150==0)
				{
					mBluetoothAdapter.cancelDiscovery();
					ctx.unregisterReceiver(mBluetoothReceiver);
				}
				Set<Entry<String, Drone>> droneset = drones.entrySet();
				Iterator<Entry<String, Drone>> it = droneset.iterator();
		    	Set<BluetoothDevice> bondeddevices = mBluetoothAdapter.getBondedDevices();
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
	
						}
					}
					else
					{
						//TODO: the device-representation is connected, however, it may have gotten out of range and that may not be known or something, so we better check in the list of bonded devices
						Iterator<BluetoothDevice> bit = bondeddevices.iterator();
						boolean actuallyconnected=false;
						while(bit.hasNext())
						{
							BluetoothDevice div = bit.next();
							if(div.getAddress().equals(dentry.getKey()))
							{
								//the adress of this device was actually in the list
								actuallyconnected=true;
							}
						}
						if(!actuallyconnected)//it wasn't found under the bonded divices.
						{
							d.disconnect();
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
			handler.removeCallbacks(this); // remove the old callback
			if(running)
			{
			handler.postDelayed(this, delay); // register a new one
			}
		}
		
		public void onResume() 
		{
			handler.postDelayed(this, delay);
		}

		public void onPause() 
		{
			handler.removeCallbacks(this); // stop the map from updating
		}
	}
	
	public static void disable()
	{
		Log.i(TAG, "Disable the runner");
		Set<Entry<String, Drone>> droneset = drones.entrySet();
		Log.i(TAG, "a");
		Iterator<Entry<String, Drone>> it = droneset.iterator();
		Log.i(TAG, "b");
		running=false;
		while(it.hasNext())
		{
			Log.i(TAG, "c");
			Entry<String, Drone> dentry = it.next();
			Log.i(TAG, "d");
			Drone d = dentry.getValue();
			Log.i(TAG, "e");
			d.disconnect();
			Log.i(TAG, "f");
			Log.i(TAG, "is it connected? "+d.isConnected);
			Log.i(TAG, "g");
		}
	
	}

	public static HashMap<String, Drone> getDroneList() 
	{
		return drones;
		
	}
	
	public static boolean isRunning()
	{
		return running;
	}
	
	public static void identifiy(String id)
	{
		HashMap<String, Drone> drones = Backend.getDroneList();
		Set<Entry<String, Drone>> droneset = drones.entrySet();
		Iterator<Entry<String, Drone>> it = droneset.iterator();
		while(it.hasNext())
		{
			Drone d = it.next().getValue();
			ConnectionBlinker cb = blinkerarray.get(d.lastMAC);
			cb.setColors(255, 0, 0);
			cb.setRate(200);
			cb.run();
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cb.setRate(1000);
			cb.setColors(0, 0, 255);
		}
	}

}
