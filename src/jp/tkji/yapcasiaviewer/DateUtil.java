package jp.tkji.yapcasiaviewer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
	private static SimpleDateFormat sJsonDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sJsonDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sDisplayDateFormat = new SimpleDateFormat("yyyy/MM/dd (E)");

	public static Calendar parseJsonDateString(String dateString) throws ParseException {
		return parseString(sJsonDateFormat, dateString);
	}
	
	public static Calendar parseJsonDateTimeString(String dateTimeString) throws ParseException {
		return parseString(sJsonDateTimeFormat, dateTimeString);
	}
	
	public static Calendar parseString(SimpleDateFormat format, String str) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(format.parse(str));
		return calendar;
	}
	
	public static String toDateString(Calendar calendar) {
		return sDisplayDateFormat.format(calendar.getTime());
	}

}
