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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SensorDronePluginConfigurationActivity extends Activity implements IContextPluginConfigurationViewFactory
{
	LinearLayout rootLayout;
	LinearLayout listLayout;
	private static final String TAG = "Sensordrone";
	private Context ctx;
	
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
		Log.i("Sensordrone", "initialize Views 6");
		ctx=context;
		// Discover our screen size for proper formatting 
		DisplayMetrics met = context.getResources().getDisplayMetrics();

		// Access our Locale via the incoming context's resource configuration to determine language
		String language = context.getResources().getConfiguration().locale.getDisplayLanguage();
		
		// Main layout. 
		rootLayout = new LinearLayout(context);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		Button updatebutton = new Button(context);
		updatebutton.setText("Update List");
		updatebutton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v)
            {
            	Log.i("Sensordrone", "pressed the button 6c");
            	updateListView();
            }
        });
		
		Log.i("Sensordrone", "now to the list b");
		listLayout = new LinearLayout(context);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		
        Button b = new Button(context);
        b.setText("Search");
        b.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v)
            {
            	Log.i("Sensordrone", "pressed the button 6");
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

        
        
        //Add Header
        rootLayout.addView(header,  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));
        //updateButton
        rootLayout.addView(updatebutton, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));
        
        //add drone 1 textview
        rootLayout.addView(listLayout, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));

        //buttons
        rootLayout.addView(b, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(b2, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));        
		return rootLayout;
	}
	
	private void updateListView()
	{
		HashMap<String, Drone> drones = Backend.getDroneList();
		Set<Entry<String, Drone>> droneset = drones.entrySet();
		Iterator<Entry<String, Drone>> it = droneset.iterator();
		int counter = 0;
		listLayout.removeAllViews();
		while(it.hasNext())
		{
			Entry<String, Drone> dentry = it.next();
			Drone d = dentry.getValue();
			TextView tv = new TextView(ctx);
			tv.setText((counter+1)+": "+d.lastMAC);
			tv.setBackgroundColor(0x00ff0000);
			tv.setTextSize(20);
			TextView v1 = new TextView(ctx);
			v1.setText(""+d.temperature_Celcius+" C");
	        listLayout.addView(tv, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
	        		FrameLayout.LayoutParams.WRAP_CONTENT));       
	        listLayout.addView(v1, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
	        		FrameLayout.LayoutParams.WRAP_CONTENT));   
		}

	}
	
	
}
