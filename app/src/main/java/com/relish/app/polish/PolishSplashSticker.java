package com.relish.app.polish;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

import com.relish.app.sticker.Sticker;


public class PolishSplashSticker extends Sticker {
    private Bitmap bitmapOver;
    private Bitmap bitmapXor;
    private Paint over;
    private Paint xor;

    public PolishSplashSticker(Bitmap paramBitmap1, Bitmap paramBitmap2) {
        Paint paint = new Paint();
        this.xor = paint;
        paint.setDither(true);
        this.xor.setAntiAlias(true);
        this.xor.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        Paint paint2 = new Paint();
        this.over = paint2;
        paint2.setDither(true);
        this.over.setAntiAlias(true);
        this.over.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        this.bitmapXor = paramBitmap1;
        this.bitmapOver = paramBitmap2;
    }

    private Bitmap getBitmapOver() {
        return this.bitmapOver;
    }

    private Bitmap getBitmapXor() {
        return this.bitmapXor;
    }

    @Override 
    public void draw(Canvas paramCanvas) {
        paramCanvas.drawBitmap(getBitmapXor(), getMatrix(), this.xor);
        paramCanvas.drawBitmap(getBitmapOver(), getMatrix(), this.over);
    }

    @Override 
    public int getAlpha() {
        return 1;
    }

    @Override 
    public Drawable getDrawable() {
        return null;
    }

    @Override 
    public int getHeight() {
        return this.bitmapOver.getHeight();
    }

    @Override 
    public int getWidth() {
        return this.bitmapXor.getWidth();
    }

    @Override 
    public void release() {
        super.release();
        this.xor = null;
        this.over = null;
        Bitmap bitmap = this.bitmapXor;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.bitmapXor = null;
        Bitmap bitmap2 = this.bitmapOver;
        if (bitmap2 != null) {
            bitmap2.recycle();
        }
        this.bitmapOver = null;
    }

    @Override 
    public PolishSplashSticker setAlpha(int paramInt) {
        return this;
    }

    @Override 
    public PolishSplashSticker setDrawable(Drawable paramDrawable) {
        return this;
    }
}
