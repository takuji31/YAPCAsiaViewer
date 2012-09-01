package jp.tkji.yapcasiaviewer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
	private static SimpleDateFormat sJsonDateFormat = new SimpleDateFormat("yyyy-mm-dd");
	private static SimpleDateFormat sDisplayDateFormat = new SimpleDateFormat("yyyy/mm/dd (E)");
	
	public static Calendar parseJsonDateString(String dateString) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sJsonDateFormat.parse(dateString));
		return calendar;
	}
	
	public static String toDateString(Calendar calendar) {
		return sDisplayDateFormat.format(calendar.getTime());
	}

}
