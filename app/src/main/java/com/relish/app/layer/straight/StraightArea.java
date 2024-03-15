package com.relish.app.layer.straight;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.relish.app.polish.grid.PolishArea;
import com.relish.app.polish.grid.PolishLine;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class StraightArea implements PolishArea {
    private Path areaPath = new Path();
    private RectF areaRect = new RectF();
    private PointF[] handleBarPoints;
    StraightLine lineBottom;
    StraightLine lineLeft;
    StraightLine lineRight;
    StraightLine lineTop;
    private float paddingBottom;
    private float paddingLeft;
    private float paddingRight;
    private float paddingTop;
    private float radian;

    StraightArea() {
        PointF[] pointFArr = new PointF[2];
        this.handleBarPoints = pointFArr;
        pointFArr[0] = new PointF();
        this.handleBarPoints[1] = new PointF();
    }

    StraightArea(StraightArea straightArea) {
        PointF[] pointFArr = new PointF[2];
        this.handleBarPoints = pointFArr;
        this.lineLeft = straightArea.lineLeft;
        this.lineTop = straightArea.lineTop;
        this.lineRight = straightArea.lineRight;
        this.lineBottom = straightArea.lineBottom;
        pointFArr[0] = new PointF();
        this.handleBarPoints[1] = new PointF();
    }

    @Override
    public float left() {
        return this.lineLeft.minX() + this.paddingLeft;
    }

    @Override
    public float top() {
        return this.lineTop.minY() + this.paddingTop;
    }

    @Override
    public float right() {
        return this.lineRight.maxX() - this.paddingRight;
    }

    @Override
    public float bottom() {
        return this.lineBottom.maxY() - this.paddingBottom;
    }

    @Override
    public float centerX() {
        return (left() + right()) / 2.0f;
    }

    @Override
    public float centerY() {
        return (top() + bottom()) / 2.0f;
    }

    @Override
    public float width() {
        return right() - left();
    }

    @Override
    public float height() {
        return bottom() - top();
    }

    @Override
    public PointF getCenterPoint() {
        return new PointF(centerX(), centerY());
    }

    @Override
    public boolean contains(PointF pointF) {
        return contains(pointF.x, pointF.y);
    }

    @Override
    public boolean contains(float f, float f2) {
        return getAreaRect().contains(f, f2);
    }

    @Override
    public boolean contains(PolishLine line) {
        return this.lineLeft == line || this.lineTop == line || this.lineRight == line || this.lineBottom == line;
    }

    @Override
    public Path getAreaPath() {
        this.areaPath.reset();
        Path path = this.areaPath;
        RectF areaRect2 = getAreaRect();
        float f = this.radian;
        path.addRoundRect(areaRect2, f, f, Path.Direction.CCW);
        return this.areaPath;
    }

    @Override
    public RectF getAreaRect() {
        this.areaRect.set(left(), top(), right(), bottom());
        return this.areaRect;
    }

    @Override
    public List<PolishLine> getLines() {
        return Arrays.asList(this.lineLeft, this.lineTop, this.lineRight, this.lineBottom);
    }

    @Override
    public PointF[] getHandleBarPoints(PolishLine line) {
        if (line == this.lineLeft) {
            this.handleBarPoints[0].x = left();
            this.handleBarPoints[0].y = top() + (height() / 4.0f);
            this.handleBarPoints[1].x = left();
            this.handleBarPoints[1].y = top() + ((height() / 4.0f) * 3.0f);
        } else if (line == this.lineTop) {
            this.handleBarPoints[0].x = left() + (width() / 4.0f);
            this.handleBarPoints[0].y = top();
            this.handleBarPoints[1].x = left() + ((width() / 4.0f) * 3.0f);
            this.handleBarPoints[1].y = top();
        } else if (line == this.lineRight) {
            this.handleBarPoints[0].x = right();
            this.handleBarPoints[0].y = top() + (height() / 4.0f);
            this.handleBarPoints[1].x = right();
            this.handleBarPoints[1].y = top() + ((height() / 4.0f) * 3.0f);
        } else if (line == this.lineBottom) {
            this.handleBarPoints[0].x = left() + (width() / 4.0f);
            this.handleBarPoints[0].y = bottom();
            this.handleBarPoints[1].x = left() + ((width() / 4.0f) * 3.0f);
            this.handleBarPoints[1].y = bottom();
        }
        return this.handleBarPoints;
    }

    @Override
    public float radian() {
        return this.radian;
    }

    @Override
    public void setRadian(float f) {
        this.radian = f;
    }

    @Override
    public float getPaddingLeft() {
        return this.paddingLeft;
    }

    @Override
    public float getPaddingTop() {
        return this.paddingTop;
    }

    @Override
    public float getPaddingRight() {
        return this.paddingRight;
    }

    @Override
    public float getPaddingBottom() {
        return this.paddingBottom;
    }

    @Override
    public void setPadding(float f) {
        setPadding(f, f, f, f);
    }

    @Override
    public void setPadding(float f, float f2, float f3, float f4) {
        this.paddingLeft = f;
        this.paddingTop = f2;
        this.paddingRight = f3;
        this.paddingBottom = f4;
    }

   
    public static class AreaComparator implements Comparator<StraightArea> {
        AreaComparator() {
        }

        public int compare(StraightArea straightArea, StraightArea straightArea2) {
            if (straightArea.top() < straightArea2.top()) {
                return -1;
            }
            if (straightArea.top() != straightArea2.top()) {
                return 1;
            }
            if (straightArea.left() < straightArea2.left()) {
                return -1;
            }
            if (straightArea.left() == straightArea2.left()) {
                return 0;
            }
            return 1;
        }
    }
}
