package org.ambientdynamix.contextplugins.sensordrone;

import java.util.Set;

public interface IAmbientLightContextInfo 
{

	public abstract String getContextType();

	public abstract String getStringRepresentation(String format);

	public abstract String getImplementingClassname();

	public abstract Set<String> getStringRepresentationFormats();
}