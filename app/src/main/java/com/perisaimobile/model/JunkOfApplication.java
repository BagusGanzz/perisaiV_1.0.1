package com.perisaimobile.model;

public class JunkOfApplication implements Comparable<JunkOfApplication> {
    private long cacheSize;
    private String packageName;

    public JunkOfApplication(String packageName, long cacheSize) {
        this.packageName = packageName;
        this.cacheSize = cacheSize;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getCacheSize() {
        return this.cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public int compareTo(JunkOfApplication another) {
        return Long.valueOf(this.cacheSize).compareTo(Long.valueOf(another.getCacheSize()));
    }

    public boolean equals(Object obj) {
        if (obj instanceof JunkOfApplication) {
            return this.packageName.equals(((JunkOfApplication) obj).getPackageName());
        }
        return super.equals(obj);
    }
}
