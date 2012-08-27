package jp.tkji.yapcasiaviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

public class TimeTableLoader extends AsyncTaskLoader<ScheduleList> {

	private String mDateString;
	
	public TimeTableLoader(Context context, String dateString) {
		super(context);
		mDateString = dateString;
	}

	@Override
	public ScheduleList loadInBackground() {
		try {
			Uri.Builder builder = Uri.parse(getContext().getString(R.string.api_url)).buildUpon();
			builder.appendQueryParameter("date", mDateString);
			URL url = new URL(builder.build().toString());
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			int code = con.getResponseCode();
			
			if (code != HttpURLConnection.HTTP_OK) {
				Log.w("TimeTable", "Load time table failure HTTP code:" + code);
				return null;
			}
			
			InputStream is = con.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			ArrayList<String> lines = new ArrayList<String>();
			String line;
			while ((line = reader.readLine() ) != null) {
				lines.add(line);
			}
			String jsonString = TextUtils.join("\n", lines);
			
			JSONObject json = new JSONObject(jsonString);
			return ScheduleList.parseJson(json);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		forceLoad();
	}
	
	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		cancelLoad();
	}

}
