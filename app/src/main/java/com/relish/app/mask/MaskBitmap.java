package com.relish.app.mask;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.core.internal.view.SupportMenu;

import java.lang.reflect.Array;
import java.util.Random;

public class MaskBitmap {
    public static float ANIMATED_FRAME = 22.0f;
    private static int ANIMATED_FRAME_CAL = ((int) (22.0f - 1.0f));
    private static final Paint paint;
    private static int[][] randRect = ((int[][]) Array.newInstance(Integer.TYPE, 20, 20));
    private static Random random = new Random();

    static {
        Paint paint2 = new Paint();
        paint = paint2;
        paint2.setColor(Color.BLACK);
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public enum EFFECT {
        CIRCLE_LEFT_TOP("CIRCLE LEFT TOP") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawCircle(0.0f, 0.0f, (((float) Math.sqrt((double) ((w * w) + (h * h)))) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor), paint);
                drawText(canvas);
                return mask;
            }
        },
        CIRCLE_RIGHT_TOP("Circle right top") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawCircle((float) w, 0.0f, (((float) Math.sqrt((double) ((w * w) + (h * h)))) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor), paint);
                drawText(canvas);
                return mask;
            }
        },
        CIRCLE_LEFT_BOTTOM("Circle left bottom") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawCircle(0.0f, (float) h, (((float) Math.sqrt((double) ((w * w) + (h * h)))) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor), paint);
                drawText(canvas);
                return mask;
            }
        },
        CIRCLE_RIGHT_BOTTOM("Circle right bottom") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawCircle((float) w, (float) h, (((float) Math.sqrt((double) ((w * w) + (h * h)))) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor), paint);
                drawText(canvas);
                return mask;
            }
        },
        CIRCLE_IN("Circle in") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint(1);
                paint.setColor(Color.BLACK);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float r = MaskBitmap.getRad(w * 2, h * 2);
                float f = (r / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                paint.setColor(Color.BLACK);
                canvas.drawColor(Color.BLACK);
                paint.setColor(0);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
                canvas.drawCircle(((float) w) / 2.0f, ((float) h) / 2.0f, r - f, paint);
                drawText(canvas);
                return mask;
            }
        },
        CIRCLE_OUT("Circle out") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawCircle(((float) w) / 2.0f, ((float) h) / 2.0f, (MaskBitmap.getRad(w * 2, h * 2) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor), paint);
                drawText(canvas);
                return mask;
            }
        },
        CROSS_IN("Cross in") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float fx = (((float) w) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float fy = (((float) h) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, 0.0f);
                path.lineTo(fx, 0.0f);
                path.lineTo(fx, fy);
                path.lineTo(0.0f, fy);
                path.lineTo(0.0f, 0.0f);
                path.close();
                path.moveTo((float) w, 0.0f);
                path.lineTo(((float) w) - fx, 0.0f);
                path.lineTo(((float) w) - fx, fy);
                path.lineTo((float) w, fy);
                path.lineTo((float) w, 0.0f);
                path.close();
                path.moveTo((float) w, (float) h);
                path.lineTo(((float) w) - fx, (float) h);
                path.lineTo(((float) w) - fx, ((float) h) - fy);
                path.lineTo((float) w, ((float) h) - fy);
                path.lineTo((float) w, (float) h);
                path.close();
                path.moveTo(0.0f, (float) h);
                path.lineTo(fx, (float) h);
                path.lineTo(fx, ((float) h) - fy);
                path.lineTo(0.0f, ((float) h) - fy);
                path.lineTo(0.0f, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return mask;
            }
        },
        CROSS_OUT("Cross out") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float fx = (((float) w) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float fy = (((float) h) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                Path path = new Path();
                path.moveTo((((float) w) / 2.0f) + fx, 0.0f);
                path.lineTo((((float) w) / 2.0f) + fx, (((float) h) / 2.0f) - fy);
                path.lineTo((float) w, (((float) h) / 2.0f) - fy);
                path.lineTo((float) w, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) + fx, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) + fx, (float) h);
                path.lineTo((((float) w) / 2.0f) - fx, (float) h);
                path.lineTo((((float) w) / 2.0f) - fx, (((float) h) / 2.0f) - fy);
                path.lineTo(0.0f, (((float) h) / 2.0f) - fy);
                path.lineTo(0.0f, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) - fx, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) - fx, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return mask;
            }
        },
        DIAMOND_IN("Diamond in") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                Path path = new Path();
                float fx = (((float) w) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float fy = (((float) h) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                path.moveTo(((float) w) / 2.0f, (((float) h) / 2.0f) - fy);
                path.lineTo((((float) w) / 2.0f) + fx, ((float) h) / 2.0f);
                path.lineTo(((float) w) / 2.0f, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) - fx, ((float) h) / 2.0f);
                path.lineTo(((float) w) / 2.0f, (((float) h) / 2.0f) - fy);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        DIAMOND_OUT("Diamond out") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint(1);
                paint.setColor(Color.BLACK);
                paint.setColor(0);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawColor(Color.BLACK);
                Path path = new Path();
                float fx = ((float) w) - ((((float) w) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor));
                float fy = ((float) h) - ((((float) h) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor));
                path.moveTo(((float) w) / 2.0f, (((float) h) / 2.0f) - fy);
                path.lineTo((((float) w) / 2.0f) + fx, ((float) h) / 2.0f);
                path.lineTo(((float) w) / 2.0f, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) - fx, ((float) h) / 2.0f);
                path.lineTo(((float) w) / 2.0f, (((float) h) / 2.0f) - fy);
                path.close();
                paint.setColor(0);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
                canvas.drawPath(path, paint);
                drawText(canvas);
                return mask;
            }
        },
        ECLIPSE_IN("Eclipse in") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float hf = (((float) h) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float wf = (((float) w) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                RectF rectFL = new RectF(-wf, 0.0f, wf, (float) h);
                RectF rectFT = new RectF(0.0f, -hf, (float) w, hf);
                RectF rectFR = new RectF(((float) w) - wf, 0.0f, ((float) w) + wf, (float) h);
                RectF rectFB = new RectF(0.0f, ((float) h) - hf, (float) w, ((float) h) + hf);
                canvas.drawOval(rectFL, MaskBitmap.paint);
                canvas.drawOval(rectFT, MaskBitmap.paint);
                canvas.drawOval(rectFR, MaskBitmap.paint);
                canvas.drawOval(rectFB, MaskBitmap.paint);
                drawText(canvas);
                return mask;
            }
        },
        FOUR_TRIANGLE("Four triangle") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float ratioY = (((float) h) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, ratioY);
                path.lineTo(0.0f, 0.0f);
                path.lineTo(ratioX, 0.0f);
                path.lineTo((float) w, ((float) h) - ratioY);
                path.lineTo((float) w, (float) h);
                path.lineTo(((float) w) - ratioX, (float) h);
                path.lineTo(0.0f, ratioY);
                path.close();
                path.moveTo(((float) w) - ratioX, 0.0f);
                path.lineTo((float) w, 0.0f);
                path.lineTo((float) w, ratioY);
                path.lineTo(ratioX, (float) h);
                path.lineTo(0.0f, (float) h);
                path.lineTo(0.0f, ((float) h) - ratioY);
                path.lineTo(((float) w) - ratioX, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        HORIZONTAL_RECT("Horizontal rect") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                float wf = ((float) w) / 10.0f;
                float rectW = (wf / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                for (int i = 0; i < 10; i++) {
                    canvas.drawRect(new Rect((int) (((float) i) * wf), 0, (int) ((((float) i) * wf) + rectW), h), paint);
                }
                drawText(canvas);
                return mask;
            }
        },
        HORIZONTAL_COLUMN_DOWNMASK("Horizontal column downmask") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float factorX = ((float) MaskBitmap.ANIMATED_FRAME_CAL) / 2.0f;
                canvas.drawRoundRect(new RectF(0.0f, 0.0f, (((float) w) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) / 2.0f)) * ((float) factor), ((float) h) / 2.0f), 0.0f, 0.0f, paint);
                if (((float) factor) >= 0.5f + factorX) {
                    canvas.drawRoundRect(new RectF(((float) w) - ((((float) w) / (((float) (MaskBitmap.ANIMATED_FRAME_CAL - 1)) / 2.0f)) * ((float) ((int) (((float) factor) - factorX)))), ((float) h) / 2.0f, (float) w, (float) h), 0.0f, 0.0f, paint);
                }
                return mask;
            }
        },
        LEAF("LEAF") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setStrokeCap(Paint.Cap.BUTT);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                float fx = (float) ((w / MaskBitmap.ANIMATED_FRAME_CAL) * factor);
                float fy = (float) ((h / MaskBitmap.ANIMATED_FRAME_CAL) * factor);
                Canvas canvas = new Canvas(mask);
                Path path = new Path();
                path.moveTo(0.0f, (float) h);
                path.cubicTo(0.0f, (float) h, (((float) w) / 2.0f) - fx, (((float) h) / 2.0f) - fy, (float) w, 0.0f);
                path.cubicTo((float) w, 0.0f, (((float) w) / 2.0f) + fx, (((float) h) / 2.0f) + fy, 0.0f, (float) h);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return mask;
            }
        },
        OPEN_DOOR("OPEN_DOOR") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setStrokeCap(Paint.Cap.BUTT);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                float fx = (float) ((w / MaskBitmap.ANIMATED_FRAME_CAL) * factor);
                float f = (float) ((h / MaskBitmap.ANIMATED_FRAME_CAL) * factor);
                Canvas canvas = new Canvas(mask);
                Path path = new Path();
                path.moveTo((float) (w / 2), 0.0f);
                path.lineTo(((float) (w / 2)) - fx, 0.0f);
                path.lineTo(((float) (w / 2)) - (fx / 2.0f), (float) (h / 6));
                path.lineTo(((float) (w / 2)) - (fx / 2.0f), (float) (h - (h / 6)));
                path.lineTo(((float) (w / 2)) - fx, (float) h);
                path.lineTo(((float) (w / 2)) + fx, (float) h);
                path.lineTo(((float) (w / 2)) + (fx / 2.0f), (float) (h - (h / 6)));
                path.lineTo(((float) (w / 2)) + (fx / 2.0f), (float) (h / 6));
                path.lineTo(((float) (w / 2)) + fx, 0.0f);
                path.lineTo(((float) (w / 2)) - fx, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return mask;
            }
        },
        PIN_WHEEL("PIN_WHEEL") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float rationX = (((float) w) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float rationY = (((float) h) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                Path path = new Path();
                path.moveTo(((float) w) / 2.0f, ((float) h) / 2.0f);
                path.lineTo(0.0f, (float) h);
                path.lineTo(rationX, (float) h);
                path.close();
                path.moveTo(((float) w) / 2.0f, ((float) h) / 2.0f);
                path.lineTo((float) w, (float) h);
                path.lineTo((float) w, ((float) h) - rationY);
                path.close();
                path.moveTo(((float) w) / 2.0f, ((float) h) / 2.0f);
                path.lineTo((float) w, 0.0f);
                path.lineTo(((float) w) - rationX, 0.0f);
                path.close();
                path.moveTo(((float) w) / 2.0f, ((float) h) / 2.0f);
                path.lineTo(0.0f, 0.0f);
                path.lineTo(0.0f, rationY);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        RECT_RANDOM("RECT_RANDOM") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                int i = 1;
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                float wf = (float) (w / MaskBitmap.ANIMATED_FRAME_CAL);
                float hf = (float) (h / MaskBitmap.ANIMATED_FRAME_CAL);
                int i2 = 0;
                while (i2 < MaskBitmap.randRect.length) {
                    int rand = MaskBitmap.random.nextInt(MaskBitmap.randRect[i2].length);
                    while (MaskBitmap.randRect[i2][rand] == i) {
                        rand = MaskBitmap.random.nextInt(MaskBitmap.randRect[i2].length);
                    }
                    MaskBitmap.randRect[i2][rand] = i;
                    int j = 0;
                    while (j < MaskBitmap.randRect[i2].length) {
                        if (MaskBitmap.randRect[i2][j] == i) {
                            canvas.drawRoundRect(new RectF(((float) i2) * wf, ((float) j) * hf, (((float) i2) + 1.0f) * wf, (((float) j) + 1.0f) * hf), 0.0f, 0.0f, paint);
                        }
                        j++;
                        i = 1;
                    }
                    i2++;
                    i = 1;
                }
                drawText(canvas);
                return mask;
            }
        },
        SKEW_LEFT_MEARGE("SKEW_LEFT_MEARGE") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float ratioY = (((float) h) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, ratioY);
                path.lineTo(ratioX, 0.0f);
                path.lineTo(0.0f, 0.0f);
                path.close();
                path.moveTo(((float) w) - ratioX, (float) h);
                path.lineTo((float) w, ((float) h) - ratioY);
                path.lineTo((float) w, (float) h);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        SKEW_LEFT_SPLIT("SKEW_LEFT_SPLIT") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float ratioY = (((float) h) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, ratioY);
                path.lineTo(0.0f, 0.0f);
                path.lineTo(ratioX, 0.0f);
                path.lineTo((float) w, ((float) h) - ratioY);
                path.lineTo((float) w, (float) h);
                path.lineTo(((float) w) - ratioX, (float) h);
                path.lineTo(0.0f, ratioY);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        SKEW_RIGHT_SPLIT("SKEW_RIGHT_SPLIT") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float ratioY = (((float) h) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                Path path = new Path();
                path.moveTo(((float) w) - ratioX, 0.0f);
                path.lineTo((float) w, 0.0f);
                path.lineTo((float) w, ratioY);
                path.lineTo(ratioX, (float) h);
                path.lineTo(0.0f, (float) h);
                path.lineTo(0.0f, ((float) h) - ratioY);
                path.lineTo(((float) w) - ratioX, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        SKEW_RIGHT_MEARGE("SKEW_RIGHT_MEARGE") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float ratioY = (((float) h) / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, ((float) h) - ratioY);
                path.lineTo(ratioX, (float) h);
                path.lineTo(0.0f, (float) h);
                path.close();
                path.moveTo(((float) w) - ratioX, 0.0f);
                path.lineTo((float) w, ratioY);
                path.lineTo((float) w, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        SQUARE_IN("SQUARE_IN") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float ratioY = (((float) h) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, 0.0f);
                path.lineTo(0.0f, (float) h);
                path.lineTo(ratioX, (float) h);
                path.lineTo(ratioX, 0.0f);
                path.moveTo((float) w, (float) h);
                path.lineTo((float) w, 0.0f);
                path.lineTo(((float) w) - ratioX, 0.0f);
                path.lineTo(((float) w) - ratioX, (float) h);
                path.moveTo(ratioX, ratioY);
                path.lineTo(ratioX, 0.0f);
                path.lineTo(((float) w) - ratioX, 0.0f);
                path.lineTo(((float) w) - ratioX, ratioY);
                path.moveTo(ratioX, ((float) h) - ratioY);
                path.lineTo(ratioX, (float) h);
                path.lineTo(((float) w) - ratioX, (float) h);
                path.lineTo(((float) w) - ratioX, ((float) h) - ratioY);
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        SQUARE_OUT("SQUARE_OUT") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                float ratioX = (((float) w) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float ratioY = (((float) h) / (((float) MaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                new Canvas(mask).drawRect(new RectF(((float) (w / 2)) - ratioX, ((float) (h / 2)) - ratioY, ((float) (w / 2)) + ratioX, ((float) (h / 2)) + ratioY), paint);
                return mask;
            }
        },
        VERTICAL_RECT("VERTICAL_RECT") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                float hf = ((float) h) / 10.0f;
                float rectH = (((float) factor) * hf) / ((float) MaskBitmap.ANIMATED_FRAME_CAL);
                for (int i = 0; i < 10; i++) {
                    canvas.drawRect(new Rect(0, (int) (((float) i) * hf), w, (int) ((((float) i) * hf) + rectH)), paint);
                }
                drawText(canvas);
                return mask;
            }
        },
        WIND_MILL("WIND_MILL") {
            @Override 
            public Bitmap getMask(int w, int h, int factor) {
                float r = MaskBitmap.getRad(w, h);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                RectF oval = new RectF();
                oval.set((((float) w) / 2.0f) - r, (((float) h) / 2.0f) - r, (((float) w) / 2.0f) + r, (((float) h) / 2.0f) + r);
                float angle = (90.0f / ((float) MaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                canvas.drawArc(oval, 90.0f, angle, true, paint);
                canvas.drawArc(oval, 180.0f, angle, true, paint);
                canvas.drawArc(oval, 270.0f, angle, true, paint);
                canvas.drawArc(oval, 360.0f, angle, true, paint);
                drawText(canvas);
                return mask;
            }
        };
        
        String name;

        public abstract Bitmap getMask(int i, int i2, int i3);

        private EFFECT(String name2) {
            this.name = "";
            this.name = name2;
        }

        public void drawText(Canvas canvas) {
            Paint paint = new Paint();
            paint.setTextSize(50.0f);
            paint.setColor(SupportMenu.CATEGORY_MASK);
        }
    }

    public static void reintRect() {
        Class cls = Integer.TYPE;
        float f = ANIMATED_FRAME;
        randRect = (int[][]) Array.newInstance(cls, (int) f, (int) f);
        for (int i = 0; i < randRect.length; i++) {
            int j = 0;
            while (true) {
                int[][] iArr = randRect;
                if (j >= iArr[i].length) {
                    break;
                }
                iArr[i][j] = 0;
                j++;
            }
        }
    }

    static float getRad(int w, int h) {
        return (float) Math.sqrt((double) (((w * w) + (h * h)) / 4));
    }
}
