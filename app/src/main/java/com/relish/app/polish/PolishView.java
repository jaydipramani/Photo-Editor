package com.relish.app.polish;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_1_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_1_UP;
import static android.view.MotionEvent.ACTION_UP;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.relish.app.R;
import com.relish.app.draw.BrushDrawingView;
import com.relish.app.draw.FilterImageView;
import com.relish.app.draw.OnSaveBitmap;

import org.wysaid.view.ImageGLSurfaceView;

import java.util.ArrayList;
import java.util.List;

public class PolishView extends PolishStickerView implements ScaleGestureDetector.OnScaleGestureListener {
    private static final float MAX_ZOOM = 4.0f;
    private static final float MIN_ZOOM = 1.0f;
    private static final String TAG = "ZoomLayout";
    private List<Bitmap> bitmaplist = new ArrayList();
    private BrushDrawingView brushDrawingView;
    private Bitmap currentBitmap;
    private float dx = 0.0f;
    private float dy = 0.0f;
    private FilterImageView filterImageView;
    private boolean firstTouch = false;
    public ImageGLSurfaceView imageGLSurfaceView;
    private int index = -1;
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

    static  float access$332(PolishView x0, float x1) {
        float f = x0.scale * x1;
        x0.scale = f;
        return f;
    }

    public PolishView(Context context) {
        super(context);
        init(context);
    }

