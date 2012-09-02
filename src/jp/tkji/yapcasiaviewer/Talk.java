package jp.tkji.yapcasiaviewer;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;

public class Talk implements Serializable {

	private static final long serialVersionUID = 1L;

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
	
	public Talk(JSONObject json) throws JSONException {
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
