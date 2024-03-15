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
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.relish.app.R;
import com.relish.app.Utils.SystemUtil;
import com.relish.app.layout.BlurLayout;

public class PolishBlurView extends ImageView {
    public static float resRatio;
    BitmapShader bitmapShader;
    Path brushPath;
    public Canvas canvas;
    Canvas canvasPreview;
    Paint circlePaint;
    Path circlePath;
    public boolean coloring = true;
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
    public int opacity = 25;
    protected float origHeight;
    protected float origWidth;
    int pCount1 = -1;
    int pCount2 = -1;
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
            if (PolishBlurView.this.mode == 1 || PolishBlurView.this.mode == 3) {
                PolishBlurView.this.mode = 3;
            } else {
                PolishBlurView.this.mode = 2;
            }
            PolishBlurView.this.draw = false;
            return true;
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float f = PolishBlurView.this.saveScale;
            PolishBlurView.this.saveScale *= scaleFactor;
            if (PolishBlurView.this.saveScale > PolishBlurView.this.maxScale) {
                PolishBlurView touchImageView = PolishBlurView.this;
                touchImageView.saveScale = touchImageView.maxScale;
                scaleFactor = PolishBlurView.this.maxScale / f;
            } else {
                float f2 = PolishBlurView.this.saveScale;
                float f3 = PolishBlurView.this.minScale;
            }
            if (PolishBlurView.this.origWidth * PolishBlurView.this.saveScale <= ((float) PolishBlurView.this.viewWidth) || PolishBlurView.this.origHeight * PolishBlurView.this.saveScale <= ((float) PolishBlurView.this.viewHeight)) {
                PolishBlurView.this.matrix.postScale(scaleFactor, scaleFactor, (float) (PolishBlurView.this.viewWidth / 2), (float) (PolishBlurView.this.viewHeight / 2));
            } else {
                PolishBlurView.this.matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            }
            PolishBlurView.this.matrix.getValues(PolishBlurView.this.m);
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            PolishBlurView.this.radius = ((float) (BlurLayout.seekBarSize.getProgress() + 50)) / PolishBlurView.this.saveScale;
            BlurLayout.brushView.setShapeRadiusRatio(((float) (BlurLayout.seekBarSize.getProgress() + 50)) / PolishBlurView.this.saveScale);
            PolishBlurView.this.updatePreviewPaint();
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

    public PolishBlurView(Context context2) {
        super(context2);
        this.context = context2;
        sharedConstructing(context2);
        this.prViewDefaultPosition = true;
        setDrawingCacheEnabled(true);
    }

