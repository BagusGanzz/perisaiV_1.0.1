package com.studioninja.locker.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;

public abstract class Util {

	/**
	 * Sets the background {@link Drawable} of a view.<br>
	 * On API level 16+ {@link View#setBackgroundDrawable(Drawable)} is
	 * deprecated, so we use the new method {@link View#setBackground(Drawable)}
	 * 
	 * @param v
	 *            The {@link View} on which to set the background
	 * @param bg
	 *            The background
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static final void setBackgroundDrawable(View v, Drawable bg) {
		if (v == null)
			return;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			v.setBackgroundDrawable(bg);
		} else {
			v.setBackground(bg);
		}
	}

	/**
	 * SWAR Algorithm<br>
	 * 
	 * @param i
	 * @return The number of set bits in a 32bit integer
	 */
	public static final int numberOfSetBits(int i) {
		i = i - ((i >> 1) & 0x55555555);
		i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
		return (((i + (i >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
	}

	/**
	 * Get a {@link Bitmap} from a {@link Drawable}
	 * 
	 * @param drawable
	 * @return The {@link Bitmap} representing this {@link Drawable}
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	/**
	 * Utility method to get an {@link ActivityInfo} for a packageName.
	 * 
	 * @param packageName
	 * @return an {@link ActivityInfo} or null if not found. (or if packageName
	 *         or context are null)
	 */
	public static final ApplicationInfo getaApplicationInfo(String packageName,
			Context c) {
		if (packageName == null || c == null)
			return null;
		try {
			return c.getPackageManager().getApplicationInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public static float dpToPx(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public static boolean checkDrawOverlayPermission(Context context, int req) {
		/** check if we already  have permission to draw over other apps */
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!Settings.canDrawOverlays(context)) {
				/** if not construct intent to request permission */
				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						Uri.parse("package:" + context.getPackageName()));
				/** request permission via start activity for result */
				if (context instanceof Activity){
					((Activity)context).startActivityForResult(intent, req);
				}else {
					context.startActivity(intent);
				}
				return false;
			}
		}
		/*if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED ) {
			ActivityCompat.requestPermissions(context,
					new String[]{android.Manifest.permission.SYSTEM_ALERT_WINDOW},
					req);
			return false;
		}*/
		return true;
	}
}
