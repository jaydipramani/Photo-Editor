package com.relish.app.event;

import android.view.MotionEvent;

import com.relish.app.polish.PolishStickerView;


public class EditTextIconEvent implements StickerIconEvent {
    @Override
    public void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    @Override
    public void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    @Override
    public void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.editTextSticker();
    }
}
