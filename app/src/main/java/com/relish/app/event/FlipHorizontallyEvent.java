package com.relish.app.event;

public class FlipHorizontallyEvent extends AbstractFlipEvent {

    @Override
    public int getFlipDirection() {
        return 1;
    }
}
