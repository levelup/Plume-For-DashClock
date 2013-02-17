package com.levelup.touiteur.dashclock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class DashClockSettings extends PreferenceActivity {

	private static final String PLUME_PACKAGE = "com.levelup.touiteur";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getActionBar()!=null)
			getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		boolean isPlumeInstalled = false;
		try {
			Intent plumeInfo = getPackageManager().getLaunchIntentForPackage("com.levelup.touiteur");
			isPlumeInstalled = plumeInfo!=null;
		} catch (Throwable e) {
		}
		if (!isPlumeInstalled) {
			AlertDialog.Builder warnMissing = new AlertDialog.Builder(this);
			warnMissing.setTitle(android.R.string.dialog_alert_title);
			warnMissing.setMessage(R.string.warn_plume_missing);
			warnMissing.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+PLUME_PACKAGE));
					startActivity(marketIntent);
				}
			});
			warnMissing.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			warnMissing.show();
		}
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
