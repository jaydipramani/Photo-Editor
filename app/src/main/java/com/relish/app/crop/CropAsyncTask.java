package com.relish.app.crop;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.relish.app.Utils.MotionUtils;
import com.relish.app.constants.StoreManager;
import com.relish.app.support.SupportedClass;


public class CropAsyncTask extends AsyncTask<Void, Void, Void> {
    Activity activity;
    Bitmap croppedBitmap;
    int left;
    Bitmap maskBitmap;
    CropTaskCompleted onTaskCompleted;
    ProgressBar progressBar;
    int top;

    private void show(final boolean z) {
        Activity activity2 = this.activity;
        if (activity2 != null) {
            activity2.runOnUiThread(new Runnable() {

                public void run() {
                    if (z) {
                        CropAsyncTask.this.progressBar.setVisibility(View.VISIBLE);
                    } else {
                        CropAsyncTask.this.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public CropAsyncTask(CropTaskCompleted mLOnCropTaskCompleted, Activity activity2, ProgressBar progressBar2) {
        this.onTaskCompleted = mLOnCropTaskCompleted;
        this.activity = activity2;
        this.progressBar = progressBar2;
    }

    public void onPreExecute() {
        super.onPreExecute();
        show(true);
    }

    public Void doInBackground(Void... voidArr) {
        this.croppedBitmap = StoreManager.getCurrentCropedBitmap(this.activity);
        this.left = StoreManager.croppedLeft;
        this.top = StoreManager.croppedTop;
        Bitmap currentCroppedMaskBitmap = StoreManager.getCurrentCroppedMaskBitmap(this.activity);
        this.maskBitmap = currentCroppedMaskBitmap;
        if (this.croppedBitmap != null || currentCroppedMaskBitmap != null) {
            return null;
        }
        DeeplabMobile deeplabMobile = new DeeplabMobile();
        deeplabMobile.initialize(this.activity.getApplicationContext());
        Bitmap loadInBackground = loadInBackground(StoreManager.getCurrentOriginalBitmap(this.activity), deeplabMobile);
        this.croppedBitmap = loadInBackground;
        StoreManager.setCurrentCropedBitmap(this.activity, loadInBackground);
        return null;
    }

    public void onPostExecute(Void voidR) {
        show(false);
        this.onTaskCompleted.onTaskCompleted(this.croppedBitmap, this.maskBitmap, this.left, this.top);
    }

    public Bitmap loadInBackground(Bitmap bitmap, DeeplabMobile deeplabMobile) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float max = 513.0f / ((float) Math.max(bitmap.getWidth(), bitmap.getHeight()));
        int round = Math.round(((float) width) * max);
        int round2 = Math.round(((float) height) * max);
        Bitmap segment = deeplabMobile.segment(SupportedClass.tfResizeBilinear(bitmap, round, round2));
        this.maskBitmap = segment;
        if (segment == null) {
            return null;
        }
        Bitmap createClippedBitmap = BitmapUtils.createClippedBitmap(segment, (segment.getWidth() - round) / 2, (this.maskBitmap.getHeight() - round2) / 2, round, round2);
        this.maskBitmap = createClippedBitmap;
        Bitmap scaleBitmap = BitmapUtils.scaleBitmap(createClippedBitmap, width, height);
        this.maskBitmap = scaleBitmap;
        this.left = (scaleBitmap.getWidth() - width) / 2;
        this.top = (this.maskBitmap.getHeight() - height) / 2;
        StoreManager.croppedLeft = this.left;
        int i = this.top;
        StoreManager.croppedTop = i;
        this.top = i;
        StoreManager.setCurrentCroppedMaskBitmap(this.activity, this.maskBitmap);
        return MotionUtils.cropBitmapWithMask(bitmap, this.maskBitmap, 0, 0);
    }
}
