package org.ambientdynamix.contextplugins.sensordrone;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.ambientdynamix.api.contextplugin.*;
import org.ambientdynamix.api.contextplugin.security.PrivacyRiskLevel;
import org.ambientdynamix.api.contextplugin.security.SecuredContextInfo;

import com.sensorcon.sdhelper.ConnectionBlinker;
import com.sensorcon.sensordrone.Drone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class SensordronePluginRuntime extends ReactiveContextPluginRuntime
{
	private final String TAG = "Sensordrone";
	Backend backend;

	@Override
	public void start() 
	{
		/*
		 * Nothing to do, since this is a pull plug-in... we're now waiting for context scan requests.
		 */

		Log.i(TAG, "Started!");
	}

	@Override
	public void stop() 
	{
		/*
		 * At this point, the plug-in should cancel any ongoing context scans, if there are any.
		 */
		Log.i(TAG, "Stopped!");
	}

	@Override
	public void destroy() 
	{
		/*
		 * At this point, the plug-in should release any resources.
		 */
		Backend.disable();
		stop();
		Log.i(TAG, "Destroyed!");
	}

	@Override
	public void updateSettings(ContextPluginSettings settings) 
	{

		// Not supported
	}

	@Override
	public void handleContextRequest(UUID requestId, String contextInfoType) 
	{
		Log.i(TAG, "sensordrone context requested "+contextInfoType);
		if(contextInfoType.equals("org.ambientdynamix.contextplugins.context.info.environment.light"))
		{
			SecuredContextInfo aci= new SecuredContextInfo(new AmbientLightContextInfo(), PrivacyRiskLevel.MEDIUM);
			sendContextEvent(requestId, aci, 10000);
		}
		if(contextInfoType.equals("org.ambientdynamix.contextplugins.context.info.environment.temperature"))
		{
			SecuredContextInfo aci= new SecuredContextInfo(new AmbientTemperatureContextInfo(), PrivacyRiskLevel.MEDIUM);
			sendContextEvent(requestId, aci, 120000);
		}
		if(contextInfoType.equals("org.ambientdynamix.contextplugins.context.info.environment.carbonmonoxide"))
		{
			SecuredContextInfo aci= new SecuredContextInfo(new AmbientCarbonMonoxideContextInfo(), PrivacyRiskLevel.MEDIUM);
			sendContextEvent(requestId, aci, 10000);
		}
		if(contextInfoType.equals("org.ambientdynamix.contextplugins.context.action.device.identification"))
		{
			IdentificationContextAction aci = new IdentificationContextAction();
			aci.identify("");
		}
	}

	@Override
	public void handleConfiguredContextRequest(UUID requestId, String contextInfoType, Bundle scanConfig) 
	{
		Log.i(TAG, "sensordrone configured context requested "+contextInfoType);
		if(contextInfoType.equals("org.ambientdynamix.contextplugins.context.info.environment.light"))
		{
			handleContextRequest(requestId, contextInfoType);
		}
		if(contextInfoType.equals("org.ambientdynamix.contextplugins.context.info.environment.temperature"))
		{
			handleContextRequest(requestId, contextInfoType);
		}
		if(contextInfoType.equals("org.ambientdynamix.contextplugins.context.info.environment.carbonmonoxide"))
		{
			handleContextRequest(requestId, contextInfoType);
		}
		if(contextInfoType.equals("org.ambientdynamix.contextplugins.context.action.device.identification"))
		{
			IdentificationContextAction aci = new IdentificationContextAction();	
			if(scanConfig.containsKey("deviceId"))
			{
				aci.identify(scanConfig.getString("deviceId"));			
			}
			else
			{
				handleContextRequest(requestId, contextInfoType);
			}

		}
	}


	@Override
	public void init(PowerScheme arg0, ContextPluginSettings settings)  throws Exception
	{
		Log.i("Sensordrone", "init");
    	backend = new Backend(this.getSecuredContext());
    	this.getPluginFacade().setPluginConfiguredStatus(getSessionId(), true);
	}

	@Override
	public void setPowerScheme(PowerScheme arg0) throws Exception 
	{
		// TODO Auto-generated method stub
		
	}

}