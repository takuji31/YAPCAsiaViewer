package com.github.takuji31.yapcasiaviewer;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VenueList extends ArrayList<Venue> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date mDate;
	
	public VenueList(String dateString) throws ParseException {
		mDate = DateUtil.parseJsonDateString(dateString);
		
	}

	public static VenueList parseJson(JSONObject json) throws JSONException, ParseException {
		VenueList result = new VenueList(json.getString("date"));
		
		JSONArray timeTableJson = json.getJSONArray("talks_by_venue");
		JSONArray venueJson = json.getJSONArray("venues");
		int length = timeTableJson.length();
		for (int i = 0; i < length; i++) {
			JSONArray talkJson = timeTableJson.getJSONArray(i);
			Venue venue = new Venue(venueJson.getJSONObject(i), talkJson);
			if (talkJson.length() != 0) {
				result.add(venue);
			}
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
