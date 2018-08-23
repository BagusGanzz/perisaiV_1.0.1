/*
    Copyright (c) 2013-2017 Darshan-Josiah Barber

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
*/

package com.studioninja.battery;

import android.content.Context;
//import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;

class CircleWidgetBackground {
    private final int DIMEN;
    private final float ARC_STROKE_WIDTH;

    //private static final int BLACK = 0xff000000;
    private static final int ICS_BLUE = 0xff33b5e5;

    private static final int BG_COLOR = 0xff222222;
    private static final int ARC_COLOR = ICS_BLUE;

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint bg_paint, arc_paint;

    CircleWidgetBackground(Context context) {
        //Resources res = context.getResources();

        canvas = new Canvas();

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        DIMEN = (int) (72 * (metrics.densityDpi / 160.0));
        ARC_STROKE_WIDTH = DIMEN * 0.07f;

        bitmap = Bitmap.createBitmap(DIMEN, DIMEN, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);

        bg_paint = new Paint();
        bg_paint.setColor(BG_COLOR);
        bg_paint.setAntiAlias(true);
        bg_paint.setStyle(Paint.Style.FILL);
        bg_paint.setDither(true);

        arc_paint = new Paint();
        arc_paint.setColor(ARC_COLOR);
        arc_paint.setAntiAlias(true);
        arc_paint.setStrokeWidth(ARC_STROKE_WIDTH);
        arc_paint.setStyle(Paint.Style.STROKE);
        //arc_paint.setStrokeCap(Paint.Cap.ROUND);
        arc_paint.setDither(true);

        //setLevel(100); // TODO: Would this be helpful?
    }

    public void setLevel(int level) {
        if (level < 0) level = 0; // I suspect we might get called with -1 in certain circumstances

        int top_left = (int) (ARC_STROKE_WIDTH / 2);
        int bottom_right = DIMEN - (int) (ARC_STROKE_WIDTH / 2);

        RectF oval = new RectF(top_left, top_left, bottom_right, bottom_right);

        canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);

        canvas.drawArc(oval, 0.0f, 360.0f, true, bg_paint);
        canvas.drawArc(oval, -90.0f, level * 360.0f / 100.0f, false, arc_paint);

        //if (android.os.Build.VERSION.SDK_INT >= 11) { // Resizeable widgets
        //    canvas.drawText(R.id.level, "" + info.percent + str.percent_symbol);
        //}
    }

    Bitmap getBitmap() {
        return bitmap;
    }

    // TODO: When do I need to call this?
    public void recycle() {
        bitmap.recycle();
    }
}
