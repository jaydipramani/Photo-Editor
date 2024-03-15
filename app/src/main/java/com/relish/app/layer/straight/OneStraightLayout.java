package com.relish.app.layer.straight;


import com.relish.app.polish.grid.PolishLayout;
import com.relish.app.polish.grid.PolishLine;

public class OneStraightLayout extends NumberStraightLayout {
    @Override
    public int getThemeCount() {
        return 6;
    }

    public OneStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public OneStraightLayout(int i) {
        super(i);
    }

    @Override
    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 1:
                addLine(0, PolishLine.Direction.VERTICAL, 0.5f);
                return;
            case 2:
                addCross(0, 0.5f);
                return;
            case 3:
                cutAreaEqualPart(0, 2, 1);
                return;
            case 4:
                cutAreaEqualPart(0, 1, 2);
                return;
            case 5:
                cutAreaEqualPart(0, 2, 2);
                return;
            default:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
        }
    }

    @Override
    public PolishLayout clone(PolishLayout quShotLayout) {
        return new OneStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
