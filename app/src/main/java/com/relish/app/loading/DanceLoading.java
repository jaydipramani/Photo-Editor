package com.relish.app.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.relish.app.loading.render.LoadingRenderer;


public class DanceLoading extends LoadingRenderer {
    private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final long ANIMATION_DURATION = 1888;
    private static final float BALL_FORWARD_END_ENTER_DURATION_OFFSET = 0.125f;
    private static final float BALL_FORWARD_END_EXIT_DURATION_OFFSET = 0.54f;
    private static final float BALL_FORWARD_START_ENTER_DURATION_OFFSET = 0.0f;
    private static final float BALL_FORWARD_START_EXIT_DURATION_OFFSET = 0.375f;
    private static final float BALL_REVERSAL_END_ENTER_DURATION_OFFSET = 0.725f;
    private static final float BALL_REVERSAL_END_EXIT_DURATION_OFFSET = 1.0f;
    private static final float BALL_REVERSAL_START_ENTER_DURATION_OFFSET = 0.6f;
    private static final float BALL_REVERSAL_START_EXIT_DURATION_OFFSET = 0.875f;
    private static final float CENTER_CIRCLE_FORWARD_END_SCALE_DURATION_OFFSET = 0.475f;
    private static final float CENTER_CIRCLE_FORWARD_START_SCALE_DURATION_OFFSET = 0.225f;
    private static final float CENTER_CIRCLE_REVERSAL_END_SCALE_DURATION_OFFSET = 0.875f;
    private static final float CENTER_CIRCLE_REVERSAL_START_SCALE_DURATION_OFFSET = 0.675f;
    private static final int DANCE_INTERVAL_ANGLE = 60;
    private static final int DANCE_START_ANGLE = 0;
    private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final float DEFAULT_CENTER_RADIUS = 12.5f;
    private static final int DEFAULT_COLOR = -1;
    private static final float DEFAULT_DANCE_BALL_RADIUS = 2.0f;
    private static final float DEFAULT_STROKE_WIDTH = 1.5f;
    private static final int DEGREE_360 = 360;
    private static final int[] DIRECTION = {1, 1, -1};
    private static final Interpolator MATERIAL_INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final int NUM_POINTS = 3;
    private static final float[] POINT_X = new float[3];
    private static final float[] POINT_Y = new float[3];
    private static final float RING_FORWARD_END_ROTATE_DURATION_OFFSET = 0.375f;
    private static final float RING_FORWARD_START_ROTATE_DURATION_OFFSET = 0.125f;
    private static final float RING_REVERSAL_END_ROTATE_DURATION_OFFSET = 0.75f;
    private static final float RING_REVERSAL_START_ROTATE_DURATION_OFFSET = 0.5f;
    private static final int RING_START_ANGLE = -90;
    private int mArcColor;
    private float mCenterRadius;
    private int mColor;
    private final RectF mCurrentBounds;
    private float mDanceBallRadius;
    private final Paint mPaint;
    private float mRotation;
    private float mScale;
    private float mShapeChangeHeight;
    private float mShapeChangeWidth;
    private float mStrokeInset;
    private float mStrokeWidth;
    private final RectF mTempBounds;

    private DanceLoading(Context context) {
        super(context);
        this.mPaint = new Paint();
        this.mTempBounds = new RectF();
        this.mCurrentBounds = new RectF();
        init(context);
        setupPaint();
    }

    private void init(Context context) {
        this.mStrokeWidth = DensityUtil.dip2px(context, DEFAULT_STROKE_WIDTH);
        this.mCenterRadius = DensityUtil.dip2px(context, DEFAULT_CENTER_RADIUS);
        this.mDanceBallRadius = DensityUtil.dip2px(context, DEFAULT_DANCE_BALL_RADIUS);
        setColor(-1);
        setInsets((int) this.mWidth, (int) this.mHeight);
        this.mDuration = ANIMATION_DURATION;
    }

