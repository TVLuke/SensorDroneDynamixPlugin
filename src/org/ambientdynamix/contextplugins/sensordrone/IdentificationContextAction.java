package org.ambientdynamix.contextplugins.sensordrone;

import java.util.Set;

import org.ambientdynamix.contextplugins.context.action.device.IIdentificationContextAction;

import android.os.Parcel;



public class IdentificationContextAction implements IIdentificationContextAction
{

	
	@Override
	public String toString() 
	{
		return this.getClass().getSimpleName();
	}
	
	@Override
	public void identify(String id)
	{
		Backend.identifiy(id);
	}

	@Override
	public String getContextType() 
	{
		return "org.ambientdynamix.contextplugins.context.action.device.identification";
	}

	@Override
	public String getImplementingClassname() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringRepresentation(String arg0) 
	{
		return null;
	}

	@Override
	public Set<String> getStringRepresentationFormats() 
	{
		return null;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
