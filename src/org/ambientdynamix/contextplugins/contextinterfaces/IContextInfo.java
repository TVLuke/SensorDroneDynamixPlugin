package org.ambientdynamix.contextplugins.contextinterfaces;

import java.util.Set;

public interface IContextInfo
{
	public abstract String getContextType();

	public abstract String getStringRepresentation(String format);

	public abstract String getImplementingClassname();

	public abstract Set<String> getStringRepresentationFormats();
}
