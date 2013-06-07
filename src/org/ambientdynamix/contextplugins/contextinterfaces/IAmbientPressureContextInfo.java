package org.ambientdynamix.contextplugins.contextinterfaces;

import org.ambientdynamix.api.application.IContextInfo;

public interface IAmbientPressureContextInfo extends IContextInfo
{
	public abstract double[] getPaValue();
}
