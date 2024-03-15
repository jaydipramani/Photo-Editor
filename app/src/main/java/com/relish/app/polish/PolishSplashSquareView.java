package com.relish.app.polish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

import com.relish.app.R;
import com.relish.app.Utils.SystemUtil;
import com.relish.app.draw.BrushDrawingView;
import com.relish.app.sticker.Sticker;

import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

public class PolishSplashSquareView extends AppCompatImageView {
    private Bitmap bitmap;
    private int brushSize;
    public int cSplashMode;
    private int currentMode;
    private float currentX;
    private float currentY;
    private Stack<BrushDrawingView.LinePath> linePathStack;
    private Stack<BrushDrawingView.LinePath> linePathStack1;
    private Stack<BrushDrawingView.LinePath> linePathStack2;
    private Path mPath;
    private float mTouchX;
    private float mTouchY;
    private final Matrix matrix;
    private PointF midPoint;
    private final Matrix moveMatrix;
    private float oldDistance;
    private float oldRotation;
    private Paint paint;
    private Paint paintCircle;
    private final float[] point;
    private final PointF pointF;
    private boolean showTouchIcon;
    private Sticker sticker;
    private final float[] tmp;

    public void setcSplashMode(int i) {
        this.cSplashMode = i;
    }

    public PolishSplashSquareView(Context context) {
        super(context);
        this.brushSize = 100;
        this.pointF = new PointF();
        this.currentMode = 0;
        this.cSplashMode = 0;
        this.matrix = new Matrix();
        this.linePathStack = new Stack<>();
        this.linePathStack1 = new Stack<>();
        this.linePathStack2 = new Stack<>();
        this.midPoint = new PointF();
        this.moveMatrix = new Matrix();
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.point = new float[2];
        this.showTouchIcon = false;
        this.tmp = new float[2];
        init();
    }

