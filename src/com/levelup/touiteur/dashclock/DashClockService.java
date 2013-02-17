package com.levelup.touiteur.dashclock;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;


public class DashClockService extends DashClockExtension {
	
	private static final String USER_PREFS = "preferences";

	@Override
	protected void onInitialize(boolean isReconnect) {
		super.onInitialize(isReconnect);
		setUpdateWhenScreenOn(true);
	}

	@Override
	protected void onUpdateData(int reason) {
		SharedPreferences storage = StatusReceiver.getStorage(getApplicationContext());
		int unreadTweets = storage.getInt(StatusReceiver.INTENT_UNREAD_TWEET, 0);
		int unreadMentions = storage.getInt(StatusReceiver.INTENT_UNREAD_MENTION, 0);
		int unreadDMs = storage.getInt(StatusReceiver.INTENT_UNREAD_DM, 0);

		ExtensionData updatedData;
		if (getApplicationContext().getSharedPreferences(USER_PREFS, 0).getBoolean("HideWhenEmpty", true)
				&& unreadTweets==0 && unreadMentions==0 && unreadDMs==0)
			updatedData = null; // reset so it not displayed
		else {
			updatedData = new ExtensionData();
			updatedData.icon(R.drawable.widget_picholder);
			updatedData.clickIntent(new Intent().setClassName("com.levelup.touiteur", "com.levelup.touiteur.TouiteurMain"));

			updatedData.visible(unreadTweets!=0 || unreadMentions!=0 || unreadDMs!=0);
			updatedData.status(getString(R.string.status, unreadTweets, unreadMentions, unreadDMs));

			updatedData.expandedTitle(getString(R.string.expanded_title, unreadTweets, unreadMentions, unreadDMs));
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
		}
		publishUpdate(updatedData);
	}
}
