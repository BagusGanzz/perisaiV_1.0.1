package com.perisaimobile.model;

import android.support.annotation.NonNull;

public class Image implements Comparable<Image> {
    private String appName;
    private String date;
    private int id;
    private String name;
    private String path;

    public Image(){}


    public Image(String name, String appName, String date, String path) {
        this.name = name;
        this.appName = appName;
        this.date = date;
        this.path = path;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int compareTo(@NonNull Image another) {
        return this.date.compareTo(another.getDate());
    }
}
