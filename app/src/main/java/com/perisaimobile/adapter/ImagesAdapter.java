package com.perisaimobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.perisaimobile.activities.AppLockImageActivity;
import com.perisaimobile.model.Image;
import com.perisaimobile.util.TypeFaceUttils;
import com.studioninja.locker.R;

import java.io.File;
import java.util.List;

public class ImagesAdapter extends Adapter<ImagesAdapter.ViewHolder> {
    private Context context;
    private List<Image> images;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ImageView img;
        public View item;
        public TextView tvAppName;
        public TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            this.img = (ImageView) itemView.findViewById(R.id.image);
            this.tvAppName = (TextView) itemView.findViewById(R.id.tv_app_name);
            this.tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            this.item = itemView.findViewById(R.id.item_image);
        }
    }

    public ImagesAdapter(Context context, List<Image> images) {
        this.context = context;
        this.images = images;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final Image image = (Image) this.images.get(position);
        Glide.with(this.context).load(Uri.fromFile(new File(image.getPath()))).into(holder.img);
        holder.tvAppName.setText(image.getAppName());
        holder.tvDate.setText(image.getDate());
        TypeFaceUttils.setNomal(this.context, holder.tvAppName);
        TypeFaceUttils.setNomal(this.context, holder.tvDate);
        holder.item.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ImagesAdapter.this.context, AppLockImageActivity.class);
                intent.putExtra("data", image.getId());
                ImagesAdapter.this.context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.images.size();
    }
}
