package com.relish.app.layer.straight;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.relish.app.polish.grid.PolishLine;

public class StraightLine implements PolishLine {
    StraightLine attachLineEnd;
    StraightLine attachLineStart;
    private RectF bounds = new RectF();
    public PolishLine.Direction direction = PolishLine.Direction.HORIZONTAL;
    private PointF end;
    private float endRatio;
    private PolishLine lowerLine;
    private PointF previousEnd = new PointF();
    private PointF previousStart = new PointF();
    private PointF start;
    private float startRatio;
    private PolishLine upperLine;

    StraightLine(PointF pointF, PointF pointF2) {
        this.start = pointF;
        this.end = pointF2;
        if (pointF.x == pointF2.x) {
            this.direction = PolishLine.Direction.VERTICAL;
        } else if (pointF.y == pointF2.y) {
            this.direction = PolishLine.Direction.HORIZONTAL;
        } else {
            Log.d("StraightLine", "StraightLine: current only support two direction");
        }
    }

    @Override 
    public void setStartRatio(float f) {
        this.startRatio = f;
    }

    @Override 
    public float getStartRatio() {
        return this.startRatio;
    }

    @Override 
    public void setEndRatio(float f) {
        this.endRatio = f;
    }

    @Override 
    public float getEndRatio() {
        return this.endRatio;
    }

    @Override 
    public float length() {
        return (float) Math.sqrt(Math.pow((double) (this.end.x - this.start.x), 2.0d) + Math.pow((double) (this.end.y - this.start.y), 2.0d));
    }

    @Override 
    public PointF startPoint() {
        return this.start;
    }

    @Override 
    public PointF endPoint() {
        return this.end;
    }

    @Override 
    public PolishLine lowerLine() {
        return this.lowerLine;
    }

    @Override 
    public PolishLine upperLine() {
        return this.upperLine;
    }

    @Override 
    public PolishLine attachStartLine() {
        return this.attachLineStart;
    }

    @Override 
    public PolishLine attachEndLine() {
        return this.attachLineEnd;
    }

    @Override 
    public void setLowerLine(PolishLine line) {
        this.lowerLine = line;
    }

    @Override 
    public void setUpperLine(PolishLine line) {
        this.upperLine = line;
    }

    public void setAttachLineStart(StraightLine straightLine) {
        this.attachLineStart = straightLine;
    }

    public void setAttachLineEnd(StraightLine straightLine) {
        this.attachLineEnd = straightLine;
    }

    @Override 
    public PolishLine.Direction direction() {
        return this.direction;
    }

    @Override 
    public float slope() {
        return this.direction == PolishLine.Direction.HORIZONTAL ? 0.0f : Float.MAX_VALUE;
    }

    @Override 
    public boolean contains(float f, float f2, float f3) {
        if (this.direction == PolishLine.Direction.HORIZONTAL) {
            this.bounds.left = this.start.x;
            this.bounds.right = this.end.x;
            float f4 = f3 / 2.0f;
            this.bounds.top = this.start.y - f4;
            this.bounds.bottom = this.start.y + f4;
        } else if (this.direction == PolishLine.Direction.VERTICAL) {
            this.bounds.top = this.start.y;
            this.bounds.bottom = this.end.y;
            float f5 = f3 / 2.0f;
            this.bounds.left = this.start.x - f5;
            this.bounds.right = this.start.x + f5;
        }
        return this.bounds.contains(f, f2);
    }

    @Override 
    public void prepareMove() {
        this.previousStart.set(this.start);
        this.previousEnd.set(this.end);
    }

    @Override 
    public boolean move(float f, float f2) {
        if (this.direction == PolishLine.Direction.HORIZONTAL) {
            if (this.previousStart.y + f < this.lowerLine.maxY() + f2 || this.previousStart.y + f > this.upperLine.minY() - f2 || this.previousEnd.y + f < this.lowerLine.maxY() + f2 || this.previousEnd.y + f > this.upperLine.minY() - f2) {
                return false;
            }
            this.start.y = this.previousStart.y + f;
            this.end.y = this.previousEnd.y + f;
            return true;
        } else if (this.previousStart.x + f < this.lowerLine.maxX() + f2 || this.previousStart.x + f > this.upperLine.minX() - f2 || this.previousEnd.x + f < this.lowerLine.maxX() + f2 || this.previousEnd.x + f > this.upperLine.minX() - f2) {
            return false;
        } else {
            this.start.x = this.previousStart.x + f;
            this.end.x = this.previousEnd.x + f;
            return true;
        }
    }

    @Override 
    public void update(float f, float f2) {
        if (this.direction == PolishLine.Direction.HORIZONTAL) {
            StraightLine straightLine = this.attachLineStart;
            if (straightLine != null) {
                this.start.x = straightLine.getPosition();
            }
            StraightLine straightLine2 = this.attachLineEnd;
            if (straightLine2 != null) {
                this.end.x = straightLine2.getPosition();
            }
        } else if (this.direction == PolishLine.Direction.VERTICAL) {
            StraightLine straightLine3 = this.attachLineStart;
            if (straightLine3 != null) {
                this.start.y = straightLine3.getPosition();
            }
            StraightLine straightLine4 = this.attachLineEnd;
            if (straightLine4 != null) {
                this.end.y = straightLine4.getPosition();
            }
        }
    }

    public float getPosition() {
        if (this.direction == PolishLine.Direction.HORIZONTAL) {
            return this.start.y;
        }
        return this.start.x;
    }

    @Override 
    public float minX() {
        return Math.min(this.start.x, this.end.x);
    }

    @Override 
    public float maxX() {
        return Math.max(this.start.x, this.end.x);
    }

    @Override 
    public float minY() {
        return Math.min(this.start.y, this.end.y);
    }

    @Override 
    public float maxY() {
        return Math.max(this.start.y, this.end.y);
    }

    @Override 
    public void offset(float f, float f2) {
        this.start.offset(f, f2);
        this.end.offset(f, f2);
    }

    public String toString() {
        return "start --> " + this.start.toString() + ",end --> " + this.end.toString();
    }
}
