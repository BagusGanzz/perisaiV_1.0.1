package com.perisaimobile.model;

import android.support.annotation.NonNull;

public class AppLock implements Comparable<AppLock> {
    private boolean isLock;
    private String name;
    private String packageName;
    private boolean recommend;

    public AppLock(String name, String packageName, boolean isLock) {
        this.name = name;
        this.packageName = packageName;
        this.isLock = isLock;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isLock() {
        return this.isLock;
    }

    public void setLock(boolean lock) {
        this.isLock = lock;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRecommend() {
        return this.recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public boolean equals(Object o) {
        if (o instanceof AppLock) {
            return this.packageName.equals(((AppLock) o).getPackageName());
        }
        return false;
    }

    public int compareTo(@NonNull AppLock another) {
        if (this.recommend && another.isRecommend()) {
            return this.name.compareTo(another.getName());
        }
        if (this.recommend) {
            return -1;
        }
        if (another.isRecommend()) {
            return 1;
        }
        if (this.isLock && another.isLock) {
            return this.name.compareTo(another.getName());
        }
        if (this.isLock) {
            return -1;
        }
        if (another.isLock) {
            return 1;
        }
        return this.name.compareTo(another.getName());
    }
}
