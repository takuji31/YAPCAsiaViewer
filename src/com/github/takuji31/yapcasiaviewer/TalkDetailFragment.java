package com.github.takuji31.yapcasiaviewer;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.github.takuji31.yapcasiaviewer.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TalkDetailFragment extends YAVFragment {

    public static final String ARG_TALK = "item_id";
    private Talk mTalk;

    public TalkDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
		if (args != null) {
        	mTalk = (Talk) args.getSerializable(ARG_TALK);
        }
		setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_talk_detail, container, false);
        
        TextView textViewTime = (TextView) v.findViewById(R.id.textViewTime);
        TextView textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
        TextView textViewName = (TextView) v.findViewById(R.id.textViewName);
        TextView textViewDesc = (TextView) v.findViewById(R.id.textViewDesc);
        
        textViewTime.setText(DateUtil.toDateTimeString(mTalk.startOn) + " - " + DateUtil.toTimeString(mTalk.getEndOn()));
        textViewTitle.setText(mTalk.getTitle());
        textViewName.setText(mTalk.speaker.name);
        textViewDesc.setText(mTalk.description);
        
        return v;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.detail, menu);
    }
}
