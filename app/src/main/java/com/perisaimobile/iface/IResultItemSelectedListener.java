package com.perisaimobile.iface;

import android.content.Context;
import android.widget.ImageView;

public interface IResultItemSelectedListener {
    void onItemSelected(IProblem iProblem, ImageView imageView, Context context);
}
