package com.perisaimobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.perisaimobile.util.TypeFaceUttils;
import com.studioninja.locker.R;


public class QuestionSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private String[] questions;

    public QuestionSpinnerAdapter(Context context, String[] questions) {
        this.context = context;
        this.questions = questions;
    }

    public int getCount() {
        return this.questions.length;
    }

    public Object getItem(int position) {
        return this.questions[position];
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(this.context);
        TypeFaceUttils.setNomal(this.context, txt);
        txt.setPadding(32, 32, 32, 32);
        txt.setTextSize(18.0f);
        txt.setGravity(16);
        txt.setText(this.questions[position]);
        txt.setTextColor(Color.parseColor("#000000"));
        return txt;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(this.context);
        TypeFaceUttils.setNomal(this.context, txt);
        txt.setTextSize(16.0f);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_spinner_down, 0);
        txt.setText(this.questions[position]);
        txt.setTextColor(Color.parseColor("#000000"));
        return txt;
    }
}
