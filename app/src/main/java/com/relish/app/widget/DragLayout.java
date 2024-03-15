package com.relish.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

public class DragLayout extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private static final float MAX_ZOOM = 1.8f;
    private static final float MIN_ZOOM = 1.0f;
    private static final String TAG = "DragLayout";
    public float dx = 0.0f;
    public float dy = 0.0f;
    private float lastScaleFactor = 1.6f;
    private Mode mode = Mode.NONE;
    private float prevDx = 0.0f;
    private float prevDy = 0.0f;
    public float scale = 1.6f;
    private float startX = 0.0f;
    private float startY = 0.0f;

    public enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    public DragLayout(Context context) {
        super(context);
        init(context);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public DragLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context, final DragLayout dragLayout) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {


            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(DragLayout.TAG, "DOWN");
                        if (DragLayout.this.scale > 1.0f) {
                            DragLayout.this.mode = Mode.DRAG;
                            DragLayout.this.startX = motionEvent.getX() - DragLayout.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(DragLayout.TAG, "UP");
                        DragLayout.this.mode = Mode.NONE;
                        DragLayout dragLayout = DragLayout.this;
                        dragLayout.prevDx = dragLayout.dx;
                        break;
                    case 2:
                        if (DragLayout.this.mode == Mode.DRAG) {
                            DragLayout.this.dx = motionEvent.getX() - DragLayout.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        DragLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        DragLayout.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((DragLayout.this.mode == Mode.DRAG && DragLayout.this.scale >= 1.0f) || DragLayout.this.mode == Mode.ZOOM) {
                    DragLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) DragLayout.this.child().getWidth()) - (((float) DragLayout.this.child().getWidth()) / DragLayout.this.scale)) / 2.0f) * DragLayout.this.scale;
                    float maxDy = ((((float) DragLayout.this.child().getHeight()) - (((float) DragLayout.this.child().getHeight()) / DragLayout.this.scale)) / 2.0f) * DragLayout.this.scale;
                    DragLayout dragLayout2 = DragLayout.this;
                    dragLayout2.dx = Math.min(Math.max(dragLayout2.dx, -maxDx), maxDx);
                    DragLayout dragLayout3 = DragLayout.this;
                    dragLayout3.dy = Math.min(Math.max(dragLayout3.dy, -maxDy), maxDy);
                    Log.i(DragLayout.TAG, "Width: " + DragLayout.this.child().getWidth() + ", scale " + DragLayout.this.scale + ", dx " + DragLayout.this.dx + ", max " + maxDx);
                    DragLayout.this.applyScaleAndTranslation();
                    dragLayout.applyScaleAndTranslation(-DragLayout.this.scale, DragLayout.this.scale, -DragLayout.this.dx, DragLayout.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final DragLayout dragLayout, boolean vertical) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(DragLayout.TAG, "DOWN");
                        if (DragLayout.this.scale > 1.0f) {
                            DragLayout.this.mode = Mode.DRAG;
                            DragLayout.this.startY = motionEvent.getY() - DragLayout.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(DragLayout.TAG, "UP");
                        DragLayout.this.mode = Mode.NONE;
                        DragLayout dragLayout = DragLayout.this;
                        dragLayout.prevDy = dragLayout.dy;
                        break;
                    case 2:
                        if (DragLayout.this.mode == Mode.DRAG) {
                            DragLayout.this.dy = motionEvent.getY() - DragLayout.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        DragLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        DragLayout.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((DragLayout.this.mode == Mode.DRAG && DragLayout.this.scale >= 1.0f) || DragLayout.this.mode == Mode.ZOOM) {
                    DragLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) DragLayout.this.child().getWidth()) - (((float) DragLayout.this.child().getWidth()) / DragLayout.this.scale)) / 2.0f) * DragLayout.this.scale;
                    float maxDy = ((((float) DragLayout.this.child().getHeight()) - (((float) DragLayout.this.child().getHeight()) / DragLayout.this.scale)) / 2.0f) * DragLayout.this.scale;
                    DragLayout dragLayout2 = DragLayout.this;
                    dragLayout2.dx = Math.min(Math.max(dragLayout2.dx, -maxDx), maxDx);
                    DragLayout dragLayout3 = DragLayout.this;
                    dragLayout3.dy = Math.min(Math.max(dragLayout3.dy, -maxDy), maxDy);
                    Log.i(DragLayout.TAG, "Width: " + DragLayout.this.child().getWidth() + ", scale " + DragLayout.this.scale + ", dx " + DragLayout.this.dx + ", max " + maxDx);
                    DragLayout.this.applyScaleAndTranslationVertical();
                    dragLayout.applyScaleAndTranslationVertical(DragLayout.this.scale, DragLayout.this.dx, -DragLayout.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final DragLayout dragLayout2, final DragLayout dragLayout3, final DragLayout dragLayout4) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(DragLayout.TAG, "DOWN");
                        if (DragLayout.this.scale > 1.0f) {
                            DragLayout.this.mode = Mode.DRAG;
                            DragLayout.this.startX = motionEvent.getX() - DragLayout.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(DragLayout.TAG, "UP");
                        DragLayout.this.mode = Mode.NONE;
                        DragLayout dragLayout = DragLayout.this;
                        dragLayout.prevDx = dragLayout.dx;
                        break;
                    case 2:
                        if (DragLayout.this.mode == Mode.DRAG) {
                            DragLayout.this.dx = motionEvent.getX() - DragLayout.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        DragLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        DragLayout.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((DragLayout.this.mode == Mode.DRAG && DragLayout.this.scale >= 1.0f) || DragLayout.this.mode == Mode.ZOOM) {
                    DragLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) DragLayout.this.child().getWidth()) - (((float) DragLayout.this.child().getWidth()) / DragLayout.this.scale)) / 2.0f) * DragLayout.this.scale;
                    float maxDy = ((((float) DragLayout.this.child().getHeight()) - (((float) DragLayout.this.child().getHeight()) / DragLayout.this.scale)) / 2.0f) * DragLayout.this.scale;
                    DragLayout dragLayout2 = DragLayout.this;
                    dragLayout2.dx = Math.min(Math.max(dragLayout2.dx, -maxDx), maxDx);
                    DragLayout dragLayout3 = DragLayout.this;
                    dragLayout3.dy = Math.min(Math.max(dragLayout3.dy, -maxDy), maxDy);
                    Log.i(DragLayout.TAG, "Width: " + DragLayout.this.child().getWidth() + ", scale " + DragLayout.this.scale + ", dx " + DragLayout.this.dx + ", max " + maxDx);
                    DragLayout.this.applyScaleAndTranslation();
                    dragLayout2.applyScaleAndTranslation(-DragLayout.this.scale, DragLayout.this.scale, -DragLayout.this.dx, DragLayout.this.dy);
                    dragLayout3.applyScaleAndTranslation(DragLayout.this.scale, DragLayout.this.scale, DragLayout.this.dx, DragLayout.this.dy);
                    dragLayout4.applyScaleAndTranslation(-DragLayout.this.scale, DragLayout.this.scale, -DragLayout.this.dx, DragLayout.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final DragLayout dragLayout2, final DragLayout dragLayout3) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(DragLayout.TAG, "DOWN");
                        if (DragLayout.this.scale > 1.0f) {
                            DragLayout.this.mode = Mode.DRAG;
                            DragLayout.this.startX = motionEvent.getX() - DragLayout.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(DragLayout.TAG, "UP");
                        DragLayout.this.mode = Mode.NONE;
                        DragLayout dragLayout = DragLayout.this;
                        dragLayout.prevDx = dragLayout.dx;
                        break;
                    case 2:
                        if (DragLayout.this.mode == Mode.DRAG) {
                            DragLayout.this.dx = motionEvent.getX() - DragLayout.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        DragLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        DragLayout.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((DragLayout.this.mode == Mode.DRAG && DragLayout.this.scale >= 1.0f) || DragLayout.this.mode == Mode.ZOOM) {
                    DragLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) DragLayout.this.child().getWidth()) - (((float) DragLayout.this.child().getWidth()) / DragLayout.this.scale)) / 2.0f) * DragLayout.this.scale;
                    float maxDy = ((((float) DragLayout.this.child().getHeight()) - (((float) DragLayout.this.child().getHeight()) / DragLayout.this.scale)) / 2.0f) * DragLayout.this.scale;
                    DragLayout dragLayout2 = DragLayout.this;
                    dragLayout2.dx = Math.min(Math.max(dragLayout2.dx, -maxDx), maxDx);
                    DragLayout dragLayout3 = DragLayout.this;
                    dragLayout3.dy = Math.min(Math.max(dragLayout3.dy, -maxDy), maxDy);
                    Log.i(DragLayout.TAG, "Width: " + DragLayout.this.child().getWidth() + ", scale " + DragLayout.this.scale + ", dx " + DragLayout.this.dx + ", max " + maxDx);
                    DragLayout.this.applyScaleAndTranslation();
                    dragLayout2.applyScaleAndTranslation(-DragLayout.this.scale, DragLayout.this.scale, -DragLayout.this.dx, DragLayout.this.dy);
                    dragLayout3.applyScaleAndTranslation(DragLayout.this.scale, DragLayout.this.scale, DragLayout.this.dx, DragLayout.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(DragLayout.TAG, "DOWN");
                        if (DragLayout.this.scale > 1.0f) {
                            DragLayout.this.mode = Mode.DRAG;
                            DragLayout.this.startX = motionEvent.getX() - DragLayout.this.prevDx;
                            DragLayout.this.startY = motionEvent.getY() - DragLayout.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(DragLayout.TAG, "UP");
                        DragLayout.this.mode = Mode.NONE;
                        DragLayout dragLayout = DragLayout.this;
                        dragLayout.prevDx = dragLayout.dx;
                        DragLayout dragLayout2 = DragLayout.this;
                        dragLayout2.prevDy = dragLayout2.dy;
                        break;
                    case 2:
                        if (DragLayout.this.mode == Mode.DRAG) {
                            DragLayout.this.dx = motionEvent.getX() - DragLayout.this.startX;
                            DragLayout.this.dy = motionEvent.getY() - DragLayout.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        DragLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        DragLayout.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((DragLayout.this.mode == Mode.DRAG && DragLayout.this.scale >= 1.0f) || DragLayout.this.mode == Mode.ZOOM) {
                    DragLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) DragLayout.this.child().getWidth()) - (((float) DragLayout.this.child().getWidth()) / DragLayout.this.scale)) / 2.0f) * DragLayout.this.scale;
                    float maxDy = ((((float) DragLayout.this.child().getHeight()) - (((float) DragLayout.this.child().getHeight()) / DragLayout.this.scale)) / 2.0f) * DragLayout.this.scale;
                    DragLayout dragLayout3 = DragLayout.this;
                    dragLayout3.dx = Math.min(Math.max(dragLayout3.dx, -maxDx), maxDx);
                    DragLayout dragLayout4 = DragLayout.this;
                    dragLayout4.dy = Math.min(Math.max(dragLayout4.dy, -maxDy), maxDy);
                    Log.i(DragLayout.TAG, "Width: " + DragLayout.this.child().getWidth() + ", scale " + DragLayout.this.scale + ", dx " + DragLayout.this.dx + ", max " + maxDx);
                    DragLayout.this.applyScaleAndTranslation();
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

    public void applyScaleAndTranslation() {
        child().setScaleX(this.scale);
        child().setScaleY(this.scale);
        child().setTranslationX(this.dx);
        child().setTranslationY(this.dy);
    }

    public void applyScaleAndTranslationVertical() {
        child().setScaleX(this.scale);
        child().setScaleY(this.scale);
        child().setTranslationX(this.dx);
        child().setTranslationY(this.dy);
    }

    public void applyScaleAndTranslation(float scalex, float scaley, float dx2, float dy2) {
        child().setScaleX(scalex);
        child().setScaleY(scaley);
        child().setTranslationX(dx2);
        child().setTranslationY(dy2);
    }

    public void applyScaleAndTranslationVertical(float scale2, float dx2, float dy2) {
        child().setScaleX(-scale2);
        child().setScaleY(scale2);
        child().setTranslationX(dx2);
        child().setTranslationY(dy2);
    }

    private View child() {
        return getChildAt(0);
    }
}
