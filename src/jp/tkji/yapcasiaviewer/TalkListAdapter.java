package jp.tkji.yapcasiaviewer;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.github.takuji31.appbase.widget.SimpleListAdapter;

public class TalkListAdapter extends SimpleListAdapter<Talk> {

	public TalkListAdapter(Context context, TalkList list) {
		super(context, list);
	}

	@Override
	public int getViewLayoutId(int position) {
		return R.layout.talk_row;
	}

	private static class ViewHolder {
		public TextView textViewTime;
		public TextView textViewName;
		public TextView textViewTitle;
	}
	
	@Override
	public View createView(int position, Talk item, View v) {
		
		ViewHolder vh = (ViewHolder) v.getTag();
		
		if (vh == null) {
			vh = new ViewHolder();
			vh.textViewTime = (TextView) v.findViewById(R.id.textViewTime);
			vh.textViewName = (TextView) v.findViewById(R.id.textViewName);
			vh.textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
			v.setTag(vh);
		}
		
		vh.textViewName.setText(item.speaker.name);
		vh.textViewTitle.setText(item.getTitle());
		vh.textViewTime.setText(DateUtil.toTimeString(item.startOn) + " - " + DateUtil.toTimeString(item.getEndOn()));
		
		return v;
	}

}
