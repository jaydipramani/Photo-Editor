package com.relish.app.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.exifinterface.media.ExifInterface;

import com.relish.app.Activity.PolishBaseActivity;
import com.relish.app.Activity.PolishEditorActivity;
import com.relish.app.R;
import com.relish.app.Utils.BitmapTransfer;
import com.relish.app.Utils.MotionUtils;
import com.relish.app.crop.CropAsyncTask;
import com.relish.app.crop.CropTaskCompleted;
import com.relish.app.eraser.StickerEraseActivity;
import com.relish.app.polish.PolishMotionView;
import com.relish.app.polish.PolishMotionViewTouchBase;
import com.relish.app.support.Constants;
import com.relish.app.support.MyExceptionHandlerPix;


public class MotionLayout extends PolishBaseActivity {
    static final LinearLayout.LayoutParams LAYOUT_PARAMS;
    public static Bitmap btimapOraginal;
    public static Bitmap resultBmp;
    public int CountCurrentProgress = 2;
    public int OpacityCurrentProgress = 200;
    double Rotation = 0.0d;
    double alpha = Math.toRadians(this.motionDirection);
    public int count = 0;
    Bitmap cropped = null;
    int currentColor = -1;
    int i = 0;
    public Bitmap imageBitmap = null;
    PolishMotionView imageViewCenter;
    ImageView imageViewCover;
    ImageView imageViewSave;
    public boolean isReady = false;
    ImageView iv_erase;
    int left;
    float leftX = ((float) this.left);
    private Bitmap mainBitmap = null;
    public Matrix matrix = null;
    Matrix matrixCenter = null;
    double motionDirection = 30.0d;
    private SeekBar seekbarCount = null;
    private SeekBar seekbarOpacity = null;
    private SeekBar seekbarRotate = null;
    public TextView textViewValueCount;
    public TextView textViewValueOpacity;
    public TextView textViewValueRotate;
    int top;
    float topY = ((float) this.top);

