package com.studioninja.locker.ui;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.studioninja.locker.util.Log;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

public class SdcardImages extends Activity implements OnClickListener {
	GridView listView;
	ArrayList<Sdcard_Itembean> items;
	Activity context;
	private LayoutInflater inflater;
	private AQuery aq;
	ImageView Back, Next;
	ArrayList<String> Imagedirectory;
	private AdapterView<ListAdapter> adapter;

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
							"Hide Your Photo.",
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
//				startActivity(new Intent(SdcardImages.this,Sdcard_photo.class));
//			}
//		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("onResume call", ">>>>>");

		getLocalImageListings();
		SetAdapter();
	}

	private void SetAdapter() {
		// TODO Auto-generated method stub

		listView.setAdapter(new ArrayAdapter<Sdcard_Itembean>(context, 0, items) {
			@Override
			public View getView(int position, View view, ViewGroup parent) {

				view = inflater.inflate(R.layout.album_row, null);
				AQuery recycle = aq.recycle(view);
				Sdcard_Itembean info = getItem(position);
				final ViewHolder holder = new ViewHolder();
				holder.pb = (ProgressBar) view.findViewById(R.id.progress1);
				holder.Albumname = (TextView) view.findViewById(R.id.albumname);
				holder.Coverimage = (ImageView) view.findViewById(R.id.coverpic);
				// holder.checkbox = (CheckBox)
				// view.findViewById(R.id.checkBox1);
				holder.rowclick = (LinearLayout) view.findViewById(R.id.rowclick);

				holder.Albumname.setText(info.Foldername);

				File file = new File(info.Filepath);
				recycle.id(holder.Coverimage).progress(holder.pb).image(file, 300);

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
						Intent i = new Intent(SdcardImages.this, Sdcard_photo.class);
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

	public void getLocalImageListings() {

		items = new ArrayList<Sdcard_Itembean>();
		String[] projection = new String[] { "DISTINCT " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
		Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		Cursor cur = managedQuery(images, projection, "", null, "" + MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

		Log.i("ListingImages", " query count=" + cur.getCount());

		if (cur.moveToFirst()) {
			String bucket;
			int bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
			do {
				// Get the field values
				bucket = cur.getString(bucketColumn);
				String[] projectionIcon = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
						// MediaStore.Images.Media.BUCKET_ID,
						MediaStore.Images.Media.DATA,
				// MediaStore.Images.Media.DISPLAY_NAME
				};
				// Get the base URI for the People table in the Contacts content
				// provider.
				Uri imagesIcon = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				String orderbyIcon = MediaStore.Images.Media._ID;
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

					int dataColumn = curIcon.getColumnIndex(MediaStore.Images.Media.DATA);
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
							items.add(new Sdcard_Itembean(bucket, data, false, directorypath));
						}
					}

				}
			} while (cur.moveToNext());
		}
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

				Intent i = new Intent(SdcardImages.this, Sdcard_photo.class);
				i.putExtra("albumlist", Imagedirectory);
				startActivity(i);
			}

		} else if (v == Back) {
			finish();
		}
	}

}
