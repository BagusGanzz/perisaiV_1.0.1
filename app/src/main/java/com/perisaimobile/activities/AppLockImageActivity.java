package com.perisaimobile.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.databases.ImagesDatabaseHelper;
import com.perisaimobile.model.Image;
import com.perisaimobile.util.TypeFaceUttils;
import com.studioninja.locker.R;

import java.io.File;

import butterknife.BindView;

public class AppLockImageActivity extends BaseToolbarActivity {
    @BindView(R.id.img_del)
    ImageView img_del;
    @BindView(R.id.tv_app_name)
    TextView tv_app_name;
    @BindView(R.id.tv_date)
    TextView tv_date;

    private void customFont() {
        TypeFaceUttils.setNomal((Context) this, this.tv_app_name);
        TypeFaceUttils.setNomal((Context) this, this.tv_date);
    }

    public int getLayoutId() {
        return R.layout.activity_app_lock_image;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customFont();
        final ImagesDatabaseHelper imagesDatabaseHelper = ImagesDatabaseHelper.getInstance(this);
        ImageView img = (ImageView) findViewById(R.id.img);
        int id = getIntent().getIntExtra("data", -1);
        if (id != -1) {
            final Image image = imagesDatabaseHelper.findByID((long) id);
            final File file = new File(image.getPath());
            Glide.with(this).load(Uri.fromFile(file)).into(img);
            this.tv_app_name.setText(image.getAppName());
            this.tv_date.setText(image.getDate());
            this.img_del.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (file.delete()) {
                        imagesDatabaseHelper.delete((long) image.getId());
                        AppLockImageActivity.this.finish();
                    }
                }
            });
        }
    }
}
