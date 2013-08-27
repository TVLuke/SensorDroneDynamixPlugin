package org.ambientdynamix.contextplugins.info.meta;

import android.widget.LinearLayout;

public interface ILocalizedContext {

	public double getLatitude();
	public double getLongitude();
	public double getAltitude();
	
	public void atUIComponentsForLocalizedContext(LinearLayout root);
}
