package com.studioninja.locker.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import com.studioninja.locker.util.Log;

public class Constants {

	private static final String NETWORK_CONNECTIVITY_MESSAGE = "No Internet connection. Please make sure that you have Internet connectivity and try again.";
	private static final String NETWORK_CONNECTIVITY_TITLE = "Network Problem";

	public static AlertDialog showNetworkAlert(Activity context) {
		final AlertDialog alert;
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(NETWORK_CONNECTIVITY_MESSAGE).setTitle(NETWORK_CONNECTIVITY_TITLE).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			alert = builder.create();
			alert.show();
			return alert;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	public static AlertDialog showAlert(final String message, final String title, final Activity context) {
		final AlertDialog alert;
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(message).setTitle(title).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

					dialog.dismiss();

					if (message.equals("Request sent")) {
						context.onBackPressed();
					}

				}
			});
			alert = builder.create();
			alert.show();
			return alert;
		} catch (Exception e) {

		}
		return null;

	}

	/**
	 * to check whether device is connected to Internet or not
	 * 
	 * @return true if exist,,,, or false if is not
	 */
	public static boolean isInternetOn(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}

		return false;
	}

	public static void unregister() {
	}

	public static Bitmap decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeFile(filePath, o2);

	}

	public static int exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}

	public static Bitmap applyMatrix(Bitmap bitmap, int orientation) {
		Matrix mtx = new Matrix();
		if (orientation == 90)
			mtx.postRotate(90);
		if (orientation == 180)
			mtx.postRotate(180);
		if (orientation == 270)
			mtx.postRotate(270);

		// Rotating Bitmap
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
	}

	public static List<String> ReadSDCard(String dir) {

		// /storage/sdcard0/DCIM/Facebook
		ArrayList<String> tFileList = new ArrayList<String>();

		// It have to be matched with the directory in SDCard
		File f = new File(dir);// Here you take your specific folder//

		File[] files = f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return ((name.endsWith(".jpg")) || (name.endsWith(".jpeg")) || (name.endsWith(".png")) || (name.endsWith(".JPG")) || (name.endsWith(".JPEG")) || (name.endsWith(".PNG")));
			}
		});

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			/*
			 * It's assumed that all file in the path are in supported type
			 */
			tFileList.add(file.getPath());
		}
		return tFileList;
	}

	public static List<String> ReadSDCard_Videos(String dir) {

		// /storage/sdcard0/DCIM/Facebook
		ArrayList<String> tFileList = new ArrayList<String>();

		// It have to be matched with the directory in SDCard
		File f = new File(dir);// Here you take your specific folder//

		File[] files = f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return ((name.endsWith(".3GPP")) || (name.endsWith(".MPEG4")) || (name.endsWith(".MP4")) || (name.endsWith(".3gpp")) || (name.endsWith(".mpeg4")) || (name.endsWith(".mp4")));
			}
		});

		for (File file : files) {
			/*
			 * It's assumed that all file in the path are in supported type
			 */
			tFileList.add(file.getPath());
		}
		return tFileList;
	}

	public static List<String> ReadSDCard_VideoDir(String dir) {

		// /storage/sdcard0/DCIM/Facebook
		ArrayList<String> tFileList = new ArrayList<String>();

		// It have to be matched with the directory in SDCard
		File f = new File(dir);// Here you take your specific folder//

		File[] files = f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return ((name.endsWith(".3gp")) || (name.endsWith(".mp4")) || (name.endsWith(".mkv")) || (name.endsWith(".3GP")) || (name.endsWith(".MP4")) || (name.endsWith(".MKV")));
			}
		});

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			/*
			 * It's assumed that all file in the path are in supported type
			 */
			tFileList.add(file.getPath());
		}
		return tFileList;
	}

	public static ArrayList<String> getLocalVideoListings(Activity ctx, String directoryName) {

		// items.add(new Sdcard_Itembean(bucket, data, false, directorypath));

		ArrayList<String> items = new ArrayList<String>();

		String[] projection = new String[] { "DISTINCT " + MediaStore.Video.Media.BUCKET_DISPLAY_NAME };
		Uri Video = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		Cursor cur = ctx.managedQuery(Video, projection, "", null, "" + MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

		Log.i("ListingImages", " query count=" + cur.getCount());

		if (cur.moveToFirst()) {
			String bucket;
			int bucketColumn = cur.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
			do {
				// Get the field values
				bucket = cur.getString(bucketColumn);
				String[] projectionIcon = new String[] { MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
						// MediaStore.Video.Media.BUCKET_ID,
						MediaStore.Video.Media.DATA,
				// MediaStore.Video.Media.DISPLAY_NAME
				};
				// Get the base URI for the People table in the Contacts content
				// provider.
				Uri imagesIcon = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				String orderbyIcon = MediaStore.Video.Media._ID;
				// Make the query.
				Cursor curIcon = ctx.managedQuery(imagesIcon, projectionIcon,
				// Which columns to return
						"bucket_display_name='" + bucket + "'", null, // Selection
																		// arguments
																		// (none)
						orderbyIcon // Ordering
						);
				String data = "";
				if (curIcon.moveToFirst()) {

					int dataColumn = curIcon.getColumnIndex(MediaStore.Video.Media.DATA);
					data = curIcon.getString(dataColumn);
					Log.e("ListingImages", " Folder=" + bucket + ", path: " + data);

				}
				if (!data.matches("")) {

					if (!bucket.startsWith(".")) {

						File file = new File(data);
						Log.e(">>>>file.length()", ">>>>>" + file.length());

						if (file.length() > 0) {

							String directorypath = data.substring(0, data.lastIndexOf("/"));
							Log.e("directorypath", " fffff" + directorypath);
							Log.e(">>>>bucket", ">>>>>" + bucket);
							Log.e(">>>>data", ">>>>>" + data);
							Log.e(">>>>directorypath", ">>>>>" + directorypath);

							if (bucket.equals(directoryName)) {
								items.add(data);
							}
						}
					}

				}
			} while (cur.moveToNext());
		}
		return items;
	}

}
