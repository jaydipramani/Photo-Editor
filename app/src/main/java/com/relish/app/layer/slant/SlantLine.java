package com.relish.app.layer.slant;

import android.graphics.PointF;

import com.relish.app.polish.grid.PolishLine;


public class SlantLine implements PolishLine {
    SlantLine attachLineEnd;
    SlantLine attachLineStart;
    public final PolishLine.Direction direction;
    CrossoverPointF end;
    private float endRatio;
    PolishLine lowerLine;
    private PointF previousEnd = new PointF();
    private PointF previousStart = new PointF();
    CrossoverPointF start;
    private float startRatio;
    PolishLine upperLine;

    SlantLine(PolishLine.Direction direction2) {
        this.direction = direction2;
    }

    SlantLine(CrossoverPointF crossoverPointF, CrossoverPointF crossoverPointF2, PolishLine.Direction direction2) {
        this.start = crossoverPointF;
        this.end = crossoverPointF2;
        this.direction = direction2;
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

    @Override 
    public PolishLine.Direction direction() {
        return this.direction;
    }

    @Override 
    public float slope() {
        return SlantUtils.calculateSlope(this);
    }

    @Override 
    public boolean contains(float f, float f2, float f3) {
        return SlantUtils.contains(this, f, f2, f3);
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
    public void prepareMove() {
        this.previousStart.set(this.start);
        this.previousEnd.set(this.end);
    }

    @Override 
    public void update(float f, float f2) {
        SlantUtils.intersectionOfLines(this.start, this, this.attachLineStart);
        SlantUtils.intersectionOfLines(this.end, this, this.attachLineEnd);
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
