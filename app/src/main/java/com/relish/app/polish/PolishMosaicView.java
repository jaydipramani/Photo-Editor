package com.relish.app.polish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;


import com.relish.app.Adapter.MosaicAdapter;
import com.relish.app.R;
import com.relish.app.Utils.SystemUtil;
import com.relish.app.draw.BrushDrawingView;

import java.util.Iterator;
import java.util.Stack;

public class PolishMosaicView extends ImageView {
    private Paint bitmapPaint;
    private Paint blurPaint;
    private int brushBitmapSize = 65;
    private Paint circlePaint;
    private float currentX;
    private float currentY;
    private Stack<BrushDrawingView.LinePath> lstPoints = new Stack<>();
    private Path mPath;
    private Stack<BrushDrawingView.LinePath> mPoints = new Stack<>();
    private Stack<BrushDrawingView.LinePath> mRedoPaths = new Stack<>();
    private float mTouchX;
    private float mTouchY;
    private MosaicAdapter.MosaicItem mosaicItem;
    private boolean showTouchIcon = false;

    public void setMosaicItem(MosaicAdapter.MosaicItem mosaicItem2) {
        this.mosaicItem = mosaicItem2;
        if (mosaicItem2.mode == MosaicAdapter.BLUR.SHADER) {
            this.bitmapPaint.setShader(new BitmapShader(BitmapFactory.decodeResource(getResources(), mosaicItem2.shaderId), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        }
    }

    public PolishMosaicView(Context context) {
        super(context);
        init();
    }

    public PolishMosaicView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public PolishMosaicView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
    }

    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        Paint paint = new Paint();
        this.blurPaint = paint;
        paint.setAntiAlias(true);
        this.blurPaint.setDither(true);
        this.blurPaint.setStyle(Paint.Style.FILL);
        this.blurPaint.setStrokeJoin(Paint.Join.ROUND);
        this.blurPaint.setStrokeCap(Paint.Cap.ROUND);
        this.blurPaint.setStrokeWidth((float) this.brushBitmapSize);
        this.blurPaint.setMaskFilter(new BlurMaskFilter(15.0f, BlurMaskFilter.Blur.NORMAL));
        this.blurPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.blurPaint.setStyle(Paint.Style.STROKE);
        Paint paint2 = new Paint();
        this.bitmapPaint = paint2;
        paint2.setAntiAlias(true);
        this.bitmapPaint.setDither(true);
        this.bitmapPaint.setStyle(Paint.Style.FILL);
        this.bitmapPaint.setStrokeJoin(Paint.Join.ROUND);
        this.bitmapPaint.setStrokeCap(Paint.Cap.ROUND);
        this.bitmapPaint.setStrokeWidth((float) this.brushBitmapSize);
        this.bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        this.bitmapPaint.setStyle(Paint.Style.STROKE);
        Paint paint3 = new Paint();
        this.circlePaint = paint3;
        paint3.setAntiAlias(true);
        this.circlePaint.setDither(true);
        this.circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        this.circlePaint.setStrokeWidth((float) SystemUtil.dpToPx(getContext(), 2));
        this.circlePaint.setStyle(Paint.Style.STROKE);
        this.mPath = new Path();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Iterator it = this.mPoints.iterator();
        while (it.hasNext()) {
            BrushDrawingView.LinePath linePath = (BrushDrawingView.LinePath) it.next();
            canvas.drawPath(linePath.getDrawPath(), linePath.getDrawPaint());
        }
        if (this.mosaicItem.mode == MosaicAdapter.BLUR.BLUR || this.mosaicItem.mode == MosaicAdapter.BLUR.MOSAIC) {
            canvas.drawPath(this.mPath, this.blurPaint);
        } else {
            canvas.drawPath(this.mPath, this.bitmapPaint);
        }
        if (this.showTouchIcon) {
            canvas.drawCircle(this.currentX, this.currentY, (float) (this.brushBitmapSize / 2), this.circlePaint);
        }
    }

    public boolean onTouchDown(float f, float f2) {
        this.showTouchIcon = true;
        this.mTouchX = f;
        this.mTouchY = f2;
        this.currentX = f;
        this.currentY = f2;
        this.mRedoPaths.clear();
        this.mPath.reset();
        this.mPath.moveTo(f, f2);
        invalidate();
        return true;
    }

