package com.relish.app.sticker;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class DrawableSticker extends Sticker {
    private Drawable drawable;
    private Rect realBounds = new Rect(0, 0, getWidth(), getHeight());

    public DrawableSticker(Drawable paramDrawable) {
        this.drawable = paramDrawable;
    }

    @Override
    public void draw(Canvas paramCanvas) {
        paramCanvas.save();
        paramCanvas.concat(getMatrix());
        this.drawable.setBounds(this.realBounds);
        this.drawable.draw(paramCanvas);
        paramCanvas.restore();
    }

    @Override
    public int getAlpha() {
        return this.drawable.getAlpha();
    }

    @Override
    public Drawable getDrawable() {
        return this.drawable;
    }

    @Override
    public int getHeight() {
        Drawable drawable2 = this.drawable;
        if (drawable2 != null) {
            return drawable2.getIntrinsicHeight();
        }
        return 50;
//        return drawable.getIntrinsicHeight();
    }

    @Override
    public int getWidth() {
        Drawable drawable2 = this.drawable;
        if (drawable2 != null) {
            return drawable2.getIntrinsicWidth();
        }
        return 50;
//        return drawable.getIntrinsicWidth();
    }

    @Override
    public void release() {
        super.release();
        if (this.drawable != null) {
            this.drawable = null;
        }
    }

    @Override
    public DrawableSticker setAlpha(int paramInt) {
        this.drawable.setAlpha(paramInt);
        return this;
    }

    @Override
    public DrawableSticker setDrawable(Drawable paramDrawable) {
        this.drawable = paramDrawable;
        return this;
    }
}
