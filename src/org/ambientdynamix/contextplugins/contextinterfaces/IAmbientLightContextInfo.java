package org.ambientdynamix.contextplugins.contextinterfaces;

import java.util.Set;

import org.ambientdynamix.api.application.IContextInfo;

public interface IAmbientLightContextInfo extends IContextInfo
{
	public abstract double[] getLuxValue();
	
	public abstract double[] getRedLevelValue();
	
	public abstract double[] getGreenLevelValue();
	
	public abstract double[] getBlueLevelValue();
}