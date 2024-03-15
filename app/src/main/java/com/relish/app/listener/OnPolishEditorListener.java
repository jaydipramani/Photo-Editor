package com.relish.app.listener;

import com.relish.app.draw.Drawing;

public interface OnPolishEditorListener {
    void onAddViewListener(Drawing drawing, int i);

    void onRemoveViewListener(int i);

    void onRemoveViewListener(Drawing drawing, int i);

    void onStartViewChangeListener(Drawing drawing);

    void onStopViewChangeListener(Drawing drawing);
}