    public PolishView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public PolishView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        FilterImageView filterImageView2 = new FilterImageView(getContext());
        this.filterImageView = filterImageView2;
        filterImageView2.setId(1);
        this.filterImageView.setAdjustViewBounds(true);
        this.filterImageView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.BackgroundCardColor));
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.addRule(13, -1);
        BrushDrawingView brushDrawingView2 = new BrushDrawingView(getContext());
        this.brushDrawingView = brushDrawingView2;
        brushDrawingView2.setVisibility(GONE);
        this.brushDrawingView.setId(2);
        LayoutParams layoutParams2 = new LayoutParams(-1, -2);
        layoutParams2.addRule(13, -1);
        layoutParams2.addRule(6, 1);
        layoutParams2.addRule(8, 1);
        ImageGLSurfaceView imageGLSurfaceView2 = new ImageGLSurfaceView(getContext(), attributeSet);
        this.imageGLSurfaceView = imageGLSurfaceView2;
        imageGLSurfaceView2.setId(3);
        this.imageGLSurfaceView.setVisibility(VISIBLE);
        this.imageGLSurfaceView.setAlpha(1.0f);
        this.imageGLSurfaceView.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT);
        LayoutParams layoutParams3 = new LayoutParams(-1, -2);
        layoutParams3.addRule(13, -1);
        layoutParams3.addRule(6, 1);
        layoutParams3.addRule(8, 1);
        addView(this.filterImageView, layoutParams);
        addView(this.imageGLSurfaceView, layoutParams3);
        addView(this.brushDrawingView, layoutParams2);
    }

    private void init(Context context) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {


            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case ACTION_DOWN:
                        if (PolishView.this.firstTouch && System.currentTimeMillis() - PolishView.this.time <= 300) {
                            if (PolishView.this.restore) {
                                PolishView.this.scale = 1.0f;
                                PolishView.this.restore = false;
                            } else {
                                PolishView.access$332(PolishView.this, 2.0f);
                                PolishView.this.restore = true;
                            }
                            PolishView.this.mode = Mode.ZOOM;
                            PolishView.this.firstTouch = false;
                            break;
                        } else {
                            if (PolishView.this.scale > 1.0f) {
                                PolishView.this.mode = Mode.DRAG;
                                PolishView.this.startX = motionEvent.getX() - PolishView.this.prevDx;
                                PolishView.this.startY = motionEvent.getY() - PolishView.this.prevDy;
                            }
                            PolishView.this.firstTouch = true;
                            PolishView.this.time = System.currentTimeMillis();
                            break;
                        }
                    case ACTION_UP:
                        Log.i(PolishView.TAG, "UP");
                        PolishView.this.mode = Mode.NONE;
                        PolishView polishView = PolishView.this;
                        polishView.prevDx = polishView.dx;
                        PolishView polishView2 = PolishView.this;
                        polishView2.prevDy = polishView2.dy;
                        break;
                    case ACTION_MOVE:
                        if (PolishView.this.mode == Mode.DRAG) {
                            PolishView.this.dx = motionEvent.getX() - PolishView.this.startX;
                            PolishView.this.dy = motionEvent.getY() - PolishView.this.startY;
                            break;
                        }
                        break;
                    case ACTION_POINTER_1_DOWN:
                        PolishView.this.mode = Mode.ZOOM;
                        break;
                    case ACTION_POINTER_1_UP:
                        PolishView.this.mode = Mode.NONE;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((PolishView.this.mode == Mode.DRAG && PolishView.this.scale >= 1.0f) || PolishView.this.mode == Mode.ZOOM) {
                    PolishView.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) PolishView.this.child().getWidth()) - (((float) PolishView.this.child().getWidth()) / PolishView.this.scale)) / 2.0f) * PolishView.this.scale;
                    float maxDy = ((((float) PolishView.this.child().getHeight()) - (((float) PolishView.this.child().getHeight()) / PolishView.this.scale)) / 2.0f) * PolishView.this.scale;
                    PolishView polishView3 = PolishView.this;
                    polishView3.dx = Math.min(Math.max(polishView3.dx, -maxDx), maxDx);
                    PolishView polishView4 = PolishView.this;
                    polishView4.dy = Math.min(Math.max(polishView4.dy, -maxDy), maxDy);
                    Log.i(PolishView.TAG, "Width: " + PolishView.this.child().getWidth() + ", scale " + PolishView.this.scale + ", dx " + PolishView.this.dx + ", max " + maxDx);
                    PolishView.this.applyScaleAndTranslation();
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

    public void setImageSource(final Bitmap bitmap) {
        this.filterImageView.setImageBitmap(bitmap);
        if (this.imageGLSurfaceView.getImageHandler() != null) {
            this.imageGLSurfaceView.setImageBitmap(bitmap);
        } else {
            this.imageGLSurfaceView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {

                @Override
                public void surfaceCreated() {
                    PolishView.this.imageGLSurfaceView.setImageBitmap(bitmap);
                }
            });
        }
        this.currentBitmap = bitmap;
        this.bitmaplist.add(bitmap);
        this.index++;
    }

    public void setImageSourceUndoRedo(final Bitmap bitmap) {
        this.filterImageView.setImageBitmap(bitmap);
        if (this.imageGLSurfaceView.getImageHandler() != null) {
            this.imageGLSurfaceView.setImageBitmap(bitmap);
        } else {
            this.imageGLSurfaceView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {

                @Override
                public void surfaceCreated() {
                    PolishView.this.imageGLSurfaceView.setImageBitmap(bitmap);
                }
            });
        }
        this.currentBitmap = bitmap;
    }

    public void setImageSource(Bitmap bitmap, ImageGLSurfaceView.OnSurfaceCreatedCallback onSurfaceCreatedCallback) {
        this.filterImageView.setImageBitmap(bitmap);
        if (this.imageGLSurfaceView.getImageHandler() != null) {
            this.imageGLSurfaceView.setImageBitmap(bitmap);
        } else {
            this.imageGLSurfaceView.setSurfaceCreatedCallback(onSurfaceCreatedCallback);
        }
        this.currentBitmap = bitmap;
    }

    public boolean undo() {
        Log.d("TAG", "undo: " + this.index);
        int i = this.index;
        if (i <= 0) {
            return false;
        }
        List<Bitmap> list = this.bitmaplist;
        int i2 = i - 1;
        this.index = i2;
        setImageSourceUndoRedo(list.get(i2));
        return true;
    }

    public boolean redo() {
        Log.d("TAG", "redo: " + this.index);
        if (this.index + 1 >= this.bitmaplist.size()) {
            return false;
        }
        List<Bitmap> list = this.bitmaplist;
        int i = this.index + 1;
        this.index = i;
        setImageSourceUndoRedo(list.get(i));
        return true;
    }

    public Bitmap getCurrentBitmap() {
        return this.currentBitmap;
    }

    public BrushDrawingView getBrushDrawingView() {
        return this.brushDrawingView;
    }

    public ImageGLSurfaceView getGLSurfaceView() {
        return this.imageGLSurfaceView;
    }

    public void saveGLSurfaceViewAsBitmap(final OnSaveBitmap onSaveBitmap) {
        if (this.imageGLSurfaceView.getVisibility() == VISIBLE) {
            this.imageGLSurfaceView.getResultBitmap(new ImageGLSurfaceView.QueryResultBitmapCallback() {

                @Override
                public void get(Bitmap bitmap) {
                    onSaveBitmap.onBitmapReady(bitmap);
                }
            });
        }
    }

    public void setFilterEffect(String str) {
        this.imageGLSurfaceView.setFilterWithConfig(str);
    }

    public void setFilterIntensity(float f) {
        this.imageGLSurfaceView.setFilterIntensity(f);
    }
}
