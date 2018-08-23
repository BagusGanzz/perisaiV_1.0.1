package com.studioninja.locker.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.studioninja.locker.util.Log;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.studioninja.locker.R;

public class Sdcard_photo extends AppCompatActivity implements OnItemClickListener, OnClickListener {
	TapTargetSequence sequence;
	MovefileTask movefiletask = null;
	private ProgressDialog pDialog;
	public static final int CUSTOM_PROGRESS_DIALOG = 0;
	private Activity context;
	private LayoutInflater inflater;
	private AQuery aq;
	private GridView lv;
	ProgressBar progress;
	ImageView Back, Next;
	ArrayList<String> Album_id;
	List<Image_Info> events;
	// ArrayList<String> Albumlist;
	String DirName = "";
	String TAG = "Sdcard_photo";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setup();
	}

	private void setup() {
		final Drawable nosdcard = ContextCompat.getDrawable(this, R.drawable.ic_important);
		final Drawable checkbox = ContextCompat.getDrawable(this, R.drawable.ic_checkbox);

		context = this;
		aq = new AQuery(context);
		setContentView(R.layout.albumphoto);
		progress = (ProgressBar) findViewById(R.id.progress);
		lv = (GridView) findViewById(R.id.lview1);
		inflater = LayoutInflater.from(this);
		Back = (ImageView) findViewById(R.id.back);
		Next = (ImageView) findViewById(R.id.next);
		// Albumlist = new ArrayList<String>();

		DirName = this.getIntent().getExtras().getString("albumlist");
		// Albumlist = (ArrayList<String>)
		// getIntent().getSerializableExtra("albumlist");

		Log.e("DirName ------>>>>>", "--->>" + DirName);

		Load_Albumphoto(DirName);

		lv.setOnItemClickListener(this);
		Next.setOnClickListener(this);
		Back.setOnClickListener(this);

		//Tab Target View
		SharedPreferences sharedPref = this.getSharedPreferences("SEQUENCE_TAP_TARGET", Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = sharedPref.edit();
		getSupportActionBar().hide();

		sequence = new TapTargetSequence(this)
				.targets(

						TapTarget.forView(findViewById(R.id.select_file),"Hide Your Media.","Pick your file from your folder.")
								.cancelable(false)
								.icon(checkbox)
								.outerCircleColor(R.color.md_black_1000),
						TapTarget.forView(findViewById(R.id.next),"Hide Your Media","Press this button to hide your file.")
								.cancelable(false)
								.transparentTarget(true)
								.outerCircleColor(R.color.md_black_1000),
						TapTarget.forView(findViewById(R.id.back),"Finish","After hide your file, Press back to Vault to see your hidden file's.")
								.cancelable(false)
								.transparentTarget(true)
								.outerCircleColor(R.color.md_black_1000),
						TapTarget.forView(findViewById(R.id.select_file),"IMPORTANT!","Currenly we dont support hidding media from External Storage.")
								.cancelable(false)
								.icon(nosdcard)
								.outerCircleColor(R.color.md_black_1000)

				).listener(new TapTargetSequence.Listener() {
					@Override
					public void onSequenceFinish() {
						editor.putBoolean("finished",true);
						editor.commit();
					}

					@Override
					public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

					}

					@Override
					public void onSequenceCanceled(TapTarget lastTarget) {
						editor.putBoolean("finished",true);
						editor.commit();
					}
				});

		boolean isSequenceFinished = sharedPref.getBoolean("finished",false);

		if(!isSequenceFinished) {

			sequence.start();

		}

		String sdCard = Environment.getExternalStorageDirectory().toString();
		File dir = new File(sdCard + "/" + ".Android_Libraries");
		File photo_dir = new File(sdCard + "/" + ".Android_Libraries/.Photo");
		File Video_dir = new File(sdCard + "/" + ".Android_Libraries/.Video");
		if (!dir.exists()) {
			dir.mkdir();
			Log.e(">>>", dir + "created");
		}
		if (!photo_dir.exists()) {
			photo_dir.mkdir();
			Log.e(">>>", photo_dir + "created");
		}

		if (!Video_dir.exists()) {
			Video_dir.mkdir();
			Log.e(">>>", Video_dir + "created");
		}

	}

	private void Load_Albumphoto(String dn) {
		// TODO Auto-generated method stub

		try {

			events = new ArrayList<Image_Info>();

			List<String> al = Constants.ReadSDCard(dn);
			for (int i = 0; i < al.size(); i++) {
				events.add(new Image_Info(al.get(i), false));
			}

			lv.setAdapter(new ArrayAdapter<Image_Info>(context, 0, events) {

				@Override
				public View getView(int position, View view, ViewGroup parent) {

					view = inflater.inflate(R.layout.albumphoto_row, null);
					AQuery recycle = aq.recycle(view);
					Image_Info info = getItem(position);
					final ViewHolder holder = new ViewHolder();
					holder.pb = (ProgressBar) view.findViewById(R.id.progress1);

					holder.Coverimage = (ImageView) view.findViewById(R.id.coverpic);
					holder.checkbox = (CheckBox) view.findViewById(R.id.checkBox1);

					File file = new File(info.Imagepath);
					recycle.id(holder.Coverimage).progress(holder.pb).image(file, 300);

					view.setTag(holder);
					holder.checkbox.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							CheckBox cb = (CheckBox) v;
							Image_Info _state = (Image_Info) cb.getTag();
							// Toast.makeText(mContext, "Checkbox: " +
							// cb.getText() + " -> " + cb.isChecked(),
							// Toast.LENGTH_LONG).show();
							_state.setImageselection(cb.isChecked());
						}
					});

					// }else {
					// holder = (ViewHolder) view.getTag();
					// }

					Image_Info state = events.get(position);

					holder.checkbox.setChecked(state.isImageselection());
					holder.checkbox.setTag(state);

					lv.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							// Searchbean info1 = (Searchbean)
							// parent.getItemAtPosition(position);

						}
					});

					return view;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {

	}

	public class Image_Info {
		String Imagepath;
		boolean Imageselection;

		public boolean isImageselection() {
			return Imageselection;
		}

		public void setImageselection(boolean imageselection) {
			Imageselection = imageselection;
		}

		public Image_Info(String imagepath, boolean imageselection) {
			this.Imagepath = imagepath;
			this.Imageselection = imageselection;
		}
	}

	static class ViewHolder {
		ImageView Coverimage;
		ProgressBar pb;
		CheckBox checkbox;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == Next) {
			Album_id = new ArrayList<String>();
			StringBuffer responseText = new StringBuffer();
			responseText.append("Selected Countries are...\n");
			ArrayList<Image_Info> stateList = (ArrayList<Image_Info>) events;

			for (int i = 0; i < stateList.size(); i++) {
				Image_Info state = stateList.get(i);

				if (state.isImageselection()) {
					// responseText.append("\n" + state.getArtist_name());
					Album_id.add(state.Imagepath);
				}
			}
			Log.e(">>>>>>", ">>>>" + Album_id);

			if (Album_id.size() > 0) {
				movefiletask = new MovefileTask(0);
				movefiletask.execute();
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
			pDialog.setMessage("Loading...Please wait");
			pDialog.setIndeterminate(true);
			pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.my_progress_indeterminate));
			pDialog.setCancelable(false);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}

	}

	private class MovefileTask extends AsyncTask<Context, Void, String> {
		String resultPath = null;
		int Index = 0;

		public MovefileTask(int i) {
			this.Index = i;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(CUSTOM_PROGRESS_DIALOG);
		}

		protected String doInBackground(Context... params) {
			try {
				MoveFile(Album_id.get(Index));
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
			return resultPath;
		}

		private void MoveFile(String path) {

			String newpath = "";
			String dirpath = "";

			String sdCard = Environment.getExternalStorageDirectory().toString();
			// the file to be moved or copied
			File sourceLocation = new File(path);
			// make sure your target location folder exists!
			// File targetLocation = new File(sdCard + "/dmnew/dm.jpg");

			String[] separated = path.split("/");

			for (int i = 0; i < separated.length - 1; i++) {

				if (i == 0) {

					// newpath = "." + newpath;

				} else {

					if (newpath.equals("")) {

						newpath = newpath + "." + separated[i];
						dirpath = dirpath + "/." + separated[i];

						File my_dir = new File(sdCard + "/" + ".Android_Libraries/.Photo" + dirpath);
						if (!my_dir.exists()) {
							my_dir.mkdir();
							Log.e("------------------>>>", my_dir + "created");
						}

					} else {
						newpath = newpath + "/." + separated[i];
						dirpath = dirpath + "/." + separated[i];

						File my_dir = new File(sdCard + "/" + ".Android_Libraries/.Photo" + dirpath);
						if (!my_dir.exists()) {
							my_dir.mkdir();
							Log.e("------------------>>>", my_dir + "created");
						}

					}

					// dirpath--------> /.storage/.sdcard0/.WhatsApp/.Media/.WhatsApp Images

				}

				Log.e(TAG, i + "newpath------> " + newpath);
				Log.e(TAG, i + "dirpath--------> " + dirpath);

			}

			File targetLocation = new File(sdCard + "/" + ".Android_Libraries/.Photo" + dirpath + "/." + path.substring(path.lastIndexOf("/") + 1));

			// just to take note of the location sources
			Log.e(TAG, "sourceLocation: " + sourceLocation);
			Log.e(TAG, "targetLocation: " + targetLocation);

			try {

				// 1 = move the file, 2 = copy the file
				int actionChoice = 2;

				// moving the file to another directory
				if (actionChoice == 1) {

					if (sourceLocation.renameTo(targetLocation)) {
						Log.e(TAG, "Move file successful.");
					} else {
						Log.e(TAG, "Move file failed.");
					}

				}

				// we will copy the file
				else {

					// make sure the target file exists

					if (sourceLocation.exists()) {

						InputStream in = new FileInputStream(sourceLocation);
						OutputStream out = new FileOutputStream(targetLocation);

						// Copy the bits from instream to outstream
						byte[] buf = new byte[1024];
						int len;

						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						in.close();
						out.close();

						Log.e(TAG, "Copy file successful.");
						deleteFile(sourceLocation.getAbsolutePath());

						// boolean isdeletefile = sourceLocation.delete();
						// Log.e("isdeletefile>>>>>>" + sourceLocation.getCanonicalPath(), ">>>>" + isdeletefile);
						// Run the media scanner to discover the decrypted file exists
						// MediaScannerConnection.scanFile(Sdcard_photo.this, new String[] { sourceLocation.getAbsolutePath() }, null, null);
						// UpdateGallery(sourceLocation.getCanonicalPath());
						// ScanMedia(path);
						// ScanMedia(sdCard + "/" + ".Android_Libraries/.Photo" + dirpath + "/." + path.substring(path.lastIndexOf("/") + 1));

						try {
							File nomediafile = new File(sdCard + "/" + ".Android_Libraries/.Photo" + dirpath + "/.nomedia");
							if (!nomediafile.exists()) {
								boolean b = nomediafile.createNewFile();
								Log.e("nomediafile file created>>>", ">>>" + b);
							}

						} catch (Exception e1) {
							e1.printStackTrace();
						}

					} else {
						Log.e(TAG, "Copy file failed. Source file missing.");
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void deleteFile(String path) {
			try {

				getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "='" + path + "'", null);
			} catch (Exception e) {
				e.printStackTrace();

			}

		}

		public void UpdateGallery(String filePath) {
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filePath))));
		}

		private void ScanMedia(String path) {
			ContentValues values = new ContentValues();
			values.put("_data", path);
			ContentResolver cr = getContentResolver();
			cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Index++;
			if (Index < Album_id.size()) {

				movefiletask = new MovefileTask(Index);
				movefiletask.execute();

			} else {
				dismissDialog(CUSTOM_PROGRESS_DIALOG);
				// Sdcard_photo.this.finish();
				setup();
				Toast.makeText(getApplicationContext(), "Photo Has Been Hide", Toast.LENGTH_SHORT).show();
			}

		}
	}

}
