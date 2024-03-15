package com.relish.app.event;

import com.relish.app.entity.Photo;

public interface Selectable {
    int getSelectedItemCount();

    boolean isSelected(Photo photo);
}
