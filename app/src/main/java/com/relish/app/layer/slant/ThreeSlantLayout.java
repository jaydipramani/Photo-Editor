package com.relish.app.layer.slant;

import com.relish.app.polish.grid.PolishLayout;
import com.relish.app.polish.grid.PolishLine;

public class ThreeSlantLayout extends NumberSlantLayout {
    @Override
    public int getThemeCount() {
        return 6;
    }

    public ThreeSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
        this.theme = ((NumberSlantLayout) slantPuzzleLayout).getTheme();
    }

    public ThreeSlantLayout(int i) {
        super(i);
    }

    @Override
    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, PolishLine.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 1:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PolishLine.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 2:
                addLine(0, PolishLine.Direction.VERTICAL, 0.5f);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.56f, 0.44f);
                return;
            case 3:
                addLine(0, PolishLine.Direction.VERTICAL, 0.5f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.56f, 0.44f);
                return;
            case 4:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.44f, 0.56f);
                addLine(0, PolishLine.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 5:
                addLine(0, PolishLine.Direction.VERTICAL, 0.56f, 0.44f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.44f, 0.56f);
                return;
            default:
                return;
        }
    }

    @Override
    public PolishLayout clone(PolishLayout quShotLayout) {
        return new ThreeSlantLayout((SlantCollageLayout) quShotLayout, true);
    }
}
