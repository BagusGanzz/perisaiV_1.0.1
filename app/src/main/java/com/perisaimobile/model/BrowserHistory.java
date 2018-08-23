package com.perisaimobile.model;

import android.graphics.drawable.Drawable;

public class BrowserHistory {
    private Drawable icon;
    private String title;
    private String url;

    public BrowserHistory(String title, String url, Drawable icon) {
        this.title = title;
        this.url = url;
        this.icon = icon;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean equals(Object o) {
        if (!(o instanceof BrowserHistory)) {
            return false;
        }
        BrowserHistory x = (BrowserHistory) o;
        if (this.title.equals(((BrowserHistory) o).getTitle()) && this.url.equals(x.getUrl())) {
            return true;
        }
        return false;
    }
}
