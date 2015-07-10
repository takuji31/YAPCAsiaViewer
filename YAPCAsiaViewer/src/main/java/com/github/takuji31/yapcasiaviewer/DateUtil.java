package com.github.takuji31.yapcasiaviewer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat sJsonDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sJsonDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sDisplayDateTimeFormat = new SimpleDateFormat("yyyy/MM/dd (E) HH:mm");
	private static SimpleDateFormat sDisplayDateFormat = new SimpleDateFormat("yyyy/MM/dd (E)");
	private static SimpleDateFormat sDisplayDayFormat = new SimpleDateFormat("MM/dd (E)");
	private static SimpleDateFormat sDisplayTimeFormat = new SimpleDateFormat("HH:mm");

	public static Date parseJsonDateString(String dateString) throws ParseException {
		return sJsonDateFormat.parse(dateString);
	}
	
	public static Date parseJsonDateTimeString(String dateTimeString) throws ParseException {
		return sJsonDateTimeFormat.parse(dateTimeString);
	}
	
	public static String toDateTimeString(Date date) {
		return sDisplayDateTimeFormat.format(date);
	}
	
	public static String toDateString(Date date) {
		return sDisplayDateFormat.format(date);
	}
	
	public static String toDayString(Date date) {
		return sDisplayDayFormat.format(date);
	}
	
	public static String toTimeString(Date date) {
		return sDisplayTimeFormat.format(date);
	}
	
	public static String convertToDisplayDateString(String jsonDateString) throws ParseException {
		return toDateString(parseJsonDateString(jsonDateString));
	}

	public static String convertToDisplayDayString(String jsonDateString) throws ParseException {
		return toDayString(parseJsonDateString(jsonDateString));
	}


}
