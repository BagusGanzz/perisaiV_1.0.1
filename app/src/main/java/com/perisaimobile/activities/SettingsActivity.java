package com.perisaimobile.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.perisaimobile.base.BaseToolbarActivity;
import com.studioninja.locker.R;

public class SettingsActivity extends BaseToolbarActivity {
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TextView) findViewById(R.id.toolbar_title)).setText(getResources().getString(R.string.settings));
    }
}
