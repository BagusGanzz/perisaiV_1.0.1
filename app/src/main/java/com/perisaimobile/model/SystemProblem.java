package com.perisaimobile.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.perisaimobile.iface.IProblem;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class SystemProblem implements IProblem {
    public abstract void doAction(Context context);

    public abstract String getDescription(Context context);

    public abstract Drawable getIcon(Context context);

    public abstract String getSerializationTypeString();

    public abstract Drawable getSubIcon(Context context);

    public abstract String getSubTitle(Context context);

    public abstract String getTitle(Context context);

    public abstract String getWhiteListOnAddDescription(Context context);

    public abstract String getWhiteListOnRemoveDescription(Context context);

    public ProblemType getType() {
        return ProblemType.SystemProblem;
    }

    public JSONObject buildJSONObject() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("type", getSerializationTypeString());
        return jsonObj;
    }

    public void loadFromJSON(JSONObject appObject) {
    }

    public void writeToJSON(String filePath) {
    }

    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean equals(Object o) {
        if (o != null && o.getClass() == getClass()) {
            return true;
        }
        return false;
    }
}
