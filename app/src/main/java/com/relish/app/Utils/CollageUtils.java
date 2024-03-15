package com.relish.app.Utils;


import com.relish.app.layer.slant.SlantLayoutHelper;
import com.relish.app.layer.straight.StraightLayoutHelper;
import com.relish.app.polish.grid.PolishLayout;

import java.util.ArrayList;
import java.util.List;

public class CollageUtils {
    private CollageUtils() {
    }

    public static List<PolishLayout> getCollageLayouts(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(SlantLayoutHelper.getAllThemeLayout(i));
        arrayList.addAll(StraightLayoutHelper.getAllThemeLayout(i));
        return arrayList;
    }
}
