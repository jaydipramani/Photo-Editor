package com.relish.app.polish;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

import com.relish.app.R;
import com.relish.app.Utils.StickerUtils;
import com.relish.app.Utils.SystemUtil;
import com.relish.app.event.DeleteIconEvent;
import com.relish.app.event.FlipHorizontallyEvent;
import com.relish.app.event.ZoomIconEvent;
import com.relish.app.sticker.DrawableSticker;
import com.relish.app.sticker.Sticker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolishStickerView extends RelativeLayout {
    private static final String TAG = "stickerView";
    private final float[] bitmapPoints;
    private final Paint borderPaint;
    private final Paint borderPaintRed;
    private final float[] bounds;
    private boolean bringToFrontCurrentSticker;
    private int circleRadius;
    private boolean constrained;
    private final PointF currentCenterPoint;
    private PolishStickerIcons currentIcon;
    private int currentMode;
    private float currentMoveingX;
    private float currentMoveingY;
    private final Matrix downMatrix;
    private float downX;
    private float downY;
    private boolean drawCirclePoint;
    private Sticker handlingSticker;
    private final List<PolishStickerIcons> icons;
    private long lastClickTime;
    private Sticker lastHandlingSticker;
    private final Paint linePaint;
    private boolean locked;
    private PointF midPoint;
    private int minClickDelayTime;
    private final Matrix moveMatrix;
    private float oldDistance;
    private float oldRotation;
    private boolean onMoving;
    private OnStickerOperationListener onStickerOperationListener;
    private Paint paintCircle;
    private final float[] point;
    private boolean showBorder;
    private boolean showIcons;
    private final Matrix sizeMatrix;
    private final RectF stickerRect;
    private final List<Sticker> stickers;
    private final float[] tmp;
    private int touchSlop;

    public interface OnStickerOperationListener {
        void onAddSticker(Sticker sticker);

        void onStickerDeleted(Sticker sticker);

        void onStickerDoubleTap(Sticker sticker);

        void onStickerDrag(Sticker sticker);

        void onStickerFlip(Sticker sticker);

        void onStickerSelected(Sticker sticker);

        void onStickerTouchOutside();

        void onStickerTouchedDown(Sticker sticker);

        void onStickerZoom(Sticker sticker);

        void onTouchDownBeauty(float f, float f2);

        void onTouchDragBeauty(float f, float f2);

        void onTouchUpBeauty(float f, float f2);
    }

    public PolishStickerView(Context context) {
        this(context, null);
    }

    public PolishStickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PolishStickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.stickers = new ArrayList();
        this.icons = new ArrayList(4);
        Paint paint = new Paint();
        this.borderPaint = paint;
        Paint paint2 = new Paint();
        this.borderPaintRed = paint2;
        this.linePaint = new Paint();
        this.stickerRect = new RectF();
        this.sizeMatrix = new Matrix();
        this.downMatrix = new Matrix();
        this.moveMatrix = new Matrix();
        this.bitmapPoints = new float[8];
        this.bounds = new float[8];
        this.point = new float[2];
        this.currentCenterPoint = new PointF();
        this.tmp = new float[2];
        this.midPoint = new PointF();
        this.drawCirclePoint = false;
        this.onMoving = false;
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.currentMode = 0;
        this.lastClickTime = 0;
        this.minClickDelayTime = 200;
        Paint paint3 = new Paint();
        this.paintCircle = paint3;
        paint3.setAntiAlias(true);
        this.paintCircle.setDither(true);
        this.paintCircle.setColor(getContext().getResources().getColor(R.color.colorAccent));
        this.paintCircle.setStrokeWidth((float) SystemUtil.dpToPx(getContext(), 2));
        this.paintCircle.setStyle(Paint.Style.STROKE);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.borderAlpha, R.attr.borderColor, R.attr.bringToFrontCurrentSticker, R.attr.showBorder, R.attr.showIcons});
            try {
                this.showIcons = typedArray.getBoolean(4, false);
                this.showBorder = typedArray.getBoolean(3, false);
                this.bringToFrontCurrentSticker = typedArray.getBoolean(2, false);
                paint.setAntiAlias(true);
                paint.setColor(typedArray.getColor(1, Color.parseColor("#000000")));
                paint.setAlpha(typedArray.getInteger(0, 255));
                paint2.setAntiAlias(true);
                paint2.setColor(typedArray.getColor(1, Color.parseColor("#000000")));
                paint2.setAlpha(typedArray.getInteger(0, 255));
                getStickerIcons();
            } finally {
                if (typedArray != null) {
                    typedArray.recycle();
                }
            }
        } catch (Throwable th) {
        }
    }

    public PolishStickerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.stickers = new ArrayList();
        this.icons = new ArrayList(4);
        this.borderPaint = new Paint();
        this.borderPaintRed = new Paint();
        this.linePaint = new Paint();
        this.stickerRect = new RectF();
        this.sizeMatrix = new Matrix();
        this.downMatrix = new Matrix();
        this.moveMatrix = new Matrix();
        this.bitmapPoints = new float[8];
        this.bounds = new float[8];
        this.point = new float[2];
        this.currentCenterPoint = new PointF();
        this.tmp = new float[2];
        this.midPoint = new PointF();
        this.drawCirclePoint = false;
        this.onMoving = false;
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.currentMode = 0;
        this.lastClickTime = 0;
        this.minClickDelayTime = 200;
    }

    public Matrix getSizeMatrix() {
        return this.sizeMatrix;
    }

    public Matrix getDownMatrix() {
        return this.downMatrix;
    }

    public Matrix getMoveMatrix() {
        return this.moveMatrix;
    }

    public List<Sticker> getStickers() {
        return this.stickers;
    }

    public void getStickerIcons() {
        PolishStickerIcons quShotStickerIconClose = new PolishStickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_close), 0, PolishStickerIcons.REMOVE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        PolishStickerIcons quShotStickerIconScale = new PolishStickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_scale), 3, PolishStickerIcons.ZOOM);
        quShotStickerIconScale.setIconEvent(new ZoomIconEvent());
        PolishStickerIcons quShotStickerIconFlip = new PolishStickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_flip), 1, PolishStickerIcons.FLIP);
        quShotStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        PolishStickerIcons quShotStickerIconEdit = new PolishStickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_edit), 2, PolishStickerIcons.EDIT);
        quShotStickerIconEdit.setIconEvent(new FlipHorizontallyEvent());
        this.icons.clear();
        this.icons.add(quShotStickerIconClose);
        this.icons.add(quShotStickerIconScale);
        this.icons.add(quShotStickerIconFlip);
        this.icons.add(quShotStickerIconEdit);
    }

    public void setHandlingSticker(Sticker sticker) {
        this.lastHandlingSticker = this.handlingSticker;
        this.handlingSticker = sticker;
        invalidate();
    }

    public void showLastHandlingSticker() {
        Sticker sticker = this.lastHandlingSticker;
        if (sticker != null && !sticker.isShow()) {
            this.lastHandlingSticker.setShow(true);
            invalidate();
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            this.stickerRect.left = (float) i;
            this.stickerRect.top = (float) i2;
            this.stickerRect.right = (float) i3;
            this.stickerRect.bottom = (float) i4;
        }
    }

    public void setCircleRadius(int i) {
        this.circleRadius = i;
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.drawCirclePoint && this.onMoving) {
            canvas.drawCircle(this.downX, this.downY, (float) this.circleRadius, this.paintCircle);
            canvas.drawLine(this.downX, this.downY, this.currentMoveingX, this.currentMoveingY, this.paintCircle);
        }
        drawStickers(canvas);
    }

