package com.github.takuji31.yapcasiaviewer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import com.github.takuji31.yapcasiaviewer.R;

import com.actionbarsherlock.view.MenuItem;
import com.github.takuji31.slidemenu.SlideMenu;
import com.github.takuji31.slidemenu.SlideMenuAdapter;
import com.github.takuji31.slidemenu.SlideMenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class TalkListActivity extends YAVActivity
        implements TalkListFragment.Callbacks {

    private boolean mTwoPane;
	private OnItemClickListener mMenuLisntener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> list, View v, int id,
				long which) {
			int pos = (int) which;
	        TalkListFragment f = getTalkListFragment();
	        if (!mDates[pos].equals(f.getDateString())) {
				Bundle args = new Bundle();
				args.putInt(TalkListFragment.BUNDLE_DATE, pos);
				TalkListFragment newFragment = (TalkListFragment) Fragment.instantiate(TalkListActivity.this, TalkListFragment.class.getName(), args);
				getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, newFragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.addToBackStack(null)
					.commit();
			}
			mMenu.hide();
		}
	};
	private String[] mDates;

	SlideMenu mMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_list);

        ArrayList<SlideMenuItem> items = new ArrayList<SlideMenuItem>();
        mDates = getResources().getStringArray(R.array.dates);
        int i = 0;
        int dateIndex = 0;
        String today = DateUtil.toDateString(Calendar.getInstance().getTime());
        for (String date : mDates) {
			SlideMenuItem item = new SlideMenuItem();
			item.id = i;
			try {
				item.label = DateUtil.convertToDisplayDateString(date);
				if (TextUtils.equals(today, item.label)) {
					dateIndex = i;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			item.icon = -1;
			items.add(item);
			i++;
		}
        TalkListFragment f = getTalkListFragment();
        if (f == null) {
            FragmentManager fm = getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putInt(TalkListFragment.BUNDLE_DATE, dateIndex);
			f = (TalkListFragment) Fragment.instantiate(this, TalkListFragment.class.getName(), args);
			fm.beginTransaction().add(R.id.container, f).commit();
		}
        
        mMenu = new SlideMenu(this);
        SlideMenu.setAnimationDuration(200);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mMenu.setAdapter(new SlideMenuAdapter(this, items));
        mMenu.setOnItemClickListener(mMenuLisntener);
        mMenu.checkEnabled();
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
        if (findViewById(R.id.talk_detail_container) != null) {
            mTwoPane = true;
            getTalkListFragment().setActivateOnItemClick(true);
        }
    	//getSupportActionBar().setDisplayShowTitleEnabled(mTwoPane);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
			mMenu.toggle();
		}
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && mMenu.isMenuShown()) {
			mMenu.hide();
    		return true;
		}
    	return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemSelected(Talk talk) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(TalkDetailFragment.ARG_TALK, talk);
            TalkDetailFragment fragment = new TalkDetailFragment();
            fragment.setArguments(arguments);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.talk_detail_container, fragment);
            if (fm.findFragmentById(R.id.talk_detail_container) != null) {
                ft.addToBackStack(null);
			}
            ft.commit();

        } else {
            Intent detailIntent = new Intent(this, TalkDetailActivity.class);
            detailIntent.putExtra(TalkDetailFragment.ARG_TALK, talk);
            startActivity(detailIntent);
        }
    }
    
    public TalkListFragment getTalkListFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return (TalkListFragment) fm.findFragmentById(R.id.container);
    }
}
