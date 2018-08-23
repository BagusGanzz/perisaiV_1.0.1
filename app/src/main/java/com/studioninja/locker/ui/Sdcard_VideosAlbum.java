package com.studioninja.locker.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.studioninja.locker.util.Log;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.studioninja.locker.R;

public class Sdcard_VideosAlbum extends Activity implements OnClickListener {
	GridView listView;
	ArrayList<Sdcard_Itembean> items;
	Activity context;
	private LayoutInflater inflater;
	private AQuery aq;
	ImageView Back, Next;
	ArrayList<String> Imagedirectory;
	private AdapterView<ListAdapter> adapter;
	String old_dir = "";
	public static final int CUSTOM_PROGRESS_DIALOG = 0;
	private ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.albumlist);
		listView = (GridView) findViewById(R.id.lview1);
		context = this;
		aq = new AQuery(context);
		inflater = LayoutInflater.from(this);
		Back = (ImageView) findViewById(R.id.back);
		Next = (ImageView) findViewById(R.id.next);

		ListfileTask ltf = new ListfileTask();
		ltf.execute();

		Next.setOnClickListener(this);
		Back.setOnClickListener(this);

		final Drawable files = ContextCompat.getDrawable(this, R.drawable.ic_folder);

		//Tab Target View
		SharedPreferences sharedPref = this.getSharedPreferences("SINGLE_TAP_TARGET", Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = sharedPref.edit();


		boolean isSingleTapFinished = sharedPref.getBoolean("isFinished",false);
		if(!isSingleTapFinished) {


			TapTargetView.showFor(this,
					TapTarget.forView(findViewById(R.id.select_folder),
							"Hide Your Video.",
							"Choose your folder from your gallery.")
							.icon(files)
							.cancelable(true)
							.outerCircleColor(R.color.md_black_1000),
					new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
						@Override
						public void onTargetClick(TapTargetView view) {
							super.onTargetClick(view);      // This call is optional

							editor.putBoolean("isFinished",true);
							editor.commit();
						}
					});

		}

