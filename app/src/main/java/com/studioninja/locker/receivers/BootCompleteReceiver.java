package com.studioninja.locker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.studioninja.locker.util.Log;

import com.studioninja.locker.lock.AppLockService;
import com.studioninja.locker.util.PrefUtils;
import com.studioninja.locker.R;

/**
 * Starts the Service at boot if it's specified in the preferences.
 * 
 * @author twinone
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context c, Intent i) {
		Log.d("BootCompleteReceiver", "bootcomplete recevied");

		boolean start = new PrefUtils(c).getBoolean(R.string.pref_key_start_boot, R.bool.pref_def_start_boot);
		if (start) {
			Log.d("BootCompleteReceiver", "Starting service");
			AppLockService.start(c);
		}
	}

}
