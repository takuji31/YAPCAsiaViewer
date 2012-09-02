package jp.tkji.yapcasiaviewer;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Venue implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public int id;
	public String name;
	public TalkList talkList;
	
	public Venue(JSONObject venueJson, JSONArray talkJson) throws JSONException {
		name = venueJson.getString("name");
		id = venueJson.getInt("id");
		talkList = TalkList.parseJson(talkJson);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
