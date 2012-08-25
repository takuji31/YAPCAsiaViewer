package jp.tkji.yapcasiaviewer;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class ScheduleList extends ArrayList<TimeTable> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static ScheduleList parseJson(JSONArray json) throws JSONException {
		ScheduleList result = new ScheduleList();
		
		int length = json.length();
		for (int i = 0; i < length; i++) {
			TimeTable timeTable = new TimeTable(json.getJSONObject(i));
			result.add(timeTable);
		}
		
		return result;
	}

}
