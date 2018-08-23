package com.perisaimobile.model;

import android.content.Context;

import com.perisaimobile.iface.IProblem;
import com.perisaimobile.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class AppProblem extends PackageData implements IProblem {
    static final String kSerializationType = "app";
    private Set<ActivityData> _activities = new HashSet();
    private boolean _installedThroughGooglePlay = false;
    private Set<PermissionData> _permissions = new HashSet();

    public AppProblem(){}

    public AppProblem(String packageName) {
        super(packageName);
    }

    public ProblemType getType() {
        return ProblemType.AppProblem;
    }

    public void addActivityData(ActivityData activityData) {
        this._activities.add(activityData);
    }

    public Set<ActivityData> getActivityData() {
        return this._activities;
    }

    public void addPermissionData(PermissionData bad) {
        this._permissions.add(bad);
    }

    public Set<PermissionData> getPermissionData() {
        return this._permissions;
    }

    public boolean getInstalledThroughGooglePlay() {
        return this._installedThroughGooglePlay;
    }

    public void setInstalledThroughGooglePlay(boolean installed) {
        this._installedThroughGooglePlay = installed;
    }

    public boolean isMenace() {
        return !getInstalledThroughGooglePlay() || getActivityData().size() > 0 || getPermissionData().size() > 0;
    }

    public boolean isDangerous() {
        for (PermissionData pd : this._permissions) {
            if (pd.getDangerous() == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean problemExists(Context context) {
        return Utils.isPackageInstalled(context, getPackageName());
    }

    public JSONObject buildJSONObject() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("type", kSerializationType);
        jsonObj.put("packageName", getPackageName());
        jsonObj.put("gplayinstalled", getInstalledThroughGooglePlay());
        JSONArray activitiesArray = new JSONArray();
        for (ActivityData ad : this._activities) {
            JSONObject activityObject = new JSONObject();
            activityObject.put("packageName", ad.getPackage());
            activitiesArray.put(activityObject);
        }
        jsonObj.put("activities", activitiesArray);
        JSONArray permissionsArray = new JSONArray();
        for (PermissionData pd : this._permissions) {
            JSONObject permissionObject = new JSONObject();
            permissionObject.put("permissionName", pd.getPermissionName());
            permissionObject.put("dangerous", pd.getDangerous());
            permissionsArray.put(permissionObject);
        }
        jsonObj.put("permissions", permissionsArray);
        return jsonObj;
    }

    public void loadFromJSON(JSONObject appObject) {
        try {
            setPackageName(appObject.getString("packageName"));
            setInstalledThroughGooglePlay(appObject.getBoolean("gplayinstalled"));
            _loadActivitesFromJSON(appObject);
            _loadPermissionsFromJSON(appObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void _loadActivitesFromJSON(JSONObject appObject) {
        try {
            JSONArray activitiesArray = appObject.getJSONArray("activities");
            for (int i = 0; i < activitiesArray.length(); i++) {
                addActivityData(new ActivityData(activitiesArray.getJSONObject(i).getString("packageName")));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void _loadPermissionsFromJSON(JSONObject appObject) {
        try {
            JSONArray activitiesArray = appObject.getJSONArray("permissions");
            for (int i = 0; i < activitiesArray.length(); i++) {
                JSONObject temp = activitiesArray.getJSONObject(i);
                addPermissionData(new PermissionData(temp.getString("permissionName"), temp.getInt("dangerous")));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void writeToJSON(String filePath) {
    }
}
