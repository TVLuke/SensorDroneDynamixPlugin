package org.ambientdynamix.contextplugins.sensordrone;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.ambientdynamix.api.application.IContextInfo;
import org.ambientdynamix.contextplugins.contextinterfaces.IAmbientTemperatureContextInfo;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.sensorcon.sensordrone.Drone;

public class AmbientTemperatureContextInfo implements IAmbientTemperatureContextInfo, IContextInfo
{
	double[] tempvalues= new double[1];
	
	public static Parcelable.Creator<AmbientTemperatureContextInfo> CREATOR = new Parcelable.Creator<AmbientTemperatureContextInfo>() 
		{
		public AmbientTemperatureContextInfo createFromParcel(Parcel in) 
		{
			return new AmbientTemperatureContextInfo(in);
		}

		public AmbientTemperatureContextInfo[] newArray(int size) 
		{
			return new AmbientTemperatureContextInfo[size];
		}
	};
	

	@Override
	public String toString() 
	{
		return this.getClass().getSimpleName();
	};

	/* (non-Javadoc)
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getContextType()
	 */
	@Override
	public String getContextType() 
	{
		return "org.ambientdynamix.contextplugins.ambienttemperature";
	}

	/* (non-Javadoc)
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getStringRepresentation(java.lang.String)
	 */
	@Override
	public String getStringRepresentation(String format) 
	{
		String result="";
		for(int i=0; i<tempvalues.length; i++)
		{
			result = tempvalues[i]+" ";
		}
		if (format.equalsIgnoreCase("text/plain"))
			return result;
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getImplementingClassname()
	 */
	@Override
	public String getImplementingClassname() 
	{
		return this.getClass().getName();
	}

	/* (non-Javadoc)
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getStringRepresentationFormats()
	 */
	@Override
	public Set<String> getStringRepresentationFormats() 
	{
		Set<String> formats = new HashSet<String>();
		formats.add("text/plain");
		return formats;
	};

	public AmbientTemperatureContextInfo()
	{
		Log.i("Sensordrone", "generate temp context 3");
		HashMap<String, Drone> drones = Backend.getDroneList();
		if(drones!=null)
		{
			Log.i("Sensordrone", "not null");
			Set<Entry<String, Drone>> droneset = drones.entrySet();
			Log.i("Sensordrone", "...");
			Iterator<Entry<String, Drone>> it = droneset.iterator();
			Log.i("Sensordrone", "now for the counter");
			int counter=0;
			while(it.hasNext())
			{
				Drone d = it.next().getValue();
				if(d.isConnected)
				{
					Log.i("Sensordrone", d.temperature_Celcius+" °C");
					tempvalues[counter]=d.temperature_Celcius;
				}
				else
				{
					tempvalues=new double[1];
					tempvalues[counter]=-999.0;		
				}
				counter++;
			}
		}
	}

	private AmbientTemperatureContextInfo(final Parcel in) 
	{
		tempvalues = in.createDoubleArray();
	}

	public IBinder asBinder() 
	{
		return null;
	}

	public int describeContents() 
	{
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) 
	{
		out.writeDoubleArray(tempvalues);
	}

	@Override
	public double[] getCelciusValue() 
	{
		return tempvalues;
	}
	
}

