package com.relish.app.polish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;

import com.relish.app.crop.Cubic;
import com.relish.app.crop.Easing;
import com.relish.app.drip.graphic.FastBitmapDrawable;
import com.relish.app.drip.org.tensorflow.IDisposable;

public abstract class PolishMotionViewTouchBase extends AppCompatImageView implements IDisposable {
    protected static final boolean LOG_ENABLED = false;
    public static final String LOG_TAG = "ImageViewTouchBase";
    public static final float ZOOM_INVALID = -1.0f;
    protected final int DEFAULT_ANIMATION_DURATION;
    protected Matrix mBaseMatrix;
    private boolean mBitmapChanged;
    protected RectF mBitmapRect;
    private PointF mCenter;
    protected RectF mCenterRect;
    protected final Matrix mDisplayMatrix;
    private OnDrawableChangeListener mDrawableChangeListener;
    protected Easing mEasing;
    protected Handler mHandler;
    protected Runnable mLayoutRunnable;
    protected final float[] mMatrixValues;
    private float mMaxZoom;
    private boolean mMaxZoomDefined;
    private float mMinZoom;
    private boolean mMinZoomDefined;
    protected Matrix mNextMatrix;
    private OnLayoutChangeListener mOnLayoutChangeListener;
    protected DisplayType mScaleType;
    private boolean mScaleTypeChanged;
    protected RectF mScrollRect;
    protected Matrix mSuppMatrix;
    private int mThisHeight;
    private int mThisWidth;
    protected boolean mUserScaled;

    public enum DisplayType {
        NONE,
        FIT_TO_SCREEN,
        FIT_IF_BIGGER
    }

    public interface OnDrawableChangeListener {
        void onDrawableChanged(Drawable drawable);
    }

    public interface OnLayoutChangeListener {
        void onLayoutChanged(boolean z, int i, int i2, int i3, int i4);
    }

    public float getRotation() {
        return 0.0f;
    }

    public void onImageMatrixChanged() {
    }

    public void onZoom(float f) {
    }

    public void onZoomAnimationCompleted(float f) {
    }

    public PolishMotionViewTouchBase(Context context) {
        super(context);
        this.DEFAULT_ANIMATION_DURATION = 200;
        this.mBaseMatrix = new Matrix();
        this.mBitmapRect = new RectF();
        this.mCenter = new PointF();
        this.mCenterRect = new RectF();
        this.mDisplayMatrix = new Matrix();
        this.mEasing = new Cubic();
        this.mHandler = new Handler();
        this.mLayoutRunnable = null;
        this.mMatrixValues = new float[9];
        this.mMaxZoom = -1.0f;
        this.mMinZoom = -1.0f;
        this.mScaleType = DisplayType.FIT_TO_SCREEN;
        this.mScrollRect = new RectF();
        this.mSuppMatrix = new Matrix();
        this.mThisHeight = -1;
        this.mThisWidth = -1;
        this.mUserScaled = false;
        init();
    }

    public PolishMotionViewTouchBase(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.DEFAULT_ANIMATION_DURATION = 200;
        this.mBaseMatrix = new Matrix();
        this.mBitmapRect = new RectF();
        this.mCenter = new PointF();
        this.mCenterRect = new RectF();
        this.mDisplayMatrix = new Matrix();
        this.mEasing = new Cubic();
        this.mHandler = new Handler();
        this.mLayoutRunnable = null;
        this.mMatrixValues = new float[9];
        this.mMaxZoom = -1.0f;
        this.mMinZoom = -1.0f;
        this.mScaleType = DisplayType.FIT_TO_SCREEN;
        this.mScrollRect = new RectF();
        this.mSuppMatrix = new Matrix();
        this.mThisHeight = -1;
        this.mThisWidth = -1;
        this.mUserScaled = false;
        init();
    }

    public void setOnDrawableChangedListener(OnDrawableChangeListener onDrawableChangeListener) {
        this.mDrawableChangeListener = onDrawableChangeListener;
    }