//		findViewById(R.id.select_folder).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//
//				startActivity(new Intent(Sdcard_VideosAlbum.this,Sdcard_Videos.class));
//			}
//		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("onResume call", ">>>>>");

	}

	private class ListfileTask extends AsyncTask<Context, Void, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(CUSTOM_PROGRESS_DIALOG);
		}

		protected String doInBackground(Context... params) {
			try {
				String sdCard = Environment.getExternalStorageDirectory().toString();
				getLocalImageListings(sdCard);
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
			return "";
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dismissDialog(CUSTOM_PROGRESS_DIALOG);
			SetAdapter();
		}
	}

	private void SetAdapter() {
		// TODO Auto-generated method stub

		Log.e(" items.size()>>>>>>>>", ">>>>>>>>" + items.size());

		listView.setAdapter(new ArrayAdapter<Sdcard_Itembean>(context, 0, items) {
			@Override
			public View getView(int position, View view, ViewGroup parent) {

				view = inflater.inflate(R.layout.video_row, null);
				AQuery recycle = aq.recycle(view);
				Sdcard_Itembean info = getItem(position);
				final ViewHolder holder = new ViewHolder();
				holder.pb = (ProgressBar) view.findViewById(R.id.progress1ew);
				holder.Albumname = (TextView) view.findViewById(R.id.albumname);
				holder.Coverimage = (ImageView) view.findViewById(R.id.coverpic);
				// holder.checkbox = (CheckBox)
				// view.findViewById(R.id.checkBox1);
				holder.rowclick = (LinearLayout) view.findViewById(R.id.rowclick);

				holder.Albumname.setText(info.Foldername);

				Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(info.Filepath, Thumbnails.MINI_KIND);
				holder.pb.setVisibility(View.INVISIBLE);
				// File file = new File(info.Filepath);
				// recycle.id(holder.Coverimage).progress(holder.pb).image(file, 300);
				recycle.id(holder.Coverimage).image(bmThumbnail);
				// holder.Coverimage.setImageBitmap(bmThumbnail);

				view.setTag(holder);
				// holder.checkbox.setOnClickListener(new View.OnClickListener()
				// {
				// public void onClick(View v) {
				// CheckBox cb = (CheckBox) v;
				// Sdcard_Itembean _state = (Sdcard_Itembean) cb.getTag();
				// // Toast.makeText(mContext, "Checkbox: " + cb.getText() +
				// " -> " + cb.isChecked(), Toast.LENGTH_LONG).show();
				// _state.setFolderSelection(cb.isChecked());
				// }
				// });

				Sdcard_Itembean state = items.get(position);
				// holder.checkbox.setChecked(state.isFolderSelection());
				// holder.checkbox.setTag(state);

				listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						Sdcard_Itembean bb = (Sdcard_Itembean) parent.getItemAtPosition(position);
						Intent i = new Intent(Sdcard_VideosAlbum.this, Sdcard_Videos.class);
						i.putExtra("albumlist", bb.getDirectoryname());
						startActivity(i);
					}
				});

				return view;
			}
		});
	}

	static class ViewHolder {
		TextView Albumname;
		ImageView Coverimage;
		ProgressBar pb;
		// CheckBox checkbox;
		LinearLayout rowclick;

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	public void getLocalImageListings(String directoryName) {

		// items.add(new Sdcard_Itembean(bucket, data, false, directorypath));

		items = new ArrayList<Sdcard_Itembean>();
		String[] projection = new String[] { "DISTINCT " + MediaStore.Video.Media.BUCKET_DISPLAY_NAME };
		Uri Video = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		Cursor cur = managedQuery(Video, projection, "", null, "" + MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

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
				Cursor curIcon = managedQuery(imagesIcon, projectionIcon,
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

						String directorypath = data.substring(0, data.lastIndexOf("/"));

						File file = new File(data);
						Log.e(">>>>file.length()", ">>>>>" + file.length());

						if (file.length() > 0) {
							Log.e("directorypath", " fffff" + directorypath);
							Log.e(">>>>bucket", ">>>>>" + bucket);
							Log.e(">>>>data", ">>>>>" + data);
							Log.e(">>>>directorypath", ">>>>>" + directorypath);
							items.add(new Sdcard_Itembean(bucket, data, false, directorypath));
						}

					}
				}
			} while (cur.moveToNext());
		}

		// File directory = new File(directoryName);
		//
		// // get all the files from a directory
		// File[] fList = directory.listFiles();
		//
		// for (File file : fList) {
		// if (file.isFile()) {
		// // System.out.println(file.getAbsolutePath());
		//
		// String extention = file.getAbsolutePath();
		// extention = extention.substring(extention.lastIndexOf("/") + 1);
		//
		// if (extention.endsWith(".mp4")) {
		//
		// String str = file.getAbsolutePath();
		// String[] separated = str.split("/");
		// String strdir = separated[separated.length - 2];
		// Log.e("strdir", "--->" + strdir);
		// // Log.e("count", "--->" + count);
		//
		// if (old_dir.equals("") || (!old_dir.equals(strdir))) {
		// items.add(new Sdcard_Itembean(strdir, "", false, file.getAbsolutePath()));
		// old_dir = strdir;
		// Log.e(">>>>>>>>", ">>>>>>>>" + file.getAbsolutePath());
		// }
		//
		// }
		// } else if (file.isDirectory()) {
		// getLocalImageListings(file.getAbsolutePath());
		// }
		// }

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == Next) {

			Imagedirectory = new ArrayList<String>();
			ArrayList<Sdcard_Itembean> stateList = (ArrayList<Sdcard_Itembean>) items;

			for (int i = 0; i < stateList.size(); i++) {
				Sdcard_Itembean state = stateList.get(i);
				if (state.isFolderSelection()) {
					// responseText.append("\n" + state.getArtist_name());
					Imagedirectory.add(state.Directoryname);
				}
			}

			if (Imagedirectory.size() > 0) {

				Intent i = new Intent(Sdcard_VideosAlbum.this, Sdcard_Videos.class);
				i.putExtra("albumlist", Imagedirectory);
				startActivity(i);
			}

		} else if (v == Back) {
			finish();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CUSTOM_PROGRESS_DIALOG:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Loading...\nPlease wait");
			pDialog.setIndeterminate(true);
			pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.my_progress_indeterminate));
			pDialog.setCancelable(false);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}

	}

}
