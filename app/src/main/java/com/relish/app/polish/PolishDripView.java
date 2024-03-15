package com.relish.app.polish;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.relish.app.drip.imagescale.TouchListener;


public class PolishDripView extends AppCompatImageView {
    TouchListener multiTouchListener;

    public PolishDripView(Context context) {
        this(context, null);
    }

    public PolishDripView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        setPadding(0, 0, 0, 0);
    }

    public PolishDripView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.multiTouchListener = null;
        initBorderPaint();
    }

    private void initBorderPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(-1);
        paint.setStrokeWidth(0.0f);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setOnTouchListenerCustom(TouchListener multiTouchListener2) {
        this.multiTouchListener = multiTouchListener2;
        setOnTouchListener(multiTouchListener2);
    }
}
