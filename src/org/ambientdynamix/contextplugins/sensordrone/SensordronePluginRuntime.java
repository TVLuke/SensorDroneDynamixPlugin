package org.ambientdynamix.contextplugins.sensordrone;

import java.util.UUID;

import org.ambientdynamix.api.contextplugin.*;

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
		Log.i(TAG, "red dot context requested3");

		Log.i(TAG, "red dot end of method");
	}

	@Override
	public void handleConfiguredContextRequest(UUID requestId, String contextInfoType, Bundle scanConfig) 
	{
		Log.i(TAG, "config noooooo");
		Log.w(TAG, "Configuration not supported!");
		handleContextRequest(requestId, contextInfoType);
	}

	@Override
	public void init(PowerScheme arg0, ContextPluginSettings arg1) throws Exception
	{
		Log.i("Sensordrone", "init");
    	backend = new Backend(this.getSecuredContext());
    	this.getPluginFacade().setPluginConfiguredStatus(getSessionId(), true);
	}

	@Override
	public void setPowerScheme(PowerScheme arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}