//    public void drawStickers(Canvas canvas) {
//        float f;
//        float f2;
//        float f3;
//        float f4;
//        int i = 0;
//        for (int i2 = 0; i2 < this.stickers.size(); i2++) {
//            Sticker sticker = this.stickers.get(i2);
//            if (sticker != null) {
//                sticker.draw(canvas);
//            }
//        }
//        Sticker sticker2 = this.handlingSticker;
//        if (!(sticker2 == null || this.locked)) {
//            if (this.showBorder || this.showIcons) {
//                getStickerPoints(sticker2, this.bitmapPoints);
//                float[] fArr = this.bitmapPoints;
//                float f5 = fArr[0];
//                int i2 = 1;
//                float f6 = fArr[1];
//                int i3 = 2;
//                float f7 = fArr[2];
//                float f8 = fArr[3];
//                float f9 = fArr[4];
//                float f10 = fArr[5];
//                float f11 = fArr[6];
//                float f12 = fArr[7];
//                if (this.showBorder) {
//                    f4 = f12;
//                    f3 = f11;
//                    f2 = f10;
//                    f = f9;
//                    canvas.drawLine(f5, f6, f7, f8, this.borderPaint);
//                    canvas.drawLine(f5, f6, f, f2, this.borderPaint);
//                    canvas.drawLine(f7, f8, f3, f4, this.borderPaint);
//                    canvas.drawLine(f3, f4, f, f2, this.borderPaint);
//                } else {
//                    f4 = f12;
//                    f3 = f11;
//                    f2 = f10;
//                    f = f9;
//                }
//                if (this.showIcons) {
//                    float calculateRotation = calculateRotation(f3, f4, f, f2);
//                    while (i < this.icons.size()) {
//                        PolishStickerIcons bitmapStickerIcon = this.icons.get(i);
//                        int position = bitmapStickerIcon.getPosition();
//                        if (position == 0) {
//                            configIconMatrix(bitmapStickerIcon, f5, f6, calculateRotation);
//                        } else if (position == i3) {
//                            configIconMatrix(bitmapStickerIcon, f7, f8, calculateRotation);
//                        } else if (position == 2) {
//                            configIconMatrix(bitmapStickerIcon, f, f2, calculateRotation);
//                        } else if (position == 3) {
//                            configIconMatrix(bitmapStickerIcon, f3, f4, calculateRotation);
//                        }
//                        bitmapStickerIcon.draw(canvas, this.borderPaint);
//                        i++;
//                        i3 = 1;
//                    }
//                }
//            }
//        }
//    }

    public void drawStickers(Canvas canvas) {
        float f2;
        float f3;
        float f4;
        float f5;
        for (int i = 0; i < this.stickers.size(); i++) {
            Sticker sticker = this.stickers.get(i);
            if (sticker != null && sticker.isShow()) {
                sticker.draw(canvas);
            }
        }
        Sticker sticker2 = this.handlingSticker;
        if (sticker2 != null && !this.locked && (this.showBorder || this.showIcons)) {
            getStickerPoints(sticker2, this.bitmapPoints);
            float[] fArr = this.bitmapPoints;
            float f6 = fArr[0];
            int i2 = 1;
            float f7 = fArr[1];
            int i3 = 2;
            float f8 = fArr[2];
            float f9 = fArr[3];
            float f10 = fArr[4];
            float f11 = fArr[5];
            float f12 = fArr[6];
            float f13 = fArr[7];
            if (this.showBorder) {
                f5 = f13;
                f4 = f12;
                f3 = f11;
                f2 = f10;
                canvas.drawLine(f6, f7, f8, f9, this.borderPaint);
                canvas.drawLine(f6, f7, f2, f3, this.borderPaint);
                canvas.drawLine(f8, f9, f4, f5, this.borderPaint);
                canvas.drawLine(f4, f5, f2, f3, this.borderPaint);
            } else {
                f5 = f13;
                f4 = f12;
                f3 = f11;
                f2 = f10;
            }
            if (this.showIcons) {
                float calculateRotation = calculateRotation(f4, f5, f2, f3);
                int i4 = 0;
                while (i4 < this.icons.size()) {
                    PolishStickerIcons polishStickerIcons = this.icons.get(i4);
                    int position = polishStickerIcons.getPosition();
                    if (position != 0) {
                        if (position != i2) {
                            if (position != i3) {
                                if (position == 3) {
                                    if ((!(this.handlingSticker instanceof PolishTextView) || !polishStickerIcons.getTag().equals(PolishStickerIcons.ROTATE)) && (!(this.handlingSticker instanceof Sticker) || !polishStickerIcons.getTag().equals(PolishStickerIcons.ZOOM))) {
                                        Sticker sticker3 = this.handlingSticker;
                                        if (sticker3 instanceof PolishSticker) {
                                            PolishSticker polishSticker = (PolishSticker) sticker3;
                                            if (polishSticker.getType() != i2) {
                                                if (polishSticker.getType() != 2 && polishSticker.getType() != 8) {
                                                    if (polishSticker.getType() != 4) {
                                                    }
                                                }
                                                configIconMatrix(polishStickerIcons, f4, f5, calculateRotation);
                                                polishStickerIcons.draw(canvas, this.borderPaint);
                                            } else {
                                                configIconMatrix(polishStickerIcons, f4, f5, calculateRotation);
                                                polishStickerIcons.draw(canvas, this.borderPaint);
                                            }
                                        }
                                    } else {
                                        configIconMatrix(polishStickerIcons, f4, f5, calculateRotation);
                                        polishStickerIcons.draw(canvas, this.borderPaint);
                                    }
                                }
                            }
                        } else if (((this.handlingSticker instanceof PolishTextView) && polishStickerIcons.getTag().equals(PolishStickerIcons.EDIT)) || ((this.handlingSticker instanceof Sticker) && polishStickerIcons.getTag().equals(PolishStickerIcons.FLIP))) {
                            configIconMatrix(polishStickerIcons, f8, f9, calculateRotation);
                            polishStickerIcons.draw(canvas, this.borderPaint);
                        }
                        Sticker sticker4 = this.handlingSticker;
                        if (!(sticker4 instanceof PolishSticker)) {
                            configIconMatrix(polishStickerIcons, f2, f3, calculateRotation);
                            polishStickerIcons.draw(canvas, this.borderPaint);
                        } else if (((PolishSticker) sticker4).getType() == 0) {
                            configIconMatrix(polishStickerIcons, f2, f3, calculateRotation);
                            polishStickerIcons.draw(canvas, this.borderPaint);
                        }
                    } else {
                        configIconMatrix(polishStickerIcons, f6, f7, calculateRotation);
                        polishStickerIcons.draw(canvas, this.borderPaintRed);
                    }
                    i4++;
                    i2 = 1;
                    i3 = 2;
                }
            }
        }
        invalidate();
    }



    public void configIconMatrix(PolishStickerIcons bitmapStickerIcon, float f, float f2, float f3) {
        bitmapStickerIcon.setX(f);
        bitmapStickerIcon.setY(f2);
        bitmapStickerIcon.getMatrix().reset();
        bitmapStickerIcon.getMatrix().postRotate(f3, (float) (bitmapStickerIcon.getWidth() / 2), (float) (bitmapStickerIcon.getHeight() / 2));
        bitmapStickerIcon.getMatrix().postTranslate(f - ((float) (bitmapStickerIcon.getWidth() / 2)), f2 - ((float) (bitmapStickerIcon.getHeight() / 2)));
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.locked) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        if (motionEvent.getAction() != 0) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        this.downX = motionEvent.getX();
        this.downY = motionEvent.getY();
        return (findCurrentIconTouched() == null && findHandlingSticker() == null) ? false : true;
    }

    public void setDrawCirclePoint(boolean z) {
        this.drawCirclePoint = z;
        this.onMoving = false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Sticker sticker;
        OnStickerOperationListener onStickerOperationListener2;
        if (this.locked) {
            return super.onTouchEvent(motionEvent);
        }
        switch (MotionEventCompat.getActionMasked(motionEvent)) {
            case 0:
                if (!onTouchDown(motionEvent)) {
                    OnStickerOperationListener onStickerOperationListener3 = this.onStickerOperationListener;
                    if (onStickerOperationListener3 == null) {
                        return false;
                    }
                    onStickerOperationListener3.onStickerTouchOutside();
                    invalidate();
                    if (!this.drawCirclePoint) {
                        return false;
                    }
                }
                break;
            case 1:
                onTouchUp(motionEvent);
                break;
            case 2:
                handleCurrentMode(motionEvent);
                invalidate();
                break;
            case 5:
                this.oldDistance = calculateDistance(motionEvent);
                this.oldRotation = calculateRotation(motionEvent);
                this.midPoint = calculateMidPoint(motionEvent);
                Sticker sticker2 = this.handlingSticker;
                if (sticker2 != null && isInStickerArea(sticker2, motionEvent.getX(1), motionEvent.getY(1)) && findCurrentIconTouched() == null) {
                    this.currentMode = 2;
                    break;
                }
            case 6:
                if (!(this.currentMode != 2 || (sticker = this.handlingSticker) == null || (onStickerOperationListener2 = this.onStickerOperationListener) == null)) {
                    onStickerOperationListener2.onStickerZoom(sticker);
                }
                this.currentMode = 0;
                break;
        }
        return true;
    }

    public boolean onTouchDown(MotionEvent motionEvent) {
        this.currentMode = 1;
        this.downX = motionEvent.getX();
        this.downY = motionEvent.getY();
        this.onMoving = true;
        this.currentMoveingX = motionEvent.getX();
        this.currentMoveingY = motionEvent.getY();
        PointF calculateMidPoint = calculateMidPoint();
        this.midPoint = calculateMidPoint;
        this.oldDistance = calculateDistance(calculateMidPoint.x, this.midPoint.y, this.downX, this.downY);
        this.oldRotation = calculateRotation(this.midPoint.x, this.midPoint.y, this.downX, this.downY);
        PolishStickerIcons findCurrentIconTouched = findCurrentIconTouched();
        this.currentIcon = findCurrentIconTouched;
        if (findCurrentIconTouched != null) {
            this.currentMode = 3;
            findCurrentIconTouched.onActionDown(this, motionEvent);
        } else {
            this.handlingSticker = findHandlingSticker();
        }
        Sticker sticker = this.handlingSticker;
        if (sticker != null) {
            this.downMatrix.set(sticker.getMatrix());
            if (this.bringToFrontCurrentSticker) {
                this.stickers.remove(this.handlingSticker);
                this.stickers.add(this.handlingSticker);
            }
            OnStickerOperationListener onStickerOperationListener2 = this.onStickerOperationListener;
            if (onStickerOperationListener2 != null) {
                onStickerOperationListener2.onStickerTouchedDown(this.handlingSticker);
            }
        }
        if (this.drawCirclePoint) {
            this.onStickerOperationListener.onTouchDownBeauty(this.currentMoveingX, this.currentMoveingY);
            invalidate();
            return true;
        } else if (this.currentIcon == null && this.handlingSticker == null) {
            return false;
        } else {
            invalidate();
            return true;
        }
    }

    public void onTouchUp(MotionEvent motionEvent) {
        Sticker sticker;
        OnStickerOperationListener onStickerOperationListener2;
        Sticker sticker2;
        OnStickerOperationListener onStickerOperationListener3;
        PolishStickerIcons polishStickerIcons;
        long uptimeMillis = SystemClock.uptimeMillis();
        this.onMoving = false;
        if (this.drawCirclePoint) {
            this.onStickerOperationListener.onTouchUpBeauty(motionEvent.getX(), motionEvent.getY());
        }
        if (!(this.currentMode != 3 || (polishStickerIcons = this.currentIcon) == null || this.handlingSticker == null)) {
            polishStickerIcons.onActionUp(this, motionEvent);
        }
        if (this.currentMode == 1 && Math.abs(motionEvent.getX() - this.downX) < ((float) this.touchSlop) && Math.abs(motionEvent.getY() - this.downY) < ((float) this.touchSlop) && (sticker2 = this.handlingSticker) != null) {
            this.currentMode = 4;
            OnStickerOperationListener onStickerOperationListener4 = this.onStickerOperationListener;
            if (onStickerOperationListener4 != null) {
                onStickerOperationListener4.onStickerSelected(sticker2);
            }
            if (uptimeMillis - this.lastClickTime < ((long) this.minClickDelayTime) && (onStickerOperationListener3 = this.onStickerOperationListener) != null) {
                onStickerOperationListener3.onStickerDoubleTap(this.handlingSticker);
            }
        }
        if (!(this.currentMode != 1 || (sticker = this.handlingSticker) == null || (onStickerOperationListener2 = this.onStickerOperationListener) == null)) {
            onStickerOperationListener2.onStickerDrag(sticker);
        }
        this.currentMode = 0;
        this.lastClickTime = uptimeMillis;
    }

    public void handleCurrentMode(MotionEvent motionEvent) {
        PolishStickerIcons polishStickerIcons;
        switch (this.currentMode) {
            case 1:
                this.currentMoveingX = motionEvent.getX();
                float y = motionEvent.getY();
                this.currentMoveingY = y;
                if (this.drawCirclePoint) {
                    this.onStickerOperationListener.onTouchDragBeauty(this.currentMoveingX, y);
                }
                if (this.handlingSticker != null) {
                    this.moveMatrix.set(this.downMatrix);
                    Sticker sticker = this.handlingSticker;
                    if (sticker instanceof PolishSticker) {
                        PolishSticker beautySticker = (PolishSticker) sticker;
                        if (beautySticker.getType() == 10 || beautySticker.getType() == 11) {
                            this.moveMatrix.postTranslate(0.0f, motionEvent.getY() - this.downY);
                        } else {
                            this.moveMatrix.postTranslate(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
                        }
                    } else {
                        this.moveMatrix.postTranslate(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
                    }
                    this.handlingSticker.setMatrix(this.moveMatrix);
                    if (this.constrained) {
                        constrainSticker(this.handlingSticker);
                        return;
                    }
                    return;
                }
                return;
            case 2:
                if (this.handlingSticker != null) {
                    float calculateDistance = calculateDistance(motionEvent);
                    float calculateRotation = calculateRotation(motionEvent);
                    this.moveMatrix.set(this.downMatrix);
                    Matrix matrix = this.moveMatrix;
                    float f = this.oldDistance;
                    matrix.postScale(calculateDistance / f, calculateDistance / f, this.midPoint.x, this.midPoint.y);
                    this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
                    this.handlingSticker.setMatrix(this.moveMatrix);
                    return;
                }
                return;
            case 3:
                if (this.handlingSticker != null && (polishStickerIcons = this.currentIcon) != null) {
                    polishStickerIcons.onActionMove(this, motionEvent);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void zoomAndRotateCurrentSticker(MotionEvent motionEvent) {
        zoomAndRotateSticker(this.handlingSticker, motionEvent);
    }

    public void alignHorizontally() {
        this.moveMatrix.set(this.downMatrix);
        this.moveMatrix.postRotate(-getCurrentSticker().getCurrentAngle(), this.midPoint.x, this.midPoint.y);
        this.handlingSticker.setMatrix(this.moveMatrix);
    }

    public void zoomAndRotateSticker(Sticker sticker, MotionEvent motionEvent) {
        float f;
        if (sticker != null) {
            boolean z = sticker instanceof PolishSticker;
            if (z) {
                PolishSticker beautySticker = (PolishSticker) sticker;
                if (beautySticker.getType() == 10 || beautySticker.getType() == 11) {
                    return;
                }
            }
            if (sticker instanceof PolishTextView) {
                f = this.oldDistance;
            } else {
                f = calculateDistance(this.midPoint.x, this.midPoint.y, motionEvent.getX(), motionEvent.getY());
            }
            float calculateRotation = calculateRotation(this.midPoint.x, this.midPoint.y, motionEvent.getX(), motionEvent.getY());
            this.moveMatrix.set(this.downMatrix);
            Matrix matrix = this.moveMatrix;
            float f2 = this.oldDistance;
            matrix.postScale(f / f2, f / f2, this.midPoint.x, this.midPoint.y);
            if (!z) {
                this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
            }
            this.handlingSticker.setMatrix(this.moveMatrix);
        }
    }

    public void constrainSticker(Sticker sticker) {
        int width = getWidth();
        int height = getHeight();
        sticker.getMappedCenterPoint(this.currentCenterPoint, this.point, this.tmp);
        float f = 0.0f;
        float f2 = this.currentCenterPoint.x < 0.0f ? -this.currentCenterPoint.x : 0.0f;
        float f3 = (float) width;
        if (this.currentCenterPoint.x > f3) {
            f2 = f3 - this.currentCenterPoint.x;
        }
        if (this.currentCenterPoint.y < 0.0f) {
            f = -this.currentCenterPoint.y;
        }
        float f4 = (float) height;
        if (this.currentCenterPoint.y > f4) {
            f = f4 - this.currentCenterPoint.y;
        }
        sticker.getMatrix().postTranslate(f2, f);
    }

    public PolishStickerIcons findCurrentIconTouched() {
        for (PolishStickerIcons next : this.icons) {
            float x = next.getX() - this.downX;
            float y = next.getY() - this.downY;
            if (((double) ((x * x) + (y * y))) <= Math.pow((double) (next.getIconRadius() + next.getIconRadius()), 2.0d)) {
                return next;
            }
        }
        return null;
    }

    public Sticker findHandlingSticker() {
        for (int size = this.stickers.size() - 1; size >= 0; size--) {
            if (isInStickerArea(this.stickers.get(size), this.downX, this.downY)) {
                return this.stickers.get(size);
            }
        }
        return null;
    }

    public boolean isInStickerArea(Sticker sticker, float f, float f2) {
        float[] fArr = this.tmp;
        fArr[0] = f;
        fArr[1] = f2;
        return sticker.contains(fArr);
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
        Sticker sticker = this.handlingSticker;
        if (sticker == null) {
            this.midPoint.set(0.0f, 0.0f);
            return this.midPoint;
        }
        sticker.getMappedCenterPoint(this.midPoint, this.point, this.tmp);
        return this.midPoint;
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

    public float calculateDistance(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateDistance(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }

    public float calculateDistance(float f, float f2, float f3, float f4) {
        double d = (double) (f - f3);
        double d2 = (double) (f2 - f4);
        return (float) Math.sqrt((d * d) + (d2 * d2));
    }

    public void transformSticker(Sticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null");
            return;
        }
        this.sizeMatrix.reset();
        float width = (float) getWidth();
        float height = (float) getHeight();
        float width2 = (float) sticker.getWidth();
        float height2 = (float) sticker.getHeight();
        this.sizeMatrix.postTranslate((width - width2) / 2.0f, (height - height2) / 2.0f);
        float f = (width < height ? width / width2 : height / height2) / 2.0f;
        this.sizeMatrix.postScale(f, f, width / 2.0f, height / 2.0f);
        sticker.getMatrix().reset();
        sticker.setMatrix(this.sizeMatrix);
        invalidate();
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        for (int i5 = 0; i5 < this.stickers.size(); i5++) {
            Sticker sticker = this.stickers.get(i5);
            if (sticker != null) {
                transformSticker(sticker);
            }
        }
    }

    public void flipCurrentSticker(int i) {
        flip(this.handlingSticker, i);
    }

    public void flip(Sticker sticker, int i) {
        if (sticker != null) {
            sticker.getCenterPoint(this.midPoint);
            if ((i & 1) > 0) {
                sticker.getMatrix().preScale(-1.0f, 1.0f, this.midPoint.x, this.midPoint.y);
                sticker.setFlippedHorizontally(!sticker.isFlippedHorizontally());
            }
            if ((i & 2) > 0) {
                sticker.getMatrix().preScale(1.0f, -1.0f, this.midPoint.x, this.midPoint.y);
                sticker.setFlippedVertically(!sticker.isFlippedVertically());
            }
            OnStickerOperationListener onStickerOperationListener2 = this.onStickerOperationListener;
            if (onStickerOperationListener2 != null) {
                onStickerOperationListener2.onStickerFlip(sticker);
            }
            invalidate();
        }
    }

    public boolean replace(Sticker sticker) {
        return replace(sticker, true);
    }

    public Sticker getLastHandlingSticker() {
        return this.lastHandlingSticker;
    }

    public boolean replace(Sticker sticker, boolean z) {
        float f;
        if (this.handlingSticker == null) {
            this.handlingSticker = this.lastHandlingSticker;
        }
        if (this.handlingSticker == null || sticker == null) {
            return false;
        }
        float width = (float) getWidth();
        float height = (float) getHeight();
        if (z) {
            sticker.setMatrix(this.handlingSticker.getMatrix());
            sticker.setFlippedVertically(this.handlingSticker.isFlippedVertically());
            sticker.setFlippedHorizontally(this.handlingSticker.isFlippedHorizontally());
        } else {
            this.handlingSticker.getMatrix().reset();
            sticker.getMatrix().postTranslate((width - ((float) this.handlingSticker.getWidth())) / 2.0f, (height - ((float) this.handlingSticker.getHeight())) / 2.0f);
            if (width < height) {
                Sticker sticker2 = this.handlingSticker;
                if (sticker2 instanceof PolishTextView) {
                    f = width / ((float) sticker2.getWidth());
                } else {
                    f = width / ((float) sticker2.getDrawable().getIntrinsicWidth());
                }
            } else {
                Sticker sticker3 = this.handlingSticker;
                if (sticker3 instanceof PolishTextView) {
                    f = height / ((float) sticker3.getHeight());
                } else {
                    f = height / ((float) sticker3.getDrawable().getIntrinsicHeight());
                }
            }
            float f2 = f / 2.0f;
            sticker.getMatrix().postScale(f2, f2, width / 2.0f, height / 2.0f);
        }
        List<Sticker> list = this.stickers;
        list.set(list.indexOf(this.handlingSticker), sticker);
        this.handlingSticker = sticker;
        invalidate();
        return true;
    }

    public boolean remove(Sticker sticker) {
        if (this.stickers.contains(sticker)) {
            this.stickers.remove(sticker);
            OnStickerOperationListener onStickerOperationListener2 = this.onStickerOperationListener;
            if (onStickerOperationListener2 != null) {
                onStickerOperationListener2.onStickerDeleted(sticker);
            }
            if (this.handlingSticker == sticker) {
                this.handlingSticker = null;
            }
            invalidate();
            return true;
        }
        Log.d(TAG, "remove: the sticker is not in this StickerView");
        return false;
    }

    public boolean removeCurrentSticker() {
        return remove(this.handlingSticker);
    }

    public void removeAllStickers() {
        this.stickers.clear();
        Sticker sticker = this.handlingSticker;
        if (sticker != null) {
            sticker.release();
            this.handlingSticker = null;
        }
        invalidate();
    }

    public PolishStickerView addSticker(Sticker sticker) {
        return addSticker(sticker, 1);
    }

    public PolishStickerView addSticker(final Sticker sticker, final int i) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, i);
        } else {
            post(new Runnable() {

                public void run() {
                    PolishStickerView.this.addStickerImmediately(sticker, i);
                }
            });
        }
        return this;
    }

    public void addStickerImmediately(Sticker sticker, int i) {
        setStickerPosition(sticker, i);
        sticker.getMatrix().postScale(1.0f, 1.0f, (float) getWidth(), (float) getHeight());
        this.handlingSticker = sticker;
        this.stickers.add(sticker);
        OnStickerOperationListener onStickerOperationListener2 = this.onStickerOperationListener;
        if (onStickerOperationListener2 != null) {
            onStickerOperationListener2.onAddSticker(sticker);
        }
        invalidate();
    }

    public void setStickerPosition(Sticker sticker, int i) {
        float f;
        float width = ((float) getWidth()) - ((float) sticker.getWidth());
        float height = ((float) getHeight()) - ((float) sticker.getHeight());
        if (sticker instanceof PolishSticker) {
            PolishSticker beautySticker = (PolishSticker) sticker;
            f = height / 2.0f;
            if (beautySticker.getType() == 0) {
                width /= 3.0f;
            } else if (beautySticker.getType() == 1) {
                width = (2.0f * width) / 3.0f;
            } else if (beautySticker.getType() == 2) {
                width /= 2.0f;
            } else if (beautySticker.getType() == 4) {
                width /= 2.0f;
            } else if (beautySticker.getType() == 10) {
                width /= 2.0f;
                f = (2.0f * f) / 3.0f;
            } else if (beautySticker.getType() == 11) {
                width /= 2.0f;
                f = (3.0f * f) / 2.0f;
            }
        } else {
            float f2 = (i & 2) > 0 ? height / 4.0f : (i & 16) > 0 ? height * 0.75f : height / 2.0f;
            width = (i & 4) > 0 ? width / 4.0f : (i & 8) > 0 ? width * 0.75f : width / 2.0f;
            f = f2;
        }
        sticker.getMatrix().postTranslate(width, f);
    }

    public void editTextSticker() {
        this.onStickerOperationListener.onStickerDoubleTap(this.handlingSticker);
    }


    public float[] getStickerPoints(Sticker sticker) {
        float[] fArr = new float[8];
        getStickerPoints(sticker, fArr);
        return fArr;
    }

    public void getStickerPoints(Sticker sticker, float[] fArr) {
        if (sticker == null) {
            Arrays.fill(fArr, 0.0f);
            return;
        }
        sticker.getBoundPoints(this.bounds);
        sticker.getMappedPoints(fArr, this.bounds);
    }

    public void save(File file) {
        try {
            StickerUtils.saveImageToGallery(file, createBitmap());
            StickerUtils.notifySystemGallery(getContext(), file);
        } catch (IllegalArgumentException | IllegalStateException e) {
        }
    }

    public Bitmap createBitmap() throws OutOfMemoryError {
        this.handlingSticker = null;
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public int getStickerCount() {
        return this.stickers.size();
    }

    public boolean isNoneSticker() {
        return getStickerCount() == 0;
    }

    public PolishStickerView setLocked(boolean z) {
        this.locked = z;
        invalidate();
        return this;
    }

    public PolishStickerView setMinClickDelayTime(int i) {
        this.minClickDelayTime = i;
        return this;
    }

    public int getMinClickDelayTime() {
        return this.minClickDelayTime;
    }

    public boolean isConstrained() {
        return this.constrained;
    }

    public PolishStickerView setConstrained(boolean z) {
        this.constrained = z;
        postInvalidate();
        return this;
    }

    public PolishStickerView setOnStickerOperationListener(OnStickerOperationListener onStickerOperationListener2) {
        this.onStickerOperationListener = onStickerOperationListener2;
        return this;
    }

    public OnStickerOperationListener getOnStickerOperationListener() {
        return this.onStickerOperationListener;
    }

    public Sticker getCurrentSticker() {
        return this.handlingSticker;
    }

    public List<PolishStickerIcons> getIcons() {
        return this.icons;
    }

    public void setIcons(List<PolishStickerIcons> list) {
        this.icons.clear();
        this.icons.addAll(list);
        invalidate();
    }
}