    public void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
        this.mOnLayoutChangeListener = onLayoutChangeListener;
    }

    public void init() {
        setScaleType(ScaleType.MATRIX);
    }

    public void setScaleType(ScaleType scaleType) {
        if (scaleType == ScaleType.MATRIX) {
            super.setScaleType(scaleType);
        } else {
            Log.w(LOG_TAG, "Unsupported scaletype. Only MATRIX can be used");
        }
    }

    public void clear() {
        setImageBitmap(null);
    }

    public void setDisplayType(DisplayType displayType) {
        if (displayType != this.mScaleType) {
            this.mUserScaled = false;
            this.mScaleType = displayType;
            this.mScaleTypeChanged = true;
            requestLayout();
        }
    }

    public DisplayType getDisplayType() {
        return this.mScaleType;
    }

    public void setMinScale(float f) {
        this.mMinZoom = f;
    }

    public void setMaxScale(float f) {
        this.mMaxZoom = f;
    }


    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int i5;
        int i6;
        float f;
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int i7 = this.mThisWidth;
            int i8 = this.mThisHeight;
            int i9 = right - left;
            this.mThisWidth = i9;
            int i10 = bottom - top;
            this.mThisHeight = i10;
            i6 = i9 - i7;
            i5 = i10 - i8;
            this.mCenter.x = ((float) i9) / 2.0f;
            this.mCenter.y = ((float) this.mThisHeight) / 2.0f;
        } else {
            i6 = 0;
            i5 = 0;
        }
        Runnable runnable = this.mLayoutRunnable;
        if (runnable != null) {
            this.mLayoutRunnable = null;
            runnable.run();
        }
        Drawable drawable = getDrawable();
        if (drawable == null) {
            if (this.mBitmapChanged) {
                onDrawableChanged(drawable);
            }
            if (changed || this.mBitmapChanged || this.mScaleTypeChanged) {
                onLayoutChanged(left, top, right, bottom);
            }
            if (this.mBitmapChanged) {
                this.mBitmapChanged = false;
            }
            if (this.mScaleTypeChanged) {
                this.mScaleTypeChanged = false;
            }
        } else if (changed || this.mScaleTypeChanged || this.mBitmapChanged) {
            getDefaultScale(this.mScaleType);
            float scale = getScale(this.mBaseMatrix);
            float scale2 = getScale();
            float f2 = 1.0f;
            float min = Math.min(1.0f, 1.0f / scale);
            getProperBaseMatrix(drawable, this.mBaseMatrix);
            float scale3 = getScale(this.mBaseMatrix);
            if (this.mBitmapChanged || this.mScaleTypeChanged) {
                Matrix matrix = this.mNextMatrix;
                if (matrix != null) {
                    this.mSuppMatrix.set(matrix);
                    this.mNextMatrix = null;
                    f = getScale();
                } else {
                    this.mSuppMatrix.reset();
                    f = getDefaultScale(this.mScaleType);
                }
                f2 = f;
                setImageMatrix(getImageViewMatrix());
                if (f2 != getScale()) {
                    zoomTo(f2);
                }
            } else if (changed) {
                if (!this.mMinZoomDefined) {
                    this.mMinZoom = -1.0f;
                }
                if (!this.mMaxZoomDefined) {
                    this.mMaxZoom = -1.0f;
                }
                setImageMatrix(getImageViewMatrix());
                postTranslate((float) (-i6), (float) (-i5));
                if (!this.mUserScaled) {
                    f2 = getDefaultScale(this.mScaleType);
                    zoomTo(f2);
                } else {
                    if (((double) Math.abs(scale2 - min)) > 0.001d) {
                        f2 = (scale / scale3) * scale2;
                    }
                    zoomTo(f2);
                }
            }
            this.mUserScaled = false;
            if (f2 > getMaxScale() || f2 < getMinScale()) {
                zoomTo(f2);
            }
            center(true, true);
            if (this.mBitmapChanged) {
                onDrawableChanged(drawable);
            }
            if (changed || this.mBitmapChanged || this.mScaleTypeChanged) {
                onLayoutChanged(left, top, right, bottom);
            }
            if (this.mScaleTypeChanged) {
                this.mScaleTypeChanged = false;
            }
            if (this.mBitmapChanged) {
                this.mBitmapChanged = false;
            }
        }
    }

    public void resetDisplay() {
        this.mBitmapChanged = true;
        requestLayout();
    }

    public float getDefaultScale(DisplayType displayType) {
        if (displayType == DisplayType.FIT_TO_SCREEN) {
            return 1.0f;
        }
        if (displayType == DisplayType.FIT_IF_BIGGER) {
            return Math.min(1.0f, 1.0f / getScale(this.mBaseMatrix));
        }
        return 1.0f / getScale(this.mBaseMatrix);
    }

    @Override
    public void setImageResource(int i) {
        setImageDrawable(getContext().getResources().getDrawable(i));
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap, null, -1.0f, -1.0f);
    }

    public void setImageBitmap(Bitmap bitmap, Matrix matrix, float f, float f2) {
        if (bitmap != null) {
            setImageDrawable(new FastBitmapDrawable(bitmap), matrix, f, f2);
        } else {
            setImageDrawable(null, matrix, f, f2);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        setImageDrawable(drawable, null, -1.0f, -1.0f);
    }

    public void setImageDrawable(final Drawable drawable, final Matrix matrix, final float f, final float f2) {
        if (getWidth() <= 0) {
            this.mLayoutRunnable = new Runnable() {

                public void run() {
                    PolishMotionViewTouchBase.this.setImageDrawable(drawable, matrix, f, f2);
                }
            };
        } else {
            _setImageDrawable(drawable, matrix, f, f2);
        }
    }

    public void _setImageDrawable(Drawable drawable, Matrix matrix, float f, float f2) {
        if (drawable != null) {
            super.setImageDrawable(drawable);
        } else {
            this.mBaseMatrix.reset();
            super.setImageDrawable(null);
        }
        if (f == -1.0f || f2 == -1.0f) {
            this.mMinZoom = -1.0f;
            this.mMaxZoom = -1.0f;
            this.mMinZoomDefined = false;
            this.mMaxZoomDefined = false;
        } else {
            float min = Math.min(f, f2);
            float max = Math.max(min, f2);
            this.mMinZoom = min;
            this.mMaxZoom = max;
            this.mMinZoomDefined = true;
            this.mMaxZoomDefined = true;
            if (this.mScaleType == DisplayType.FIT_TO_SCREEN || this.mScaleType == DisplayType.FIT_IF_BIGGER) {
                if (this.mMinZoom >= 1.0f) {
                    this.mMinZoomDefined = false;
                    this.mMinZoom = -1.0f;
                }
                if (this.mMaxZoom <= 1.0f) {
                    this.mMaxZoomDefined = true;
                    this.mMaxZoom = -1.0f;
                }
            }
        }
        if (matrix != null) {
            this.mNextMatrix = new Matrix(matrix);
        }
        this.mBitmapChanged = true;
        requestLayout();
    }

    public void onDrawableChanged(Drawable drawable) {
        fireOnDrawableChangeListener(drawable);
    }

    public void fireOnLayoutChangeListener(int i, int i2, int i3, int i4) {
        OnLayoutChangeListener onLayoutChangeListener = this.mOnLayoutChangeListener;
        if (onLayoutChangeListener != null) {
            onLayoutChangeListener.onLayoutChanged(true, i, i2, i3, i4);
        }
    }

    public void fireOnDrawableChangeListener(Drawable drawable) {
        OnDrawableChangeListener onDrawableChangeListener = this.mDrawableChangeListener;
        if (onDrawableChangeListener != null) {
            onDrawableChangeListener.onDrawableChanged(drawable);
        }
    }

    public void onLayoutChanged(int i, int i2, int i3, int i4) {
        fireOnLayoutChangeListener(i, i2, i3, i4);
    }

    public float computeMaxZoom() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return 1.0f;
        }
        return Math.max(((float) drawable.getIntrinsicWidth()) / ((float) this.mThisWidth), ((float) drawable.getIntrinsicHeight()) / ((float) this.mThisHeight)) * 8.0f;
    }

    public float computeMinZoom() {
        if (getDrawable() == null) {
            return 1.0f;
        }
        return Math.min(1.0f, 1.0f / getScale(this.mBaseMatrix));
    }

    public float getMaxScale() {
        if (this.mMaxZoom == -1.0f) {
            this.mMaxZoom = computeMaxZoom();
        }
        return this.mMaxZoom;
    }

    public float getMinScale() {
        if (this.mMinZoom == -1.0f) {
            this.mMinZoom = computeMinZoom();
        }
        return this.mMinZoom;
    }

    public Matrix getImageViewMatrix() {
        return getImageViewMatrix(this.mSuppMatrix);
    }

    public Matrix getImageViewMatrix(Matrix matrix) {
        this.mDisplayMatrix.set(this.mBaseMatrix);
        this.mDisplayMatrix.postConcat(matrix);
        return this.mDisplayMatrix;
    }

    public void setImageMatrix(Matrix matrix) {
        Matrix imageMatrix = getImageMatrix();
        boolean z = (matrix == null && !imageMatrix.isIdentity()) || (matrix != null && !imageMatrix.equals(matrix));
        super.setImageMatrix(matrix);
        if (z) {
            onImageMatrixChanged();
        }
    }

    public Matrix getDisplayMatrix() {
        return new Matrix(this.mSuppMatrix);
    }

    public void getProperBaseMatrix(Drawable drawable, Matrix matrix) {
        float f = (float) this.mThisWidth;
        float f2 = (float) this.mThisHeight;
        float intrinsicWidth = (float) drawable.getIntrinsicWidth();
        float intrinsicHeight = (float) drawable.getIntrinsicHeight();
        matrix.reset();
        if (intrinsicWidth > f || intrinsicHeight > f2) {
            float min = Math.min(f / intrinsicWidth, f2 / intrinsicHeight);
            matrix.postScale(min, min);
            matrix.postTranslate((f - (intrinsicWidth * min)) / 2.0f, (f2 - (intrinsicHeight * min)) / 2.0f);
            return;
        }
        float min2 = Math.min(f / intrinsicWidth, f2 / intrinsicHeight);
        matrix.postScale(min2, min2);
        matrix.postTranslate((f - (intrinsicWidth * min2)) / 2.0f, (f2 - (intrinsicHeight * min2)) / 2.0f);
    }

    public void getProperBaseMatrix2(Drawable drawable, Matrix matrix) {
        float f = (float) this.mThisWidth;
        float f2 = (float) this.mThisHeight;
        float intrinsicWidth = (float) drawable.getIntrinsicWidth();
        float intrinsicHeight = (float) drawable.getIntrinsicHeight();
        matrix.reset();
        float min = Math.min(f / intrinsicWidth, f2 / intrinsicHeight);
        matrix.postScale(min, min);
        matrix.postTranslate((f - (intrinsicWidth * min)) / 2.0f, (f2 - (intrinsicHeight * min)) / 2.0f);
    }

    public float getValue(Matrix matrix, int i) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[i];
    }

    public void printMatrix(Matrix matrix) {
        float value = getValue(matrix, 0);
        float value2 = getValue(matrix, 4);
        float value3 = getValue(matrix, 2);
        float value4 = getValue(matrix, 5);
        Log.d(LOG_TAG, "matrix: { x: " + value3 + ", y: " + value4 + ", scalex: " + value + ", scaley: " + value2 + " }");
    }

    public RectF getBitmapRect() {
        return getBitmapRect(this.mSuppMatrix);
    }

    public RectF getBitmapRect(Matrix matrix) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }
        Matrix imageViewMatrix = getImageViewMatrix(matrix);
        this.mBitmapRect.set(0.0f, 0.0f, (float) drawable.getIntrinsicWidth(), (float) drawable.getIntrinsicHeight());
        imageViewMatrix.mapRect(this.mBitmapRect);
        return this.mBitmapRect;
    }

    public float getScale(Matrix matrix) {
        return getValue(matrix, 0);
    }

    public float getScale() {
        return getScale(this.mSuppMatrix);
    }

    public void center(boolean z, boolean z2) {
        if (getDrawable() != null) {
            RectF center = getCenter(this.mSuppMatrix, z, z2);
            if (center.left != 0.0f || center.top != 0.0f) {
                postTranslate(center.left, center.top);
            }
        }
    }

    public RectF getCenter(Matrix supportMatrix, boolean horizontal, boolean vertical) {
        if (getDrawable() == null) {
            return new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        }
        this.mCenterRect.set(0.0f, 0.0f, 0.0f, 0.0f);
        RectF rect = getBitmapRect(supportMatrix);
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        if (vertical) {
            if (height < this.mBitmapRect.height()) {
                deltaY = ((this.mBitmapRect.height() - height) / 2.0f) - (rect.top - this.mBitmapRect.top);
            } else if (rect.top > this.mBitmapRect.top) {
                deltaY = -(rect.top - this.mBitmapRect.top);
            } else if (rect.bottom < this.mBitmapRect.bottom) {
                deltaY = this.mBitmapRect.bottom - rect.bottom;
            }
        }
        if (horizontal) {
            if (width < this.mBitmapRect.width()) {
                deltaX = ((this.mBitmapRect.width() - width) / 2.0f) - (rect.left - this.mBitmapRect.left);
            } else if (rect.left > this.mBitmapRect.left) {
                deltaX = -(rect.left - this.mBitmapRect.left);
            } else if (rect.right < this.mBitmapRect.right) {
                deltaX = this.mBitmapRect.right - rect.right;
            }
        }
        this.mCenterRect.set(deltaX, deltaY, 0.0f, 0.0f);
        return this.mCenterRect;
    }

    public void postTranslate(float f, float f2) {
        if (f != 0.0f || f2 != 0.0f) {
            this.mSuppMatrix.postTranslate(f, f2);
            setImageMatrix(getImageViewMatrix());
        }
    }

    public void postScale(float f, float f2, float f3) {
        this.mSuppMatrix.postScale(f, f, f2, f3);
        setImageMatrix(getImageViewMatrix());
    }

    public PointF getCenter() {
        return this.mCenter;
    }

    public void zoomTo(float f) {
        if (f > getMaxScale()) {
            f = getMaxScale();
        }
        if (f < getMinScale()) {
            f = getMinScale();
        }
        PointF center = getCenter();
        zoomTo(f, center.x, center.y);
    }

    public void zoomTo(float f, float f2) {
        PointF center = getCenter();
        zoomTo(f, center.x, center.y, f2);
    }

    public void zoomTo(float f, float f2, float f3) {
        if (f > getMaxScale()) {
            f = getMaxScale();
        }
        postScale(f / getScale(), f2, f3);
        onZoom(getScale());
        center(true, true);
    }

    public void scrollBy(float f, float f2) {
        panBy((double) f, (double) f2);
    }

    public void panBy(double d, double d2) {
        RectF bitmapRect = getBitmapRect();
        this.mScrollRect.set((float) d, (float) d2, 0.0f, 0.0f);
        updateRect(bitmapRect, this.mScrollRect);
        postTranslate(this.mScrollRect.left, this.mScrollRect.top);
        center(true, true);
    }

    public void updateRect(RectF rectF, RectF rectF2) {
        if (rectF != null) {
            if (rectF.top >= 0.0f && rectF.bottom <= ((float) this.mThisHeight)) {
                rectF2.top = 0.0f;
            }
            if (rectF.left >= 0.0f && rectF.right <= ((float) this.mThisWidth)) {
                rectF2.left = 0.0f;
            }
            if (rectF.top + rectF2.top >= 0.0f && rectF.bottom > ((float) this.mThisHeight)) {
                rectF2.top = (float) ((int) (0.0f - rectF.top));
            }
            if (rectF.bottom + rectF2.top <= ((float) (this.mThisHeight + 0)) && rectF.top < 0.0f) {
                rectF2.top = (float) ((int) (((float) (this.mThisHeight + 0)) - rectF.bottom));
            }
            if (rectF.left + rectF2.left >= 0.0f) {
                rectF2.left = (float) ((int) (0.0f - rectF.left));
            }
            float f = rectF.right + rectF2.left;
            int i = this.mThisWidth;
            if (f <= ((float) (i + 0))) {
                rectF2.left = (float) ((int) (((float) (i + 0)) - rectF.right));
            }
        }
    }

    public void scrollBy(float f, float f2, final double d) {
        final double d2 = (double) f;
        final double d3 = (double) f2;
        final long currentTimeMillis = System.currentTimeMillis();
        this.mHandler.post(new Runnable() {

            double old_x = 0.0d;
            double old_y = 0.0d;

            public void run() {
                double min = Math.min(d, (double) (System.currentTimeMillis() - currentTimeMillis));
                double easeOut = PolishMotionViewTouchBase.this.mEasing.easeOut(min, 0.0d, d2, d);
                double easeOut2 = PolishMotionViewTouchBase.this.mEasing.easeOut(min, 0.0d, d3, d);
                PolishMotionViewTouchBase.this.panBy(easeOut - this.old_x, easeOut2 - this.old_y);
                this.old_x = easeOut;
                this.old_y = easeOut2;
                if (min < d) {
                    PolishMotionViewTouchBase.this.mHandler.post(this);
                    return;
                }
                PolishMotionViewTouchBase polishMotionViewTouchBase = PolishMotionViewTouchBase.this;
                RectF center = polishMotionViewTouchBase.getCenter(polishMotionViewTouchBase.mSuppMatrix, true, true);
                if (center.left != 0.0f || center.top != 0.0f) {
                    PolishMotionViewTouchBase.this.scrollBy(center.left, center.top);
                }
            }
        });
    }

    public void zoomTo(float f, float f2, float f3, final float f4) {
        float f5;
        if (f > getMaxScale()) {
            f5 = getMaxScale();
        } else {
            f5 = f;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        final float scale = getScale();
        final float f52 = f5 - scale;
        Matrix matrix = new Matrix(this.mSuppMatrix);
        matrix.postScale(f5, f5, f2, f3);
        RectF center = getCenter(matrix, true, true);
        final float f6 = f2 + (center.left * f5);
        final float f7 = f3 + (center.top * f5);
        this.mHandler.post(new Runnable() {

            public void run() {
                float min = Math.min(f4, (float) (System.currentTimeMillis() - currentTimeMillis));
                PolishMotionViewTouchBase polishMotionViewTouchBase = PolishMotionViewTouchBase.this;
                polishMotionViewTouchBase.zoomTo(scale + ((float) polishMotionViewTouchBase.mEasing.easeInOut((double) min, 0.0d, (double) f52, (double) f4)), f6, f7);
                if (min < f4) {
                    PolishMotionViewTouchBase.this.mHandler.post(this);
                    return;
                }
                PolishMotionViewTouchBase polishMotionViewTouchBase2 = PolishMotionViewTouchBase.this;
                polishMotionViewTouchBase2.onZoomAnimationCompleted(polishMotionViewTouchBase2.getScale());
                PolishMotionViewTouchBase.this.center(true, true);
            }
        });
    }

    @Override
    public void dispose() {
        clear();
    }
}