    public PolishBlurView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        this.context = context2;
        sharedConstructing(context2);
        this.prViewDefaultPosition = true;
        setDrawingCacheEnabled(true);
    }

    public void initDrawing() {
        this.splashBitmap = BlurLayout.bitmapClear.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap copy = Bitmap.createBitmap(BlurLayout.bitmapBlur).copy(Bitmap.Config.ARGB_8888, true);
        this.drawingBitmap = copy;
        setImageBitmap(copy);
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
        Paint paint2 = new Paint(Paint.HINTING_ON);
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
        paint5.setShader(new BitmapShader(BlurLayout.bitmapBlur, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        BitmapShader bitmapShader2 = new BitmapShader(this.splashBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        this.bitmapShader = bitmapShader2;
        this.drawPaint.setShader(bitmapShader2);
        this.logPaintColor = new Paint(this.drawPaint);
    }

    public void updatePaintBrush() {
        try {
            this.drawPaint.setStrokeWidth(this.radius * resRatio);
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
        if (BlurLayout.bitmapClear.getWidth() > BlurLayout.bitmapClear.getHeight()) {
            float width = ((float) BlurLayout.displayWidth) / ((float) BlurLayout.bitmapClear.getWidth());
            resRatio = width;
            resRatio = width * this.saveScale;
        } else {
            float height = this.origHeight / ((float) BlurLayout.bitmapClear.getHeight());
            resRatio = height;
            resRatio = height * this.saveScale;
        }
        this.drawPaint.setStrokeWidth(this.radius * resRatio);
        this.drawPaint.setMaskFilter(new BlurMaskFilter(resRatio * 30.0f, BlurMaskFilter.Blur.NORMAL));
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
                PolishBlurView.this.mScaleDetector.onTouchEvent(motionEvent);
                PolishBlurView.this.pCount2 = motionEvent.getPointerCount();
                PolishBlurView.this.curr = new PointF(motionEvent.getX(), motionEvent.getY() - (((float) BlurLayout.seekBarBlur.getProgress()) * 3.0f));
                PolishBlurView touchImageView = PolishBlurView.this;
                touchImageView.x = (touchImageView.curr.x - PolishBlurView.this.m[2]) / PolishBlurView.this.m[0];
                PolishBlurView touchImageView2 = PolishBlurView.this;
                touchImageView2.y = (touchImageView2.curr.y - PolishBlurView.this.m[5]) / PolishBlurView.this.m[4];
                int action = motionEvent.getAction();
                if (action == 0) {
                    PolishBlurView.this.drawPaint.setStrokeWidth(PolishBlurView.this.radius * PolishBlurView.resRatio);
                    PolishBlurView.this.drawPaint.setMaskFilter(new BlurMaskFilter(PolishBlurView.resRatio * 30.0f, BlurMaskFilter.Blur.NORMAL));
                    PolishBlurView.this.drawPaint.getShader().setLocalMatrix(PolishBlurView.this.matrix);
                    PolishBlurView touchImageView3 = PolishBlurView.this;
                    touchImageView3.oldX = 0.0f;
                    touchImageView3.oldY = 0.0f;
                    touchImageView3.last.set(PolishBlurView.this.curr);
                    PolishBlurView.this.start.set(PolishBlurView.this.last);
                    if (!(PolishBlurView.this.mode == 1 || PolishBlurView.this.mode == 3)) {
                        PolishBlurView.this.draw = true;
                    }
                    PolishBlurView.this.circlePath.reset();
                    PolishBlurView.this.circlePath.moveTo(PolishBlurView.this.curr.x, PolishBlurView.this.curr.y);
                    PolishBlurView.this.circlePath.addCircle(PolishBlurView.this.curr.x, PolishBlurView.this.curr.y, (PolishBlurView.this.radius * PolishBlurView.resRatio) / 2.0f, Path.Direction.CW);
                    PolishBlurView.this.drawPath.moveTo(PolishBlurView.this.x, PolishBlurView.this.y);
                    PolishBlurView.this.brushPath.moveTo(PolishBlurView.this.curr.x, PolishBlurView.this.curr.y);
                    PolishBlurView.this.showBoxPreview();
                } else if (action == 1) {
                    if (PolishBlurView.this.mode == 1) {
                        PolishBlurView.this.matrix.getValues(PolishBlurView.this.m);
                    }
                    int abs = (int) Math.abs(PolishBlurView.this.curr.y - PolishBlurView.this.start.y);
                    if (((int) Math.abs(PolishBlurView.this.curr.x - PolishBlurView.this.start.x)) < 3 && abs < 3) {
                        PolishBlurView.this.performClick();
                    }
                    if (PolishBlurView.this.draw) {
                        PolishBlurView.this.drawPaint.setStrokeWidth(PolishBlurView.this.radius);
                        PolishBlurView.this.drawPaint.setMaskFilter(new BlurMaskFilter(30.0f, BlurMaskFilter.Blur.NORMAL));
                        PolishBlurView.this.drawPaint.getShader().setLocalMatrix(new Matrix());
                        PolishBlurView.this.canvas.drawPath(PolishBlurView.this.drawPath, PolishBlurView.this.drawPaint);
                    }
                    PolishBlurView.this.circlePath.reset();
                    PolishBlurView.this.drawPath.reset();
                    PolishBlurView.this.brushPath.reset();
                    PolishBlurView.this.draw = false;
                } else if (action != 2) {
                    if (action == 6 && PolishBlurView.this.mode == 2) {
                        PolishBlurView.this.mode = 0;
                    }
                } else if (PolishBlurView.this.mode == 1 || PolishBlurView.this.mode == 3 || !PolishBlurView.this.draw) {
                    if (PolishBlurView.this.pCount1 == 1 && PolishBlurView.this.pCount2 == 1) {
                        PolishBlurView.this.matrix.postTranslate(PolishBlurView.this.curr.x - PolishBlurView.this.last.x, PolishBlurView.this.curr.y - PolishBlurView.this.last.y);
                    }
                    PolishBlurView.this.last.set(PolishBlurView.this.curr.x, PolishBlurView.this.curr.y);
                } else {
                    PolishBlurView.this.circlePath.reset();
                    PolishBlurView.this.circlePath.moveTo(PolishBlurView.this.curr.x, PolishBlurView.this.curr.y);
                    PolishBlurView.this.circlePath.addCircle(PolishBlurView.this.curr.x, PolishBlurView.this.curr.y, (PolishBlurView.this.radius * PolishBlurView.resRatio) / 2.0f, Path.Direction.CW);
                    PolishBlurView.this.drawPath.lineTo(PolishBlurView.this.x, PolishBlurView.this.y);
                    PolishBlurView.this.brushPath.lineTo(PolishBlurView.this.curr.x, PolishBlurView.this.curr.y);
                    PolishBlurView.this.showBoxPreview();
                }
                PolishBlurView touchImageView6 = PolishBlurView.this;
                touchImageView6.pCount1 = touchImageView6.pCount2;
                PolishBlurView touchImageView7 = PolishBlurView.this;
                touchImageView7.setImageMatrix(touchImageView7.matrix);
                PolishBlurView.this.invalidate();
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
        float f = this.origHeight;
        float f2 = this.saveScale;
        float f3 = (float) i2;
        float f4 = (f * f2) + f3;
        if (i2 < 0) {
            float f5 = (float) i;
            float f6 = (this.origWidth * f2) + f5;
            int i3 = this.viewHeight;
            if (f4 > ((float) i3)) {
                f4 = (float) i3;
            }
            canvas2.clipRect(f5, 0.0f, f6, f4);
        } else {
            float f7 = (float) i;
            float f8 = (this.origWidth * f2) + f7;
            int i4 = this.viewHeight;
            if (f4 > ((float) i4)) {
                f4 = (float) i4;
            }
            canvas2.clipRect(f7, f3, f8, f4);
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
            float f = (float) drawable.getIntrinsicWidth();
            float intrinsicHeight = (float) drawable.getIntrinsicHeight();
            float min = Math.min(((float) this.viewWidth) / f, ((float) this.viewHeight) / intrinsicHeight);
            this.matrix.setScale(min, min);
            float f2 = (((float) this.viewHeight) - (intrinsicHeight * min)) / 2.0f;
            float f3 = (((float) this.viewWidth) - (f * min)) / 2.0f;
            this.matrix.postTranslate(f3, f2);
            this.origWidth = ((float) this.viewWidth) - (f3 * 2.0f);
            this.origHeight = ((float) this.viewHeight) - (2.0f * f2);
            setImageMatrix(this.matrix);
            this.matrix.getValues(this.m);
            fixTrans();
        }
    }
}