    public PolishSplashSquareView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.brushSize = 100;
        this.pointF = new PointF();
        this.currentMode = 0;
        this.cSplashMode = 0;
        this.matrix = new Matrix();
        this.linePathStack = new Stack<>();
        this.linePathStack1 = new Stack<>();
        this.linePathStack2 = new Stack<>();
        this.midPoint = new PointF();
        this.moveMatrix = new Matrix();
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.point = new float[2];
        this.showTouchIcon = false;
        this.tmp = new float[2];
        init();
    }

    public PolishSplashSquareView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.brushSize = 100;
        this.pointF = new PointF();
        this.currentMode = 0;
        this.cSplashMode = 0;
        this.matrix = new Matrix();
        this.linePathStack = new Stack<>();
        this.linePathStack1 = new Stack<>();
        this.linePathStack2 = new Stack<>();
        this.midPoint = new PointF();
        this.moveMatrix = new Matrix();
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.point = new float[2];
        this.showTouchIcon = false;
        this.tmp = new float[2];
        init();
    }

    @Override
    public void setImageBitmap(Bitmap bitmap2) {
        super.setImageBitmap(bitmap2);
        setBitmap(bitmap2);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    private void init() {
        Paint paint2 = new Paint();
        this.paint = paint2;
        paint2.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeWidth((float) this.brushSize);
        this.paint.setMaskFilter(new BlurMaskFilter(20.0f, BlurMaskFilter.Blur.NORMAL));
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.paint.setStyle(Paint.Style.STROKE);
        Paint paint3 = new Paint();
        this.paintCircle = paint3;
        paint3.setAntiAlias(true);
        this.paintCircle.setDither(true);
        this.paintCircle.setColor(getContext().getResources().getColor(R.color.colorAccent));
        this.paintCircle.setStrokeWidth((float) SystemUtil.dpToPx(getContext(), 2));
        this.paintCircle.setStyle(Paint.Style.STROKE);
        this.mPath = new Path();
    }

    public void updateBrush() {
        this.mPath = new Path();
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeWidth((float) this.brushSize);
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.paint.setStyle(Paint.Style.STROKE);
        this.showTouchIcon = false;
        invalidate();
    }

    public void addSticker(Sticker sticker2) {
        addSticker(sticker2, 1);
    }

    public void addSticker(final Sticker sticker2, final int i) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker2, i);
        } else {
            post(new Runnable() {

                public void run() {
                    PolishSplashSquareView.this.addStickerImmediately(sticker2, i);
                }
            });
        }
    }

    public void addStickerImmediately(Sticker sticker2, int i) {
        this.sticker = sticker2;
        setStickerPosition(sticker2, i);
        invalidate();
    }

    public void setStickerPosition(Sticker sticker2, int i) {
        float f;
        float width = (float) getWidth();
        float height = (float) getHeight();
        if (width > height) {
            f = ((height * 4.0f) / 5.0f) / ((float) sticker2.getHeight());
        } else {
            f = ((width * 4.0f) / 5.0f) / ((float) sticker2.getWidth());
        }
        this.midPoint.set(0.0f, 0.0f);
        this.matrix.reset();
        this.moveMatrix.set(this.matrix);
        this.moveMatrix.postScale(f, f);
        this.moveMatrix.postRotate((float) (new Random().nextInt(20) - 10), this.midPoint.x, this.midPoint.y);
        float width2 = width - ((float) ((int) (((float) sticker2.getWidth()) * f)));
        float height2 = height - ((float) ((int) (((float) sticker2.getHeight()) * f)));
        this.moveMatrix.postTranslate((i & 4) > 0 ? width2 / 4.0f : (i & 8) > 0 ? width2 * 0.75f : width2 / 2.0f, (i & 2) > 0 ? height2 / 4.0f : (i & 16) > 0 ? height2 * 0.75f : height2 / 2.0f);
        sticker2.setMatrix(this.moveMatrix);
    }

    public void onDraw(Canvas canvas) {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            super.onDraw(canvas);
            if (this.cSplashMode == 0) {
                drawStickers(canvas);
                return;
            }
            Iterator it = this.linePathStack1.iterator();
            while (it.hasNext()) {
                BrushDrawingView.LinePath linePath = (BrushDrawingView.LinePath) it.next();
                canvas.drawPath(linePath.getDrawPath(), linePath.getDrawPaint());
            }
            canvas.drawPath(this.mPath, this.paint);
            if (this.showTouchIcon) {
                canvas.drawCircle(this.currentX, this.currentY, (float) (this.brushSize / 2), this.paintCircle);
            }
        }
    }

    public void drawStickers(Canvas canvas) {
        Sticker sticker2 = this.sticker;
        if (sticker2 != null && sticker2.isShow()) {
            this.sticker.draw(canvas);
        }
        invalidate();
    }

    public float calculateDistance(float f, float f2, float f3, float f4) {
        double d = (double) (f - f3);
        double d2 = (double) (f2 - f4);
        return (float) Math.sqrt((d * d) + (d2 * d2));
    }

    public float calculateDistance(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateDistance(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }

    public float calculateRotation(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateRotation(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }

    public float calculateRotation(float f, float f2, float f3, float f4) {
        return (float) Math.toDegrees(Math.atan2((double) (f2 - f4), (double) (f - f3)));
    }

    public PointF calculateMidPoint(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            this.midPoint.set(0.0f, 0.0f);
            return this.midPoint;
        }
        this.midPoint.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
        return this.midPoint;
    }

    public PointF calculateMidPoint() {
        Sticker sticker2 = this.sticker;
        if (sticker2 != null) {
            sticker2.getMappedCenterPoint(this.midPoint, this.point, this.tmp);
        }
        return this.midPoint;
    }

    public boolean isInStickerArea(Sticker sticker2, float f, float f2) {
        if (sticker2 == null) {
            return false;
        }
        float[] fArr = this.tmp;
        fArr[0] = f;
        fArr[1] = f2;
        return sticker2.contains(fArr);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        this.currentX = x;
        this.currentY = y;
        switch (actionMasked) {
            case 0:
                if (!onTouchDown(x, y)) {
                    invalidate();
                    return false;
                }
                break;
            case 1:
                onTouchUp(motionEvent);
                break;
            case 2:
                handleCurrentMode(x, y, motionEvent);
                invalidate();
                break;
            case 5:
                this.oldDistance = calculateDistance(motionEvent);
                this.oldRotation = calculateRotation(motionEvent);
                this.midPoint = calculateMidPoint(motionEvent);
                Sticker sticker2 = this.sticker;
                if (sticker2 != null && isInStickerArea(sticker2, motionEvent.getX(1), motionEvent.getY(1))) {
                    this.currentMode = 2;
                    break;
                }
            case 6:
                this.currentMode = 0;
                break;
        }
        return true;
    }

    public void constrainSticker(Sticker sticker2) {
        int width = getWidth();
        int height = getHeight();
        sticker2.getMappedCenterPoint(this.pointF, this.point, this.tmp);
        float f = 0.0f;
        float f2 = this.pointF.x < 0.0f ? -this.pointF.x : 0.0f;
        float f3 = (float) width;
        if (this.pointF.x > f3) {
            f2 = f3 - this.pointF.x;
        }
        if (this.pointF.y < 0.0f) {
            f = -this.pointF.y;
        }
        float f4 = (float) height;
        if (this.pointF.y > f4) {
            f = f4 - this.pointF.y;
        }
        sticker2.getMatrix().postTranslate(f2, f);
    }

    public synchronized void handleCurrentMode(float f, float f2, MotionEvent motionEvent) {
        if (this.cSplashMode == 0) {
            int i = this.currentMode;
            if (i != 4) {
                switch (i) {
                    case 1:
                        if (this.sticker != null) {
                            this.moveMatrix.set(this.matrix);
                            this.moveMatrix.postTranslate(motionEvent.getX() - this.mTouchX, motionEvent.getY() - this.mTouchY);
                            this.sticker.setMatrix(this.moveMatrix);
                            break;
                        }
                        break;
                    case 2:
                        if (this.sticker != null) {
                            float calculateDistance = calculateDistance(motionEvent);
                            float calculateRotation = calculateRotation(motionEvent);
                            this.moveMatrix.set(this.matrix);
                            Matrix matrix2 = this.moveMatrix;
                            float f3 = this.oldDistance;
                            matrix2.postScale(calculateDistance / f3, calculateDistance / f3, this.midPoint.x, this.midPoint.y);
                            this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
                            this.sticker.setMatrix(this.moveMatrix);
                            break;
                        }
                        break;
                }
            }
        } else {
            Path path = this.mPath;
            float f4 = this.mTouchX;
            float f5 = this.mTouchY;
            path.quadTo(f4, f5, (f4 + f) / 2.0f, (f5 + f2) / 2.0f);
            this.mTouchX = f;
            this.mTouchY = f2;
        }
    }

    public void setBrushSize(int i) {
        this.brushSize = i;
        this.paint.setStrokeWidth((float) i);
        this.showTouchIcon = true;
        this.currentX = (float) (getWidth() / 2);
        this.currentY = (float) (getHeight() / 2);
        invalidate();
    }

    public Sticker findHandlingSticker() {
        if (isInStickerArea(this.sticker, this.mTouchX, this.mTouchY)) {
            return this.sticker;
        }
        return null;
    }

    public boolean onTouchDown(float f, float f2) {
        this.currentMode = 1;
        this.mTouchX = f;
        this.mTouchY = f2;
        this.currentX = f;
        this.currentY = f2;
        if (this.cSplashMode == 0) {
            PointF calculateMidPoint = calculateMidPoint();
            this.midPoint = calculateMidPoint;
            this.oldDistance = calculateDistance(calculateMidPoint.x, this.midPoint.y, this.mTouchX, this.mTouchY);
            this.oldRotation = calculateRotation(this.midPoint.x, this.midPoint.y, this.mTouchX, this.mTouchY);
            Sticker findHandlingSticker = findHandlingSticker();
            if (findHandlingSticker != null) {
                this.matrix.set(this.sticker.getMatrix());
            }
            if (findHandlingSticker == null) {
                return false;
            }
        } else {
            this.showTouchIcon = true;
            this.linePathStack2.clear();
            this.mPath.reset();
            this.mPath.moveTo(f, f2);
        }
        invalidate();
        return true;
    }

    public void onTouchUp(MotionEvent motionEvent) {
        this.showTouchIcon = false;
        if (this.cSplashMode == 0) {
            this.currentMode = 0;
        } else {
            BrushDrawingView.LinePath linePath = new BrushDrawingView.LinePath(this.mPath, this.paint);
            this.linePathStack1.push(linePath);
            this.linePathStack.push(linePath);
            this.mPath = new Path();
        }
        invalidate();
    }

    public boolean undo() {
        if (!this.linePathStack.empty()) {
            BrushDrawingView.LinePath pop = this.linePathStack.pop();
            this.linePathStack2.push(pop);
            this.linePathStack1.remove(pop);
            invalidate();
        }
        return !this.linePathStack.empty();
    }

    public boolean redo() {
        if (!this.linePathStack2.empty()) {
            BrushDrawingView.LinePath pop = this.linePathStack2.pop();
            this.linePathStack1.push(pop);
            this.linePathStack.push(pop);
            invalidate();
        }
        return !this.linePathStack2.empty();
    }

    public Sticker getSticker() {
        return this.sticker;
    }

    public Bitmap getBitmap(Bitmap bitmap2) {
        int width = getWidth();
        int height = getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Rect rect = null;
        Paint paint2 = null;
        canvas.drawBitmap(this.bitmap, rect, new RectF(0.0f, 0.0f, (float) width, (float) height), paint2);
        if (this.cSplashMode == 0) {
            drawStickers(canvas);
        } else {
            Iterator it = this.linePathStack1.iterator();
            while (it.hasNext()) {
                BrushDrawingView.LinePath linePath = (BrushDrawingView.LinePath) it.next();
                canvas.drawPath(linePath.getDrawPath(), linePath.getDrawPaint());
            }
        }
        Bitmap createBitmap2 = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawBitmap(bitmap2, rect, new RectF(0.0f, 0.0f, (float) bitmap2.getWidth(), (float) bitmap2.getHeight()), paint2);
        canvas2.drawBitmap(createBitmap, rect, new RectF(0.0f, 0.0f, (float) bitmap2.getWidth(), (float) bitmap2.getHeight()), paint2);
        return createBitmap2;
    }
}
