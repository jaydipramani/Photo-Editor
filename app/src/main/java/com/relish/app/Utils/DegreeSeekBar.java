package com.relish.app.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.kxml2.wap.Wbxml;

public class DegreeSeekBar extends View {
    private static final String TAG = "DegreeSeekBar";
    private int mBaseLine;
    private final Rect mCanvasClipBounds;
    private int mCenterTextColor;
    private Paint mCirclePaint;
    private int mCurrentDegrees;
    private float mDragFactor;
    private Paint.FontMetricsInt mFontMetrics;
    private Path mIndicatorPath;
    private float mLastTouchedPosition;
    private int mMaxReachableDegrees;
    private int mMinReachableDegrees;
    private int mPointColor;
    private int mPointCount;
    private float mPointMargin;
    private Paint mPointPaint;
    private boolean mScrollStarted;
    private ScrollingListener mScrollingListener;
    private int mTextColor;
    private Paint mTextPaint;
    private float[] mTextWidths;
    private int mTotalScrollDistance;
    private String suffix;

    public interface ScrollingListener {
        void onScroll(int i);

        void onScrollEnd();

        void onScrollStart();
    }

    public DegreeSeekBar(Context context) {
        this(context, null);
    }

    public DegreeSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DegreeSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCanvasClipBounds = new Rect();
        this.mIndicatorPath = new Path();
        this.mCurrentDegrees = 0;
        this.mPointCount = 51;
        this.mPointColor = 0xFF574FFF;
        this.mTextColor = 0xFF574FFF;
        this.mCenterTextColor = 0xFF574FFF;
        this.mDragFactor = 2.1f;
        this.mMinReachableDegrees = -100;
        this.mMaxReachableDegrees = 100;
        this.suffix = "";
        init();
    }


    public DegreeSeekBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mCanvasClipBounds = new Rect();
        this.mIndicatorPath = new Path();
        this.mCurrentDegrees = 0;
        this.mPointCount = 51;
        this.mPointColor = Color.BLACK;
        this.mTextColor = Color.BLACK;
        this.mCenterTextColor = Color.BLACK;
        this.mDragFactor = 2.1f;
        this.mMinReachableDegrees = -100;
        this.mMaxReachableDegrees = 100;
        this.suffix = "";
        init();
    }

    private void init() {
        Paint paint = new Paint(1);
        this.mPointPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.mPointPaint.setColor(this.mPointColor);
        this.mPointPaint.setStrokeWidth(2.0f);
        Paint paint2 = new Paint();
        this.mTextPaint = paint2;
        paint2.setColor(this.mTextColor);
        this.mTextPaint.setStyle(Paint.Style.FILL);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(20.0f);
        this.mTextPaint.setTextAlign(Paint.Align.LEFT);
        this.mTextPaint.setAlpha(100);
        this.mFontMetrics = this.mTextPaint.getFontMetricsInt();
        float[] fArr = new float[1];
        this.mTextWidths = fArr;
        this.mTextPaint.getTextWidths("0", fArr);
        Paint paint3 = new Paint();
        this.mCirclePaint = paint3;
        paint3.setStyle(Paint.Style.FILL);
        this.mCirclePaint.setAlpha(255);
        this.mCirclePaint.setAntiAlias(true);
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mPointMargin = ((float) i) / ((float) this.mPointCount);
        this.mBaseLine = (((i2 - this.mFontMetrics.bottom) + this.mFontMetrics.top) / 2) - this.mFontMetrics.top;
        this.mIndicatorPath.moveTo((float) (i / 2), (float) (((i2 / 2) + (this.mFontMetrics.top / 2)) - 18));
        this.mIndicatorPath.rLineTo(-8.0f, -8.0f);
        this.mIndicatorPath.rLineTo(16.0f, 0.0f);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.mLastTouchedPosition = motionEvent.getX();
                if (!this.mScrollStarted) {
                    this.mScrollStarted = true;
                    ScrollingListener scrollingListener = this.mScrollingListener;
                    if (scrollingListener != null) {
                        scrollingListener.onScrollStart();
                        break;
                    }
                }
                break;
            case 1:
                this.mScrollStarted = false;
                ScrollingListener scrollingListener2 = this.mScrollingListener;
                if (scrollingListener2 != null) {
                    scrollingListener2.onScrollEnd();
                }
                invalidate();
                break;
            case 2:
                float x = motionEvent.getX() - this.mLastTouchedPosition;
                int i = this.mCurrentDegrees;
                int i2 = this.mMaxReachableDegrees;
                if (i >= i2 && x < 0.0f) {
                    this.mCurrentDegrees = i2;
                    invalidate();
                    break;
                } else {
                    int i3 = this.mMinReachableDegrees;
                    if (i > i3 || x <= 0.0f) {
                        if (x != 0.0f) {
                            onScrollEvent(motionEvent, x);
                            break;
                        }
                    } else {
                        this.mCurrentDegrees = i3;
                        invalidate();
                        break;
                    }
                }
                break;
        }
        return true;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(this.mCanvasClipBounds);
        int i = (this.mPointCount / 2) + ((0 - this.mCurrentDegrees) / 2);
        this.mPointPaint.setColor(this.mPointColor);
        for (int i2 = 0; i2 < this.mPointCount; i2++) {
            if (i2 <= i - (Math.abs(this.mMinReachableDegrees) / 2) || i2 >= (Math.abs(this.mMaxReachableDegrees) / 2) + i || !this.mScrollStarted) {
                this.mPointPaint.setAlpha(100);
            } else {
                this.mPointPaint.setAlpha(255);
            }
            int i3 = this.mPointCount;
            if (i2 > (i3 / 2) - 8 && i2 < (i3 / 2) + 8 && i2 > i - (Math.abs(this.mMinReachableDegrees) / 2) && i2 < (Math.abs(this.mMaxReachableDegrees) / 2) + i) {
                if (this.mScrollStarted) {
                    this.mPointPaint.setAlpha((Math.abs((this.mPointCount / 2) - i2) * 255) / 8);
                } else {
                    this.mPointPaint.setAlpha((Math.abs((this.mPointCount / 2) - i2) * 100) / 8);
                }
            }
            canvas.drawPoint(((float) this.mCanvasClipBounds.centerX()) + (((float) (i2 - (this.mPointCount / 2))) * this.mPointMargin), (float) this.mCanvasClipBounds.centerY(), this.mPointPaint);
            if (this.mCurrentDegrees != 0 && i2 == i) {
                if (this.mScrollStarted) {
                    this.mTextPaint.setAlpha(255);
                } else {
                    this.mTextPaint.setAlpha(Wbxml.EXT_0);
                }
                this.mPointPaint.setStrokeWidth(4.0f);
                canvas.drawPoint(((float) this.mCanvasClipBounds.centerX()) + (((float) (i2 - (this.mPointCount / 2))) * this.mPointMargin), (float) this.mCanvasClipBounds.centerY(), this.mPointPaint);
                this.mPointPaint.setStrokeWidth(2.0f);
                this.mTextPaint.setAlpha(100);
            }
        }
        for (int i32 = -100; i32 <= 100; i32 += 10) {
            if (i32 < this.mMinReachableDegrees || i32 > this.mMaxReachableDegrees) {
                drawDegreeText(i32, canvas, false);
            } else {
                drawDegreeText(i32, canvas, true);
            }
        }
        this.mTextPaint.setTextSize(22.0f);
        this.mTextPaint.setAlpha(255);
        this.mTextPaint.setColor(this.mCenterTextColor);
        int i4 = this.mCurrentDegrees;
        if (i4 >= 10) {
            canvas.drawText(this.mCurrentDegrees + this.suffix, ((float) (getWidth() / 2)) - this.mTextWidths[0], (float) this.mBaseLine, this.mTextPaint);
        } else if (i4 <= -10) {
            canvas.drawText(this.mCurrentDegrees + this.suffix, ((float) (getWidth() / 2)) - ((this.mTextWidths[0] / 2.0f) * 3.0f), (float) this.mBaseLine, this.mTextPaint);
        } else if (i4 < 0) {
            canvas.drawText(this.mCurrentDegrees + this.suffix, ((float) (getWidth() / 2)) - this.mTextWidths[0], (float) this.mBaseLine, this.mTextPaint);
        } else {
            canvas.drawText(this.mCurrentDegrees + this.suffix, ((float) (getWidth() / 2)) - (this.mTextWidths[0] / 2.0f), (float) this.mBaseLine, this.mTextPaint);
        }
        this.mTextPaint.setAlpha(100);
        this.mTextPaint.setTextSize(20.0f);
        this.mTextPaint.setColor(this.mTextColor);
        this.mCirclePaint.setColor(this.mCenterTextColor);
        canvas.drawPath(this.mIndicatorPath, this.mCirclePaint);
        this.mCirclePaint.setColor(this.mCenterTextColor);
    }

    private void drawDegreeText(int i, Canvas canvas, boolean z) {
        if (!z) {
            this.mTextPaint.setAlpha(100);
        } else if (this.mScrollStarted) {
            this.mTextPaint.setAlpha(Math.min(255, (Math.abs(i - this.mCurrentDegrees) * 255) / 15));
            if (Math.abs(i - this.mCurrentDegrees) <= 7) {
                this.mTextPaint.setAlpha(0);
            }
        } else {
            this.mTextPaint.setAlpha(100);
            if (Math.abs(i - this.mCurrentDegrees) <= 7) {
                this.mTextPaint.setAlpha(0);
            }
        }
        if (i == 0) {
            if (Math.abs(this.mCurrentDegrees) >= 15 && !this.mScrollStarted) {
                this.mTextPaint.setAlpha(180);
            }
            canvas.drawText("0°", (((float) (getWidth() / 2)) - (this.mTextWidths[0] / 2.0f)) - (((float) (this.mCurrentDegrees / 2)) * this.mPointMargin), (float) ((getHeight() / 2) - 10), this.mTextPaint);
            return;
        }
        float f = this.mPointMargin;
        canvas.drawText(i + this.suffix, ((((float) (getWidth() / 2)) + ((((float) i) * f) / 2.0f)) - ((this.mTextWidths[0] / 2.0f) * 3.0f)) - (((float) (this.mCurrentDegrees / 2)) * f), (float) ((getHeight() / 2) - 10), this.mTextPaint);
    }

    private void onScrollEvent(MotionEvent motionEvent, float f) {
        this.mTotalScrollDistance = (int) (((float) this.mTotalScrollDistance) - f);
        postInvalidate();
        this.mLastTouchedPosition = motionEvent.getX();
        int i = (int) ((((float) this.mTotalScrollDistance) * this.mDragFactor) / this.mPointMargin);
        this.mCurrentDegrees = i;
        ScrollingListener scrollingListener = this.mScrollingListener;
        if (scrollingListener != null) {
            scrollingListener.onScroll(i);
        }
    }

    public void setDegreeRange(int i, int i2) {
        if (i > i2) {
            Log.e(TAG, "setDegreeRange: error, max must greater than min");
            return;
        }
        this.mMinReachableDegrees = i;
        this.mMaxReachableDegrees = i2;
        int i3 = this.mCurrentDegrees;
        if (i3 > i2 || i3 < i) {
            this.mCurrentDegrees = (i + i2) / 2;
        }
        this.mTotalScrollDistance = (int) ((((float) this.mCurrentDegrees) * this.mPointMargin) / this.mDragFactor);
        invalidate();
    }

    public void setCurrentDegrees(int i) {
        if (i <= this.mMaxReachableDegrees && i >= this.mMinReachableDegrees) {
            this.mCurrentDegrees = i;
            this.mTotalScrollDistance = (int) ((((float) i) * this.mPointMargin) / this.mDragFactor);
            invalidate();
        }
    }

    public void setScrollingListener(ScrollingListener scrollingListener) {
        this.mScrollingListener = scrollingListener;
    }

    public int getPointColor() {
        return this.mPointColor;
    }

    public void setPointColor(int i) {
        this.mPointColor = i;
        this.mPointPaint.setColor(i);
        postInvalidate();
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
        this.mTextPaint.setColor(i);
        postInvalidate();
    }

    public int getCenterTextColor() {
        return this.mCenterTextColor;
    }

    public void setCenterTextColor(int i) {
        this.mCenterTextColor = i;
        postInvalidate();
    }

    public float getDragFactor() {
        return this.mDragFactor;
    }

    public void setDragFactor(float f) {
        this.mDragFactor = f;
    }

    public void setSuffix(String str) {
        this.suffix = str;
    }
}
