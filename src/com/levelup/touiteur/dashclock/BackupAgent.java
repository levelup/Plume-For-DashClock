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
