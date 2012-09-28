package com.github.takuji31.yapcasiaviewer;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class Talk implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final String PREF_KEY_CHECK_LIST = "check_list";
	
	private static Gson sGson;
	
	static {
		sGson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	}

	public static Talk findCheckedTalkById(YapcAsiaViewer app, String talkId) {
		List<Talk> checkList = getCheckList(app);

		for (Talk talk : checkList) {
			if (TextUtils.equals(talkId, talk.id)) {
				return talk;
			}
		}
		
		return null;
	}
	
	public static List<Talk> getCheckList(YapcAsiaViewer app) {
		String jsonString = app.getPref(PREF_KEY_CHECK_LIST, "");
		if (TextUtils.isEmpty(jsonString)) {
			return new ArrayList<Talk>();
		}
		Type type = new TypeToken<Collection<Talk>>(){}.getType();
		List<Talk> list = sGson.fromJson(jsonString, type);
		Collections.sort(list, new TalkCompareator());
		
		for (Talk talk : list) {
			talk.addToAlarmManager(app);
		}
		
		return list;
	}

	public static boolean addTalkList(YapcAsiaViewer app, Talk talk) {
		if (findCheckedTalkById(app, talk.id) != null) {
			return false;
		}
		List<Talk> checkList = getCheckList(app);
		checkList.add(talk);
		talk.addToAlarmManager(app);
		saveCheckList(app, checkList);
		return true;
	}
	
	public static boolean removeTalkList(YapcAsiaViewer app, Talk talk) {
		if (findCheckedTalkById(app, talk.id) == null) {
			return false;
		}
		List<Talk> checkList = getCheckList(app);
		for (Talk checked : checkList) {
			if (TextUtils.equals(talk.id, checked.id)) {
				checkList.remove(checked);
				saveCheckList(app, checkList);
				return true;
			}
		}
		return false;
	}
	
	public static void saveCheckList(YapcAsiaViewer app, List<Talk> checkList) {
		String jsonString = sGson.toJson(checkList);
		app.setPref(PREF_KEY_CHECK_LIST, jsonString);
	}

	public String id;
	public String title;
	public String titleEn;
	//XXX abstract
	public String description;
	public String category;
	public int duration;
	public Date startOn;
	public int venueId;
	public Speaker speaker;
	
	public Talk() {
	}
	
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
	
	public Date getEndOn() {
		Calendar endOn = Calendar.getInstance();
		endOn.setTimeInMillis(startOn.getTime());
		endOn.add(Calendar.MINUTE, duration);
		return endOn.getTime();
	}
	
	public PendingIntent getPendingIntent(Context c) {
		Intent intent = new Intent(c, AlarmReceiver.class);
		intent.setAction(YapcAsiaViewer.ACTION_NOTIFY);
		intent.setData(getUri());
		intent.putExtra(YapcAsiaViewer.BUNDLE_KEY_TALK, this);
        return PendingIntent.getBroadcast(c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public Uri getUri() {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("yav");
		builder.authority("talk");
		builder.path("/" + id);
		return builder.build();
	}
	
	private void addToAlarmManager(YapcAsiaViewer app) {
		AlarmManager am = (AlarmManager)app.getSystemService(Context.ALARM_SERVICE);
		PendingIntent intent = getPendingIntent(app);
		long time = startOn.getTime() - 300 * 1000;
		if (time < Calendar.getInstance().getTimeInMillis()) {
			return;
		}
		am.cancel(intent);
		am.set(AlarmManager.RTC_WAKEUP, time, intent);
	}
	

}
