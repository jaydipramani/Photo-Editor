package com.relish.app.event;

import android.view.MotionEvent;

import com.relish.app.polish.PolishStickerView;


public interface StickerIconEvent {
    void onActionDown(PolishStickerView polishStickerView, MotionEvent motionEvent);

    void onActionMove(PolishStickerView polishStickerView, MotionEvent motionEvent);

    void onActionUp(PolishStickerView polishStickerView, MotionEvent motionEvent);
}
