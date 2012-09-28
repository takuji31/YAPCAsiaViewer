package com.github.takuji31.yapcasiaviewer;

import java.util.List;

import com.github.takuji31.yapcasiaviewer.R;

import com.github.takuji31.appbase.app.BaseApp;

public class YapcAsiaViewer extends BaseApp {
	
	private static final int sPreferenceVersion = 1;
	public static final String BUNDLE_KEY_TALK  = "talk";
	public static final String ACTION_NOTIFY = "com.github.takuji31.yapcasiaviewer.notify";
	
	public static List<Talk> talkList;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		talkList = Talk.getCheckList(this);
	}
	
	@Override
	public int getDefaultPrefVersion() {
		return sPreferenceVersion;
	}
	
	public void showErrorToast() {
		toast(R.string.message_something_wrong).show();
	}

}
