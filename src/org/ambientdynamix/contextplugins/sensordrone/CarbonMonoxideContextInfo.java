package org.ambientdynamix.contextplugins.sensordrone;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.ambientdynamix.contextplugins.contextinterfaces.ICarbonMonoxideContextInfo;
import org.ambientdynamix.contextplugins.contextinterfaces.IContextInfo;

import com.sensorcon.sensordrone.Drone;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class CarbonMonoxideContextInfo implements ICarbonMonoxideContextInfo, IContextInfo
{

	double[] covalues;
	
	public static Parcelable.Creator<CarbonMonoxideContextInfo> CREATOR = new Parcelable.Creator<CarbonMonoxideContextInfo>() 
		{
		public CarbonMonoxideContextInfo createFromParcel(Parcel in) 
		{
			return new CarbonMonoxideContextInfo(in);
		}

		public CarbonMonoxideContextInfo[] newArray(int size) 
		{
			return new CarbonMonoxideContextInfo[size];
		}
	};
	
	@Override
	public String toString() 
	{
		return this.getClass().getSimpleName();
	};
	
	public CarbonMonoxideContextInfo(Parcel in) 
	{
		in.readDoubleArray(covalues);
	}

	/* (non-Javadoc)
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getContextType()
	 */
	@Override
	public String getContextType() 
	{
		return "org.ambientdynamix.contextplugins.carbonmonoxide";
	}


	/* (non-Javadoc)
	 * @see org.ambientdynamix.contextplugins.ambientsound.IAmbientSoundContextInfo#getStringRepresentation(java.lang.String)
	 */
	@Override
	public String getStringRepresentation(String format) 
	{
		String result="";
		for(int i=0; i<covalues.length; i++)
		{
			result=result+covalues[i]+" ";
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
	
	public CarbonMonoxideContextInfo()
	{
		Log.i("Sensordrone", "generate CO context");
		HashMap<String, Drone> drones = Backend.getDroneList();
		if(drones!=null)
		{
			covalues = new double[drones.size()];
			Set<Entry<String, Drone>> droneset = drones.entrySet();
			Iterator<Entry<String, Drone>> it = droneset.iterator();
			int counter=0;
			while(it.hasNext())
			{
				covalues[counter]=it.next().getValue().precisionGas_ppmCarbonMonoxide;
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
		out.writeDoubleArray(getCOValue());
	}


	@Override
	public double[] getCOValue()
	{
		return covalues;
	}
	
}
