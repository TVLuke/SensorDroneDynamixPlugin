package org.ambientdynamix.contextplugins.info.meta;

import org.ambientdynamix.contextplugins.context.info.device.*;

public interface ISourcedContext {

	//this offcourse is no good, there should be a class called source that has subclasses and all that.
	public IContextSource[] getSources();
}
