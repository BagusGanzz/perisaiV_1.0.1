package com.perisaimobile.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.studioninja.locker.R;


public class ScanningProgress extends FrameLayout {
    private Context context;
    private ImageView imageView;
    private int mProgress;
    private int mWaveToTop;
    private Paint paint;

    public ScanningProgress(Context context) {
        super(context);
        init(context);
    }

    public ScanningProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScanningProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.paint = new Paint();
        this.context = context;
        this.imageView = new ImageView(context);
        this.imageView.setImageResource(R.mipmap.bg4);
        this.mWaveToTop = (int) (((float) getHeight()) * 1.0f);
        addView(this.imageView, new LayoutParams(-1, -2, 48));
    }

    public void setProgress(int progress) {
        if (progress > 100) {
            progress = 100;
        }
        this.mProgress = progress;
        computeWaveToTop();
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            computeWaveToTop();
        }
    }

    private void computeWaveToTop() {
        this.mWaveToTop = (int) (((float) getHeight()) * (1.0f - (((float) this.mProgress) / 100.0f)));
        ViewGroup.LayoutParams params = this.imageView.getLayoutParams();
        if (params != null) {
            ((LayoutParams) params).topMargin = this.mWaveToTop;
        }
        this.imageView.setLayoutParams(params);
    }

    public float getYProgress() {
        int[] location = new int[2];
        this.imageView.getLocationOnScreen(location);
        return (float) (location[1] - this.imageView.getHeight());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
