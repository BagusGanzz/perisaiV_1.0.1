package com.perisaimobile.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.perisaimobile.util.TypeFaceUttils;
import com.studioninja.locker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseToolbarActivity extends AppCompatActivity {
    private int layoutId;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);
        TextView mTitle = (TextView) this.toolbar.findViewById(R.id.toolbar_title);
        TypeFaceUttils.setNomal((Context) this, mTitle);
        mTitle.setText(R.string.app_name);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public int getLayoutId() {
        return this.layoutId;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
        }
        return true;
    }
}
