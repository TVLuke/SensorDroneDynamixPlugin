package org.ambientdynamix.contextplugins.sensordrone;

import java.util.UUID;

import org.ambientdynamix.api.contextplugin.ContextPluginRuntime;
import org.ambientdynamix.api.contextplugin.IContextPluginConfigurationViewFactory;
import org.ambientdynamix.api.contextplugin.IContextPluginInteractionViewFactory;
import org.ambientdynamix.api.contextplugin.InteractiveContextPluginRuntime;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SensorDronePluginConfigurationActivity extends Activity implements IContextPluginConfigurationViewFactory
{

	@Override
	public void destroyView() throws Exception 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public View initializeView(final Context context, ContextPluginRuntime arg1, int arg2)
			throws Exception 
	{
		Log.i("Sensordrone", "initialize Views 2");
		// Discover our screen size for proper formatting 
		DisplayMetrics met = context.getResources().getDisplayMetrics();

		// Access our Locale via the incoming context's resource configuration to determine language
		String language = context.getResources().getConfiguration().locale.getDisplayLanguage();
		
		// Main layout. 
		final LinearLayout rootLayout = new LinearLayout(context);
		
        Button b = new Button(context);
        b.setText("Search");
        b.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v)
            {
            	Log.i("Sensordrone", "pressed the button 2");
            	Backend backend = new Backend(context);
            }
        });
        rootLayout.addView(b, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        		FrameLayout.LayoutParams.WRAP_CONTENT));
		return rootLayout;
	}
}
