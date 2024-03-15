package com.relish.app.loading;

import android.content.Context;

public class DensityUtil {
    public static float dip2px(Context context, float dpValue) {
        return dpValue * context.getResources().getDisplayMetrics().density;
    }
}
