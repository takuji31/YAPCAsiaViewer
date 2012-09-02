package jp.tkji.yapcasiaviewer;

import java.util.ArrayList;

import com.actionbarsherlock.view.MenuItem;

import jp.senchan.lib.widget.SlideMenu;
import jp.senchan.lib.widget.SlideMenuAdapter;
import jp.senchan.lib.widget.SlideMenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
			Bundle args = new Bundle();
			args.putInt(TalkListFragment.BUNDLE_DATE, (int)which);
		}
	};
	private SlideMenu mMenu;
	private String[] mDates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_list);

        FragmentManager fm = getSupportFragmentManager();
        TalkListFragment f = (TalkListFragment) fm.findFragmentById(R.id.container);
        if (f == null) {
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
			item.label = date;
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
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(TalkDetailFragment.ARG_ITEM_ID, id);
            TalkDetailFragment fragment = new TalkDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.talk_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, TalkDetailActivity.class);
            detailIntent.putExtra(TalkDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
