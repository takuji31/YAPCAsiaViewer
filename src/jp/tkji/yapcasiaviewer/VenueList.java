package jp.tkji.yapcasiaviewer;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VenueList extends ArrayList<Venue> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Calendar mDate;
	
	public VenueList(String dateString) throws ParseException {
		mDate = DateUtil.parseJsonDateString(dateString);
		
	}

	public static VenueList parseJson(JSONObject json) throws JSONException, ParseException {
		VenueList result = new VenueList(json.getString("date"));
		
		int length = json.length();
		JSONArray timeTableJson = json.getJSONArray("talks_by_venue");
		JSONArray venueJson = json.getJSONArray("venues");
		for (int i = 0; i < length; i++) {
			Venue venue = new Venue(venueJson.getJSONObject(i), timeTableJson.getJSONArray(i));
			result.add(venue);
		}
		
		return result;
	}
	
	public String getDateString() {
		return DateUtil.toDateString(mDate);
	}
	
	@Override
	public String toString() {
		return getDateString();
	}

}