package org.ambientdynamix.contextplugins.contextinterfaces;

import org.ambientdynamix.api.application.IContextInfo;

public interface IAmbientHumidityContextInfo extends IContextInfo
{
	public abstract double[] getHumidityValue();
}
