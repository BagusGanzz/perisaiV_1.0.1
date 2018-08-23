package com.perisaimobile.model;

import android.content.Context;

import com.perisaimobile.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class AppsLocked {
    private String _filePath;
    private Hashtable<String, AppLock> appLocks = new Hashtable();
    private Context context;

    public Hashtable<String, AppLock> getAppLocks() {
        return this.appLocks;
    }

    public AppsLocked(Context context) {
        this.context = context;
        this._filePath = Utils.getInternalDataPath(context) + File.separatorChar + "apps_locked.json";
        if (Utils.existsFile(this._filePath)) {
            loadFromJson();
            return;
        }
        try {
            Utils.writeTextFile(this._filePath, "{\n  \"data\": [{\"package_name\":\"com.android.settings\"}  ]\n}\n");
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
        loadFromJson();
    }

    public void loadFromJson() {
        try {
            JSONArray appsArray = new JSONObject(Utils.loadJSONFromFile(this.context, this._filePath)).getJSONArray("data");
            for (int i = 0; i < appsArray.length(); i++) {
                String packageName = appsArray.getJSONObject(i).getString("package_name");
                this.appLocks.put(packageName, new AppLock(Utils.getAppNameFromPackage(this.context, packageName), packageName, true));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeToJSON() {
        Exception e;
        JSONArray jsonArray = new JSONArray();
        try {
            for (String key : this.appLocks.keySet()) {
                JSONObject jo = new JSONObject();
                jo.put("package_name", ((AppLock) this.appLocks.get(key)).getPackageName());
                jsonArray.put(jo);
            }
            JSONObject rootObj = new JSONObject();
            rootObj.put("data", jsonArray);
            Utils.writeTextFile(this._filePath, rootObj.toString());
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
        }
    }

    public void add(AppLock appLock) {
        this.appLocks.put(appLock.getPackageName(), appLock);
        writeToJSON();
    }

    public void remove(AppLock appLock) {
        this.appLocks.remove(appLock.getPackageName());
        writeToJSON();
    }

    public boolean isAppLocked(String packageNAme) {
        return this.appLocks.containsKey(packageNAme);
    }
}
