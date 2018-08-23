package com.perisaimobile;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.studioninja.locker.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import com.perisaimobile.adapter.AppInfo;
import com.perisaimobile.adapter.MyAdapter;

public class uninstaller extends AppCompatActivity
        implements  SearchView.OnQueryTextListener{

    private final int iSPermissionAccepted = 1960;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<AppInfo> res = new ArrayList<>();

    LinearLayout linearlayout;
    public static String packageDeleted = "";

    int appTotal = 0;

    CheckBox checkbox;
    public static ArrayList<String> allPackagesSelected = new ArrayList<>();
    Boolean bool = false;
    static TextView unInstall;
    public static int appCounter = 0;
    ImageButton sort;
    int sortCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uninstaller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        unInstall = (TextView)findViewById(R.id.uninstall);
        checkbox = (CheckBox)findViewById(R.id.checkbox);
        sort = (ImageButton) findViewById(R.id.sort);
        sort.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sortbydate));

        unInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(allPackagesSelected.size()>0){
                    for(String everyApp : allPackagesSelected){
                        Intent intent = new Intent(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse("package:"+everyApp));
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getApplicationContext() , "No app to uninstall" , Toast.LENGTH_LONG).show();
                }

            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){

                    bool = true;
                    loadApp();
                }else{
                    bool = false;
                    loadApp();
                }
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sortCounter == 0){

                    sortCounter++;
                    Collections.sort(res, new Comparator<AppInfo>() {
                        public int compare(AppInfo m1, AppInfo m2) {
                            return m2.getSize().compareTo(m1.getSize());
                        }
                    });

                    sort.setImageDrawable(null);
                    sort.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sortbysizeable));

                }else if(sortCounter == 1){

                    sortCounter++;
                    Collections.sort(res, new Comparator<AppInfo>() {
                        public int compare(AppInfo m1, AppInfo m2) {
                            return m1.getAppName().compareTo(m2.getAppName());
                        }
                    });

                    sort.setImageDrawable(null);
                    sort.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sortbyname));

                }else if(sortCounter == 2){

                    sortCounter=0;
                    Collections.sort(res, new Comparator<AppInfo>() {
                        public int compare(AppInfo m1, AppInfo m2) {
                            return m2.getDateInstalled().compareTo(m1.getDateInstalled());
                        }
                    });

                    sort.setImageDrawable(null);
                    sort.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sortbydate));
                }


                RecyclerView.Adapter mAdapter = new MyAdapter(uninstaller.this, res , bool);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }
        });

        linearlayout = (LinearLayout)findViewById(R.id.unitads);


        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);





        loadApp();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }





    @SuppressWarnings("StatementWithEmptyBody")


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onResume() {

        super.onResume();
        bool = false;
        loadApp();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("onQueryTextSubmit" , query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        ArrayList<AppInfo> newfilter = new ArrayList<>();

        for(int i = 0; i < res.size(); i++){

            if(res.get(i).getAppName().toLowerCase().contains(newText.toLowerCase())){
                newfilter.add(res.get(i));
            }
        }

        RecyclerView.Adapter mAdapter = new MyAdapter(this, newfilter , bool);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return false;
    }


    private boolean isSystemPackage(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    void loadApp(){

        res.clear();

        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);

        for(int i=0;i<apps.size();i++) {
            PackageInfo p = apps.get(i);

            // check if system Packages
            if(!isSystemPackage(p) && !p.packageName.equals(getPackageName()) && !p.packageName.startsWith("com.android")){
                if(bool){
                    allPackagesSelected.add(p.packageName);
                    appCounter++;
                }else{
                    allPackagesSelected.clear();
                    appCounter = 0;
                }

                Long size = Long.parseLong("0");

                try{
                    File file = new File(p.applicationInfo.sourceDir);
                    size = file.length();
                }catch(Exception e){

                }

                AppInfo newInfo = new AppInfo(p.applicationInfo.loadLabel(getPackageManager()).toString(), p.packageName , p.versionName , p.versionCode, p.applicationInfo.loadIcon(getPackageManager()), size , p.firstInstallTime , false);
                res.add(newInfo);
                appTotal++;
            }

        }

        if(bool){
            setTextForUninstallerCounterApp(appCounter);
        }else{
            setTextForUninstallerCounterApp(appCounter);
        }

        Collections.sort(res, new Comparator<AppInfo>() {
            public int compare(AppInfo m1, AppInfo m2) {
                return m2.getDateInstalled().compareTo(m1.getDateInstalled());
            }
        });

        RecyclerView.Adapter mAdapter = new MyAdapter(this, res , bool);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public static void setTextForUninstallerCounterApp(int appTextCounter){
        if(appTextCounter>0){
            unInstall.setText("UnInstall("+appTextCounter+")");
            unInstall.setTypeface(null, Typeface.BOLD);
        }else{
            unInstall.setText("UnInstall");
            unInstall.setTypeface(null, Typeface.NORMAL);
        }

    }

}
