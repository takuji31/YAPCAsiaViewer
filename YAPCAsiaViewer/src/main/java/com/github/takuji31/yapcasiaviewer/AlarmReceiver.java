package com.github.takuji31.yapcasiaviewer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Talk talk = (Talk) intent.getSerializableExtra(YapcAsiaViewer.BUNDLE_KEY_TALK);
		Intent talkIntent = new Intent(context, TalkDetailActivity.class);
		talkIntent.putExtra(TalkDetailFragment.ARG_TALK, talk);
		PendingIntent talkPendingIntent = PendingIntent.getActivity(context, 0, talkIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setTicker(context.getString(R.string.talk_notify_title));
		builder.setContentTitle(context.getString(R.string.talk_notify_title));
		builder.setContentText(talk.getTitle());
		builder.setSmallIcon(R.drawable.ic_stat_talk);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
		builder.setContentIntent(talkPendingIntent);
		manager.notify(0, builder.build());
	}

}
