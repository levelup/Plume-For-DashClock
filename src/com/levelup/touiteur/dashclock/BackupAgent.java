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

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;


public class BackupAgent extends BackupAgentHelper {

	private static String PREFS_BACKUP_KEY = "prefs";
	
	@Override
	public void onCreate() {
		super.onCreate();
		String defaultPrefsFilename = getPackageName() + "_preferences";
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, defaultPrefsFilename);
        addHelper(PREFS_BACKUP_KEY, helper);
	}
	
}
