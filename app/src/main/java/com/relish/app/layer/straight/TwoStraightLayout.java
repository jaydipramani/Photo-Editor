package com.relish.app.layer.straight;


import com.relish.app.polish.grid.PolishLayout;
import com.relish.app.polish.grid.PolishLine;

public class TwoStraightLayout extends NumberStraightLayout {
    private float mRadio = 0.5f;

    @Override
    public int getThemeCount() {
        return 6;
    }

    public TwoStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public TwoStraightLayout(int i) {
        super(i);
    }

    @Override
    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PolishLine.Direction.HORIZONTAL, this.mRadio);
                return;
            case 1:
                addLine(0, PolishLine.Direction.VERTICAL, this.mRadio);
                return;
            case 2:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.33333334f);
                return;
            case 3:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.6666667f);
                return;
            case 4:
                addLine(0, PolishLine.Direction.VERTICAL, 0.33333334f);
                return;
            case 5:
                addLine(0, PolishLine.Direction.VERTICAL, 0.6666667f);
                return;
            default:
                addLine(0, PolishLine.Direction.HORIZONTAL, this.mRadio);
                return;
        }
    }

    @Override
    public PolishLayout clone(PolishLayout quShotLayout) {
        return new TwoStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
