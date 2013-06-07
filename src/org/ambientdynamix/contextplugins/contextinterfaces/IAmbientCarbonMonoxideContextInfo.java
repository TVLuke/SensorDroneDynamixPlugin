package org.ambientdynamix.contextplugins.contextinterfaces;

import org.ambientdynamix.api.application.IContextInfo;

public interface IAmbientCarbonMonoxideContextInfo extends IContextInfo
{
	public abstract double[] getCOValue();
}
