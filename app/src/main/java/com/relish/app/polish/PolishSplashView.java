package com.relish.app.polish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;


import com.relish.app.R;
import com.relish.app.Utils.FilePath;
import com.relish.app.Utils.SystemUtil;
import com.relish.app.layout.SplashLayout;

import java.util.ArrayList;

public class PolishSplashView extends ImageView {
    public static float resRatio;
    BitmapShader bitmapShader;
    Path brushPath;
    public Canvas canvas;
    Canvas canvasPreview;
    Paint circlePaint;
    Path circlePath;
    public int coloring = -1;
    Context context;
    PointF curr = new PointF();
    public int currentImageIndex = 0;
    boolean draw = false;
    Paint drawPaint;
    Path drawPath;
    public Bitmap drawingBitmap;
    Rect dstRect;
    PointF last = new PointF();
    Paint logPaintColor;
    Paint logPaintGray;
    float[] m;
    ScaleGestureDetector mScaleDetector;
    Matrix matrix;
    float maxScale = 5.0f;
    float minScale = 1.0f;
    public int mode = 0;
    int oldMeasuredHeight;
    int oldMeasuredWidth;
    float oldX = 0.0f;
    float oldY = 0.0f;
    boolean onMeasureCalled = false;
    public int opacity = 240;
    protected float origHeight;
    protected float origWidth;
    int pCount1 = -1;
    int pCount2 = -1;
    ArrayList<PointF> pathPoints;
    public boolean prViewDefaultPosition;
    Paint previewPaint;
    public float radius = 150.0f;
    public float saveScale = 1.0f;
    public Bitmap splashBitmap;
    PointF start = new PointF();
    Paint tempPaint;
    Bitmap tempPreviewBitmap;
    int viewHeight;
    int viewWidth;
    float x;
    float y;


    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            if (PolishSplashView.this.mode == 1 || PolishSplashView.this.mode == 3) {
                PolishSplashView.this.mode = 3;
            } else {
                PolishSplashView.this.mode = 2;
            }
            PolishSplashView.this.draw = false;
            return true;
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float f = PolishSplashView.this.saveScale;
            PolishSplashView.this.saveScale *= scaleFactor;
            if (PolishSplashView.this.saveScale > PolishSplashView.this.maxScale) {
                PolishSplashView touchImageView = PolishSplashView.this;
                touchImageView.saveScale = touchImageView.maxScale;
                scaleFactor = PolishSplashView.this.maxScale / f;
            } else {
                float f2 = PolishSplashView.this.saveScale;
                float f3 = PolishSplashView.this.minScale;
            }
            if (PolishSplashView.this.origWidth * PolishSplashView.this.saveScale <= ((float) PolishSplashView.this.viewWidth) || PolishSplashView.this.origHeight * PolishSplashView.this.saveScale <= ((float) PolishSplashView.this.viewHeight)) {
                PolishSplashView.this.matrix.postScale(scaleFactor, scaleFactor, (float) (PolishSplashView.this.viewWidth / 2), (float) (PolishSplashView.this.viewHeight / 2));
            } else {
                PolishSplashView.this.matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            }
            PolishSplashView.this.matrix.getValues(PolishSplashView.this.m);
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            PolishSplashView.this.radius = ((float) (SplashLayout.seekBarSize.getProgress() + 10)) / PolishSplashView.this.saveScale;
            PolishSplashView.this.updatePreviewPaint();
        }
    }

    public float getFixTrans(float f, float f2, float f3) {
        float f5;
        float f4;
        if (f3 <= f2) {
            f4 = f2 - f3;
            f5 = 0.0f;
        } else {
            f5 = f2 - f3;
            f4 = 0.0f;
        }
        if (f < f5) {
            return (-f) + f5;
        }
        if (f > f4) {
            return (-f) + f4;
        }
        return 0.0f;
    }

    public PolishSplashView(Context context2) {
        super(context2);
        this.context = context2;
        sharedConstructing(context2);
        this.prViewDefaultPosition = true;
        setDrawingCacheEnabled(true);
    }

    public PolishSplashView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        this.context = context2;
        sharedConstructing(context2);
        this.prViewDefaultPosition = true;
        setDrawingCacheEnabled(true);
    }

    public void initDrawing() {
        this.splashBitmap = SplashLayout.colorBitmap;
        Bitmap createBitmap = Bitmap.createBitmap(SplashLayout.grayBitmap);
        this.drawingBitmap = createBitmap;
        setImageBitmap(createBitmap);
        this.canvas = new Canvas(this.drawingBitmap);
        this.circlePath = new Path();
        this.drawPath = new Path();
        this.brushPath = new Path();
        Paint paint = new Paint();
        this.circlePaint = paint;
        paint.setAntiAlias(true);
        this.circlePaint.setDither(true);
        this.circlePaint.setColor(getContext().getResources().getColor(R.color.white));
        this.circlePaint.setStrokeWidth((float) SystemUtil.dpToPx(getContext(), 2));
        this.circlePaint.setStyle(Paint.Style.STROKE);
        Paint paint2 = new Paint(1);
        this.drawPaint = paint2;
        paint2.setStyle(Paint.Style.STROKE);
        this.drawPaint.setStrokeWidth(this.radius);
        this.drawPaint.setStrokeCap(Paint.Cap.ROUND);
        this.drawPaint.setStrokeJoin(Paint.Join.ROUND);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        Paint paint3 = new Paint();
        this.tempPaint = paint3;
        paint3.setStyle(Paint.Style.FILL);
        this.tempPaint.setColor(-1);
        Paint paint4 = new Paint();
        this.previewPaint = paint4;
        paint4.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        this.tempPreviewBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        this.canvasPreview = new Canvas(this.tempPreviewBitmap);
        this.dstRect = new Rect(0, 0, 100, 100);
        Paint paint5 = new Paint(this.drawPaint);
        this.logPaintGray = paint5;
        paint5.setShader(new BitmapShader(SplashLayout.grayBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        BitmapShader bitmapShader2 = new BitmapShader(this.splashBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        this.bitmapShader = bitmapShader2;
        this.drawPaint.setShader(bitmapShader2);
        this.logPaintColor = new Paint(this.drawPaint);
    }

    public void updatePaintBrush() {
        try {
            this.drawPaint.setStrokeWidth(this.radius * resRatio);
            this.drawPaint.setAlpha(this.opacity);
        } catch (Exception e) {
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        updatePreviewPaint();
    }

    public void changeShaderBitmap() {
        BitmapShader bitmapShader2 = new BitmapShader(this.splashBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        this.bitmapShader = bitmapShader2;
        this.drawPaint.setShader(bitmapShader2);
        updatePreviewPaint();
    }

    public void updatePreviewPaint() {
        if (SplashLayout.colorBitmap.getWidth() > SplashLayout.colorBitmap.getHeight()) {
            float width = ((float) SplashLayout.displayWidth) / ((float) SplashLayout.colorBitmap.getWidth());
            resRatio = width;
            resRatio = width * this.saveScale;
        } else {
            float height = this.origHeight / ((float) SplashLayout.colorBitmap.getHeight());
            resRatio = height;
            resRatio = height * this.saveScale;
        }
        this.drawPaint.setStrokeWidth(this.radius * resRatio);
        this.drawPaint.setMaskFilter(new BlurMaskFilter(resRatio * 15.0f, BlurMaskFilter.Blur.NORMAL));
        this.drawPaint.getShader().setLocalMatrix(this.matrix);
    }

    private void sharedConstructing(Context context2) {
        super.setClickable(true);
        this.context = context2;
        this.mScaleDetector = new ScaleGestureDetector(context2, new ScaleListener());
        Matrix matrix2 = new Matrix();
        this.matrix = matrix2;
        this.m = new float[9];
        setImageMatrix(matrix2);
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                PolishSplashView.this.mScaleDetector.onTouchEvent(motionEvent);
                PolishSplashView.this.pCount2 = motionEvent.getPointerCount();
                PolishSplashView.this.curr = new PointF(motionEvent.getX(), motionEvent.getY());
                PolishSplashView splashView1 = PolishSplashView.this;
                splashView1.x = (splashView1.curr.x - PolishSplashView.this.m[2]) / PolishSplashView.this.m[0];
                PolishSplashView splashView2 = PolishSplashView.this;
                splashView2.y = (splashView2.curr.y - PolishSplashView.this.m[5]) / PolishSplashView.this.m[4];
                int action = motionEvent.getAction();
                if (action != 6) {
                    if (action == 0) {
                        PolishSplashView.this.drawPaint.setStrokeWidth(PolishSplashView.this.radius * PolishSplashView.resRatio);
                        PolishSplashView.this.drawPaint.setMaskFilter(new BlurMaskFilter(PolishSplashView.resRatio * 15.0f, BlurMaskFilter.Blur.NORMAL));
                        PolishSplashView.this.drawPaint.getShader().setLocalMatrix(PolishSplashView.this.matrix);
                        PolishSplashView splashView = PolishSplashView.this;
                        splashView.oldX = 0.0f;
                        splashView.oldY = 0.0f;
                        splashView.last.set(PolishSplashView.this.curr);
                        PolishSplashView.this.start.set(PolishSplashView.this.last);
                        if (!(PolishSplashView.this.mode == 1 || PolishSplashView.this.mode == 3)) {
                            PolishSplashView.this.draw = true;
                        }
                        PolishSplashView.this.circlePath.reset();
                        PolishSplashView.this.circlePath.moveTo(PolishSplashView.this.curr.x, PolishSplashView.this.curr.y);
                        PolishSplashView.this.circlePath.addCircle(PolishSplashView.this.curr.x, PolishSplashView.this.curr.y, (PolishSplashView.this.radius * PolishSplashView.resRatio) / 2.0f, Path.Direction.CW);
                        PolishSplashView.this.pathPoints = new ArrayList<>();
                        PolishSplashView.this.pathPoints.add(new PointF(PolishSplashView.this.x, PolishSplashView.this.y));
                        PolishSplashView.this.drawPath.moveTo(PolishSplashView.this.x, PolishSplashView.this.y);
                        PolishSplashView.this.brushPath.moveTo(PolishSplashView.this.curr.x, PolishSplashView.this.curr.y);
                    } else if (action == 1) {
                        if (PolishSplashView.this.mode == 1) {
                            PolishSplashView.this.matrix.getValues(PolishSplashView.this.m);
                        }
                        int abs = (int) Math.abs(PolishSplashView.this.curr.y - PolishSplashView.this.start.y);
                        if (((int) Math.abs(PolishSplashView.this.curr.x - PolishSplashView.this.start.x)) < 3 && abs < 3) {
                            PolishSplashView.this.performClick();
                        }
                        if (PolishSplashView.this.draw) {
                            PolishSplashView.this.drawPaint.setStrokeWidth(PolishSplashView.this.radius);
                            PolishSplashView.this.drawPaint.setMaskFilter(new BlurMaskFilter(15.0f, BlurMaskFilter.Blur.NORMAL));
                            PolishSplashView.this.drawPaint.getShader().setLocalMatrix(new Matrix());
                            PolishSplashView.this.canvas.drawPath(PolishSplashView.this.drawPath, PolishSplashView.this.drawPaint);
                        }
                        SplashLayout.vector.add(new FilePath(PolishSplashView.this.pathPoints, PolishSplashView.this.coloring, PolishSplashView.this.radius));
                        PolishSplashView.this.circlePath.reset();
                        PolishSplashView.this.drawPath.reset();
                        PolishSplashView.this.brushPath.reset();
                        PolishSplashView.this.draw = false;
                    } else if (action == 2) {
                        if (PolishSplashView.this.mode == 1 || PolishSplashView.this.mode == 3 || !PolishSplashView.this.draw) {
                            if (PolishSplashView.this.pCount1 == 1 && PolishSplashView.this.pCount2 == 1) {
                                PolishSplashView.this.matrix.postTranslate(PolishSplashView.this.curr.x - PolishSplashView.this.last.x, PolishSplashView.this.curr.y - PolishSplashView.this.last.y);
                            }
                            PolishSplashView.this.last.set(PolishSplashView.this.curr.x, PolishSplashView.this.curr.y);
                        } else {
                            PolishSplashView.this.circlePath.reset();
                            PolishSplashView.this.circlePath.moveTo(PolishSplashView.this.curr.x, PolishSplashView.this.curr.y);
                            PolishSplashView.this.circlePath.addCircle(PolishSplashView.this.curr.x, PolishSplashView.this.curr.y, (PolishSplashView.this.radius * PolishSplashView.resRatio) / 2.0f, Path.Direction.CW);
                            PolishSplashView.this.pathPoints.add(new PointF(PolishSplashView.this.x, PolishSplashView.this.y));
                            PolishSplashView.this.drawPath.lineTo(PolishSplashView.this.x, PolishSplashView.this.y);
                            PolishSplashView.this.brushPath.lineTo(PolishSplashView.this.curr.x, PolishSplashView.this.curr.y);
                            PolishSplashView.this.showBoxPreview();
                            if ((PolishSplashView.this.curr.x > 0.0f || PolishSplashView.this.curr.y > 0.0f || !PolishSplashView.this.prViewDefaultPosition) && PolishSplashView.this.curr.x <= 0.0f && PolishSplashView.this.curr.y >= ((float) PolishSplashView.this.viewHeight) && !PolishSplashView.this.prViewDefaultPosition) {
                                PolishSplashView.this.prViewDefaultPosition = true;
                            } else {
                                PolishSplashView.this.prViewDefaultPosition = false;
                            }
                        }
                    }
                } else if (PolishSplashView.this.mode == 2) {
                    PolishSplashView.this.mode = 0;
                }
                PolishSplashView touchImageView6 = PolishSplashView.this;
                touchImageView6.pCount1 = touchImageView6.pCount2;
                PolishSplashView touchImageView7 = PolishSplashView.this;
                touchImageView7.setImageMatrix(touchImageView7.matrix);
                PolishSplashView.this.invalidate();
                return true;
            }
        });
    }

    public void updateRefMetrix() {
        this.matrix.getValues(this.m);
    }

    public void showBoxPreview() {
        buildDrawingCache();
        try {
            Bitmap createBitmap = Bitmap.createBitmap(getDrawingCache());
            this.canvasPreview.drawRect(this.dstRect, this.tempPaint);
            this.canvasPreview.drawBitmap(createBitmap, new Rect(((int) this.curr.x) - 100, ((int) this.curr.y) - 100, ((int) this.curr.x) + 100, ((int) this.curr.y) + 100), this.dstRect, this.previewPaint);
            destroyDrawingCache();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onDraw(Canvas canvas2) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        int i = (int) fArr[2];
        int i2 = (int) fArr[5];
        super.onDraw(canvas2);
        float f = (float) i2;
        float f2 = this.origHeight;
        float f3 = this.saveScale;
        float f4 = (f2 * f3) + f;
        if (i2 < 0) {
            float f5 = (float) i;
            float f6 = (this.origWidth * f3) + f5;
            int i3 = this.viewHeight;
            if (f4 > ((float) i3)) {
                f4 = (float) i3;
            }
            canvas2.clipRect(f5, 0.0f, f6, f4);
        } else {
            float f7 = (float) i;
            float f8 = (this.origWidth * f3) + f7;
            int i4 = this.viewHeight;
            if (f4 > ((float) i4)) {
                f4 = (float) i4;
            }
            canvas2.clipRect(f7, f, f8, f4);
        }
        if (this.draw) {
            canvas2.drawPath(this.brushPath, this.drawPaint);
            canvas2.drawPath(this.circlePath, this.circlePaint);
        }
    }

    public void fixTrans() {
        this.matrix.getValues(this.m);
        float[] fArr = this.m;
        float f = fArr[2];
        float f2 = fArr[5];
        float fixTrans = getFixTrans(f, (float) this.viewWidth, this.origWidth * this.saveScale);
        float fixTrans2 = getFixTrans(f2, (float) this.viewHeight, this.origHeight * this.saveScale);
        if (!(fixTrans == 0.0f && fixTrans2 == 0.0f)) {
            this.matrix.postTranslate(fixTrans, fixTrans2);
        }
        this.matrix.getValues(this.m);
        updatePreviewPaint();
    }

    public void onMeasure(int i, int i2) {
        int i4;
        int i5;
        super.onMeasure(i, i2);
        if (!this.onMeasureCalled) {
            Log.wtf("OnMeasured Call :", "OnMeasured Call");
            this.viewWidth = MeasureSpec.getSize(i);
            int size = MeasureSpec.getSize(i2);
            this.viewHeight = size;
            int i3 = this.oldMeasuredHeight;
            if ((i3 != this.viewWidth || i3 != size) && (i4 = this.viewWidth) != 0 && (i5 = this.viewHeight) != 0) {
                this.oldMeasuredHeight = i5;
                this.oldMeasuredWidth = i4;
                if (this.saveScale == 1.0f) {
                    fitScreen();
                }
                this.onMeasureCalled = true;
            }
        }
    }

    public void fitScreen() {
        Drawable drawable = getDrawable();
        if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            Log.d("bmSize", "bmWidth: " + intrinsicWidth + " bmHeight : " + intrinsicHeight);
            float f = (float) intrinsicWidth;
            float f2 = (float) intrinsicHeight;
            float min = Math.min(((float) this.viewWidth) / f, ((float) this.viewHeight) / f2);
            this.matrix.setScale(min, min);
            float f3 = (((float) this.viewHeight) - (f2 * min)) / 2.0f;
            float f4 = (((float) this.viewWidth) - (min * f)) / 2.0f;
            this.matrix.postTranslate(f4, f3);
            this.origWidth = ((float) this.viewWidth) - (f4 * 2.0f);
            this.origHeight = ((float) this.viewHeight) - (2.0f * f3);
            setImageMatrix(this.matrix);
            this.matrix.getValues(this.m);
            fixTrans();
        }
    }
}
