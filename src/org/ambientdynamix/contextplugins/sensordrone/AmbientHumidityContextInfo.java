/*
 * Copyright (C) Institute of Telematics, Lukas Ruge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ambientdynamix.contextplugins.sensordrone;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.ambientdynamix.api.application.IContextInfo;
import org.ambientdynamix.contextplugins.context.info.environment.IHumidityContextInfo;

import com.sensorcon.sensordrone.Drone;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class AmbientHumidityContextInfo  implements IHumidityContextInfo
{

	double[] humidityvalues= new double[1];;
	
	public static Parcelable.Creator<AmbientHumidityContextInfo> CREATOR = new Parcelable.Creator<AmbientHumidityContextInfo>() 
		{
		public AmbientHumidityContextInfo createFromParcel(Parcel in) 
		{
			return new AmbientHumidityContextInfo(in);
		}

		public AmbientHumidityContextInfo[] newArray(int size) 
		{
			return new AmbientHumidityContextInfo[size];
		}
	};
	
	public AmbientHumidityContextInfo(Parcel in) 
	{
		humidityvalues = in.createDoubleArray();
	}

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
		return "org.ambientdynamix.contextplugins.context.info.environment.humidity";
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
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getStringRepresentation(java.lang.String)
	 */
	@Override
	public String getStringRepresentation(String format) 
	{
		String result="";
		if (format.equalsIgnoreCase("text/plain"))
		{
			for(int i=0; i<humidityvalues.length; i++)
			{
				result=result+humidityvalues[i]+" ";
			}
			return result;	
		}
		if(format.equalsIgnoreCase("xml"))
		{
			result=result+"<values>";
			for(int i=0; i<humidityvalues.length; i++)
			{
				result="<value>"+humidityvalues[i]+"<value>";
			}
			result=result+"</values>";
			return result;
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getStringRepresentationFormats()
	 */
	@Override
	public Set<String> getStringRepresentationFormats() 
	{
		Set<String> formats = new HashSet<String>();
		formats.add("text/plain");
		formats.add("XML");
		return formats;
	};
	
	public AmbientHumidityContextInfo()
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
			humidityvalues = new double[drones.size()];
			while(it.hasNext())
			{
				Drone d = it.next().getValue();
				if(d.isConnected)
				{
					Log.i("Sensordrone", d.temperature_Celcius+" �C");
					humidityvalues[counter]=d.humidity_Percent;
				}
				else
				{
					humidityvalues=new double[1];
					humidityvalues[counter]=-999.0;		
				}
				counter++;
			}
		}
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
		out.writeDoubleArray(getHumidityValue());
	}

	
	@Override
	public double[] getHumidityValue() 
	{
		return humidityvalues;
	}

}
