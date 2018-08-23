package com.perisaimobile.model;

import android.graphics.drawable.Drawable;

public class Application {
    private Drawable icon;
    private boolean isChoose;
    private String name;
    private String packageName;
    private int pid;
    private long size;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Application(){

    }

    public Application(int pid, String packageName) {
        this.pid = pid;
        this.packageName = packageName;
    }

    public int getPid() {
        return this.pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isChoose() {
        return this.isChoose;
    }

    public void setChoose(boolean choose) {
        this.isChoose = choose;
    }

    public boolean equals(Object o) {
        if ((o instanceof Application) && this.packageName.equals(((Application) o).getPackageName())) {
            return true;
        }
        return false;
    }
}
