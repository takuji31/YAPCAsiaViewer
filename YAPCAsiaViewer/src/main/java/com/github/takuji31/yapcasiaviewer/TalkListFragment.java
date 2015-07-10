package com.github.takuji31.yapcasiaviewer;

import java.text.ParseException;

import com.github.takuji31.appbase.util.IntentUtil;
import com.github.takuji31.yapcasiaviewer.R;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TalkListFragment extends YAVListFragment implements LoaderCallbacks<VenueList> {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private static final String STATE_NAVIGATION_POSITION = "navigation_position";
	private static final String STATE_VENUE_LIST = "venue_list";

	public static final String BUNDLE_DATE = "date";
	private static final String TAG_DIALOG_LICENSE = "LicenseDialogFragment";

    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private OnNavigationListener mNavigationListener = new OnNavigationListener() {
		
		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			if (mNavigationPosition != itemPosition) {
				mVenue = mVenuList.get(itemPosition);
				setTalkList(mVenue.talkList);
			}
			mNavigationPosition = itemPosition;
			return true;
		}
	};
	private ArrayAdapter<Venue> mVenuAdapter;
	private TalkListAdapter mTalkAdapter;
	private VenueList mVenuList;
	private TalkList mTalkList;
	private String mDateString;
	private int loaderId;

    public interface Callbacks {
        public void onItemSelected(Talk talk);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Talk talk) {
        }
    };
    
    Venue mVenue;
    int mNavigationPosition = -1;

    protected void setTalkList(TalkList talkList) {
    	if (talkList == null) {
			talkList = new TalkList();
		}
    	mTalkList = talkList;
		mTalkAdapter = new TalkListAdapter(getMyActivity(), talkList);
		setListAdapter(mTalkAdapter);
	}

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	setHasOptionsMenu(true);
    	
        if (savedInstanceState != null) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
            mNavigationPosition = savedInstanceState.getInt(STATE_NAVIGATION_POSITION);
            mVenuList = (VenueList) savedInstanceState.getSerializable(STATE_VENUE_LIST);
            if (mVenuList != null) {
				mVenue = mVenuList.get(mNavigationPosition);
				setListNavigation();
				setTalkList(mVenue.talkList);
			}
        }
    	
    	Bundle args = getArguments();
    	int pos = 0;
    	if (args != null) {
			pos = args.getInt(BUNDLE_DATE, 0);
		}
    	loaderId = pos;
    	if (pos != -1) {
        	mDateString = getResources().getStringArray(R.array.dates)[pos];
        	try {
        		getActivity().setTitle(DateUtil.convertToDisplayDayString(mDateString));
        	} catch (ParseException e) {
        		e.printStackTrace();
        	}
        	if (mVenuList == null) {
            	getLoaderManager().initLoader(loaderId, null, this);
    		}
		}
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	if (mVenuList != null) {
    		setListNavigation();
		}
    }
    
    @Override
    public void onResume() {
    	if (loaderId == -1) {
			TalkList checkList = new TalkList();
			checkList.addAll(Talk.getCheckList(getMyApp()));
			setTalkList(checkList);
			getActivity().setTitle(R.string.check_list);
			setListNavigation();
		}
    	super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.main, menu);
    	if (loaderId == -1) {
			menu.removeItem(R.id.menu_refresh);
			menu.removeItem(R.id.menu_check_list);
		}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	int id = item.getItemId();
    	if (id == R.id.menu_refresh) {
    		mVenue = null;
    		mVenuList = null;
    		mVenuAdapter = null;
    		mTalkAdapter = null;
			setListAdapter(null);
			setListShown(false);
			mActivatedPosition = ListView.INVALID_POSITION;
			mNavigationPosition = -1;
			getMyApp().setPref(mDateString, "");
			getLoaderManager().restartLoader(loaderId, null, this);
		} else if (id == R.id.menu_yapc) {
			Intent browserIntent = IntentUtil.getOpenBrowserIntent(getMyActivity().getString(R.string.yapc_url));
			startActivity(browserIntent);
		} else if (id == R.id.menu_check_list) {
			Bundle args = new Bundle();
			args.putInt(BUNDLE_DATE, -1);
			TalkListFragment fragment = (TalkListFragment) Fragment.instantiate(getActivity(), TalkListFragment.class.getName(), args);
			getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null)
				.commit();
		} else if (id == R.id.menu_license) {
			LicenseDialogFragment fragment = new LicenseDialogFragment();
			fragment.show(getFragmentManager(), TAG_DIALOG_LICENSE);
		} else {
	    	return super.onOptionsItemSelected(item);
		}
    	return true;
    }
    
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Talk talk = mTalkList.get(position);
        mCallbacks.onItemSelected(talk);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        outState.putInt(STATE_NAVIGATION_POSITION, mNavigationPosition);
        outState.putSerializable(STATE_VENUE_LIST, mVenuList);
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
    
    private void setListNavigation() {
    	ActionBar ab = getMyActivity().getSupportActionBar();
    	if (mVenuList != null) {
			mVenuAdapter = new ArrayAdapter<Venue>(ab.getThemedContext(), android.R.layout.simple_list_item_1, mVenuList);
	    	ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	    	ab.setListNavigationCallbacks(mVenuAdapter, mNavigationListener);
	       	ab.setSelectedNavigationItem(mNavigationPosition == -1 ? 0 : mNavigationPosition);
		} else {
			mVenuAdapter = null;
	    	ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
    }

	@Override
	public Loader<VenueList> onCreateLoader(int id, Bundle args) {
        ActionBar ab = getMyActivity().getSupportActionBar();
        ab.setListNavigationCallbacks(null, null);
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		return new TimeTableLoader(getMyApp(), mDateString);
	}

	@Override
	public void onLoadFinished(Loader<VenueList> loader, VenueList data) {
		if (data == null) {
			getMyApp().showErrorToast();
		} else if(mVenuList == null) {
			mVenuList = data;
			setListNavigation();
		}
	}

	@Override
	public void onLoaderReset(Loader<VenueList> loader) {}
	
	public String getDateString() {
		return mDateString;
	}

}
