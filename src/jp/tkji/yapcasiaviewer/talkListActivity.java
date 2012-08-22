package jp.tkji.yapcasiaviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class talkListActivity extends FragmentActivity
        implements talkListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_list);

        if (findViewById(R.id.talk_detail_container) != null) {
            mTwoPane = true;
            ((talkListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.talk_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(talkDetailFragment.ARG_ITEM_ID, id);
            talkDetailFragment fragment = new talkDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.talk_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, talkDetailActivity.class);
            detailIntent.putExtra(talkDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
