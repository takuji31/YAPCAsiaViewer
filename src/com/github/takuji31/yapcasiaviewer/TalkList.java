package com.github.takuji31.yapcasiaviewer;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TalkList extends ArrayList<Talk> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static TalkList parseJson(JSONArray json) throws JSONException {
		TalkList result = new TalkList();
		
		int length = json.length();
		for (int i = 0; i < length; i++) {
			JSONObject talkJson = json.getJSONObject(i);
			Talk talk = new Talk(talkJson);
			result.add(talk);
		}
		return result;
	}
	
}
