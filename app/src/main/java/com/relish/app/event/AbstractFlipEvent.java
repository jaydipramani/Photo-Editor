package com.relish.app.event;

import android.view.MotionEvent;

import com.relish.app.polish.PolishStickerView;

public abstract class AbstractFlipEvent implements StickerIconEvent {
    public abstract int getFlipDirection();

    @Override
    public void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    @Override
    public void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    @Override
    public void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.flipCurrentSticker(getFlipDirection());
    }
}