    private void setupPaint() {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        this.mPaint.setStyle(Paint.Style.STROKE);
    }

    
    @Override
    public void draw(Canvas canvas, Rect bounds) {
        int saveCount = canvas.save();
        this.mTempBounds.set(bounds);
        RectF rectF = this.mTempBounds;
        float f = this.mStrokeInset;
        rectF.inset(f, f);
        this.mCurrentBounds.set(this.mTempBounds);
        float outerCircleRadius = Math.min(this.mTempBounds.height(), this.mTempBounds.width()) / DEFAULT_DANCE_BALL_RADIUS;
        float interCircleRadius = outerCircleRadius / DEFAULT_DANCE_BALL_RADIUS;
        float centerRingWidth = interCircleRadius - (this.mStrokeWidth / DEFAULT_DANCE_BALL_RADIUS);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.mColor);
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        canvas.drawCircle(this.mTempBounds.centerX(), this.mTempBounds.centerY(), outerCircleRadius, this.mPaint);
        this.mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(this.mTempBounds.centerX(), this.mTempBounds.centerY(), this.mScale * interCircleRadius, this.mPaint);
        if (this.mRotation != 0.0f) {
            this.mPaint.setColor(this.mArcColor);
            this.mPaint.setStyle(Paint.Style.STROKE);
            RectF rectF2 = this.mTempBounds;
            float f2 = centerRingWidth / DEFAULT_DANCE_BALL_RADIUS;
            float f3 = this.mStrokeWidth;
            rectF2.inset(f2 + (f3 / DEFAULT_DANCE_BALL_RADIUS), (centerRingWidth / DEFAULT_DANCE_BALL_RADIUS) + (f3 / DEFAULT_DANCE_BALL_RADIUS));
            this.mPaint.setStrokeWidth(centerRingWidth);
            canvas.drawArc(this.mTempBounds, -90.0f, this.mRotation, false, this.mPaint);
        }
        this.mPaint.setColor(this.mColor);
        this.mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 3; i++) {
            float[] fArr = POINT_X;
            float f4 = fArr[i];
            float[] fArr2 = POINT_Y;
            canvas.rotate((float) (i * 60), f4, fArr2[i]);
            float f5 = fArr[i];
            float f6 = this.mDanceBallRadius;
            float f7 = this.mShapeChangeWidth;
            float f8 = this.mShapeChangeHeight;
            canvas.drawOval(new RectF((f5 - f6) - (f7 / DEFAULT_DANCE_BALL_RADIUS), (fArr2[i] - f6) - (f8 / DEFAULT_DANCE_BALL_RADIUS), fArr[i] + f6 + (f7 / DEFAULT_DANCE_BALL_RADIUS), fArr2[i] + f6 + (f8 / DEFAULT_DANCE_BALL_RADIUS)), this.mPaint);
            canvas.rotate((float) ((-i) * 60), fArr[i], fArr2[i]);
        }
        canvas.restoreToCount(saveCount);
    }

    
    @Override
    public void computeRender(float renderProgress) {
        float radius;
        float originCoordinateX;
        DanceLoading danceLoading;
        float originCoordinateY;
        float radius2;
        DanceLoading danceLoading2;
        float radius3;
        float min = Math.min(this.mCurrentBounds.height(), this.mCurrentBounds.width());
        float f = DEFAULT_DANCE_BALL_RADIUS;
        float radius4 = min / DEFAULT_DANCE_BALL_RADIUS;
        float originCoordinateX2 = this.mCurrentBounds.left;
        float originCoordinateY2 = this.mCurrentBounds.top + radius4;
        double d = 6.283185307179586d;
        int i = 3;
        float f2 = 360.0f;
        float f3 = 0.5f;
        if (renderProgress > 0.125f || renderProgress <= 0.0f) {
            radius = radius4;
            originCoordinateX = originCoordinateX2;
        } else {
            float ballForwardEnterProgress = (renderProgress - 0.0f) / 0.125f;
            float f4 = ((0.5f - ballForwardEnterProgress) * this.mDanceBallRadius) / DEFAULT_DANCE_BALL_RADIUS;
            this.mShapeChangeHeight = f4;
            this.mShapeChangeWidth = -f4;
            int i2 = 0;
            while (i2 < i) {
                float k = (float) Math.tan(((double) (((float) ((i2 * 60) + 0)) / f2)) * d);
                float progress = ((ACCELERATE_INTERPOLATOR.getInterpolation(ballForwardEnterProgress) / f) - f3) * f * ((float) DIRECTION[i2]);
                float[] fArr = POINT_X;
                fArr[i2] = (float) (((double) radius4) + (((double) progress) * (((double) radius4) / Math.sqrt(Math.pow((double) k, 2.0d) + 1.0d))));
                float[] fArr2 = POINT_Y;
                fArr2[i2] = (fArr[i2] - radius4) * k;
                fArr[i2] = fArr[i2] + originCoordinateX2;
                fArr2[i2] = fArr2[i2] + originCoordinateY2;
                i2++;
                originCoordinateX2 = originCoordinateX2;
                radius4 = radius4;
                f = DEFAULT_DANCE_BALL_RADIUS;
                d = 6.283185307179586d;
                i = 3;
                f2 = 360.0f;
                f3 = 0.5f;
            }
            radius = radius4;
            originCoordinateX = originCoordinateX2;
        }
        if (renderProgress > 0.375f || renderProgress <= 0.125f) {
            danceLoading = this;
        } else {
            danceLoading = this;
            danceLoading.mRotation = MATERIAL_INTERPOLATOR.getInterpolation((renderProgress - 0.125f) / 0.25f) * 360.0f;
        }
        if (renderProgress <= CENTER_CIRCLE_FORWARD_END_SCALE_DURATION_OFFSET && renderProgress > CENTER_CIRCLE_FORWARD_START_SCALE_DURATION_OFFSET) {
            float centerCircleScaleProgress = (renderProgress - CENTER_CIRCLE_FORWARD_START_SCALE_DURATION_OFFSET) / 0.25f;
            if (centerCircleScaleProgress <= 0.5f) {
                danceLoading.mScale = (DECELERATE_INTERPOLATOR.getInterpolation(centerCircleScaleProgress * DEFAULT_DANCE_BALL_RADIUS) * 0.2f) + 1.0f;
            } else {
                danceLoading.mScale = 1.2f - (ACCELERATE_INTERPOLATOR.getInterpolation((centerCircleScaleProgress - 0.5f) * DEFAULT_DANCE_BALL_RADIUS) * 0.2f);
            }
        }
        if (renderProgress > 0.54f || renderProgress <= 0.375f) {
            originCoordinateY = originCoordinateY2;
            radius2 = radius;
        } else {
            float ballForwardExitProgress = (renderProgress - 0.375f) / 0.16500002f;
            float f5 = ((ballForwardExitProgress - 0.5f) * danceLoading.mDanceBallRadius) / DEFAULT_DANCE_BALL_RADIUS;
            danceLoading.mShapeChangeHeight = f5;
            danceLoading.mShapeChangeWidth = -f5;
            int i3 = 0;
            while (i3 < 3) {
                float k2 = (float) Math.tan(((double) (((float) ((i3 * 60) + 0)) / 360.0f)) * 6.283185307179586d);
                float progress2 = (DECELERATE_INTERPOLATOR.getInterpolation(ballForwardExitProgress) / DEFAULT_DANCE_BALL_RADIUS) * DEFAULT_DANCE_BALL_RADIUS * ((float) DIRECTION[i3]);
                float[] fArr3 = POINT_X;
                fArr3[i3] = (float) (((double) radius) + (((double) progress2) * (((double) radius) / Math.sqrt(Math.pow((double) k2, 2.0d) + 1.0d))));
                float[] fArr4 = POINT_Y;
                fArr4[i3] = (fArr3[i3] - radius) * k2;
                fArr3[i3] = fArr3[i3] + originCoordinateX;
                fArr4[i3] = fArr4[i3] + originCoordinateY2;
                i3++;
                ballForwardExitProgress = ballForwardExitProgress;
                originCoordinateY2 = originCoordinateY2;
            }
            originCoordinateY = originCoordinateY2;
            radius2 = radius;
        }
        if (renderProgress > 0.75f || renderProgress <= 0.5f) {
            danceLoading2 = this;
            if (renderProgress > 0.75f) {
                danceLoading2.mRotation = 0.0f;
            }
        } else {
            danceLoading2 = this;
            danceLoading2.mRotation = (MATERIAL_INTERPOLATOR.getInterpolation((renderProgress - 0.5f) / 0.25f) * 360.0f) - 360.0f;
        }
        if (renderProgress > BALL_REVERSAL_END_ENTER_DURATION_OFFSET || renderProgress <= BALL_REVERSAL_START_ENTER_DURATION_OFFSET) {
            radius3 = radius2;
        } else {
            float ballReversalEnterProgress = (renderProgress - BALL_REVERSAL_START_ENTER_DURATION_OFFSET) / 0.125f;
            float f6 = ((0.5f - ballReversalEnterProgress) * danceLoading2.mDanceBallRadius) / DEFAULT_DANCE_BALL_RADIUS;
            danceLoading2.mShapeChangeHeight = f6;
            danceLoading2.mShapeChangeWidth = -f6;
            int i4 = 0;
            while (i4 < 3) {
                float k3 = (float) Math.tan(((double) (((float) ((i4 * 60) + 0)) / 360.0f)) * 6.283185307179586d);
                float progress3 = (0.5f - (ACCELERATE_INTERPOLATOR.getInterpolation(ballReversalEnterProgress) / DEFAULT_DANCE_BALL_RADIUS)) * DEFAULT_DANCE_BALL_RADIUS * ((float) DIRECTION[i4]);
                float[] fArr5 = POINT_X;
                fArr5[i4] = (float) (((double) radius2) + ((((double) radius2) / Math.sqrt(Math.pow((double) k3, 2.0d) + 1.0d)) * ((double) progress3)));
                float[] fArr6 = POINT_Y;
                fArr6[i4] = (fArr5[i4] - radius2) * k3;
                fArr5[i4] = fArr5[i4] + originCoordinateX;
                fArr6[i4] = fArr6[i4] + originCoordinateY;
                i4++;
                radius2 = radius2;
            }
            radius3 = radius2;
        }
        if (renderProgress <= 0.875f && renderProgress > CENTER_CIRCLE_REVERSAL_START_SCALE_DURATION_OFFSET) {
            float centerCircleScaleProgress2 = (renderProgress - CENTER_CIRCLE_REVERSAL_START_SCALE_DURATION_OFFSET) / 0.19999999f;
            if (centerCircleScaleProgress2 <= 0.5f) {
                danceLoading2.mScale = (DECELERATE_INTERPOLATOR.getInterpolation(centerCircleScaleProgress2 * DEFAULT_DANCE_BALL_RADIUS) * 0.2f) + 1.0f;
            } else {
                danceLoading2.mScale = 1.2f - (ACCELERATE_INTERPOLATOR.getInterpolation((centerCircleScaleProgress2 - 0.5f) * DEFAULT_DANCE_BALL_RADIUS) * 0.2f);
            }
        }
        if (renderProgress <= 1.0f && renderProgress > 0.875f) {
            float ballReversalExitProgress = (renderProgress - 0.875f) / 0.125f;
            float f7 = ((ballReversalExitProgress - 0.5f) * danceLoading2.mDanceBallRadius) / DEFAULT_DANCE_BALL_RADIUS;
            danceLoading2.mShapeChangeHeight = f7;
            danceLoading2.mShapeChangeWidth = -f7;
            for (int i5 = 0; i5 < 3; i5++) {
                float k4 = (float) Math.tan(((double) (((float) ((i5 * 60) + 0)) / 360.0f)) * 6.283185307179586d);
                float progress4 = (0.0f - (DECELERATE_INTERPOLATOR.getInterpolation(ballReversalExitProgress) / DEFAULT_DANCE_BALL_RADIUS)) * DEFAULT_DANCE_BALL_RADIUS * ((float) DIRECTION[i5]);
                float[] fArr7 = POINT_X;
                fArr7[i5] = (float) (((double) radius3) + (((double) progress4) * (((double) radius3) / Math.sqrt(Math.pow((double) k4, 2.0d) + 1.0d))));
                float[] fArr8 = POINT_Y;
                fArr8[i5] = (fArr7[i5] - radius3) * k4;
                fArr7[i5] = fArr7[i5] + originCoordinateX;
                fArr8[i5] = fArr8[i5] + originCoordinateY;
            }
        }
    }

    
    @Override
    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    
    @Override
    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    
    @Override
    public void reset() {
        this.mScale = 1.0f;
        this.mRotation = 0.0f;
    }

    private void setColor(int color) {
        this.mColor = color;
        this.mArcColor = halfAlphaColor(color);
    }

    private void setRotation(float rotation) {
        this.mRotation = rotation;
    }

    private void setDanceBallRadius(float danceBallRadius) {
        this.mDanceBallRadius = danceBallRadius;
    }

    private float getDanceBallRadius() {
        return this.mDanceBallRadius;
    }

    private float getRotation() {
        return this.mRotation;
    }

    private void setInsets(int width, int height) {
        float insets;
        float minEdge = (float) Math.min(width, height);
        float f = this.mCenterRadius;
        if (f <= 0.0f || minEdge < 0.0f) {
            insets = (float) Math.ceil((double) (this.mStrokeWidth / DEFAULT_DANCE_BALL_RADIUS));
        } else {
            insets = (minEdge / DEFAULT_DANCE_BALL_RADIUS) - f;
        }
        this.mStrokeInset = insets;
    }

    private int halfAlphaColor(int colorValue) {
        return ((((colorValue >> 24) & 255) / 2) << 24) | (((colorValue >> 16) & 255) << 16) | (((colorValue >> 8) & 255) << 8) | (colorValue & 255);
    }

    public static class Builder {
        private Context mContext;

        public Builder(Context mContext2) {
            this.mContext = mContext2;
        }

        public DanceLoading build() {
            return new DanceLoading(this.mContext);
        }
    }
}
