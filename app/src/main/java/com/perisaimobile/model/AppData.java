package com.perisaimobile.model;

import android.content.Context;

import com.perisaimobile.util.Utils;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.Serializable;

public class AppData implements Serializable {
    static transient AppData _instance = null;
    static final transient String filePath = "state.data";
    public static transient DateTime kNullDate = new DateTime(1973, 1, 1, 0, 0);
    private boolean _eulaAccepted = false;
    private boolean _firstScanDone = false;
    private DateTime _lastAdDate = new DateTime(1973, 1, 1, 0, 0);
    private DateTime _lastScanDate = new DateTime(1973, 1, 1, 0, 0);
    private boolean _voted;

    public boolean getVoted() {
        return this._voted;
    }

    public void setVoted(boolean voted) {
        this._voted = voted;
    }

    public boolean getFirstScanDone() {
        return this._firstScanDone;
    }

    public void setFirstScanDone(boolean firstScanDone) {
        this._firstScanDone = firstScanDone;
    }

    public boolean getEulaAccepted() {
        return this._eulaAccepted;
    }

    public void setEulaAccepted(boolean eulaAccepted) {
        this._eulaAccepted = eulaAccepted;
    }

    public DateTime getLastScanDate() {
        return this._lastScanDate;
    }

    public void setLastScanDate(DateTime date) {
        this._lastScanDate = date;
    }

    public DateTime getLastAdDate() {
        return this._lastAdDate;
    }

    public void setLastAdDate(DateTime date) {
        this._lastAdDate = date;
    }

    public static boolean isAppDataInited() {
        return _instance != null;
    }

    public static synchronized AppData getInstance(Context context) {
        AppData appData;
        synchronized (AppData.class) {
            if (_instance != null) {
                appData = _instance;
            } else {
                _instance = (AppData) Utils.deserializeFromDataFolder(context, filePath);
                if (_instance == null) {
                    _instance = new AppData();
                }
                appData = _instance;
            }
        }
        return appData;
    }

    public synchronized void serialize(Context ctx) {
        try {
            Utils.serializeToDataFolder(ctx, this, filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
