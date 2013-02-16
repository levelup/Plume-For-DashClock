package com.levelup.touiteur.dashclock;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;


public class DashClockService extends DashClockExtension {

	@Override
	protected void onUpdateData(int reason) {
		SharedPreferences storage = StatusReceiver.getStorage(getApplicationContext());
		ExtensionData updatedData = new ExtensionData();
		updatedData.icon(R.drawable.widget_picholder);
		updatedData.clickIntent(new Intent().setClassName("com.levelup.touiteur", "com.levelup.touiteur.TouiteurMain"));
		
		int unreadTweets = storage.getInt(StatusReceiver.INTENT_UNREAD_TWEET, 0);
		int unreadMentions = storage.getInt(StatusReceiver.INTENT_UNREAD_MENTION, 0);
		int unreadDMs = storage.getInt(StatusReceiver.INTENT_UNREAD_DM, 0);
		
		updatedData.visible(unreadTweets!=0 || unreadMentions!=0 || unreadDMs!=0);
		updatedData.status(String.format("%d unread", (unreadTweets + unreadMentions + unreadDMs)));
		
		updatedData.expandedTitle(String.format("Tweets %d / Mentions %d / DMs %d", unreadTweets, unreadMentions, unreadDMs));
		String user = storage.getString(StatusReceiver.INTENT_UNREAD_USER, null);
		String notifText = storage.getString(StatusReceiver.INTENT_UNREAD_TEXT, null);
		StringBuilder expandedBody = new StringBuilder();
		if (!TextUtils.isEmpty(user)) {
			expandedBody.append(user);
			expandedBody.append(": ");
		}
		if (!TextUtils.isEmpty(notifText)) {
			expandedBody.append(notifText);
		}
		updatedData.expandedBody(expandedBody.toString());
		publishUpdate(updatedData);
	}

}
