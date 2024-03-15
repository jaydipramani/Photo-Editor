package com.relish.app.mirror;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

public class Mirror3D_5Layer extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private static final float MAX_ZOOM = 3.0f;
    private static final float MIN_ZOOM = 2.5f;
    private static final String TAG = "DragLayout";
    public float dx = 0.0f;
    public float dy = 0.0f;
    private float lastScaleFactor = 2.8f;
    private Mode mode = Mode.NONE;
    private float prevDx = 0.0f;
    private float prevDy = 0.0f;
    public float scale = 2.8f;
    private float startX = 0.0f;
    private float startY = 0.0f;

    public enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    public Mirror3D_5Layer(Context context) {
        super(context);
        init(context);
    }

    public Mirror3D_5Layer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Mirror3D_5Layer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public Mirror3D_5Layer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context, final Mirror3D_5Layer dragLayout) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror3D_5Layer.TAG, "DOWN");
                        if (Mirror3D_5Layer.this.scale > Mirror3D_5Layer.MIN_ZOOM) {
                            Mirror3D_5Layer.this.mode = Mode.DRAG;
                            Mirror3D_5Layer.this.startX = motionEvent.getX() - Mirror3D_5Layer.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror3D_5Layer.TAG, "UP");
                        Mirror3D_5Layer.this.mode = Mode.NONE;
                        Mirror3D_5Layer mirror3D_5Layer = Mirror3D_5Layer.this;
                        mirror3D_5Layer.prevDx = mirror3D_5Layer.dx;
                        break;
                    case 2:
                        if (Mirror3D_5Layer.this.mode == Mode.DRAG) {
                            Mirror3D_5Layer.this.dx = motionEvent.getX() - Mirror3D_5Layer.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        Mirror3D_5Layer.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror3D_5Layer.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror3D_5Layer.this.mode == Mode.DRAG && Mirror3D_5Layer.this.scale >= Mirror3D_5Layer.MIN_ZOOM) || Mirror3D_5Layer.this.mode == Mode.ZOOM) {
                    Mirror3D_5Layer.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror3D_5Layer.this.child().getWidth()) - (((float) Mirror3D_5Layer.this.child().getWidth()) / Mirror3D_5Layer.this.scale)) / 2.0f) * Mirror3D_5Layer.this.scale;
                    float maxDy = ((((float) Mirror3D_5Layer.this.child().getHeight()) - (((float) Mirror3D_5Layer.this.child().getHeight()) / Mirror3D_5Layer.this.scale)) / 2.0f) * Mirror3D_5Layer.this.scale;
                    Mirror3D_5Layer mirror3D_5Layer2 = Mirror3D_5Layer.this;
                    mirror3D_5Layer2.dx = Math.min(Math.max(mirror3D_5Layer2.dx, -maxDx), maxDx);
                    Mirror3D_5Layer mirror3D_5Layer3 = Mirror3D_5Layer.this;
                    mirror3D_5Layer3.dy = Math.min(Math.max(mirror3D_5Layer3.dy, -maxDy), maxDy);
                    Log.i(Mirror3D_5Layer.TAG, "Width: " + Mirror3D_5Layer.this.child().getWidth() + ", scale " + Mirror3D_5Layer.this.scale + ", dx " + Mirror3D_5Layer.this.dx + ", max " + maxDx);
                    Mirror3D_5Layer.this.applyScaleAndTranslation();
                    dragLayout.applyScaleAndTranslation(-Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.scale, -Mirror3D_5Layer.this.dx, -Mirror3D_5Layer.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final Mirror3D_5Layer dragLayout, boolean vertical) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror3D_5Layer.TAG, "DOWN");
                        if (Mirror3D_5Layer.this.scale > Mirror3D_5Layer.MIN_ZOOM) {
                            Mirror3D_5Layer.this.mode = Mode.DRAG;
                            Mirror3D_5Layer.this.startY = motionEvent.getY() - Mirror3D_5Layer.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror3D_5Layer.TAG, "UP");
                        Mirror3D_5Layer.this.mode = Mode.NONE;
                        Mirror3D_5Layer mirror3D_5Layer = Mirror3D_5Layer.this;
                        mirror3D_5Layer.prevDy = mirror3D_5Layer.dy;
                        break;
                    case 2:
                        if (Mirror3D_5Layer.this.mode == Mode.DRAG) {
                            Mirror3D_5Layer.this.dy = motionEvent.getY() - Mirror3D_5Layer.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        Mirror3D_5Layer.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror3D_5Layer.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror3D_5Layer.this.mode == Mode.DRAG && Mirror3D_5Layer.this.scale >= Mirror3D_5Layer.MIN_ZOOM) || Mirror3D_5Layer.this.mode == Mode.ZOOM) {
                    Mirror3D_5Layer.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror3D_5Layer.this.child().getWidth()) - (((float) Mirror3D_5Layer.this.child().getWidth()) / Mirror3D_5Layer.this.scale)) / 2.0f) * Mirror3D_5Layer.this.scale;
                    float maxDy = ((((float) Mirror3D_5Layer.this.child().getHeight()) - (((float) Mirror3D_5Layer.this.child().getHeight()) / Mirror3D_5Layer.this.scale)) / 2.0f) * Mirror3D_5Layer.this.scale;
                    Mirror3D_5Layer mirror3D_5Layer2 = Mirror3D_5Layer.this;
                    mirror3D_5Layer2.dx = Math.min(Math.max(mirror3D_5Layer2.dx, -maxDx), maxDx);
                    Mirror3D_5Layer mirror3D_5Layer3 = Mirror3D_5Layer.this;
                    mirror3D_5Layer3.dy = Math.min(Math.max(mirror3D_5Layer3.dy, -maxDy), maxDy);
                    Log.i(Mirror3D_5Layer.TAG, "Width: " + Mirror3D_5Layer.this.child().getWidth() + ", scale " + Mirror3D_5Layer.this.scale + ", dx " + Mirror3D_5Layer.this.dx + ", max " + maxDx);
                    Mirror3D_5Layer.this.applyScaleAndTranslationVertical();
                    dragLayout.applyScaleAndTranslationVertical(Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.dx, -Mirror3D_5Layer.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final Mirror3D_5Layer dragLayout2, final Mirror3D_5Layer dragLayout3, final Mirror3D_5Layer dragLayout4) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror3D_5Layer.TAG, "DOWN");
                        if (Mirror3D_5Layer.this.scale > Mirror3D_5Layer.MIN_ZOOM) {
                            Mirror3D_5Layer.this.mode = Mode.DRAG;
                            Mirror3D_5Layer.this.startX = motionEvent.getX() - Mirror3D_5Layer.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror3D_5Layer.TAG, "UP");
                        Mirror3D_5Layer.this.mode = Mode.NONE;
                        Mirror3D_5Layer mirror3D_5Layer = Mirror3D_5Layer.this;
                        mirror3D_5Layer.prevDx = mirror3D_5Layer.dx;
                        break;
                    case 2:
                        if (Mirror3D_5Layer.this.mode == Mode.DRAG) {
                            Mirror3D_5Layer.this.dx = motionEvent.getX() - Mirror3D_5Layer.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        Mirror3D_5Layer.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror3D_5Layer.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror3D_5Layer.this.mode == Mode.DRAG && Mirror3D_5Layer.this.scale >= Mirror3D_5Layer.MIN_ZOOM) || Mirror3D_5Layer.this.mode == Mode.ZOOM) {
                    Mirror3D_5Layer.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror3D_5Layer.this.child().getWidth()) - (((float) Mirror3D_5Layer.this.child().getWidth()) / Mirror3D_5Layer.this.scale)) / 2.0f) * Mirror3D_5Layer.this.scale;
                    float maxDy = ((((float) Mirror3D_5Layer.this.child().getHeight()) - (((float) Mirror3D_5Layer.this.child().getHeight()) / Mirror3D_5Layer.this.scale)) / 2.0f) * Mirror3D_5Layer.this.scale;
                    Mirror3D_5Layer mirror3D_5Layer2 = Mirror3D_5Layer.this;
                    mirror3D_5Layer2.dx = Math.min(Math.max(mirror3D_5Layer2.dx, -maxDx), maxDx);
                    Mirror3D_5Layer mirror3D_5Layer3 = Mirror3D_5Layer.this;
                    mirror3D_5Layer3.dy = Math.min(Math.max(mirror3D_5Layer3.dy, -maxDy), maxDy);
                    Log.i(Mirror3D_5Layer.TAG, "Width: " + Mirror3D_5Layer.this.child().getWidth() + ", scale " + Mirror3D_5Layer.this.scale + ", dx " + Mirror3D_5Layer.this.dx + ", max " + maxDx);
                    Mirror3D_5Layer.this.applyScaleAndTranslation();
                    dragLayout2.applyScaleAndTranslation(-Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.dx, Mirror3D_5Layer.this.dy);
                    dragLayout3.applyScaleAndTranslation(-Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.dx, Mirror3D_5Layer.this.dy);
                    dragLayout4.applyScaleAndTranslation(-Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.dx, Mirror3D_5Layer.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final Mirror3D_5Layer dragLayout2, final Mirror3D_5Layer dragLayout3) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(Mirror3D_5Layer.TAG, "DOWN");
                        if (Mirror3D_5Layer.this.scale > Mirror3D_5Layer.MIN_ZOOM) {
                            Mirror3D_5Layer.this.mode = Mode.DRAG;
                            Mirror3D_5Layer.this.startX = motionEvent.getX() - Mirror3D_5Layer.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror3D_5Layer.TAG, "UP");
                        Mirror3D_5Layer.this.mode = Mode.NONE;
                        Mirror3D_5Layer mirror3D_5Layer = Mirror3D_5Layer.this;
                        mirror3D_5Layer.prevDx = mirror3D_5Layer.dx;
                        break;
                    case 2:
                        if (Mirror3D_5Layer.this.mode == Mode.DRAG) {
                            Mirror3D_5Layer.this.dx = motionEvent.getX() - Mirror3D_5Layer.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        Mirror3D_5Layer.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror3D_5Layer.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror3D_5Layer.this.mode == Mode.DRAG && Mirror3D_5Layer.this.scale >= Mirror3D_5Layer.MIN_ZOOM) || Mirror3D_5Layer.this.mode == Mode.ZOOM) {
                    Mirror3D_5Layer.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror3D_5Layer.this.child().getWidth()) - (((float) Mirror3D_5Layer.this.child().getWidth()) / Mirror3D_5Layer.this.scale)) / 2.0f) * Mirror3D_5Layer.this.scale;
                    float maxDy = ((((float) Mirror3D_5Layer.this.child().getHeight()) - (((float) Mirror3D_5Layer.this.child().getHeight()) / Mirror3D_5Layer.this.scale)) / 2.0f) * Mirror3D_5Layer.this.scale;
                    Mirror3D_5Layer mirror3D_5Layer2 = Mirror3D_5Layer.this;
                    mirror3D_5Layer2.dx = Math.min(Math.max(mirror3D_5Layer2.dx, -maxDx), maxDx);
                    Mirror3D_5Layer mirror3D_5Layer3 = Mirror3D_5Layer.this;
                    mirror3D_5Layer3.dy = Math.min(Math.max(mirror3D_5Layer3.dy, -maxDy), maxDy);
                    Log.i(Mirror3D_5Layer.TAG, "Width: " + Mirror3D_5Layer.this.child().getWidth() + ", scale " + Mirror3D_5Layer.this.scale + ", dx " + Mirror3D_5Layer.this.dx + ", max " + maxDx);
                    Mirror3D_5Layer.this.applyScaleAndTranslation();
                    dragLayout2.applyScaleAndTranslation(-Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.scale, -Mirror3D_5Layer.this.dx, Mirror3D_5Layer.this.dy);
                    dragLayout3.applyScaleAndTranslation(Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.scale, Mirror3D_5Layer.this.dx, Mirror3D_5Layer.this.dy);
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
                        Log.i(Mirror3D_5Layer.TAG, "DOWN");
                        if (Mirror3D_5Layer.this.scale > Mirror3D_5Layer.MIN_ZOOM) {
                            Mirror3D_5Layer.this.mode = Mode.DRAG;
                            Mirror3D_5Layer.this.startX = motionEvent.getX() - Mirror3D_5Layer.this.prevDx;
                            Mirror3D_5Layer.this.startY = motionEvent.getY() - Mirror3D_5Layer.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(Mirror3D_5Layer.TAG, "UP");
                        Mirror3D_5Layer.this.mode = Mode.NONE;
                        Mirror3D_5Layer mirror3D_5Layer = Mirror3D_5Layer.this;
                        mirror3D_5Layer.prevDx = mirror3D_5Layer.dx;
                        Mirror3D_5Layer mirror3D_5Layer2 = Mirror3D_5Layer.this;
                        mirror3D_5Layer2.prevDy = mirror3D_5Layer2.dy;
                        break;
                    case 2:
                        if (Mirror3D_5Layer.this.mode == Mode.DRAG) {
                            Mirror3D_5Layer.this.dx = motionEvent.getX() - Mirror3D_5Layer.this.startX;
                            Mirror3D_5Layer.this.dy = motionEvent.getY() - Mirror3D_5Layer.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        Mirror3D_5Layer.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        Mirror3D_5Layer.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((Mirror3D_5Layer.this.mode == Mode.DRAG && Mirror3D_5Layer.this.scale >= Mirror3D_5Layer.MIN_ZOOM) || Mirror3D_5Layer.this.mode == Mode.ZOOM) {
                    Mirror3D_5Layer.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) Mirror3D_5Layer.this.child().getWidth()) - (((float) Mirror3D_5Layer.this.child().getWidth()) / Mirror3D_5Layer.this.scale)) / 2.0f) * Mirror3D_5Layer.this.scale;
                    float maxDy = ((((float) Mirror3D_5Layer.this.child().getHeight()) - (((float) Mirror3D_5Layer.this.child().getHeight()) / Mirror3D_5Layer.this.scale)) / 2.0f) * Mirror3D_5Layer.this.scale;
                    Mirror3D_5Layer mirror3D_5Layer3 = Mirror3D_5Layer.this;
                    mirror3D_5Layer3.dx = Math.min(Math.max(mirror3D_5Layer3.dx, -maxDx), maxDx);
                    Mirror3D_5Layer mirror3D_5Layer4 = Mirror3D_5Layer.this;
                    mirror3D_5Layer4.dy = Math.min(Math.max(mirror3D_5Layer4.dy, -maxDy), maxDy);
                    Log.i(Mirror3D_5Layer.TAG, "Width: " + Mirror3D_5Layer.this.child().getWidth() + ", scale " + Mirror3D_5Layer.this.scale + ", dx " + Mirror3D_5Layer.this.dx + ", max " + maxDx);
                    Mirror3D_5Layer.this.applyScaleAndTranslation();
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
