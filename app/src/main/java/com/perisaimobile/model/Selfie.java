package com.perisaimobile.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.WindowManager;

import com.perisaimobile.activities.AppLockCreatePasswordActivity;
import com.perisaimobile.databases.ImagesDatabaseHelper;
import com.perisaimobile.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Selfie {
    public static final String KEY_APP_THIEVES = "app_thieves";
    public static final String KEY_THIEVES = "thieves";
    public final String APP_TAG = getClass().getName();
    private Camera camera;
    private Context context;
    private ImagesDatabaseHelper imagesDatabaseHelper;
    private String pakageName;
    private SharedPreferences sharedPreferences;

    public Selfie(Context context, String pakageName) {
        this.context = context;
        this.pakageName = pakageName;
        this.sharedPreferences = context.getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0);
        this.imagesDatabaseHelper = ImagesDatabaseHelper.getInstance(context);
    }

    public void takePhoto() {
        final String imagesPath = this.context.getFilesDir().getPath() + File.separator + "images";
        File imageFolder = new File(imagesPath);
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        int id = getFrontCameraId();
        if (id == -1) {
            Log.w(this.APP_TAG, "No front camera available");
            return;
        }
        try {
            Log.d(this.APP_TAG, "trying id " + id);
            this.camera = Camera.open(id);
            this.camera.setPreviewTexture(new SurfaceTexture(10));
            setCameraDisplayOrientation(this.context, id, this.camera);
            this.camera.startPreview();
            this.camera.autoFocus(null);
            this.camera.takePicture(null, null, new PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    Log.d(Selfie.this.APP_TAG, "picturesaver!");
                    if (data != null) {
                        String file = new SimpleDateFormat("yyyymmdd-hhmmss").format(new Date()) + ".jpg";
                        String filename = imagesPath + File.separator + file;
                        try {
                            FileOutputStream fos = new FileOutputStream(new File(filename));
                            Selfie.rotate(BitmapFactory.decodeByteArray(data, 0, data.length), -90).compress(CompressFormat.JPEG, 100, fos);
                            fos.close();
                        } catch (Exception e) {
                            Log.w(Selfie.this.APP_TAG, "Could not save image");
                        }
                        Image image = new Image();
                        image.setName(file);
                        image.setAppName(Utils.getAppNameFromPackage(Selfie.this.context, Selfie.this.pakageName));
                        image.setPath(filename);
                        long id = Selfie.this.imagesDatabaseHelper.add(image);
                        if (id != -1) {
                            Selfie.this.sharedPreferences.edit().putBoolean(Selfie.KEY_THIEVES, true).apply();
                            Selfie.this.sharedPreferences.edit().putLong(Selfie.KEY_APP_THIEVES, id).apply();
                        }
                        if (camera != null) {
                            camera.release();
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.e(this.APP_TAG, "Failed to take picture", e);
            close();
        }
    }

    public void close() {
        if (this.camera != null) {
            this.camera.release();
            this.camera = null;
        }
    }

    public static void setCameraDisplayOrientation(Context c, int cameraId, Camera camera) {
        int result;
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = ((WindowManager) c.getSystemService("window")).getDefaultDisplay().getRotation();
        int degrees = rotation == 0 ? 0 : rotation == 1 ? 90 : rotation == 2 ? 180 : 270;
        if (info.facing == 1) {
            result = (info.orientation + degrees) % 360;
        } else {
            result = ((info.orientation - degrees) + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public boolean hasFrontCamera() {
        return getFrontCameraId() != -1;
    }

    private int getFrontCameraId() {
        if (VERSION.SDK_INT < 9) {
            return -1;
        }
        if (!this.context.getPackageManager().hasSystemFeature("android.hardware.camera.front")) {
            return -1;
        }
        CameraInfo ci = new CameraInfo();
        for (int id = 0; id < Camera.getNumberOfCameras(); id++) {
            Camera.getCameraInfo(id, ci);
            if (ci.facing == 1) {
                return id;
            }
        }
        return -1;
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.setRotate((float) degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
}
