package jp.tkji.yapcasiaviewer;

import com.github.takuji31.appbase.app.BaseApp;

public class YapcAsiaViewer extends BaseApp {
	
	private static final int sPreferenceVersion = 1;
	
	@Override
	public int getDefaultPrefVersion() {
		return sPreferenceVersion;
	}
	
	public void showErrorToast() {
		toast(R.string.message_something_wrong).show();
	}

}
