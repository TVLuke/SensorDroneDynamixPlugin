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
