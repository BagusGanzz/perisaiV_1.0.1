package com.perisaimobile.model;

import android.content.Context;

import com.perisaimobile.iface.IProblem;

public class MenacesCacheSet extends JSONDataSet<IProblem> {
    public MenacesCacheSet(Context context) {
        super(context, "menacescache.json", new ProblemFactory());
    }
}
