package org.ambientdynamix.contextplugins.sensordrone;

import org.ambientdynamix.contextplugins.info.meta.IDevice;

import android.os.Parcel;
import android.os.Parcelable;

import com.sensorcon.sensordrone.Drone;

public class Sensordrone implements IDevice
{

	/**
     * Static Creator factory for Parcelable.
     */
    public static Parcelable.Creator<Sensordrone> CREATOR = new Parcelable.Creator<Sensordrone>() 
    {
		public Sensordrone createFromParcel(Parcel in) 
		{
		    return new Sensordrone(in);
		}
	
		public Sensordrone[] newArray(int size) 
		{
		    return new Sensordrone[size];
		}
    };
    
	float batteryLevel;
	String mac;
	int firmwarerev;
	int firmwarevers;
	int hardwarevers;
	boolean connected;
	boolean ischarging;
	
	Sensordrone(Drone d)
	{
		batteryLevel = d.batteryVoltage_Volts;
		mac = d.lastMAC;
		firmwarerev = d.firmwareRevision;
		firmwarevers = d.firmwareVersion;
		hardwarevers = d.hardwareVersion;
		connected = d.isConnected;
		ischarging = d.isCharging;
		
	}

	public Sensordrone(Parcel in) 
	{
		batteryLevel = in.readFloat();
		mac = in.readString();
		firmwarerev = in.readInt();
		firmwarevers = in.readInt();
		hardwarevers = in.readInt();
		connected = in.readByte() == 1;
		ischarging = in.readByte() == 1;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) 
	{
		out.writeFloat(batteryLevel);
		out.writeString(mac);
		out.writeInt(firmwarerev);
		out.writeInt(firmwarevers);
		out.writeInt(hardwarevers);
		out.writeByte((byte) (connected ? 1 : 0));
		out.writeByte((byte) (ischarging ? 1 : 0));
	}

	@Override
	public String sourceId() 
	{
		return mac;
	}

	@Override
	public String diviceName() 
	{
		return "Sensordrone "+mac;
	}

	@Override
	public String urlOfDeviceSymbol() 
	{
		return null;
	}

	@Override
	public float energyLevel() 
	{
		return batteryLevel;
	}

	@Override
	public int hardwareVersionNumber() 
	{
		return hardwarevers;
	}

	@Override
	public int firmwareVersionNumber() 
	{
		return firmwarevers;
	}

	@Override
	public int firmwareRevisionNumber() 
	{
		return firmwarerev;
	}

	@Override
	public boolean isBatteryPowered() 
	{
		return true;
	}

	@Override
	public boolean isRecharable() 
	{
		return true;
	}

	@Override
	public boolean isCharging() 
	{
		return ischarging;
	}

	@Override
	public boolean isWireless() 
	{
		return true;
	}

	@Override
	public boolean isConnected() 
	{
		return connected;
	}

	@Override
	public String ConnectionType() 
	{
		return "Bluetooth";
	}

	@Override
	public String getMACAddress() 
	{
		return mac;
	}
	
	public int remainingStorage()
	{
		return 0;
	}

	@Override
	public int describeContents() 
	{
		return 0;
	}
}
