package com.perisaimobile.model;

import android.content.Context;

import com.perisaimobile.iface.IDataSet;
import com.perisaimobile.iface.IDataSetChangesListener;
import com.perisaimobile.iface.IFactory;
import com.perisaimobile.iface.IJSONSerializer;
import com.perisaimobile.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class JSONDataSet<T extends IJSONSerializer> implements IDataSet<T> {
    Context _context = null;
    IDataSetChangesListener _dataSetChangesListener = null;
    String _filePath = null;
    IFactory<T> _nodeFactory = null;
    private Set<T> _set;

    public int getItemCount() {
        return this._set.size();
    }

    public Set<T> getSet() {
        return this._set;
    }

    void setSet(Set<T> set) {
        this._set = set;
    }

    public void setDataSetChangesListener(IDataSetChangesListener listener) {
        this._dataSetChangesListener = listener;
    }

    public void unregisterDataSetChangesListener() {
        this._dataSetChangesListener = null;
    }

    private JSONDataSet() {
    }

    public JSONDataSet(Context context, String serializeFileName, IFactory<T> nodeFactory) {
        this._context = context;
        this._nodeFactory = nodeFactory;
        this._set = new HashSet();
        this._filePath = Utils.getInternalDataPath(this._context) + File.separatorChar + serializeFileName;
        if (Utils.existsFile(this._filePath)) {
            loadFromJSON();
            return;
        }
        try {
            Utils.writeTextFile(this._filePath, "{\n  \"data\": [  ]\n}\n");
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    public void loadFromJSON() {
        try {

            this._set = new HashSet();

            JSONArray badAppsArray = new JSONObject(Utils.loadJSONFromFile(this._context, this._filePath)).getJSONArray("data");
            for (int i = 0; i < badAppsArray.length(); i++) {
                JSONObject badAppObj = badAppsArray.getJSONObject(i);
                IJSONSerializer bpd =  this._nodeFactory.createInstance(badAppObj.getString("type"));
            //    bpd.loadFromJSON(badAppObj);
             //   this._set.add((T)bpd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeToJSON() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (IJSONSerializer pd : this._set) {
                jsonArray.put(pd.buildJSONObject());
            }
            JSONObject rootObj = new JSONObject();
            rootObj.put("data", jsonArray);
            Utils.writeTextFile(this._filePath, rootObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return;
    }

    public boolean addItem(T item) {
        boolean b = this._set.add(item);
        if (b && this._dataSetChangesListener != null) {
            this._dataSetChangesListener.onSetChanged();
        }
        return b;
    }

    public boolean removeItem(T item) {
        boolean b = this._set.remove(item);
        if (b && this._dataSetChangesListener != null) {
            this._dataSetChangesListener.onSetChanged();
        }
        return b;
    }

    public void removeAll() {
        this._set.clear();
    }

    public boolean addItems(Collection<? extends T> item) {
        boolean b = this._set.addAll(item);
        if (b && this._dataSetChangesListener != null) {
            this._dataSetChangesListener.onSetChanged();
        }
        return b;
    }
}
