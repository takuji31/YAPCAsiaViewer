package jp.tkji.yapcasiaviewer;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleList extends ArrayList<TimeTable> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static ScheduleList parseJson(JSONObject json) throws JSONException {
		ScheduleList result = new ScheduleList();
		
		int length = json.length();
		JSONArray timeTableJson = json.getJSONArray("talks_by_venue");
		for (int i = 0; i < length; i++) {
			TimeTable timeTable = new TimeTable(timeTableJson.getJSONObject(i));
			result.add(timeTable);
		}
		
		return result;
	}

}
