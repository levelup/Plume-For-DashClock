/*******************************************************************************
 * Copyright 2013 LevelUp Studio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.levelup.touiteur.dashclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;


public class DashClockService extends DashClockExtension {
	
	private static final String PREF_HIDE_EMPTY = "HideWhenEmpty";
	private static final String PREF_SHOW_TWEET = "ShowCounterTweet";
	private static final String PREF_SHOW_MENTION = "ShowCounterMentions";
	private static final String PREF_SHOW_DM = "ShowCounterDMs";

	@Override
	protected void onInitialize(boolean isReconnect) {
		super.onInitialize(isReconnect);
		setUpdateWhenScreenOn(true);
	}

	@Override
	protected void onUpdateData(int reason) {
		SharedPreferences userPrefs = getApplicationContext().getSharedPreferences(getPackageName() + "_preferences", 0);
		
		SharedPreferences storage = StatusReceiver.getStorage(getApplicationContext());
		Integer unreadTweets = !userPrefs.getBoolean(PREF_SHOW_TWEET, true) ? null : storage.getInt(StatusReceiver.INTENT_UNREAD_TWEET, 0);
		Integer unreadMentions = !userPrefs.getBoolean(PREF_SHOW_MENTION, true) ? null : storage.getInt(StatusReceiver.INTENT_UNREAD_MENTION, 0);
		Integer unreadDMs = !userPrefs.getBoolean(PREF_SHOW_DM, true) ? null : storage.getInt(StatusReceiver.INTENT_UNREAD_DM, 0);

		//Log.d(StatusReceiver.LOG_TAG, "update "+unreadTweets+" / "+unreadMentions+" / "+unreadDMs+" hide:"+userPrefs.getBoolean(PREF_HIDE_EMPTY, true));
		ExtensionData updatedData;
		if (userPrefs.getBoolean(PREF_HIDE_EMPTY, true)
				&& (unreadTweets==null   || unreadTweets==0)
				&& (unreadMentions==null || unreadMentions==0)
				&& (unreadDMs==null      || unreadDMs==0)) {
			updatedData = null; // reset so it not displayed
		} else {
			updatedData = new ExtensionData();
			updatedData.visible(true);
			updatedData.icon(R.drawable.ic_launcher);
			updatedData.clickIntent(new Intent().setClassName("com.levelup.touiteur", "com.levelup.touiteur.TouiteurMain"));
			
			StringBuilder status = new StringBuilder();
			if (unreadTweets!=null) {
				status.append(String.valueOf(unreadTweets));
			}
			if (unreadMentions!=null) {
				if (status.length()!=0) status.append('/');
				status.append(String.valueOf(unreadMentions));
			}
			if (unreadDMs!=null) {
				if (status.length()!=0) status.append('/');
				status.append(String.valueOf(unreadDMs));
			}
			updatedData.status(status.toString());

			StringBuilder expanded = new StringBuilder();
			if (null!=unreadTweets && 0 != unreadTweets) {
				expanded.append(getString(R.string.expanded_title_tweet, unreadTweets));
			}
			if (null!=unreadMentions && 0 != unreadMentions) {
				if (expanded.length()!=0) expanded.append(" / ");
				expanded.append(getString(R.string.expanded_title_mention, unreadMentions));
			}
			if (null!=unreadDMs && 0 != unreadDMs) {
				if (expanded.length()!=0) expanded.append(" / ");
				expanded.append(getString(R.string.expanded_title_dm, unreadDMs));
			}
			updatedData.expandedTitle(expanded.toString());
			
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
