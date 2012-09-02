package jp.tkji.yapcasiaviewer;

import java.text.ParseException;
import java.util.ArrayList;

import com.actionbarsherlock.view.MenuItem;

import jp.senchan.lib.widget.SlideMenu;
import jp.senchan.lib.widget.SlideMenuAdapter;
import jp.senchan.lib.widget.SlideMenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

        TalkListFragment f = getTalkListFragment();
        if (f == null) {
            FragmentManager fm = getSupportFragmentManager();
			f = (TalkListFragment) Fragment.instantiate(this, TalkListFragment.class.getName(), new Bundle());
			fm.beginTransaction().add(R.id.container, f).commit();
		}
        
        if (findViewById(R.id.talk_detail_container) != null) {
            mTwoPane = true;
            f.setActivateOnItemClick(true);
        }
        
        mMenu = new SlideMenu(this);
        SlideMenu.setAnimationDuration(200);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<SlideMenuItem> items = new ArrayList<SlideMenuItem>();
        mDates = getResources().getStringArray(R.array.dates);
        int i = 0;
        for (String date : mDates) {
			SlideMenuItem item = new SlideMenuItem();
			item.id = i;
			try {
				item.label = DateUtil.convertToDisplayDateString(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			item.icon = -1;
			items.add(item);
			i++;
		}
        mMenu.setAdapter(new SlideMenuAdapter(this, items));
        mMenu.setOnItemClickListener(mMenuLisntener);
        mMenu.checkEnabled();
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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.talk_detail_container, fragment)
                    .commit();

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
