package com.github.takuji31.slidemenu;


import java.util.ArrayList;

import com.github.takuji31.appbase.widget.SimpleListAdapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SlideMenuAdapter extends SimpleListAdapter<SlideMenuItem> {

	public SlideMenuAdapter(Context context, ArrayList<SlideMenuItem> list) {
		super(context, list);
	}

	static class ViewHolder {
		public TextView label;
		public ImageView icon;
	}

	@Override
	public int getViewLayoutId(int position) {
		return R.layout.menu_listitem;
	}

	@Override
	public View createView(int position, SlideMenuItem item, View v, View parent) {
		ViewHolder holder = (ViewHolder) v.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.label = (TextView) v
					.findViewById(R.id.menu_label);
			holder.icon = (ImageView) v
					.findViewById(R.id.menu_icon);
			v.setTag(holder);
		}
		String s = item.label;
		holder.label.setText(s);
		if (item.icon != -1) {
			//TODO Drawable support
			holder.icon.setVisibility(View.VISIBLE);
			holder.icon.setImageResource(item.icon);
		} else {
			holder.icon.setVisibility(View.GONE);
		}
		return v;
	}
}