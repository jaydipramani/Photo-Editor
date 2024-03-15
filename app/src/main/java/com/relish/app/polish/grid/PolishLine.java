package com.relish.app.polish.grid;

import android.graphics.PointF;

public interface PolishLine {

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    PolishLine attachEndLine();

    PolishLine attachStartLine();

    boolean contains(float f, float f2, float f3);

    Direction direction();

    PointF endPoint();

    float getEndRatio();

    float getStartRatio();

    float length();

    PolishLine lowerLine();

    float maxX();

    float maxY();

    float minX();

    float minY();

    boolean move(float f, float f2);

    void offset(float f, float f2);

    void prepareMove();

    void setEndRatio(float f);

    void setLowerLine(PolishLine polishLine);

    void setStartRatio(float f);

    void setUpperLine(PolishLine polishLine);

    float slope();

    PointF startPoint();

    void update(float f, float f2);

    PolishLine upperLine();
}
