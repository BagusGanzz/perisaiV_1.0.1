package com.perisaimobile.model;

public class ActivityData {
    private String _package;

    public String getPackage() {
        return this._package;
    }

    public ActivityData(String packageName) {
        this._package = packageName;
    }

    public int hashCode() {
        return this._package.hashCode();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        return this._package.equals(((ActivityData) o)._package);
    }
}
