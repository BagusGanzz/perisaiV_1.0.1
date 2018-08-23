package com.perisaimobile.animation;

import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

public class BezierTranslateAnimation extends TranslateAnimation {
    private float mBezierXDelta;
    private float mBezierYDelta;
    private float mFromXDelta;
    private int mFromXType = 0;
    private float mFromXValue = 0.0f;
    private float mFromYDelta;
    private int mFromYType = 0;
    private float mFromYValue = 0.0f;
    private float mToXDelta;
    private int mToXType = 0;
    private float mToXValue = 0.0f;
    private float mToYDelta;
    private int mToYType = 0;
    private float mToYValue = 0.0f;

    public BezierTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, float bezierXDelta, float bezierYDelta) {
        super(fromXDelta, toXDelta, fromYDelta, toYDelta);
        this.mFromXValue = fromXDelta;
        this.mToXValue = toXDelta;
        this.mFromYValue = fromYDelta;
        this.mToYValue = toYDelta;
        this.mFromXType = 0;
        this.mToXType = 0;
        this.mFromYType = 0;
        this.mToYType = 0;
        this.mBezierXDelta = bezierXDelta;
        this.mBezierYDelta = bezierYDelta;
    }

    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float dx = 0.0f;
        float dy = 0.0f;
        if (this.mFromXValue != this.mToXValue) {
            dx = (float) (((((1.0d - ((double) interpolatedTime)) * (1.0d - ((double) interpolatedTime))) * ((double) this.mFromXValue)) + (((((double) interpolatedTime) * 2.0d) * (1.0d - ((double) interpolatedTime))) * ((double) this.mBezierXDelta))) + ((double) ((interpolatedTime * interpolatedTime) * this.mToXValue)));
        }
        if (this.mFromYValue != this.mToYValue) {
            dy = (float) (((((1.0d - ((double) interpolatedTime)) * (1.0d - ((double) interpolatedTime))) * ((double) this.mFromYValue)) + (((((double) interpolatedTime) * 2.0d) * (1.0d - ((double) interpolatedTime))) * ((double) this.mBezierYDelta))) + ((double) ((interpolatedTime * interpolatedTime) * this.mToYValue)));
        }
        t.getMatrix().setTranslate(dx, dy);
    }
}
