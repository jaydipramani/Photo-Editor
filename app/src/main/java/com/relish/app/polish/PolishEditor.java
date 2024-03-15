package com.relish.app.polish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.relish.app.draw.BrushDrawingView;
import com.relish.app.draw.DrawModel;
import com.relish.app.draw.Drawing;
import com.relish.app.draw.OnSaveBitmap;
import com.relish.app.listener.BrushColorChangeListener;
import com.relish.app.listener.OnPolishEditorListener;

import org.wysaid.view.ImageGLSurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PolishEditor implements BrushColorChangeListener {
    private static final String TAG = "PolishEditor";
    private BrushDrawingView brushDrawingView;
    private Context context;
    private View deleteView;
    private List<String> filterlist;
    private int fliterindex;
    private ImageGLSurfaceView glSurfaceView;
    private boolean isTextPinchZoomable;
    private Typeface mDefaultEmojiTypeface;
    private Typeface mDefaultTextTypeface;
    private final LayoutInflater mLayoutInflater;
    private OnPolishEditorListener mOnPhotoEditorListener;
    public PolishView parentView;
    private List<View> redoViews;
    private List<View> viewList;

    public interface OnSaveListener {
        void onFailure(Exception exc);

        void onSuccess(String str);
    }

    private PolishEditor(Builder builder) {
        this.fliterindex = -1;
        this.context = builder.context;
        this.parentView = builder.parentView;
        this.deleteView = builder.deleteView;
        this.brushDrawingView = builder.brushDrawingView;
        this.glSurfaceView = builder.glSurfaceView;
        this.isTextPinchZoomable = builder.isTextPinchZoomable;
        this.mDefaultTextTypeface = builder.textTypeface;
        this.mDefaultEmojiTypeface = builder.emojiTypeface;
        this.mLayoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.brushDrawingView.setBrushViewChangeListener(this);
        this.viewList = new ArrayList();
        this.redoViews = new ArrayList();
        this.filterlist = new ArrayList();
    }

    public BrushDrawingView getBrushDrawingView() {
        return this.brushDrawingView;
    }

    public void setBrushDrawingMode(boolean z) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushDrawingMode(z);
        }
    }

    public void setAdjustFilter(String str) {
        this.glSurfaceView.setFilterWithConfig(str);
    }

    public void setFilterIntensityForIndex(float f, int i, boolean z) {
        this.glSurfaceView.setFilterIntensityForIndex(f, i, z);
    }

    public void setBrushMode(int i) {
        this.brushDrawingView.setDrawMode(i);
    }

    public void setBrushMagic(DrawModel drawBitmapModel) {
        this.brushDrawingView.setCurrentMagicBrush(drawBitmapModel);
    }

    public void setBrushSize(float f) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushSize(f);
        }
    }

    public void setBrushColor(int i) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushColor(i);
        }
    }

    public void setBrushEraserSize(float f) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushEraserSize(f);
        }
    }

    public void brushEraser() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.brushEraser();
        }
    }

    public boolean undo() {
        if (this.fliterindex <= 0) {
            return false;
        }
        Log.d(TAG, "undo: " + this.fliterindex);
        PolishView polishView = this.parentView;
        List<String> list = this.filterlist;
        int i = this.fliterindex - 1;
        this.fliterindex = i;
        polishView.setFilterEffect(list.get(i));
        return true;
    }

    public boolean redo() {
        if (this.fliterindex >= this.filterlist.size()) {
            return false;
        }
        Log.d(TAG, "redo: " + this.fliterindex);
        PolishView polishView = this.parentView;
        List<String> list = this.filterlist;
        int i = this.fliterindex + 1;
        this.fliterindex = i;
        polishView.setFilterEffect(list.get(i));
        return true;
    }

    public void redoBrush() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.redo();
        }
    }

    public void undoBrush() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.undo();
        }
    }

    public void setPaintOpacity(int i) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setPaintOpacity((int) ((((double) i) / 100.0d) * 255.0d));
        }
    }

    public void setMagicOpacity(int i) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setMagicOpacity((int) ((((double) i) / 100.0d) * 255.0d));
        }
    }

    public void clearBrushAllViews() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.clearAll();
        }
    }

    public void clearAllViews() {
        for (int i = 0; i < this.viewList.size(); i++) {
            this.parentView.removeView(this.viewList.get(i));
        }
        if (this.viewList.contains(this.brushDrawingView)) {
            this.parentView.addView(this.brushDrawingView);
        }
        this.viewList.clear();
        this.redoViews.clear();
        clearBrushAllViews();
    }

