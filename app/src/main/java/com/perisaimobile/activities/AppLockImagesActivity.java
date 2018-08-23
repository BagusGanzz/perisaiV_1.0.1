package com.perisaimobile.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.perisaimobile.adapter.ImagesAdapter;
import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.databases.ImagesDatabaseHelper;
import com.perisaimobile.model.Image;
import com.studioninja.locker.R;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class AppLockImagesActivity extends BaseToolbarActivity {
    private List<Image> images;
    private ImagesDatabaseHelper imagesDatabaseHelper;
    @BindView(R.id.rv_images)
    RecyclerView rv_images;

    public int getLayoutId() {
        return R.layout.activity_app_lock_images;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.imagesDatabaseHelper = ImagesDatabaseHelper.getInstance(this);
    }

    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        this.rv_images.setLayoutManager(new GridLayoutManager(this, 2));
        this.rv_images.setHasFixedSize(true);
        this.images = this.imagesDatabaseHelper.getAllImages();
        Collections.sort(this.images, Collections.reverseOrder());
        this.rv_images.setAdapter(new ImagesAdapter(this, this.images));
    }
}
