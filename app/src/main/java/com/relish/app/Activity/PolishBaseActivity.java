package com.relish.app.Activity;

import android.content.res.Resources;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;


import com.relish.app.model.PathModelPix;
import com.relish.app.support.Constants;

import java.util.ArrayList;

public class PolishBaseActivity extends AppCompatActivity {
    public void isPermissionGranted(boolean z, String str) {
    }

    public void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 52) {
            isPermissionGranted(iArr[0] == 0, strArr[0]);
        }
    }

    public ArrayList<PathModelPix> getIconAllFrames() {
        ArrayList<PathModelPix> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 38; i++) {
            PathModelPix pathModel = new PathModelPix();
            Resources resources = getResources();
            pathModel.setPathInt(Integer.valueOf(resources.getIdentifier(Constants.PIX_ICON_FILE_NAME + i, "drawable", getPackageName())));
            arrSticker.add(pathModel);
        }
        return arrSticker;
    }

    public ArrayList<PathModelPix> getMaskAll() {
        ArrayList<PathModelPix> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 38; i++) {
            PathModelPix pathModel = new PathModelPix();
            Resources resources = getResources();
            pathModel.setPathInt(Integer.valueOf(resources.getIdentifier(Constants.PIX_MASK_FILE_NAME + i, "drawable", getPackageName())));
            arrSticker.add(pathModel);
        }
        return arrSticker;
    }
}
