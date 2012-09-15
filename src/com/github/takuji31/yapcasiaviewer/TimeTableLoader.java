package com.github.takuji31.yapcasiaviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import com.github.takuji31.yapcasiaviewer.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

public class TimeTableLoader extends AsyncTaskLoader<VenueList> {

	private String mDateString;
	private YapcAsiaViewer mApp;
	
	public TimeTableLoader(YapcAsiaViewer app, String dateString) {
		super(app);
		mApp = app;
		mDateString = dateString;
	}

	@Override
	public VenueList loadInBackground() {
		try {
			String jsonString = mApp.getPref(mDateString, "");
			if (TextUtils.isEmpty(jsonString)) {
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
				jsonString = TextUtils.join("\n", lines);
				mApp.setPref(mDateString, jsonString);
			}
			JSONObject json = new JSONObject(jsonString);
			return VenueList.parseJson(json);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
