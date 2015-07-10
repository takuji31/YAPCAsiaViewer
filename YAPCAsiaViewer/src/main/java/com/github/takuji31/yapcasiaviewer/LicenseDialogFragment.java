package com.github.takuji31.yapcasiaviewer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.webkit.WebView;

public class LicenseDialogFragment extends YAVDialogFragment {
	private WebView mWebView;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.license);
		mWebView = new WebView(getActivity());
		builder.setView(mWebView);
		builder.setPositiveButton(android.R.string.ok, null);
		return builder.create();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mWebView.loadUrl("file:///android_res/raw/license.txt");
	}
}
