package org.ambientdynamix.contextplugins.sensordrone;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.ambientdynamix.api.contextplugin.ContextPluginRuntime;
import org.ambientdynamix.api.contextplugin.IContextPluginConfigurationViewFactory;
import org.ambientdynamix.api.contextplugin.IContextPluginInteractionViewFactory;
import org.ambientdynamix.api.contextplugin.InteractiveContextPluginRuntime;

import com.sensorcon.sensordrone.Drone;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SensorDronePluginConfigurationActivity extends Activity implements IContextPluginConfigurationViewFactory
{

	UIUpdater uiupdater;
	LinearLayout rootLayout;
	private static final String TAG = "Sensordrone";
	
	@Override
	public void destroyView() throws Exception 
	{
		// TODO Auto-generated method stub
		Backend.disable();
	}

	@Override
	public View initializeView(final Context context, ContextPluginRuntime arg1, int arg2)
			throws Exception 
	{
		Log.i("Sensordrone", "initialize Views 5");
		// Discover our screen size for proper formatting 
		DisplayMetrics met = context.getResources().getDisplayMetrics();

		// Access our Locale via the incoming context's resource configuration to determine language
		String language = context.getResources().getConfiguration().locale.getDisplayLanguage();
		
		// Main layout. 
		rootLayout = new LinearLayout(context);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
        Button b = new Button(context);
        b.setText("Search");
        b.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v)
            {
            	Log.i("Sensordrone", "pressed the button 5");
            	Backend backend = new Backend(context);
            }
        });
        Button b2 = new Button(context);
        b2.setText("StopAll");
        b2.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v)
            {
            	Log.i("Sensordrone", "pressed the button 5b");
            	Backend.disable();
            }
        });
        //Header
        TextView header = new TextView(context);
        header.setText("Sensordrone Plugin Configuration");
        //Drones
        TextView drone1 = new TextView(context);
        drone1.setBackgroundColor(0xfff00000);
        
        TextView drone2 = new TextView(context);
        drone2.setBackgroundColor(0x000fff00);
        
        
        //Add Header
        rootLayout.addView(header,  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));
        //add drone 1 textview
        rootLayout.addView(drone1, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));
        //add drone 2 textview
        rootLayout.addView(drone2, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));
        //buttons
        rootLayout.addView(b, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(b2, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));        
        uiupdater = new UIUpdater();
        uiupdater.run();
		return rootLayout;
	}
	
	private class UIUpdater implements Runnable
	{

		private Handler handler = new Handler();
		private int delay=1000;
		
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			handler.removeCallbacks(this); // remove the old callback
			handler.postDelayed(this, delay); // register a new one
			Log.i(TAG, "child count"+rootLayout.getChildCount());
			HashMap<String, Drone> drones = Backend.getDroneList();
			Set<Entry<String, Drone>> droneset = drones.entrySet();
			Iterator<Entry<String, Drone>> it = droneset.iterator();
			while(it.hasNext())
			{
				Entry<String, Drone> dentry = it.next();
				Drone d = dentry.getValue();
				
			}
		}
		
		public void onResume() 
		{
			handler.postDelayed(this, delay);
		}

		public void onPause() 
		{
			handler.removeCallbacks(this); // stop the map from updating
		}
		
	}
}
