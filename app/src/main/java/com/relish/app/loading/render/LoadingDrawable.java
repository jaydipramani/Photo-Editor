package com.relish.app.loading.render;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public class LoadingDrawable extends Drawable implements Animatable {
    private final Callback mCallback;
    private final LoadingRenderer mLoadingRender;

    public LoadingDrawable(LoadingRenderer loadingRender) {
        Callback callback = new Callback() {


            public void invalidateDrawable(Drawable d) {
                LoadingDrawable.this.invalidateSelf();
            }

            public void scheduleDrawable(Drawable d, Runnable what, long when) {
                LoadingDrawable.this.scheduleSelf(what, when);
            }

            public void unscheduleDrawable(Drawable d, Runnable what) {
                LoadingDrawable.this.unscheduleSelf(what);
            }
        };
        this.mCallback = callback;
        this.mLoadingRender = loadingRender;
        loadingRender.setCallback(callback);
    }

    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mLoadingRender.setBounds(bounds);
    }

    public void draw(Canvas canvas) {
        if (!getBounds().isEmpty()) {
            this.mLoadingRender.draw(canvas);
        }
    }

    public void setAlpha(int alpha) {
        this.mLoadingRender.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mLoadingRender.setColorFilter(cf);
    }

    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void start() {
        this.mLoadingRender.start();
    }

    public void stop() {
        this.mLoadingRender.stop();
    }

    public boolean isRunning() {
        return this.mLoadingRender.isRunning();
    }

    public int getIntrinsicHeight() {
        return (int) this.mLoadingRender.mHeight;
    }

    public int getIntrinsicWidth() {
        return (int) this.mLoadingRender.mWidth;
    }
}
