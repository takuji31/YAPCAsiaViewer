package com.github.takuji31.yapcasiaviewer;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

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
		title = !json.isNull("title") ? json.getString("title") : null;
		titleEn = !json.isNull("title_en") ? json.getString("title_en") : null;
		description = !json.isNull("abstract") ? json.getString("abstract") : null;
		category = !json.isNull("category") ? json.getString("category") : null;
		duration = json.getInt("duration");
		

		try {
			startOn = DateUtil.parseJsonDateTimeString(json.getString("start_on"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		venueId = json.getInt("venue_id");
		speaker = new Speaker(json.getJSONObject("speaker"));
	}
	
	public String getTitle() {
		if (!TextUtils.isEmpty(title) && Locale.JAPAN.equals(Locale.getDefault())) {
			return title;
		} else if (!TextUtils.isEmpty(titleEn)) {
			return titleEn;
		} else if (!TextUtils.isEmpty(title)) {
			return title;
		} else {
			return "";
		}
	}
	
	public Calendar getEndOn() {
		Calendar endOn = (Calendar) startOn.clone();
		endOn.add(Calendar.MINUTE, duration);
		return endOn;
	}
}
