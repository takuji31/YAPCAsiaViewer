package jp.tkji.yapcasiaviewer;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TalkListFragment extends YAVListFragment implements LoaderCallbacks<VenueList> {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

	public static final String BUNDLE_DATE = "date";

    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private OnNavigationListener mNavigationListener = new OnNavigationListener() {
		
		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			return false;
		}
	};
	private ArrayAdapter<Venue> mAdapter;
	private VenueList mVenuList;
	private String mDateString;
	private int loaderId;

    public interface Callbacks {
        public void onItemSelected(String id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState
                .containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
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
    	
    	Bundle args = getArguments();
    	int pos = 0;
    	if (args != null) {
			pos = args.getInt(BUNDLE_DATE, 0);
		}
    	loaderId = pos;
    	getActivity().setTitle(String.valueOf(pos));
    	mDateString = getResources().getStringArray(R.array.dates)[pos];
    	getLoaderManager().initLoader(loaderId, null, this);
    }

    @Override
    public void onStop() {
    	super.onStop();
        ActionBar ab = activity().getSupportActionBar();
        app().toast("onStop").show();
        ab.setListNavigationCallbacks(null, null);
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }
    
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
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
    	ActionBar ab = activity().getSupportActionBar();
    	if (mVenuList != null) {
			mAdapter = new ArrayAdapter<Venue>(ab.getThemedContext(), android.R.layout.simple_list_item_1, mVenuList);
		} else {
			mAdapter = null;
		}
    	ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    	ab.setListNavigationCallbacks(mAdapter, mNavigationListener);
    }

	@Override
	public Loader<VenueList> onCreateLoader(int id, Bundle args) {
		return new TimeTableLoader(app(), mDateString);
	}

	@Override
	public void onLoadFinished(Loader<VenueList> loader, VenueList data) {
		if (data == null) {
			app().showErrorToast();
		} else {
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
