package com.perisaimobile.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.studioninja.locker.R;
import com.perisaimobile.uninstaller;

import java.util.ArrayList;

import static com.perisaimobile.uninstaller.packageDeleted;


/**
 * Created by mac on 26/01/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<AppInfo> appInfo;
    private Activity activity;
    private boolean bool = false;
    ColorStateList originColor;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        private TextView  title , size , version;
        private ImageView imagevi;
        private CardView cardView;
        private  CheckBox checkbox;

        public ViewHolder(View v) {
            super(v);

            imagevi = (ImageView)v.findViewById(R.id.imageView);
            title = (TextView)v.findViewById(R.id.title);
            size = (TextView)v.findViewById(R.id.size);
            version = (TextView)v.findViewById(R.id.version);
            checkbox = (CheckBox)v.findViewById(R.id.checkbox);
            cardView = (CardView)v.findViewById(R.id.card_view);

            cardView.setOnClickListener(this);

            originColor = title.getTextColors();

            if(bool){

                checkbox.setChecked(true);
                title.setTextColor(Color.RED);
                size.setTextColor(Color.RED);
                version.setTextColor(Color.RED);
            }

            this.setIsRecyclable(false);

        }

        @Override
        public void onClick(View v) {

            final AppInfo appati = appInfo.get(getAdapterPosition());

            if(R.id.card_view == v.getId()){

                packageDeleted = appati.getPackageName();
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:"+appati.getPackageName()));
                activity.startActivity(intent);

            }


        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Activity a, ArrayList<AppInfo> jData , boolean bool) {
        this.appInfo = jData;
        this.activity = a;
        this.bool = bool;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_videos_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final AppInfo tabiqati = appInfo.get(position);

                    holder.imagevi.setImageDrawable(tabiqati.getIcon());
                    holder.title.setText((tabiqati.getAppName().length() < 24)? tabiqati.getAppName() : tabiqati.getAppName().substring(0 , 24));
                    holder.size.setText(android.text.format.Formatter.formatShortFileSize(activity, tabiqati.size));
                    holder.version.setText(tabiqati.getVersionName());

        //in some cases, it will prevent unwanted situations
        holder.checkbox.setOnCheckedChangeListener(null);

        if(tabiqati.isChecked()){
            holder.checkbox.setChecked(true);
            holder.title.setTextColor(Color.RED);
            holder.size.setTextColor(Color.RED);
            holder.version.setTextColor(Color.RED);
        }

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status

                if(isChecked){

                    tabiqati.setChecked(true);
                    uninstaller.allPackagesSelected.add(tabiqati.getPackageName());
                    uninstaller.appCounter++;
                    uninstaller.setTextForUninstallerCounterApp(uninstaller.appCounter);

                    holder.title.setTextColor(Color.RED);
                    holder.size.setTextColor(Color.RED);
                    holder.version.setTextColor(Color.RED);

                }else{

                    tabiqati.setChecked(false);
                    uninstaller.allPackagesSelected.remove(tabiqati.getPackageName());
                    uninstaller.appCounter--;
                    uninstaller.setTextForUninstallerCounterApp(uninstaller.appCounter);

                    holder.title.setTextColor(originColor);
                    holder.size.setTextColor(originColor);
                    holder.version.setTextColor(originColor);

                }

            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return appInfo.size();
    }

}