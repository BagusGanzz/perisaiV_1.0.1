package com.perisaimobile.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.perisaimobile.model.Image;
import com.perisaimobile.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ImagesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "imagesDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_APP_NAME = "appName";
    private static final String KEY_DATE = "date";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PATH = "path";
    private static final String TABLE_IMAGES = "images";
    private static ImagesDatabaseHelper sInstance;

    public static synchronized ImagesDatabaseHelper getInstance(Context context) {
        ImagesDatabaseHelper imagesDatabaseHelper;
        synchronized (ImagesDatabaseHelper.class) {
            if (sInstance == null) {
                sInstance = new ImagesDatabaseHelper(context.getApplicationContext());
            }
            imagesDatabaseHelper = sInstance;
        }
        return imagesDatabaseHelper;
    }

    public ImagesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE images(id INTEGER PRIMARY KEY,name TEXT,appName TEXT,date DATETIME DEFAULT CURRENT_TIMESTAMP,path TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS images");
            onCreate(db);
        }
    }

    public long add(Image image) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, image.getName());
        values.put(KEY_APP_NAME, image.getAppName());
        values.put(KEY_DATE, Utils.getDateTime());
        values.put(KEY_PATH, image.getPath());
        return db.insert(TABLE_IMAGES, null, values);
    }

    public List<Image> getAllImages() {
        List<Image> images = new ArrayList();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM images", null);
        if (cursor.moveToFirst()) {
            do {
                Image image = new Image();
                image.setId(cursor.getInt(0));
                image.setName(cursor.getString(DATABASE_VERSION));
                image.setAppName(cursor.getString(2));
                image.setDate(cursor.getString(3));
                image.setPath(cursor.getString(4));
                images.add(image);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return images;
    }

    public Image findByID(long id) {
        Image image = new Image();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM images WHERE id = " + id, null);
        if (cursor.moveToFirst()) {
            do {
                image.setId(cursor.getInt(0));
                image.setName(cursor.getString(DATABASE_VERSION));
                image.setAppName(cursor.getString(2));
                image.setDate(cursor.getString(3));
                image.setPath(cursor.getString(4));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return image;
    }

    public void delete(long id) {
        getWritableDatabase().execSQL("DELETE FROM images WHERE id = " + id);
    }
}
