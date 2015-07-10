package com.github.takuji31.yapcasiaviewer;

import com.github.takuji31.yapcasiaviewer.R;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;

public class TalkDetailActivity extends YAVActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(TalkDetailFragment.ARG_TALK,
                    getIntent().getSerializableExtra(TalkDetailFragment.ARG_TALK));
            TalkDetailFragment fragment = new TalkDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.talk_detail_container, fragment)
                    .commit();
        }
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, TalkListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
