package jp.tkji.yapcasiaviewer;

import java.util.ArrayList;

import com.androidquery.AQuery;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import jp.senchan.lib.app.ArrayListAdapter;

public class TalkListAdapter extends ArrayListAdapter<Talk> {

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
		public TextView textViewDesc;
		public AQuery imageViewIcon;
	}
	
	@Override
	public View createView(int position, Talk item, View v) {
		
		ViewHolder vh = (ViewHolder) v.getTag();
		
		if (vh == null) {
			vh = new ViewHolder();
			vh.textViewTime = (TextView) v.findViewById(R.id.textViewTime);
			vh.textViewName = (TextView) v.findViewById(R.id.textViewName);
			vh.textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
			vh.textViewDesc = (TextView) v.findViewById(R.id.textViewDesc);
			vh.imageViewIcon = new AQuery(v).find(R.id.imageViewIcon);
			v.setTag(vh);
		}
		
		vh.textViewName.setText(item.speaker.name);
		vh.textViewTitle.setText(item.title);
		vh.textViewDesc.setText(item.description);
		//TODO イメージ取得
		//vh.imageViewIcon.progress(R.id.imageViewIcon).image("");
		
		return v;
	}

}
