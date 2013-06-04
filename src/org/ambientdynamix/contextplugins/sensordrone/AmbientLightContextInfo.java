/*
 * Copyright (C) the Dynamix Framework Project
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

import com.sensorcon.sensordrone.Drone;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

class AmbientLightContextInfo implements IContextInfo, IAmbientLightContextInfo 
{
	double[] lightvalues;
	
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
	};

	/* (non-Javadoc)
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getContextType()
	 */
	@Override
	public String getContextType() 
	{
		return "org.ambientdynamix.contextplugins.ambientlight";
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
	};

	public AmbientLightContextInfo()
	{
		Log.i("Sensordrone", "generate light context");
		HashMap<String, Drone> drones = Backend.getDroneList();
		if(drones!=null)
		{
			lightvalues = new double[drones.size()];
			Set<Entry<String, Drone>> droneset = drones.entrySet();
			Iterator<Entry<String, Drone>> it = droneset.iterator();
			int counter=0;
			while(it.hasNext())
			{
				lightvalues[counter]=it.next().getValue().rgbcLux;
				counter++;
			}
		}
	}

	private AmbientLightContextInfo(final Parcel in) 
	{
		in.readDoubleArray(lightvalues);
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
	
}
