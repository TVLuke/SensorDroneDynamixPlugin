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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.ambientdynamix.contextplugins.context.info.environment.ITemperatureContextInfo;
import org.ambientdynamix.contextplugins.info.meta.IContextSource;
import org.ambientdynamix.contextplugins.info.meta.IDevice;
import org.ambientdynamix.contextplugins.info.meta.ISourcedContext;
import org.ambientdynamix.contextplugins.info.meta.ILocalizedContext;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.LinearLayout;

import com.sensorcon.sensordrone.Drone;

public class AmbientTemperatureContextInfo implements ITemperatureContextInfo, ISourcedContext, ILocalizedContext
{
	double[] tempvalues= new double[1];
	List<IContextSource> sources = new ArrayList<IContextSource>();
	
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
	}

	/* (non-Javadoc)
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getContextType()
	 */
	@Override
	public String getContextType() 
	{
		return "org.ambientdynamix.contextplugins.context.info.environment.temperature";
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
			for(int i=0; i<tempvalues.length; i++)
			{
				result = tempvalues[i]+" ";
			}
		}
		else if (format.equalsIgnoreCase("XML"))
		{
			for(int i=0; i<tempvalues.length; i++)
			{
				result="<temperature>"+tempvalues[i]+"</temperature>\n";
			}
		}
		return result;
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
		formats.add("XML");
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
			tempvalues = new double[drones.size()];
			sources = new ArrayList<IContextSource>();
			while(it.hasNext())
			{
				Drone d = it.next().getValue();
				Sensordrone dev = new Sensordrone(d);
				if(d.isConnected)
				{
					Log.i("Sensordrone", d.temperature_Celcius+" °C");
					tempvalues[counter]=d.temperature_Celcius;
					sources.add(dev);
				}
				else
				{
					tempvalues=new double[1];
					tempvalues[counter]=-999.0;	
					sources.add(dev);
				}
				counter++;
			}
		}
	}

	private AmbientTemperatureContextInfo(final Parcel in) 
	{
		tempvalues = in.createDoubleArray();
		in.readList(sources, getClass().getClassLoader()); //TODO: not sure why there is a need to cast?
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
		out.writeList(sources);
	}

	@Override
	public double[] getCelciusValue() 
	{
		return tempvalues;
	}

	@Override
	public List<IContextSource> getSources() 
	{
		return sources;
	}

	@Override
	public double getLatitude() 
	{
		return 0; //TODO:
	}

	@Override
	public double getLongitude() 
	{
		return 0;//TODO:
	}

	@Override
	public double getAltitude() 
	{
		return 0;//TODO:
	}

	@Override
	public void atUIComponentsForLocalizedContext(LinearLayout root) 
	{
		
	}
	
}

