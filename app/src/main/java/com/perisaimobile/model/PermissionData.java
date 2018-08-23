package com.perisaimobile.model;

public class PermissionData {
    private int _dangerous;
    private String _permissionName;

    public int getDangerous() {
        return this._dangerous;
    }

    public String getPermissionName() {
        return this._permissionName;
    }

    public PermissionData(String permissionName, int dangerous) {
        this._permissionName = permissionName;
        this._dangerous = dangerous;
    }

    public int hashCode() {
        return this._permissionName.hashCode() + this._dangerous;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        return this._permissionName.equals(((PermissionData) o)._permissionName);
    }
}
