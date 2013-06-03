package org.ambientdynamix.contextplugins.sensordrone;

import java.util.Set;

public interface IAmbientLightContextInfo extends IContextInfo
{
	public abstract double[] getLuxValue();
}