package com.perisaimobile.model;

import java.util.List;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class PackageData {
    private String _packageName;

    public PackageData(){

    }

    public String getPackageName() {
        return this._packageName;
    }

    public void setPackageName(String packageName) {
        this._packageName = packageName;
    }

    public PackageData(String packageName) {
        setPackageName(packageName);
    }

    public int hashCode() {
        return this._packageName.hashCode();
    }

    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return this._packageName.equals(((PackageData) o)._packageName);
    }

    public JSONObject buildJSONObject() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("packageName", this._packageName);
        return jsonObj;
    }

    public static List<PackageData> getPackagesByName(Set<PackageData> packages, String filter, List<PackageData> result) {
        boolean wildcard;
        result.clear();
        if (filter.charAt(filter.length() - 1) == '*') {
            wildcard = true;
            filter = filter.substring(0, filter.length() - 2);
        } else {
            wildcard = false;
        }
        for (PackageData packInfo : packages) {
            if (packInfo._packageName.startsWith(filter)) {
                result.add(packInfo);
                if (!wildcard) {
                    break;
                }
            }
        }
        return result;
    }

    public static boolean isPackageInListByName(Set<PackageData> packages, String filter) {
        if (filter.charAt(filter.length() - 1) == '*') {
            filter = filter.substring(0, filter.length() - 2);
        }
        for (PackageData packInfo : packages) {
            if (packInfo._packageName.startsWith(filter)) {
                return true;
            }
        }
        return false;
    }
}
