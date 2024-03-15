package com.relish.app.layer.straight;


import com.relish.app.polish.grid.PolishLayout;
import com.relish.app.polish.grid.PolishLine;

public class ThreeStraightLayout extends NumberStraightLayout {
    @Override
    public int getThemeCount() {
        return 6;
    }

    public ThreeStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public ThreeStraightLayout(int i) {
        super(i);
    }

    @Override
    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, PolishLine.Direction.VERTICAL, 0.5f);
                return;
            case 1:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PolishLine.Direction.VERTICAL, 0.5f);
                return;
            case 2:
                addLine(0, PolishLine.Direction.VERTICAL, 0.5f);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 3:
                addLine(0, PolishLine.Direction.VERTICAL, 0.5f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 4:
                cutAreaEqualPart(0, 3, PolishLine.Direction.HORIZONTAL);
                return;
            case 5:
                cutAreaEqualPart(0, 3, PolishLine.Direction.VERTICAL);
                return;
            default:
                cutAreaEqualPart(0, 3, PolishLine.Direction.HORIZONTAL);
                return;
        }
    }

    @Override
    public PolishLayout clone(PolishLayout quShotLayout) {
        return new ThreeStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
