package org.ambientdynamix.contextplugins.contextinterfaces;

import java.util.Set;

public interface IAmbientLightContextInfo
{
	public abstract double[] getLuxValue();
	
	public abstract double[] getRedLevelValue();
	
	public abstract double[] getGreenLevelValue();
	
	public abstract double[] getBlueLevelValue();
}