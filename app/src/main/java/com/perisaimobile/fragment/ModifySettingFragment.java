package com.perisaimobile.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;


public class ModifySettingFragment extends Fragment {

    TextView textViewAllowDrawingOverOtherApps;
    Button buttonAllowDrawingOverOtherApps;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overidesetting, null );

        buttonAllowDrawingOverOtherApps = (Button) view.findViewById(R.id.buttonAllowDrawingOverOtherApps);
        textViewAllowDrawingOverOtherApps = (TextView) view.findViewById(R.id.textViewAllowDrawingOverOtherApps);

        buttonAllowDrawingOverOtherApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isAccessibilitySettingsOn(getActivity())) {
                    getActivity().finish();

                    return;
                }

                getActivity().startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().setType(2003);
                dialog.requestWindowFeature(1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.setContentView(R.layout.dialog_guide_accessibility);
                dialog.show();
            }
        });



        return view;
    }

    public void onResume() {
        super.onResume();
        checkDrawingOverOtherApps();
    }

    void checkDrawingOverOtherApps() {
        if (getActivity() != null && !getActivity().isFinishing() && this.textViewAllowDrawingOverOtherApps != null && this.buttonAllowDrawingOverOtherApps != null) {
            if (Utils.isAccessibilitySettingsOn(getActivity())) {
                this.textViewAllowDrawingOverOtherApps.setText(R.string.start_drawing_over_apps_desc_enabled);
                this.buttonAllowDrawingOverOtherApps.setText(R.string.well_done);
             //   activity.changeStartViewPagerPageCount(4);

                return;
            }
            this.textViewAllowDrawingOverOtherApps.setText(R.string.start_stystem_setting);
            this.buttonAllowDrawingOverOtherApps.setText(R.string.start_system_setting_button);
        //    activity.changeStartViewPagerPageCount(2);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkDrawingOverOtherApps();
    }


}