    static {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MotionUtils.dpToPx(30), MotionUtils.dpToPx(30));
        LAYOUT_PARAMS = layoutParams;
        int dpToPx = MotionUtils.dpToPx(5);
        layoutParams.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
    }

    public static void setFaceBitmap(Bitmap bitmap) {
        btimapOraginal = bitmap;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_motion);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(this));
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.crop_progress_bar);
        this.imageViewCenter = (PolishMotionView) findViewById(R.id.imageViewTouch);
        this.imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
        this.iv_erase = (ImageView) findViewById(R.id.image_view_compare_eraser);
        this.imageViewSave = (ImageView) findViewById(R.id.imageViewSaveMotion);
        this.imageViewCenter.setImageBitmap(btimapOraginal);
        this.imageViewCenter.setDisplayType(PolishMotionViewTouchBase.DisplayType.FIT_TO_SCREEN);
        this.imageViewCenter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    MotionLayout.this.imageViewCenter.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    MotionLayout.this.imageViewCenter.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                if (MotionLayout.this.i == 0) {
                    MotionLayout.this.imageBitmap = Bitmap.createScaledBitmap(MotionLayout.btimapOraginal, MotionLayout.btimapOraginal.getWidth(), MotionLayout.btimapOraginal.getHeight(), true);
                    MotionLayout.this.imageViewCover.setImageBitmap(MotionLayout.this.imageBitmap);
                    MotionLayout motionLayout = MotionLayout.this;
                    motionLayout.matrixCenter = motionLayout.imageViewCenter.getImageViewMatrix();
                    MotionLayout.this.i++;
                    MotionLayout.this.imageViewCover.setImageMatrix(MotionLayout.this.matrixCenter);
                    if (MotionLayout.this.matrix == null) {
                        MotionLayout motionLayout2 = MotionLayout.this;
                        motionLayout2.matrix = motionLayout2.matrixCenter;
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    new CountDownTimer(21000, 1000) {

                        public void onFinish() {
                        }

                        public void onTick(long j) {
                            MotionLayout.this.count++;
                            if (progressBar.getProgress() <= 90) {
                                progressBar.setProgress(MotionLayout.this.count * 5);
                            }
                        }
                    }.start();
                    new CropAsyncTask(new CropTaskCompleted() {

                        @Override
                        public final void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int i, int i2) {
                            taskCompleted(bitmap, bitmap2, i, i2);
                        }
                    }, MotionLayout.this, progressBar).execute(new Void[0]);
                }
            }

            public void taskCompleted(Bitmap bitmap, Bitmap bitmap2, int i, int i2) {
                if (bitmap != null) {
                    MotionLayout.this.cropped = bitmap;
                    MotionLayout.this.left = i;
                    MotionLayout.this.top = i2;
                    MotionLayout motionLayout = MotionLayout.this;
                    motionLayout.leftX = (float) motionLayout.left;
                    MotionLayout motionLayout2 = MotionLayout.this;
                    motionLayout2.topY = (float) motionLayout2.top;
                    MotionLayout.this.isReady = true;
                    int i3 = MotionLayout.this.currentColor;
                    Color.parseColor("#FFFFFF");
                    MotionLayout.this.methodCropAsyncTaskPaint(0.0f, 0.0f, 0.0f, 0.0f);
                }
            }
        });
        setRotate();
        setCount();
        setOpacity();
        this.imageViewSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (MotionLayout.this.mainBitmap != null) {
                    BitmapTransfer.bitmap = MotionLayout.this.mainBitmap;
                    Intent intent = new Intent(MotionLayout.this, PolishEditorActivity.class);
                    intent.putExtra("MESSAGE", "done");
                    MotionLayout.this.setResult(-1, intent);
                    MotionLayout.this.finish();
                }
            }
        });
        findViewById(R.id.image_view_compare_eraser).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                StickerEraseActivity.b = MotionLayout.this.imageBitmap;
                Intent intent = new Intent(MotionLayout.this, StickerEraseActivity.class);
                intent.putExtra(Constants.KEY_OPEN_FROM, Constants.VALUE_OPEN_FROM_MOTION);
                MotionLayout.this.startActivityForResult(intent, 1024);
            }
        });
        findViewById(R.id.imageViewCloseMotion).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                MotionLayout.this.onBackPressed();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1024 && (bitmap = resultBmp) != null) {
            this.cropped = bitmap;
            this.imageViewCover.setImageBitmap(this.mainBitmap);
            this.isReady = true;
            methodCropAsyncTaskPaint(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    public void setRotate() {
        if (this.seekbarRotate == null) {
            this.seekbarRotate = (SeekBar) findViewById(R.id.seekbarRotate);
            this.textViewValueRotate = (TextView) findViewById(R.id.textViewValueRotate);
            this.seekbarRotate.setMax(360);
            this.seekbarRotate.setProgress(0);
            this.textViewValueRotate.setText("0");
            this.seekbarRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    TextView textViewValueRotate = MotionLayout.this.textViewValueRotate;
                    textViewValueRotate.setText("" + i);
                    MotionLayout.this.Rotation = Math.toRadians((double) i);
                    MotionLayout.this.methodCropAsyncTaskPaint(0.0f, 0.0f, 0.0f, 0.0f);
                }
            });
        }
    }

    public void setCount() {
        if (this.seekbarCount == null) {
            this.seekbarCount = (SeekBar) findViewById(R.id.seekbarCount);
            this.textViewValueCount = (TextView) findViewById(R.id.textViewValueCount);
            this.seekbarCount.setMax(50);
            this.seekbarCount.setProgress(2);
            this.textViewValueCount.setText(ExifInterface.GPS_MEASUREMENT_2D);
            this.seekbarCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    MotionLayout.this.CountCurrentProgress = i;
                    if (MotionLayout.this.CountCurrentProgress < 0) {
                        MotionLayout.this.CountCurrentProgress = 0;
                    }
                    TextView textViewValueCount = MotionLayout.this.textViewValueCount;
                    textViewValueCount.setText("" + i);
                    MotionLayout.this.methodCropAsyncTaskPaint(0.0f, 0.0f, 0.0f, 0.0f);
                }
            });
        }
    }

    public void setOpacity() {
        if (this.seekbarOpacity == null) {
            this.seekbarOpacity = (SeekBar) findViewById(R.id.seekbarOpacity);
            this.textViewValueOpacity = (TextView) findViewById(R.id.textViewValueOpacity);
            this.seekbarOpacity.setMax(100);
            int i2 = (this.OpacityCurrentProgress * 100) / 255;
            TextView textView = this.textViewValueOpacity;
            textView.setText("" + i2);
            this.seekbarOpacity.setProgress(i2);
            this.seekbarOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    MotionLayout.this.OpacityCurrentProgress = (i * 255) / 100;
                    if (MotionLayout.this.OpacityCurrentProgress < 0) {
                        MotionLayout.this.OpacityCurrentProgress = 0;
                    }
                    TextView textViewValueOpacity = MotionLayout.this.textViewValueOpacity;
                    textViewValueOpacity.setText("" + i);
                    MotionLayout.this.methodCropAsyncTaskPaint(0.0f, 0.0f, 0.0f, 0.0f);
                }
            });
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        methodCropAsyncTaskPaint(motionEvent.getX(), motionEvent.getY(), 0.0f, 0.0f);
        return true;
    }

    public void methodCropAsyncTaskPaint(float f, float f2, float f3, float f4) {
        this.alpha = this.Rotation;
        paintBitmaps();
        this.leftX += f;
        this.topY += f2;
    }

    private Bitmap paintBitmaps() {
        Paint paint = null;
        if (!this.isReady) {
            return null;
        }
        Log.d("alpha=", "" + this.alpha);
        Bitmap copy = btimapOraginal.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(copy);
        Bitmap changeAlpha = MotionUtils.changeAlpha(this.cropped, this.OpacityCurrentProgress);
        int i2 = this.left;
        int i22 = this.top;
        double cos = Math.cos(this.alpha);
        double sin = Math.sin(this.alpha);
        Log.d("alphacos=", "" + cos);
        Log.d("alphasin=", "" + sin);
        int i3 = this.CountCurrentProgress;
        if (i3 > 0) {
            int dpToPx = MotionUtils.dpToPx(180 / i3);
            int i32 = this.CountCurrentProgress;
            while (i32 > 0) {
                double d = (double) this.left;
                double d2 = (double) (dpToPx * i32);
                Double.isNaN(d2);
                Double.isNaN(d);
                double d3 = (double) this.top;
                Double.isNaN(d2);
                Double.isNaN(d3);
                canvas.drawBitmap(changeAlpha, (float) ((int) (d + (d2 * cos))), (float) ((int) (d3 - (d2 * sin))), (Paint) null);
                i32--;
                paint = null;
                dpToPx = dpToPx;
                i2 = i2;
                i22 = i22;
                cos = cos;
            }
        }
        canvas.drawBitmap(this.cropped, (float) this.left, (float) this.top, paint);
        this.mainBitmap = copy;
        this.imageViewCover.setImageBitmap(copy);
        return copy;
    }
}
