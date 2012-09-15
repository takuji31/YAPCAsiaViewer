package com.github.takuji31.yapcasiaviewer;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Speaker implements Serializable {

	private static final long serialVersionUID = 1L;

	public String name;
	public String nickname;
	
	public Speaker(JSONObject json) throws JSONException {
		name = json.getString("name");
		nickname = json.getString("nickname");
	}
}
