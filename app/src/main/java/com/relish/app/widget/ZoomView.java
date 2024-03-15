package com.relish.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

public class ZoomView extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private static final float MAX_ZOOM = 4.0f;
    private static final float MIN_ZOOM = 1.0f;
    private static final String TAG = "ZoomLayout";
    private float dx = 0.0f;
    private float dy = 0.0f;
    private boolean firstTouch = false;
    private float lastScaleFactor = 1.8f;
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

    static  float access$332(ZoomView x0, float x1) {
        float f = x0.scale * x1;
        x0.scale = f;
        return f;
    }

    public ZoomView(Context context) {
        super(context);
        init(context);
    }

    public ZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        if (ZoomView.this.firstTouch && System.currentTimeMillis() - ZoomView.this.time <= 300) {
                            if (ZoomView.this.restore) {
                                ZoomView.this.scale = 1.0f;
                                ZoomView.this.restore = false;
                            } else {
                                ZoomView.access$332(ZoomView.this, 2.0f);
                                ZoomView.this.restore = true;
                            }
                            ZoomView.this.mode = Mode.ZOOM;
                            ZoomView.this.firstTouch = false;
                            break;
                        } else {
                            if (ZoomView.this.scale > 1.0f) {
                                ZoomView.this.mode = Mode.DRAG;
                                ZoomView.this.startX = motionEvent.getX() - ZoomView.this.prevDx;
                                ZoomView.this.startY = motionEvent.getY() - ZoomView.this.prevDy;
                            }
                            ZoomView.this.firstTouch = true;
                            ZoomView.this.time = System.currentTimeMillis();
                            break;
                        }
                    case 1:
                        Log.i(ZoomView.TAG, "UP");
                        ZoomView.this.mode = Mode.NONE;
                        ZoomView zoomView = ZoomView.this;
                        zoomView.prevDx = zoomView.dx;
                        ZoomView zoomView2 = ZoomView.this;
                        zoomView2.prevDy = zoomView2.dy;
                        break;
                    case 2:
                        if (ZoomView.this.mode == Mode.DRAG) {
                            ZoomView.this.dx = motionEvent.getX() - ZoomView.this.startX;
                            ZoomView.this.dy = motionEvent.getY() - ZoomView.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        ZoomView.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        ZoomView.this.mode = Mode.NONE;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((ZoomView.this.mode == Mode.DRAG && ZoomView.this.scale >= 1.0f) || ZoomView.this.mode == Mode.ZOOM) {
                    ZoomView.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) ZoomView.this.child().getWidth()) - (((float) ZoomView.this.child().getWidth()) / ZoomView.this.scale)) / 2.0f) * ZoomView.this.scale;
                    float maxDy = ((((float) ZoomView.this.child().getHeight()) - (((float) ZoomView.this.child().getHeight()) / ZoomView.this.scale)) / 2.0f) * ZoomView.this.scale;
                    ZoomView zoomView3 = ZoomView.this;
                    zoomView3.dx = Math.min(Math.max(zoomView3.dx, -maxDx), maxDx);
                    ZoomView zoomView4 = ZoomView.this;
                    zoomView4.dy = Math.min(Math.max(zoomView4.dy, -maxDy), maxDy);
                    Log.i(ZoomView.TAG, "Width: " + ZoomView.this.child().getWidth() + ", scale " + ZoomView.this.scale + ", dx " + ZoomView.this.dx + ", max " + maxDx);
                    ZoomView.this.applyScaleAndTranslation();
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