    public synchronized void handleCurrentMode(float f, float f2, MotionEvent motionEvent) {
        Path path = this.mPath;
        float f3 = this.mTouchX;
        float f4 = this.mTouchY;
        path.quadTo(f3, f4, (f3 + f) / 2.0f, (f4 + f2) / 2.0f);
        this.mTouchX = f;
        this.mTouchY = f2;
    }

    public void setBrushBitmapSize(int i) {
        this.brushBitmapSize = i;
        float f = (float) i;
        this.blurPaint.setStrokeWidth(f);
        this.bitmapPaint.setStrokeWidth(f);
        this.showTouchIcon = true;
        this.currentX = (float) (getWidth() / 2);
        this.currentY = (float) (getHeight() / 2);
        invalidate();
    }

    public void onTouchUp(MotionEvent motionEvent) {
        BrushDrawingView.LinePath linePath;
        this.showTouchIcon = false;
        if (this.mosaicItem.mode == MosaicAdapter.BLUR.BLUR || this.mosaicItem.mode == MosaicAdapter.BLUR.MOSAIC) {
            linePath = new BrushDrawingView.LinePath(this.mPath, this.blurPaint);
        } else {
            linePath = new BrushDrawingView.LinePath(this.mPath, this.bitmapPaint);
        }
        this.mPoints.push(linePath);
        this.lstPoints.push(linePath);
        this.mPath = new Path();
        invalidate();
    }

    public void updateBrush() {
        this.mPath = new Path();
        this.showTouchIcon = false;
        this.blurPaint.setAntiAlias(true);
        this.blurPaint.setDither(true);
        this.blurPaint.setStyle(Paint.Style.FILL);
        this.blurPaint.setStrokeJoin(Paint.Join.ROUND);
        this.blurPaint.setStrokeCap(Paint.Cap.ROUND);
        this.blurPaint.setStrokeWidth((float) this.brushBitmapSize);
        this.blurPaint.setMaskFilter(new BlurMaskFilter(15.0f, BlurMaskFilter.Blur.NORMAL));
        this.blurPaint.setStrokeWidth((float) this.brushBitmapSize);
        this.blurPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.blurPaint.setStyle(Paint.Style.STROKE);
        this.bitmapPaint.setAntiAlias(true);
        this.bitmapPaint.setDither(true);
        this.bitmapPaint.setStyle(Paint.Style.FILL);
        this.bitmapPaint.setStrokeJoin(Paint.Join.ROUND);
        this.bitmapPaint.setStrokeCap(Paint.Cap.ROUND);
        this.bitmapPaint.setStrokeWidth((float) this.brushBitmapSize);
        this.bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        this.bitmapPaint.setStyle(Paint.Style.STROKE);
        this.bitmapPaint.setStrokeWidth((float) this.brushBitmapSize);
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getAction();
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        this.currentX = x;
        this.currentY = y;
        switch (actionMasked) {
            case 0:
                if (onTouchDown(x, y)) {
                    return true;
                }
                invalidate();
                return false;
            case 1:
                onTouchUp(motionEvent);
                return true;
            case 2:
                handleCurrentMode(x, y, motionEvent);
                invalidate();
                return true;
            default:
                return true;
        }
    }

    public boolean undo() {
        if (!this.lstPoints.empty()) {
            BrushDrawingView.LinePath pop = this.lstPoints.pop();
            this.mRedoPaths.push(pop);
            this.mPoints.remove(pop);
            invalidate();
        }
        return !this.lstPoints.empty();
    }

    public boolean redo() {
        if (!this.mRedoPaths.empty()) {
            BrushDrawingView.LinePath pop = this.mRedoPaths.pop();
            this.mPoints.push(pop);
            this.lstPoints.push(pop);
            invalidate();
        }
        return !this.mRedoPaths.empty();
    }

    public Bitmap getBitmap(Bitmap bitmap, Bitmap bitmap2) {
        int width = getWidth();
        int height = getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, (Rect) null, new RectF(0.0f, 0.0f, (float) width, (float) height), (Paint) null);
        Iterator it = this.mPoints.iterator();
        while (it.hasNext()) {
            BrushDrawingView.LinePath linePath = (BrushDrawingView.LinePath) it.next();
            canvas.drawPath(linePath.getDrawPath(), linePath.getDrawPaint());
        }
        Bitmap createBitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(createBitmap2);
        Rect rect = null;
        Paint paint = null;
        canvas2.drawBitmap(bitmap2, rect, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), paint);
        canvas2.drawBitmap(createBitmap, rect, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), paint);
        return createBitmap2;
    }
}
