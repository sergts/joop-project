package project.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeStamp {
	public static String getFullTimeStamp(){
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		return ("["+sdf.format(cal.getTime())+"] ");
	}

	public static String getShortTimeStamp(){
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return ("["+sdf.format(cal.getTime())+"] ");
	}
}
