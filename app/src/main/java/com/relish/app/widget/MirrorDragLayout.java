package com.relish.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

public class MirrorDragLayout extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private static final float MAX_ZOOM = 2.0f;
    private static final float MIN_ZOOM = 1.35f;
    private static final String TAG = "DragLayout";
    public float dx = 0.0f;
    public float dy = 0.0f;
    private float lastScaleFactor = 1.4f;
    private Mode mode = Mode.NONE;
    private float prevDx = 0.0f;
    private float prevDy = 0.0f;
    public float scale = 1.4f;
    private float startX = 0.0f;
    private float startY = 0.0f;


    public enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    public MirrorDragLayout(Context context) {
        super(context);
        init(context);
    }

    public MirrorDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public MirrorDragLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public MirrorDragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context, final MirrorDragLayout dragLayout) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {
            /* class com.photoz.photoeditor.pro.widget.MirrorDragLayout.AnonymousClass1 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(MirrorDragLayout.TAG, "DOWN");
                        if (MirrorDragLayout.this.scale > MirrorDragLayout.MIN_ZOOM) {
                            MirrorDragLayout.this.mode = Mode.DRAG;
                            MirrorDragLayout.this.startX = motionEvent.getX() - MirrorDragLayout.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(MirrorDragLayout.TAG, "UP");
                        MirrorDragLayout.this.mode = Mode.NONE;
                        MirrorDragLayout mirrorDragLayout = MirrorDragLayout.this;
                        mirrorDragLayout.prevDx = mirrorDragLayout.dx;
                        break;
                    case 2:
                        if (MirrorDragLayout.this.mode == Mode.DRAG) {
                            MirrorDragLayout.this.dx = motionEvent.getX() - MirrorDragLayout.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        MirrorDragLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        MirrorDragLayout.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((MirrorDragLayout.this.mode == Mode.DRAG && MirrorDragLayout.this.scale >= MirrorDragLayout.MIN_ZOOM) || MirrorDragLayout.this.mode == Mode.ZOOM) {
                    MirrorDragLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) MirrorDragLayout.this.child().getWidth()) - (((float) MirrorDragLayout.this.child().getWidth()) / MirrorDragLayout.this.scale)) / MirrorDragLayout.MAX_ZOOM) * MirrorDragLayout.this.scale;
                    float maxDy = ((((float) MirrorDragLayout.this.child().getHeight()) - (((float) MirrorDragLayout.this.child().getHeight()) / MirrorDragLayout.this.scale)) / MirrorDragLayout.MAX_ZOOM) * MirrorDragLayout.this.scale;
                    MirrorDragLayout mirrorDragLayout2 = MirrorDragLayout.this;
                    mirrorDragLayout2.dx = Math.min(Math.max(mirrorDragLayout2.dx, -maxDx), maxDx);
                    MirrorDragLayout mirrorDragLayout3 = MirrorDragLayout.this;
                    mirrorDragLayout3.dy = Math.min(Math.max(mirrorDragLayout3.dy, -maxDy), maxDy);
                    Log.i(MirrorDragLayout.TAG, "Width: " + MirrorDragLayout.this.child().getWidth() + ", scale " + MirrorDragLayout.this.scale + ", dx " + MirrorDragLayout.this.dx + ", max " + maxDx);
                    MirrorDragLayout.this.applyScaleAndTranslation();
                    dragLayout.applyScaleAndTranslation(-MirrorDragLayout.this.scale, MirrorDragLayout.this.scale, -MirrorDragLayout.this.dx, -MirrorDragLayout.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final MirrorDragLayout dragLayout, boolean vertical) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {
            /* class com.photoz.photoeditor.pro.widget.MirrorDragLayout.AnonymousClass2 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(MirrorDragLayout.TAG, "DOWN");
                        if (MirrorDragLayout.this.scale > MirrorDragLayout.MIN_ZOOM) {
                            MirrorDragLayout.this.mode = Mode.DRAG;
                            MirrorDragLayout.this.startY = motionEvent.getY() - MirrorDragLayout.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(MirrorDragLayout.TAG, "UP");
                        MirrorDragLayout.this.mode = Mode.NONE;
                        MirrorDragLayout mirrorDragLayout = MirrorDragLayout.this;
                        mirrorDragLayout.prevDy = mirrorDragLayout.dy;
                        break;
                    case 2:
                        if (MirrorDragLayout.this.mode == Mode.DRAG) {
                            MirrorDragLayout.this.dy = motionEvent.getY() - MirrorDragLayout.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        MirrorDragLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        MirrorDragLayout.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((MirrorDragLayout.this.mode == Mode.DRAG && MirrorDragLayout.this.scale >= MirrorDragLayout.MIN_ZOOM) || MirrorDragLayout.this.mode == Mode.ZOOM) {
                    MirrorDragLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) MirrorDragLayout.this.child().getWidth()) - (((float) MirrorDragLayout.this.child().getWidth()) / MirrorDragLayout.this.scale)) / MirrorDragLayout.MAX_ZOOM) * MirrorDragLayout.this.scale;
                    float maxDy = ((((float) MirrorDragLayout.this.child().getHeight()) - (((float) MirrorDragLayout.this.child().getHeight()) / MirrorDragLayout.this.scale)) / MirrorDragLayout.MAX_ZOOM) * MirrorDragLayout.this.scale;
                    MirrorDragLayout mirrorDragLayout2 = MirrorDragLayout.this;
                    mirrorDragLayout2.dx = Math.min(Math.max(mirrorDragLayout2.dx, -maxDx), maxDx);
                    MirrorDragLayout mirrorDragLayout3 = MirrorDragLayout.this;
                    mirrorDragLayout3.dy = Math.min(Math.max(mirrorDragLayout3.dy, -maxDy), maxDy);
                    Log.i(MirrorDragLayout.TAG, "Width: " + MirrorDragLayout.this.child().getWidth() + ", scale " + MirrorDragLayout.this.scale + ", dx " + MirrorDragLayout.this.dx + ", max " + maxDx);
                    MirrorDragLayout.this.applyScaleAndTranslationVertical();
                    dragLayout.applyScaleAndTranslationVertical(MirrorDragLayout.this.scale, MirrorDragLayout.this.dx, -MirrorDragLayout.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final MirrorDragLayout dragLayout2, final MirrorDragLayout dragLayout3, final MirrorDragLayout dragLayout4) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {
            /* class com.photoz.photoeditor.pro.widget.MirrorDragLayout.AnonymousClass3 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(MirrorDragLayout.TAG, "DOWN");
                        if (MirrorDragLayout.this.scale > MirrorDragLayout.MIN_ZOOM) {
                            MirrorDragLayout.this.mode = Mode.DRAG;
                            MirrorDragLayout.this.startX = motionEvent.getX() - MirrorDragLayout.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(MirrorDragLayout.TAG, "UP");
                        MirrorDragLayout.this.mode = Mode.NONE;
                        MirrorDragLayout mirrorDragLayout = MirrorDragLayout.this;
                        mirrorDragLayout.prevDx = mirrorDragLayout.dx;
                        break;
                    case 2:
                        if (MirrorDragLayout.this.mode == Mode.DRAG) {
                            MirrorDragLayout.this.dx = motionEvent.getX() - MirrorDragLayout.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        MirrorDragLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        MirrorDragLayout.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((MirrorDragLayout.this.mode == Mode.DRAG && MirrorDragLayout.this.scale >= MirrorDragLayout.MIN_ZOOM) || MirrorDragLayout.this.mode == Mode.ZOOM) {
                    MirrorDragLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) MirrorDragLayout.this.child().getWidth()) - (((float) MirrorDragLayout.this.child().getWidth()) / MirrorDragLayout.this.scale)) / MirrorDragLayout.MAX_ZOOM) * MirrorDragLayout.this.scale;
                    float maxDy = ((((float) MirrorDragLayout.this.child().getHeight()) - (((float) MirrorDragLayout.this.child().getHeight()) / MirrorDragLayout.this.scale)) / MirrorDragLayout.MAX_ZOOM) * MirrorDragLayout.this.scale;
                    MirrorDragLayout mirrorDragLayout2 = MirrorDragLayout.this;
                    mirrorDragLayout2.dx = Math.min(Math.max(mirrorDragLayout2.dx, -maxDx), maxDx);
                    MirrorDragLayout mirrorDragLayout3 = MirrorDragLayout.this;
                    mirrorDragLayout3.dy = Math.min(Math.max(mirrorDragLayout3.dy, -maxDy), maxDy);
                    Log.i(MirrorDragLayout.TAG, "Width: " + MirrorDragLayout.this.child().getWidth() + ", scale " + MirrorDragLayout.this.scale + ", dx " + MirrorDragLayout.this.dx + ", max " + maxDx);
                    MirrorDragLayout.this.applyScaleAndTranslation();
                    dragLayout2.applyScaleAndTranslation(-MirrorDragLayout.this.scale, MirrorDragLayout.this.scale, -MirrorDragLayout.this.dx, MirrorDragLayout.this.dy);
                    dragLayout3.applyScaleAndTranslation(MirrorDragLayout.this.scale, MirrorDragLayout.this.scale, MirrorDragLayout.this.dx, MirrorDragLayout.this.dy);
                    dragLayout4.applyScaleAndTranslation(-MirrorDragLayout.this.scale, MirrorDragLayout.this.scale, -MirrorDragLayout.this.dx, MirrorDragLayout.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context, final MirrorDragLayout dragLayout2, final MirrorDragLayout dragLayout3) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {
            /* class com.photoz.photoeditor.pro.widget.MirrorDragLayout.AnonymousClass4 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(MirrorDragLayout.TAG, "DOWN");
                        if (MirrorDragLayout.this.scale > MirrorDragLayout.MIN_ZOOM) {
                            MirrorDragLayout.this.mode = Mode.DRAG;
                            MirrorDragLayout.this.startX = motionEvent.getX() - MirrorDragLayout.this.prevDx;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(MirrorDragLayout.TAG, "UP");
                        MirrorDragLayout.this.mode = Mode.NONE;
                        MirrorDragLayout mirrorDragLayout = MirrorDragLayout.this;
                        mirrorDragLayout.prevDx = mirrorDragLayout.dx;
                        break;
                    case 2:
                        if (MirrorDragLayout.this.mode == Mode.DRAG) {
                            MirrorDragLayout.this.dx = motionEvent.getX() - MirrorDragLayout.this.startX;
                            break;
                        }
                        break;
                    case 5:
                        MirrorDragLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        MirrorDragLayout.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((MirrorDragLayout.this.mode == Mode.DRAG && MirrorDragLayout.this.scale >= MirrorDragLayout.MIN_ZOOM) || MirrorDragLayout.this.mode == Mode.ZOOM) {
                    MirrorDragLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) MirrorDragLayout.this.child().getWidth()) - (((float) MirrorDragLayout.this.child().getWidth()) / MirrorDragLayout.this.scale)) / MirrorDragLayout.MAX_ZOOM) * MirrorDragLayout.this.scale;
                    float maxDy = ((((float) MirrorDragLayout.this.child().getHeight()) - (((float) MirrorDragLayout.this.child().getHeight()) / MirrorDragLayout.this.scale)) / MirrorDragLayout.MAX_ZOOM) * MirrorDragLayout.this.scale;
                    MirrorDragLayout mirrorDragLayout2 = MirrorDragLayout.this;
                    mirrorDragLayout2.dx = Math.min(Math.max(mirrorDragLayout2.dx, -maxDx), maxDx);
                    MirrorDragLayout mirrorDragLayout3 = MirrorDragLayout.this;
                    mirrorDragLayout3.dy = Math.min(Math.max(mirrorDragLayout3.dy, -maxDy), maxDy);
                    Log.i(MirrorDragLayout.TAG, "Width: " + MirrorDragLayout.this.child().getWidth() + ", scale " + MirrorDragLayout.this.scale + ", dx " + MirrorDragLayout.this.dx + ", max " + maxDx);
                    MirrorDragLayout.this.applyScaleAndTranslation();
                    dragLayout2.applyScaleAndTranslation(-MirrorDragLayout.this.scale, MirrorDragLayout.this.scale, -MirrorDragLayout.this.dx, MirrorDragLayout.this.dy);
                    dragLayout3.applyScaleAndTranslation(MirrorDragLayout.this.scale, MirrorDragLayout.this.scale, MirrorDragLayout.this.dx, MirrorDragLayout.this.dy);
                }
                return true;
            }
        });
    }

    public void init(Context context) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {
            /* class com.photoz.photoeditor.pro.widget.MirrorDragLayout.AnonymousClass5 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(MirrorDragLayout.TAG, "DOWN");
                        if (MirrorDragLayout.this.scale > MirrorDragLayout.MIN_ZOOM) {
                            MirrorDragLayout.this.mode = Mode.DRAG;
                            MirrorDragLayout.this.startX = motionEvent.getX() - MirrorDragLayout.this.prevDx;
                            MirrorDragLayout.this.startY = motionEvent.getY() - MirrorDragLayout.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(MirrorDragLayout.TAG, "UP");
                        MirrorDragLayout.this.mode = Mode.NONE;
                        MirrorDragLayout mirrorDragLayout = MirrorDragLayout.this;
                        mirrorDragLayout.prevDx = mirrorDragLayout.dx;
                        MirrorDragLayout mirrorDragLayout2 = MirrorDragLayout.this;
                        mirrorDragLayout2.prevDy = mirrorDragLayout2.dy;
                        break;
                    case 2:
                        if (MirrorDragLayout.this.mode == Mode.DRAG) {
                            MirrorDragLayout.this.dx = motionEvent.getX() - MirrorDragLayout.this.startX;
                            MirrorDragLayout.this.dy = motionEvent.getY() - MirrorDragLayout.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        MirrorDragLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        MirrorDragLayout.this.mode = Mode.DRAG;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((MirrorDragLayout.this.mode == Mode.DRAG && MirrorDragLayout.this.scale >= MirrorDragLayout.MIN_ZOOM) || MirrorDragLayout.this.mode == Mode.ZOOM) {
                    MirrorDragLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) MirrorDragLayout.this.child().getWidth()) - (((float) MirrorDragLayout.this.child().getWidth()) / MirrorDragLayout.this.scale)) / MirrorDragLayout.MAX_ZOOM) * MirrorDragLayout.this.scale;
                    float maxDy = ((((float) MirrorDragLayout.this.child().getHeight()) - (((float) MirrorDragLayout.this.child().getHeight()) / MirrorDragLayout.this.scale)) / MirrorDragLayout.MAX_ZOOM) * MirrorDragLayout.this.scale;
                    MirrorDragLayout mirrorDragLayout3 = MirrorDragLayout.this;
                    mirrorDragLayout3.dx = Math.min(Math.max(mirrorDragLayout3.dx, -maxDx), maxDx);
                    MirrorDragLayout mirrorDragLayout4 = MirrorDragLayout.this;
                    mirrorDragLayout4.dy = Math.min(Math.max(mirrorDragLayout4.dy, -maxDy), maxDy);
                    Log.i(MirrorDragLayout.TAG, "Width: " + MirrorDragLayout.this.child().getWidth() + ", scale " + MirrorDragLayout.this.scale + ", dx " + MirrorDragLayout.this.dx + ", max " + maxDx);
                    MirrorDragLayout.this.applyScaleAndTranslation();
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
