package com.relish.app.mirror;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

public class Mirror2D_3Layer extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private static final float MAX_ZOOM = 3.5f;
    private static final float MIN_ZOOM = 3.0f;
    private static final String TAG = "DragLayout";
    public float dx = 0.0f;
    public float dy = 0.0f;
    private float lastScaleFactor = 3.01f;
    private Mode mode = Mode.NONE;
    private float prevDx = 0.0f;
    private float prevDy = 0.0f;
    public float scale = 3.01f;
    private float startX = 0.0f;
    private float startY = 0.0f;

    public enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    public Mirror2D_3Layer(Context context) {
        super(context);
        init(context);
    }

    public Mirror2D_3Layer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Mirror2D_3Layer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public Mirror2D_3Layer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context, final Mirror2D_3Layer dragLayout) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror2D_3Layer.TAG, "DOWN");
                        if (Mirror2D_3Layer.this.scale > Mirror2D_3Layer.MIN_ZOOM) {
                            Mirror2D_3Layer.this.mode = Mode.DRAG;
                            Mirror2D_3Layer.this.startX = motionEvent.getX() - Mirror2D_3Layer.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror2D_3Layer.TAG, "UP");
                        Mirror2D_3Layer.this.mode = Mode.NONE;
                        Mirror2D_3Layer mirror2D_3Layer = Mirror2D_3Layer.this;
                        mirror2D_3Layer.prevDx = mirror2D_3Layer.dx;
                        break;
                    case 2:
                        if (Mirror2D_3Layer.this.mode == Mode.DRAG) {
                            Mirror2D_3Layer.this.dx = motionEvent.getX() - Mirror2D_3Layer.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        Mirror2D_3Layer.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror2D_3Layer.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror2D_3Layer.this.mode == Mode.DRAG && Mirror2D_3Layer.this.scale >= Mirror2D_3Layer.MIN_ZOOM) || Mirror2D_3Layer.this.mode == Mode.ZOOM) {
                    Mirror2D_3Layer.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror2D_3Layer.this.child().getWidth()) - (((float) Mirror2D_3Layer.this.child().getWidth()) / Mirror2D_3Layer.this.scale)) / 2.0f) * Mirror2D_3Layer.this.scale;
                    float maxDy = ((((float) Mirror2D_3Layer.this.child().getHeight()) - (((float) Mirror2D_3Layer.this.child().getHeight()) / Mirror2D_3Layer.this.scale)) / 2.0f) * Mirror2D_3Layer.this.scale;
                    Mirror2D_3Layer mirror2D_3Layer2 = Mirror2D_3Layer.this;
                    mirror2D_3Layer2.dx = Math.min(Math.max(mirror2D_3Layer2.dx, -maxDx), maxDx);
                    Mirror2D_3Layer mirror2D_3Layer3 = Mirror2D_3Layer.this;
                    mirror2D_3Layer3.dy = Math.min(Math.max(mirror2D_3Layer3.dy, -maxDy), maxDy);
                    Log.i(Mirror2D_3Layer.TAG, "Width: " + Mirror2D_3Layer.this.child().getWidth() + ", scale " + Mirror2D_3Layer.this.scale + ", dx " + Mirror2D_3Layer.this.dx + ", max " + maxDx);
                    Mirror2D_3Layer.this.applyScaleAndTranslation();
                    dragLayout.applyScaleAndTranslation(-Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.dx, -Mirror2D_3Layer.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final Mirror2D_3Layer dragLayout, boolean vertical) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror2D_3Layer.TAG, "DOWN");
                        if (Mirror2D_3Layer.this.scale > Mirror2D_3Layer.MIN_ZOOM) {
                            Mirror2D_3Layer.this.mode = Mode.DRAG;
                            Mirror2D_3Layer.this.startY = motionEvent.getY() - Mirror2D_3Layer.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror2D_3Layer.TAG, "UP");
                        Mirror2D_3Layer.this.mode = Mode.NONE;
                        Mirror2D_3Layer mirror2D_3Layer = Mirror2D_3Layer.this;
                        mirror2D_3Layer.prevDy = mirror2D_3Layer.dy;
                        break;
                    case 2:
                        if (Mirror2D_3Layer.this.mode == Mode.DRAG) {
                            Mirror2D_3Layer.this.dy = motionEvent.getY() - Mirror2D_3Layer.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        Mirror2D_3Layer.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror2D_3Layer.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror2D_3Layer.this.mode == Mode.DRAG && Mirror2D_3Layer.this.scale >= Mirror2D_3Layer.MIN_ZOOM) || Mirror2D_3Layer.this.mode == Mode.ZOOM) {
                    Mirror2D_3Layer.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror2D_3Layer.this.child().getWidth()) - (((float) Mirror2D_3Layer.this.child().getWidth()) / Mirror2D_3Layer.this.scale)) / 2.0f) * Mirror2D_3Layer.this.scale;
                    float maxDy = ((((float) Mirror2D_3Layer.this.child().getHeight()) - (((float) Mirror2D_3Layer.this.child().getHeight()) / Mirror2D_3Layer.this.scale)) / 2.0f) * Mirror2D_3Layer.this.scale;
                    Mirror2D_3Layer mirror2D_3Layer2 = Mirror2D_3Layer.this;
                    mirror2D_3Layer2.dx = Math.min(Math.max(mirror2D_3Layer2.dx, -maxDx), maxDx);
                    Mirror2D_3Layer mirror2D_3Layer3 = Mirror2D_3Layer.this;
                    mirror2D_3Layer3.dy = Math.min(Math.max(mirror2D_3Layer3.dy, -maxDy), maxDy);
                    Log.i(Mirror2D_3Layer.TAG, "Width: " + Mirror2D_3Layer.this.child().getWidth() + ", scale " + Mirror2D_3Layer.this.scale + ", dx " + Mirror2D_3Layer.this.dx + ", max " + maxDx);
                    Mirror2D_3Layer.this.applyScaleAndTranslationVertical();
                    dragLayout.applyScaleAndTranslationVertical(Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.dx, -Mirror2D_3Layer.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final Mirror2D_3Layer dragLayout2, final Mirror2D_3Layer dragLayout3, final Mirror2D_3Layer dragLayout4) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror2D_3Layer.TAG, "DOWN");
                        if (Mirror2D_3Layer.this.scale > Mirror2D_3Layer.MIN_ZOOM) {
                            Mirror2D_3Layer.this.mode = Mode.DRAG;
                            Mirror2D_3Layer.this.startX = motionEvent.getX() - Mirror2D_3Layer.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror2D_3Layer.TAG, "UP");
                        Mirror2D_3Layer.this.mode = Mode.NONE;
                        Mirror2D_3Layer mirror2D_3Layer = Mirror2D_3Layer.this;
                        mirror2D_3Layer.prevDx = mirror2D_3Layer.dx;
                        break;
                    case 2:
                        if (Mirror2D_3Layer.this.mode == Mode.DRAG) {
                            Mirror2D_3Layer.this.dx = motionEvent.getX() - Mirror2D_3Layer.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        Mirror2D_3Layer.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror2D_3Layer.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror2D_3Layer.this.mode == Mode.DRAG && Mirror2D_3Layer.this.scale >= Mirror2D_3Layer.MIN_ZOOM) || Mirror2D_3Layer.this.mode == Mode.ZOOM) {
                    Mirror2D_3Layer.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror2D_3Layer.this.child().getWidth()) - (((float) Mirror2D_3Layer.this.child().getWidth()) / Mirror2D_3Layer.this.scale)) / 2.0f) * Mirror2D_3Layer.this.scale;
                    float maxDy = ((((float) Mirror2D_3Layer.this.child().getHeight()) - (((float) Mirror2D_3Layer.this.child().getHeight()) / Mirror2D_3Layer.this.scale)) / 2.0f) * Mirror2D_3Layer.this.scale;
                    Mirror2D_3Layer mirror2D_3Layer2 = Mirror2D_3Layer.this;
                    mirror2D_3Layer2.dx = Math.min(Math.max(mirror2D_3Layer2.dx, -maxDx), maxDx);
                    Mirror2D_3Layer mirror2D_3Layer3 = Mirror2D_3Layer.this;
                    mirror2D_3Layer3.dy = Math.min(Math.max(mirror2D_3Layer3.dy, -maxDy), maxDy);
                    Log.i(Mirror2D_3Layer.TAG, "Width: " + Mirror2D_3Layer.this.child().getWidth() + ", scale " + Mirror2D_3Layer.this.scale + ", dx " + Mirror2D_3Layer.this.dx + ", max " + maxDx);
                    Mirror2D_3Layer.this.applyScaleAndTranslation();
                    dragLayout2.applyScaleAndTranslation(-Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.scale, -Mirror2D_3Layer.this.dx, -Mirror2D_3Layer.this.dy);
                    dragLayout2.applyScaleAndTranslation(-Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.scale, -Mirror2D_3Layer.this.dx, Mirror2D_3Layer.this.dy);
                    dragLayout3.applyScaleAndTranslation(Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.dx, Mirror2D_3Layer.this.dy);
                    dragLayout4.applyScaleAndTranslation(-Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.scale, -Mirror2D_3Layer.this.dx, Mirror2D_3Layer.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final Mirror2D_3Layer dragLayout2, final Mirror2D_3Layer dragLayout3) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror2D_3Layer.TAG, "DOWN");
                        if (Mirror2D_3Layer.this.scale > Mirror2D_3Layer.MIN_ZOOM) {
                            Mirror2D_3Layer.this.mode = Mode.DRAG;
                            Mirror2D_3Layer.this.startX = motionEvent.getX() - Mirror2D_3Layer.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror2D_3Layer.TAG, "UP");
                        Mirror2D_3Layer.this.mode = Mode.NONE;
                        Mirror2D_3Layer mirror2D_3Layer = Mirror2D_3Layer.this;
                        mirror2D_3Layer.prevDx = mirror2D_3Layer.dx;
                        break;
                    case 2:
                        if (Mirror2D_3Layer.this.mode == Mode.DRAG) {
                            Mirror2D_3Layer.this.dx = motionEvent.getX() - Mirror2D_3Layer.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        Mirror2D_3Layer.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror2D_3Layer.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror2D_3Layer.this.mode == Mode.DRAG && Mirror2D_3Layer.this.scale >= Mirror2D_3Layer.MIN_ZOOM) || Mirror2D_3Layer.this.mode == Mode.ZOOM) {
                    Mirror2D_3Layer.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror2D_3Layer.this.child().getWidth()) - (((float) Mirror2D_3Layer.this.child().getWidth()) / Mirror2D_3Layer.this.scale)) / 2.0f) * Mirror2D_3Layer.this.scale;
                    float maxDy = ((((float) Mirror2D_3Layer.this.child().getHeight()) - (((float) Mirror2D_3Layer.this.child().getHeight()) / Mirror2D_3Layer.this.scale)) / 2.0f) * Mirror2D_3Layer.this.scale;
                    Mirror2D_3Layer mirror2D_3Layer2 = Mirror2D_3Layer.this;
                    mirror2D_3Layer2.dx = Math.min(Math.max(mirror2D_3Layer2.dx, -maxDx), maxDx);
                    Mirror2D_3Layer mirror2D_3Layer3 = Mirror2D_3Layer.this;
                    mirror2D_3Layer3.dy = Math.min(Math.max(mirror2D_3Layer3.dy, -maxDy), maxDy);
                    Log.i(Mirror2D_3Layer.TAG, "Width: " + Mirror2D_3Layer.this.child().getWidth() + ", scale " + Mirror2D_3Layer.this.scale + ", dx " + Mirror2D_3Layer.this.dx + ", max " + maxDx);
                    Mirror2D_3Layer.this.applyScaleAndTranslation();
                    dragLayout2.applyScaleAndTranslation(-Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.scale, -Mirror2D_3Layer.this.dx, Mirror2D_3Layer.this.dy);
                    dragLayout3.applyScaleAndTranslation(Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.scale, Mirror2D_3Layer.this.dx, Mirror2D_3Layer.this.dy);
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
                        Log.i(Mirror2D_3Layer.TAG, "DOWN");
                        if (Mirror2D_3Layer.this.scale > Mirror2D_3Layer.MIN_ZOOM) {
                            Mirror2D_3Layer.this.mode = Mode.DRAG;
                            Mirror2D_3Layer.this.startX = motionEvent.getX() - Mirror2D_3Layer.this.prevDx;
                            Mirror2D_3Layer.this.startY = motionEvent.getY() - Mirror2D_3Layer.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror2D_3Layer.TAG, "UP");
                        Mirror2D_3Layer.this.mode = Mode.NONE;
                        Mirror2D_3Layer mirror2D_3Layer = Mirror2D_3Layer.this;
                        mirror2D_3Layer.prevDx = mirror2D_3Layer.dx;
                        Mirror2D_3Layer mirror2D_3Layer2 = Mirror2D_3Layer.this;
                        mirror2D_3Layer2.prevDy = mirror2D_3Layer2.dy;
                        break;
                    case 2:
                        if (Mirror2D_3Layer.this.mode == Mode.DRAG) {
                            Mirror2D_3Layer.this.dx = motionEvent.getX() - Mirror2D_3Layer.this.startX;
                            Mirror2D_3Layer.this.dy = motionEvent.getY() - Mirror2D_3Layer.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        Mirror2D_3Layer.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror2D_3Layer.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror2D_3Layer.this.mode == Mode.DRAG && Mirror2D_3Layer.this.scale >= Mirror2D_3Layer.MIN_ZOOM) || Mirror2D_3Layer.this.mode == Mode.ZOOM) {
                    Mirror2D_3Layer.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror2D_3Layer.this.child().getWidth()) - (((float) Mirror2D_3Layer.this.child().getWidth()) / Mirror2D_3Layer.this.scale)) / 2.0f) * Mirror2D_3Layer.this.scale;
                    float maxDy = ((((float) Mirror2D_3Layer.this.child().getHeight()) - (((float) Mirror2D_3Layer.this.child().getHeight()) / Mirror2D_3Layer.this.scale)) / 2.0f) * Mirror2D_3Layer.this.scale;
                    Mirror2D_3Layer mirror2D_3Layer3 = Mirror2D_3Layer.this;
                    mirror2D_3Layer3.dx = Math.min(Math.max(mirror2D_3Layer3.dx, -maxDx), maxDx);
                    Mirror2D_3Layer mirror2D_3Layer4 = Mirror2D_3Layer.this;
                    mirror2D_3Layer4.dy = Math.min(Math.max(mirror2D_3Layer4.dy, -maxDy), maxDy);
                    Log.i(Mirror2D_3Layer.TAG, "Width: " + Mirror2D_3Layer.this.child().getWidth() + ", scale " + Mirror2D_3Layer.this.scale + ", dx " + Mirror2D_3Layer.this.dx + ", max " + maxDx);
                    Mirror2D_3Layer.this.applyScaleAndTranslation();
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
