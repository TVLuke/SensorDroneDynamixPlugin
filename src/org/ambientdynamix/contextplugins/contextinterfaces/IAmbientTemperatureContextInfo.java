package org.ambientdynamix.contextplugins.contextinterfaces;

import org.ambientdynamix.api.application.IContextInfo;

public interface IAmbientTemperatureContextInfo extends IContextInfo
{
	public abstract double[] getCelciusValue();
}
