package com.relish.app.polish;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.relish.app.event.StickerIconEvent;
import com.relish.app.sticker.DrawableSticker;


public class PolishStickerIcons extends DrawableSticker implements StickerIconEvent {
    public static final String ALIGN_HORIZONTALLY = "ALIGN_HORIZONTALLY";
    public static final String EDIT = "EDIT";
    public static final String FLIP = "FLIP";
    public static final String REMOVE = "REMOVE";
    public static final String ROTATE = "ROTATE";
    public static final String ZOOM = "ZOOM";
    private StickerIconEvent iconEvent;
    private float iconExtraRadius = 10.0f;
    private float iconRadius = 30.0f;
    private int position = 0;
    private String tag;
    private float x;
    private float y;

    public PolishStickerIcons(Drawable paramDrawable, int paramInt, String paramString) {
        super(paramDrawable);
        this.position = paramInt;
        this.tag = paramString;
    }

    public void draw(Canvas paramCanvas, Paint paramPaint) {
        paramCanvas.drawCircle(this.x, this.y, this.iconRadius, paramPaint);
        draw(paramCanvas);
    }

    public float getIconRadius() {
        return this.iconRadius;
    }

    public int getPosition() {
        return this.position;
    }

    public String getTag() {
        return this.tag;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @Override 
    public void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        StickerIconEvent stickerIconEvent = this.iconEvent;
        if (stickerIconEvent != null) {
            stickerIconEvent.onActionDown(paramStickerView, paramMotionEvent);
        }
    }

    @Override 
    public void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        StickerIconEvent stickerIconEvent = this.iconEvent;
        if (stickerIconEvent != null) {
            stickerIconEvent.onActionMove(paramStickerView, paramMotionEvent);
        }
    }

    @Override 
    public void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        StickerIconEvent stickerIconEvent = this.iconEvent;
        if (stickerIconEvent != null) {
            stickerIconEvent.onActionUp(paramStickerView, paramMotionEvent);
        }
    }

    public void setIconEvent(StickerIconEvent paramStickerIconEvent) {
        this.iconEvent = paramStickerIconEvent;
    }

    public void setTag(String paramString) {
        this.tag = paramString;
    }

    public void setX(float paramFloat) {
        this.x = paramFloat;
    }

    public void setY(float paramFloat) {
        this.y = paramFloat;
    }
}
