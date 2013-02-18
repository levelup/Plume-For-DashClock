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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class StatusReceiver extends BroadcastReceiver {
	private static final String GLOBAL_NOTIF = "com.levelup.touiteur.action.GLOBALNOTIF";
	static final String INTENT_UNREAD_TWEET   = "UnreadT";
	static final String INTENT_UNREAD_MENTION = "UnreadM";
	static final String INTENT_UNREAD_DM      = "UnreadD";
	static final String INTENT_UNREAD_TEXT    = "UnreadText";
	static final String INTENT_UNREAD_USER    = "User";
	
	private static final String PERSISTANT_STORAGE = "storage";

	static final String LOG_TAG = "PlumeDashClock";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent!=null && GLOBAL_NOTIF.equals(intent.getAction())) {
			// we received the broadcast status from Plume
			//Log.d(LOG_TAG, "received Plume notification "+intent.getExtras());
			
			SharedPreferences storage = getStorage(context);
			Editor e = storage.edit();
			e.putInt(INTENT_UNREAD_TWEET,   intent.getIntExtra(INTENT_UNREAD_TWEET, 0));
			e.putInt(INTENT_UNREAD_MENTION, intent.getIntExtra(INTENT_UNREAD_MENTION, 0));
			e.putInt(INTENT_UNREAD_DM,      intent.getIntExtra(INTENT_UNREAD_DM, 0));
			e.putString(INTENT_UNREAD_TEXT, intent.getStringExtra(INTENT_UNREAD_TEXT));
			e.putString(INTENT_UNREAD_USER, intent.getStringExtra(INTENT_UNREAD_USER));
			e.commit();
		}
	}
	
	public static SharedPreferences getStorage(Context context) {
		return context.getSharedPreferences(PERSISTANT_STORAGE, 0);
		
	}

}
