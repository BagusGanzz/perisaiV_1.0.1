package com.perisaimobile.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.studioninja.locker.R;


public class ExitDialog extends Dialog {

    private MainActivityantivirus mContext;
    public static Typeface type_Roboto_Bold;
    public static Typeface type_Roboto_Medium;
    public static Typeface type_Roboto_Regular;
    int randomNumber;
    SharedPreferences sharepref;
    boolean isBuyAds;

    public ExitDialog(MainActivityantivirus mainactivity, int i) {
        super(mainactivity, i);
        mContext = mainactivity;


        requestWindowFeature(1);
        setContentView(R.layout.rate_dialog);

        type_Roboto_Bold = Typeface.createFromAsset(mainactivity.getAssets(),
                "fonts/Roboto-Bold.ttf");
        type_Roboto_Medium = Typeface.createFromAsset(mainactivity.getAssets(),
                "fonts/Roboto-Medium.ttf");
        type_Roboto_Regular = Typeface.createFromAsset(
                mainactivity.getAssets(), "fonts/Roboto-Regular.ttf");

        setCanceledOnTouchOutside(false);
        TextView text = (TextView) findViewById(R.id.btCancle);
        TextView textview = (TextView) findViewById(R.id.btExit);
        TextView textview1 = (TextView) findViewById(R.id.btRate);
        TextView title = (TextView) findViewById(R.id.tvRateTitle);
        TextView detail = (TextView) findViewById(R.id.tvRateContent);
        text.setTypeface(type_Roboto_Medium);
        textview.setTypeface(type_Roboto_Medium);
        textview1.setTypeface(type_Roboto_Medium);
        ((TextView) findViewById(R.id.tvRateTitle))
                .setTypeface(type_Roboto_Medium);
        ((TextView) findViewById(R.id.tvRateContent))
                .setTypeface(type_Roboto_Regular);

        text.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dismiss();

            }

        });
        textview.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dismiss();
            }
        });
        textview1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                String link = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                mContext.startActivity(myIntent);

            }

        });
        show();

    }

}
