package org.ambientdynamix.contextplugins.context.info.environment;

import org.ambientdynamix.api.application.IContextInfo;

public interface ICurrentTimeContextInfo  extends IContextInfo
{
	public int dayOfMonth();
	
	public int dayOfWeek();
	
	public int dayOfYear();
	
	public int era();
	
	public int hourOfDay();
	
	public int millisOfDay();
	
	public int millisOfSecond();
	
	public int minuteOfDay();
	
	public int minuteOfHour();
	
	public int monthOfYear();
	
	public int secondOfDay();
	
	public int secondOfMinute();
	
	public int weekOfWeekyear();
	
	public int year();
	
	public int yearOfCentury();
	
	public int yearOfEra();
}
