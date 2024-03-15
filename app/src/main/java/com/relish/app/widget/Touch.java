package com.relish.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

public class Touch extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private static final float MAX_ZOOM = 4.0f;
    private static final float MIN_ZOOM = 1.0f;
    private static final String TAG = "ZoomLayout";
    private float dx = 0.0f;
    private float dy = 0.0f;
    private boolean firstTouch = false;
    private float lastScaleFactor = 0.0f;
    private Mode mode = Mode.NONE;
    private float prevDx = 0.0f;
    private float prevDy = 0.0f;
    private boolean restore = false;
    private float scale = 1.0f;
    private float startX = 0.0f;
    private float startY = 0.0f;
    private long time = System.currentTimeMillis();

    public enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    static float access$332(Touch x0, float x1) {
        float f = x0.scale * x1;
        x0.scale = f;
        return f;
    }

    public Touch(Context context) {
        super(context);
        init(context);
    }

    public Touch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Touch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        if (Touch.this.firstTouch && System.currentTimeMillis() - Touch.this.time <= 300) {
                            if (Touch.this.restore) {
                                Touch.this.scale = 1.0f;
                                Touch.this.restore = false;
                            } else {
                                Touch.access$332(Touch.this, 2.0f);
                                Touch.this.restore = true;
                            }
                            Touch.this.mode = Mode.ZOOM;
                            Touch.this.firstTouch = false;
                            break;
                        } else {
                            if (Touch.this.scale > 1.0f) {
                                Touch.this.mode = Mode.DRAG;
                                Touch.this.startX = motionEvent.getX() - Touch.this.prevDx;
                                Touch.this.startY = motionEvent.getY() - Touch.this.prevDy;
                            }
                            Touch.this.firstTouch = true;
                            Touch.this.time = System.currentTimeMillis();
                            break;
                        }
                    case 1:
                        Log.i(Touch.TAG, "UP");
                        Touch.this.mode = Mode.NONE;
                        Touch touch = Touch.this;
                        touch.prevDx = touch.dx;
                        Touch touch2 = Touch.this;
                        touch2.prevDy = touch2.dy;
                        break;
                    case 2:
                        if (Touch.this.mode == Mode.DRAG) {
                            Touch.this.dx = motionEvent.getX() - Touch.this.startX;
                            Touch.this.dy = motionEvent.getY() - Touch.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        Touch.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Touch.this.mode = Mode.NONE;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Touch.this.mode == Mode.DRAG && Touch.this.scale >= 1.0f) || Touch.this.mode == Mode.ZOOM) {
                    Touch.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Touch.this.child().getWidth()) - (((float) Touch.this.child().getWidth()) / Touch.this.scale)) / 2.0f) * Touch.this.scale;
                    float maxDy = ((((float) Touch.this.child().getHeight()) - (((float) Touch.this.child().getHeight()) / Touch.this.scale)) / 2.0f) * Touch.this.scale;
                    Touch touch3 = Touch.this;
                    touch3.dx = Math.min(Math.max(touch3.dx, -maxDx), maxDx);
                    Touch touch4 = Touch.this;
                    touch4.dy = Math.min(Math.max(touch4.dy, -maxDy), maxDy);
                    Log.i(Touch.TAG, "Width: " + Touch.this.child().getWidth() + ", scale " + Touch.this.scale + ", dx " + Touch.this.dx + ", max " + maxDx);
                    Touch.this.applyScaleAndTranslation();
                }
                return true;
            }
        });
    }

    public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleBegin");
        return true;
    }

    public boolean onScale(ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        Log.i(TAG, "onScale" + scaleFactor);
        if (this.lastScaleFactor == 0.0f || Math.signum(scaleFactor) == Math.signum(this.lastScaleFactor)) {
            float f = this.scale * scaleFactor;
            this.scale = f;
            this.scale = Math.max(1.0f, Math.min(f, (float) MAX_ZOOM));
            this.lastScaleFactor = scaleFactor;
            return true;
        }
        this.lastScaleFactor = 0.0f;
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleEnd");
    }


    private void applyScaleAndTranslation() {
        child().setScaleX(this.scale);
        child().setScaleY(this.scale);
        child().setTranslationX(this.dx);
        child().setTranslationY(this.dy);
    }

    private View child() {
        return getChildAt(0);
    }
}
