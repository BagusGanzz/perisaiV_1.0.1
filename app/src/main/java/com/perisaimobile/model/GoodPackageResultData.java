package com.perisaimobile.model;

import android.content.pm.PackageInfo;

public class GoodPackageResultData {
    private PackageInfo _packageInfo;

    public String getPackageName() {
        return this._packageInfo.packageName;
    }

    public PackageInfo getPackageInfo() {
        return this._packageInfo;
    }

    public void setPackageInfo(PackageInfo pi) {
        this._packageInfo = pi;
    }

    public GoodPackageResultData(PackageInfo packageInfo) {
        this._packageInfo = packageInfo;
    }

    public int hashCode() {
        return getPackageName().hashCode();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        return this._packageInfo.packageName.equals(((GoodPackageResultData) o)._packageInfo);
    }
}
