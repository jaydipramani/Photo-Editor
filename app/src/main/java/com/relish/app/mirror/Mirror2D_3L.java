package com.relish.app.mirror;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

public class Mirror2D_3L extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private static final float MAX_ZOOM = 2.8f;
    private static final float MIN_ZOOM = 1.9f;
    private static final String TAG = "DragLayout";
    public float dx = 0.0f;
    public float dy = 0.0f;
    private float lastScaleFactor = 1.95f;
    private Mode mode = Mode.NONE;
    private float prevDx = 0.0f;
    private float prevDy = 0.0f;
    public float scale = 1.95f;
    private float startX = 0.0f;
    private float startY = 0.0f;

    
    public enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    public Mirror2D_3L(Context context) {
        super(context);
        init(context);
    }

    public Mirror2D_3L(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Mirror2D_3L(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public Mirror2D_3L(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context, final Mirror2D_3L dragLayout) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror2D_3L.TAG, "DOWN");
                        if (Mirror2D_3L.this.scale > Mirror2D_3L.MIN_ZOOM) {
                            Mirror2D_3L.this.mode = Mode.DRAG;
                            Mirror2D_3L.this.startX = motionEvent.getX() - Mirror2D_3L.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror2D_3L.TAG, "UP");
                        Mirror2D_3L.this.mode = Mode.NONE;
                        Mirror2D_3L mirror2D_3L = Mirror2D_3L.this;
                        mirror2D_3L.prevDx = mirror2D_3L.dx;
                        break;
                    case 2:
                        if (Mirror2D_3L.this.mode == Mode.DRAG) {
                            Mirror2D_3L.this.dx = motionEvent.getX() - Mirror2D_3L.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        Mirror2D_3L.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror2D_3L.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror2D_3L.this.mode == Mode.DRAG && Mirror2D_3L.this.scale >= Mirror2D_3L.MIN_ZOOM) || Mirror2D_3L.this.mode == Mode.ZOOM) {
                    Mirror2D_3L.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror2D_3L.this.child().getWidth()) - (((float) Mirror2D_3L.this.child().getWidth()) / Mirror2D_3L.this.scale)) / 2.0f) * Mirror2D_3L.this.scale;
                    float maxDy = ((((float) Mirror2D_3L.this.child().getHeight()) - (((float) Mirror2D_3L.this.child().getHeight()) / Mirror2D_3L.this.scale)) / 2.0f) * Mirror2D_3L.this.scale;
                    Mirror2D_3L mirror2D_3L2 = Mirror2D_3L.this;
                    mirror2D_3L2.dx = Math.min(Math.max(mirror2D_3L2.dx, -maxDx), maxDx);
                    Mirror2D_3L mirror2D_3L3 = Mirror2D_3L.this;
                    mirror2D_3L3.dy = Math.min(Math.max(mirror2D_3L3.dy, -maxDy), maxDy);
                    Log.i(Mirror2D_3L.TAG, "Width: " + Mirror2D_3L.this.child().getWidth() + ", scale " + Mirror2D_3L.this.scale + ", dx " + Mirror2D_3L.this.dx + ", max " + maxDx);
                    Mirror2D_3L.this.applyScaleAndTranslation();
                    dragLayout.applyScaleAndTranslation(-Mirror2D_3L.this.scale, Mirror2D_3L.this.scale, -Mirror2D_3L.this.dx, -Mirror2D_3L.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final Mirror2D_3L dragLayout, boolean vertical) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror2D_3L.TAG, "DOWN");
                        if (Mirror2D_3L.this.scale > Mirror2D_3L.MIN_ZOOM) {
                            Mirror2D_3L.this.mode = Mode.DRAG;
                            Mirror2D_3L.this.startY = motionEvent.getY() - Mirror2D_3L.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror2D_3L.TAG, "UP");
                        Mirror2D_3L.this.mode = Mode.NONE;
                        Mirror2D_3L mirror2D_3L = Mirror2D_3L.this;
                        mirror2D_3L.prevDy = mirror2D_3L.dy;
                        break;
                    case 2:
                        if (Mirror2D_3L.this.mode == Mode.DRAG) {
                            Mirror2D_3L.this.dy = motionEvent.getY() - Mirror2D_3L.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        Mirror2D_3L.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror2D_3L.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror2D_3L.this.mode == Mode.DRAG && Mirror2D_3L.this.scale >= Mirror2D_3L.MIN_ZOOM) || Mirror2D_3L.this.mode == Mode.ZOOM) {
                    Mirror2D_3L.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror2D_3L.this.child().getWidth()) - (((float) Mirror2D_3L.this.child().getWidth()) / Mirror2D_3L.this.scale)) / 2.0f) * Mirror2D_3L.this.scale;
                    float maxDy = ((((float) Mirror2D_3L.this.child().getHeight()) - (((float) Mirror2D_3L.this.child().getHeight()) / Mirror2D_3L.this.scale)) / 2.0f) * Mirror2D_3L.this.scale;
                    Mirror2D_3L mirror2D_3L2 = Mirror2D_3L.this;
                    mirror2D_3L2.dx = Math.min(Math.max(mirror2D_3L2.dx, -maxDx), maxDx);
                    Mirror2D_3L mirror2D_3L3 = Mirror2D_3L.this;
                    mirror2D_3L3.dy = Math.min(Math.max(mirror2D_3L3.dy, -maxDy), maxDy);
                    Log.i(Mirror2D_3L.TAG, "Width: " + Mirror2D_3L.this.child().getWidth() + ", scale " + Mirror2D_3L.this.scale + ", dx " + Mirror2D_3L.this.dx + ", max " + maxDx);
                    Mirror2D_3L.this.applyScaleAndTranslationVertical();
                    dragLayout.applyScaleAndTranslationVertical(Mirror2D_3L.this.scale, Mirror2D_3L.this.dx, -Mirror2D_3L.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final Mirror2D_3L dragLayout2, final Mirror2D_3L dragLayout3, final Mirror2D_3L dragLayout4) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror2D_3L.TAG, "DOWN");
                        if (Mirror2D_3L.this.scale > Mirror2D_3L.MIN_ZOOM) {
                            Mirror2D_3L.this.mode = Mode.DRAG;
                            Mirror2D_3L.this.startX = motionEvent.getX() - Mirror2D_3L.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror2D_3L.TAG, "UP");
                        Mirror2D_3L.this.mode = Mode.NONE;
                        Mirror2D_3L mirror2D_3L = Mirror2D_3L.this;
                        mirror2D_3L.prevDx = mirror2D_3L.dx;
                        break;
                    case 2:
                        if (Mirror2D_3L.this.mode == Mode.DRAG) {
                            Mirror2D_3L.this.dx = motionEvent.getX() - Mirror2D_3L.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        Mirror2D_3L.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror2D_3L.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror2D_3L.this.mode == Mode.DRAG && Mirror2D_3L.this.scale >= Mirror2D_3L.MIN_ZOOM) || Mirror2D_3L.this.mode == Mode.ZOOM) {
                    Mirror2D_3L.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror2D_3L.this.child().getWidth()) - (((float) Mirror2D_3L.this.child().getWidth()) / Mirror2D_3L.this.scale)) / 2.0f) * Mirror2D_3L.this.scale;
                    float maxDy = ((((float) Mirror2D_3L.this.child().getHeight()) - (((float) Mirror2D_3L.this.child().getHeight()) / Mirror2D_3L.this.scale)) / 2.0f) * Mirror2D_3L.this.scale;
                    Mirror2D_3L mirror2D_3L2 = Mirror2D_3L.this;
                    mirror2D_3L2.dx = Math.min(Math.max(mirror2D_3L2.dx, -maxDx), maxDx);
                    Mirror2D_3L mirror2D_3L3 = Mirror2D_3L.this;
                    mirror2D_3L3.dy = Math.min(Math.max(mirror2D_3L3.dy, -maxDy), maxDy);
                    Log.i(Mirror2D_3L.TAG, "Width: " + Mirror2D_3L.this.child().getWidth() + ", scale " + Mirror2D_3L.this.scale + ", dx " + Mirror2D_3L.this.dx + ", max " + maxDx);
                    Mirror2D_3L.this.applyScaleAndTranslation();
                    dragLayout2.applyScaleAndTranslation(-Mirror2D_3L.this.scale, Mirror2D_3L.this.scale, -Mirror2D_3L.this.dx, -Mirror2D_3L.this.dy);
                    dragLayout2.applyScaleAndTranslation(-Mirror2D_3L.this.scale, Mirror2D_3L.this.scale, -Mirror2D_3L.this.dx, Mirror2D_3L.this.dy);
                    dragLayout3.applyScaleAndTranslation(Mirror2D_3L.this.scale, Mirror2D_3L.this.scale, Mirror2D_3L.this.dx, Mirror2D_3L.this.dy);
                    dragLayout4.applyScaleAndTranslation(-Mirror2D_3L.this.scale, Mirror2D_3L.this.scale, -Mirror2D_3L.this.dx, Mirror2D_3L.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final Mirror2D_3L dragLayout2, final Mirror2D_3L dragLayout3) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror2D_3L.TAG, "DOWN");
                        if (Mirror2D_3L.this.scale > Mirror2D_3L.MIN_ZOOM) {
                            Mirror2D_3L.this.mode = Mode.DRAG;
                            Mirror2D_3L.this.startX = motionEvent.getX() - Mirror2D_3L.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror2D_3L.TAG, "UP");
                        Mirror2D_3L.this.mode = Mode.NONE;
                        Mirror2D_3L mirror2D_3L = Mirror2D_3L.this;
                        mirror2D_3L.prevDx = mirror2D_3L.dx;
                        break;
                    case 2:
                        if (Mirror2D_3L.this.mode == Mode.DRAG) {
                            Mirror2D_3L.this.dx = motionEvent.getX() - Mirror2D_3L.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        Mirror2D_3L.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror2D_3L.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror2D_3L.this.mode == Mode.DRAG && Mirror2D_3L.this.scale >= Mirror2D_3L.MIN_ZOOM) || Mirror2D_3L.this.mode == Mode.ZOOM) {
                    Mirror2D_3L.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror2D_3L.this.child().getWidth()) - (((float) Mirror2D_3L.this.child().getWidth()) / Mirror2D_3L.this.scale)) / 2.0f) * Mirror2D_3L.this.scale;
                    float maxDy = ((((float) Mirror2D_3L.this.child().getHeight()) - (((float) Mirror2D_3L.this.child().getHeight()) / Mirror2D_3L.this.scale)) / 2.0f) * Mirror2D_3L.this.scale;
                    Mirror2D_3L mirror2D_3L2 = Mirror2D_3L.this;
                    mirror2D_3L2.dx = Math.min(Math.max(mirror2D_3L2.dx, -maxDx), maxDx);
                    Mirror2D_3L mirror2D_3L3 = Mirror2D_3L.this;
                    mirror2D_3L3.dy = Math.min(Math.max(mirror2D_3L3.dy, -maxDy), maxDy);
                    Log.i(Mirror2D_3L.TAG, "Width: " + Mirror2D_3L.this.child().getWidth() + ", scale " + Mirror2D_3L.this.scale + ", dx " + Mirror2D_3L.this.dx + ", max " + maxDx);
                    Mirror2D_3L.this.applyScaleAndTranslation();
                    dragLayout2.applyScaleAndTranslation(-Mirror2D_3L.this.scale, Mirror2D_3L.this.scale, -Mirror2D_3L.this.dx, Mirror2D_3L.this.dy);
                    dragLayout3.applyScaleAndTranslation(Mirror2D_3L.this.scale, Mirror2D_3L.this.scale, Mirror2D_3L.this.dx, Mirror2D_3L.this.dy);
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
                        Log.i(Mirror2D_3L.TAG, "DOWN");
                        if (Mirror2D_3L.this.scale > Mirror2D_3L.MIN_ZOOM) {
                            Mirror2D_3L.this.mode = Mode.DRAG;
                            Mirror2D_3L.this.startX = motionEvent.getX() - Mirror2D_3L.this.prevDx;
                            Mirror2D_3L.this.startY = motionEvent.getY() - Mirror2D_3L.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror2D_3L.TAG, "UP");
                        Mirror2D_3L.this.mode = Mode.NONE;
                        Mirror2D_3L mirror2D_3L = Mirror2D_3L.this;
                        mirror2D_3L.prevDx = mirror2D_3L.dx;
                        Mirror2D_3L mirror2D_3L2 = Mirror2D_3L.this;
                        mirror2D_3L2.prevDy = mirror2D_3L2.dy;
                        break;
                    case 2:
                        if (Mirror2D_3L.this.mode == Mode.DRAG) {
                            Mirror2D_3L.this.dx = motionEvent.getX() - Mirror2D_3L.this.startX;
                            Mirror2D_3L.this.dy = motionEvent.getY() - Mirror2D_3L.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        Mirror2D_3L.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror2D_3L.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror2D_3L.this.mode == Mode.DRAG && Mirror2D_3L.this.scale >= Mirror2D_3L.MIN_ZOOM) || Mirror2D_3L.this.mode == Mode.ZOOM) {
                    Mirror2D_3L.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror2D_3L.this.child().getWidth()) - (((float) Mirror2D_3L.this.child().getWidth()) / Mirror2D_3L.this.scale)) / 2.0f) * Mirror2D_3L.this.scale;
                    float maxDy = ((((float) Mirror2D_3L.this.child().getHeight()) - (((float) Mirror2D_3L.this.child().getHeight()) / Mirror2D_3L.this.scale)) / 2.0f) * Mirror2D_3L.this.scale;
                    Mirror2D_3L mirror2D_3L3 = Mirror2D_3L.this;
                    mirror2D_3L3.dx = Math.min(Math.max(mirror2D_3L3.dx, -maxDx), maxDx);
                    Mirror2D_3L mirror2D_3L4 = Mirror2D_3L.this;
                    mirror2D_3L4.dy = Math.min(Math.max(mirror2D_3L4.dy, -maxDy), maxDy);
                    Log.i(Mirror2D_3L.TAG, "Width: " + Mirror2D_3L.this.child().getWidth() + ", scale " + Mirror2D_3L.this.scale + ", dx " + Mirror2D_3L.this.dx + ", max " + maxDx);
                    Mirror2D_3L.this.applyScaleAndTranslation();
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
            this.scale = Math.max((float) MIN_ZOOM, Math.min(f, (float) MAX_ZOOM));
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
