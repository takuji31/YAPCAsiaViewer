package jp.tkji.yapcasiaviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class TalkListActivity extends FragmentActivity
        implements TalkListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_list);

        if (findViewById(R.id.talk_detail_container) != null) {
            mTwoPane = true;
            ((TalkListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.talk_list))
                    .setActivateOnItemClick(true);
        }
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
