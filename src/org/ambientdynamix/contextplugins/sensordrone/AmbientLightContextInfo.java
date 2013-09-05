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
import org.ambientdynamix.contextplugins.context.info.environment.ILightContextInfo;

import com.sensorcon.sensordrone.Drone;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

class AmbientLightContextInfo implements ILightContextInfo
{
	double[] lightvalues= new double[1];
	double[] redvalues = new double[1];
	double[] greenvalues = new double[1];
	double[] bluevalues = new double[1];
	
	public static Parcelable.Creator<AmbientLightContextInfo> CREATOR = new Parcelable.Creator<AmbientLightContextInfo>() 
		{
		public AmbientLightContextInfo createFromParcel(Parcel in) 
		{
			return new AmbientLightContextInfo(in);
		}

		public AmbientLightContextInfo[] newArray(int size) 
		{
			return new AmbientLightContextInfo[size];
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
		return "org.ambientdynamix.contextplugins.context.info.environment.light";
	}

	/* (non-Javadoc)
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getStringRepresentation(java.lang.String)
	 */
	@Override
	public String getStringRepresentation(String format) 
	{
		String result="";
		for(int i=0; i<lightvalues.length; i++)
		{
			result=result+lightvalues[i]+" ";
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
	}

	public AmbientLightContextInfo()
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
			lightvalues = new double[drones.size()];
			redvalues = new double[drones.size()];
			greenvalues = new double[drones.size()];
			bluevalues = new double[drones.size()];
			while(it.hasNext())
			{
				Drone d = it.next().getValue();
				if(d.isConnected)
				{
					Log.i("Sensordrone", d.temperature_Celcius+" °C");
					lightvalues[counter]=d.rgbcLux;
					redvalues[counter]=d.rgbcRedChannel;
					greenvalues[counter]=d.rgbcGreenChannel;
					bluevalues[counter]=d.rgbcBlueChannel;
				}
				else
				{
					lightvalues=new double[1];
					lightvalues[counter]=-999.0;	
					redvalues[counter]=-999.0;
					greenvalues[counter]=-999.0;
					bluevalues[counter]=-999.0;
				}
				counter++;
			}
		}
	}

	private AmbientLightContextInfo(final Parcel in) 
	{
		lightvalues = in.createDoubleArray();
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
		out.writeDoubleArray(getLuxValue());
	}

	@Override
	public double[] getLuxValue() 
	{
		return lightvalues;
	}

	@Override
	public double[] getRedLevelValue() 
	{
		return redvalues;
	}

	@Override
	public double[] getGreenLevelValue() 
	{
		return greenvalues;
	}

	@Override
	public double[] getBlueLevelValue() 
	{
		return bluevalues;
	}
	
}