//    public void clearHelperBox() {
//        for (int i = 0; i < this.parentView.getChildCount(); i++) {
//            View childAt = this.parentView.getChildAt(i);
//            FrameLayout frameLayout = (FrameLayout) childAt.findViewById(R.id.frmBorder);
//            if (frameLayout != null) {
//                frameLayout.setBackgroundResource(0);
//            }
//            ImageView imageView = (ImageView) childAt.findViewById(R.id.imgPhotoEditorClose);
//            if (imageView != null) {
//                imageView.setVisibility(View.GONE);
//            }
//        }
//    }

    public void setFilterEffect(String str) {
        this.parentView.setFilterEffect(str);
        this.filterlist.add(str);
        this.fliterindex++;
    }

    @Deprecated
    public void saveImage(String str, OnSaveListener onSaveListener) {
        saveAsFile(str, onSaveListener);
    }

    public void saveAsFile(String str, OnSaveListener onSaveListener) {
        saveAsFile(str, new PolishSettings.Builder().build(), onSaveListener);
    }

    public void saveAsFile(final String str, final PolishSettings saveSettings, final OnSaveListener onSaveListener) {
        this.parentView.saveGLSurfaceViewAsBitmap(new OnSaveBitmap() {

            @Override
            public void onBitmapReady(Bitmap bitmap) {
                new AsyncTask<String, String, Exception>() {

                    public void onPreExecute() {
                        super.onPreExecute();
//                        PolishEditor.this.clearHelperBox();
                    }

                    public Exception doInBackground(String... strArr) {
                        Bitmap bitmap;
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(str), false);
                            if (PolishEditor.this.parentView != null) {
                                if (saveSettings.isTransparencyEnabled()) {
                                    bitmap = PolishBitmapUtils.removeTransparency(PolishEditor.this.getBitmapFromView(PolishEditor.this.parentView));
                                } else {
                                    bitmap = PolishEditor.this.getBitmapFromView(PolishEditor.this.parentView);
                                }
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                            }
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            Log.d(PolishEditor.TAG, "Filed Saved Successfully");
                            return null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(PolishEditor.TAG, "Failed to save File");
                            return e;
                        }
                    }

                    public void onPostExecute(Exception exc) {
                        super.onPostExecute(exc);
                        if (exc == null) {
                            if (saveSettings.isClearViewsEnabled()) {
                                PolishEditor.this.clearAllViews();
                            }
                            onSaveListener.onSuccess(str);
                            return;
                        }
                        onSaveListener.onFailure(exc);
                    }
                }.execute(new String[0]);
            }

            public void onFailure(Exception exc) {
                onSaveListener.onFailure(exc);
            }
        });
    }

    public void saveStickerAsBitmap(OnSaveBitmap onSaveBitmap) {
        saveStickerAsBitmap(new PolishSettings.Builder().build(), onSaveBitmap);
    }

    public void saveStickerAsBitmap(PolishSettings saveSettings, OnSaveBitmap onSaveBitmap) {
        Bitmap bitmap;
        if (saveSettings.isTransparencyEnabled()) {
            bitmap = PolishBitmapUtils.removeTransparency(getBitmapFromView(this.parentView));
        } else {
            bitmap = getBitmapFromView(this.parentView);
        }
        onSaveBitmap.onBitmapReady(bitmap);
    }

    private static String convertEmoji(String str) {
        try {
            return new String(Character.toChars(Integer.parseInt(str.substring(2), 16)));
        } catch (NumberFormatException e) {
            return "";
        }
    }

    public void setOnPhotoEditorListener(OnPolishEditorListener onPhotoEditorListener) {
        this.mOnPhotoEditorListener = onPhotoEditorListener;
    }

    public boolean isCacheEmpty() {
        return this.viewList.size() == 0 && this.redoViews.size() == 0;
    }

    @Override
    public void onViewAdd(BrushDrawingView brushDrawingView2) {
        if (this.redoViews.size() > 0) {
            List<View> list = this.redoViews;
            list.remove(list.size() - 1);
        }
        this.viewList.add(brushDrawingView2);
        OnPolishEditorListener onPolishEditorListener = this.mOnPhotoEditorListener;
        if (onPolishEditorListener != null) {
            onPolishEditorListener.onAddViewListener(Drawing.BRUSH_DRAWING, this.viewList.size());
        }
    }

    @Override 
    public void onViewRemoved(BrushDrawingView brushDrawingView2) {
        if (this.viewList.size() > 0) {
            List<View> list = this.viewList;
            View remove = list.remove(list.size() - 1);
            if (!(remove instanceof BrushDrawingView)) {
                this.parentView.removeView(remove);
            }
            this.redoViews.add(remove);
        }
        OnPolishEditorListener onPolishEditorListener = this.mOnPhotoEditorListener;
        if (onPolishEditorListener != null) {
            onPolishEditorListener.onRemoveViewListener(this.viewList.size());
            this.mOnPhotoEditorListener.onRemoveViewListener(Drawing.BRUSH_DRAWING, this.viewList.size());
        }
    }

    @Override 
    public void onStartDrawing() {
        OnPolishEditorListener onPolishEditorListener = this.mOnPhotoEditorListener;
        if (onPolishEditorListener != null) {
            onPolishEditorListener.onStartViewChangeListener(Drawing.BRUSH_DRAWING);
        }
    }

    @Override 
    public void onStopDrawing() {
        OnPolishEditorListener onPolishEditorListener = this.mOnPhotoEditorListener;
        if (onPolishEditorListener != null) {
            onPolishEditorListener.onStopViewChangeListener(Drawing.BRUSH_DRAWING);
        }
    }

    public static class Builder {
        public BrushDrawingView brushDrawingView;
        public Context context;
        public View deleteView;
        public Typeface emojiTypeface;
        public ImageGLSurfaceView glSurfaceView;
        public boolean isTextPinchZoomable = true;
        public PolishView parentView;
        public Typeface textTypeface;

        public Builder(Context context2, PolishView polishView) {
            this.context = context2;
            this.parentView = polishView;
            this.brushDrawingView = polishView.getBrushDrawingView();
            this.glSurfaceView = polishView.getGLSurfaceView();
        }

        public Builder setDeleteView(View view) {
            this.deleteView = view;
            return this;
        }

        public Builder setDefaultTextTypeface(Typeface typeface) {
            this.textTypeface = typeface;
            return this;
        }

        public Builder setDefaultEmojiTypeface(Typeface typeface) {
            this.emojiTypeface = typeface;
            return this;
        }

        public Builder setPinchTextScalable(boolean z) {
            this.isTextPinchZoomable = z;
            return this;
        }

        public PolishEditor build() {
            return new PolishEditor(this);
        }
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }
}
