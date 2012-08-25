package jp.tkji.yapcasiaviewer.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimeTable implements Serializable {

	private static final long serialVersionUID = 1L;

	public static ArrayList<TimeTable> parseJson(JSONArray json) throws JSONException {
		ArrayList<TimeTable> result = new ArrayList<TimeTable>();
		
		int length = json.length();
		for (int i = 0; i < length; i++) {
			TimeTable timeTable = new TimeTable(json.getJSONObject(i));
			result.add(timeTable);
		}
		
		return result;
	}
	
	public String id;
	public String title;
	public String titleEn;
	//XXX abstract
	public String description;
	public String category;
	public int duration;
	public Calendar startOn;
	public int venueId;
	public Speaker speaker;
	
	public TimeTable(JSONObject json) throws JSONException {
		id = json.getString("id");
		title = json.getString("title");
		titleEn = json.getString("title_en");
		description = json.getString("abstract");
		category = json.getString("category");
		duration = json.getInt("duration");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
		
		try {
			startOn = Calendar.getInstance();
			startOn.setTime(sdf.parse(json.getString("start_on")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		venueId = json.getInt("venue_id");
		speaker = new Speaker(json.getJSONObject("speaker"));
	}
}
