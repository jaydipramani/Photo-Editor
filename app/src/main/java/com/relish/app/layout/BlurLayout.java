package com.relish.app.layout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.relish.app.Activity.PolishBaseActivity;
import com.relish.app.Activity.PolishEditorActivity;
import com.relish.app.R;
import com.relish.app.Utils.BitmapTransfer;
import com.relish.app.draw.BlurBrushView;
import com.relish.app.polish.PolishBlurView;
import com.relish.app.support.Constants;
import com.relish.app.support.MyExceptionHandlerPix;
import com.relish.app.support.SupportedClass;

import java.io.File;
import java.io.IOException;

public class BlurLayout extends PolishBaseActivity implements SeekBar.OnSeekBarChangeListener {
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_GALLERY = 3;
    public static Bitmap bitmapBlur;
    public static Bitmap bitmapClear;
    static PolishBlurView blurView;
    public static BlurBrushView brushView;
    static int displayHight;
    public static int displayWidth;
    public static SeekBar seekBarBlur;
    public static SeekBar seekBarSize;
    private boolean erase;
    private ImageView imageViewColor;
    RelativeLayout imageViewContainer;
    private ImageView imageViewGray;
    public String mSelectedImagePath;
    public Uri mSelectedImageUri;
    public String mSelectedOutputPath;
    private ProgressDialog progressBlurring;
    private int startBlurSeekbarPosition;
    private TextView textViewColor;
    private TextView textViewGray;

    public static Bitmap blur(Context context, Bitmap bitmap, int i) {
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap createBitmap = Bitmap.createBitmap(copy);
        RenderScript create = RenderScript.create(context);
        ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, Element.U8_4(create));
        Allocation createFromBitmap = Allocation.createFromBitmap(create, copy);
        Allocation createFromBitmap2 = Allocation.createFromBitmap(create, createBitmap);
        create2.setRadius((float) i);
        create2.setInput(createFromBitmap);
        create2.forEach(createFromBitmap2);
        createFromBitmap2.copyTo(createBitmap);
        return createBitmap;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_blur);
        getWindow().setFlags(1024, 1024);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(this));
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        displayWidth = point.x;
        displayHight = point.y;
        this.imageViewContainer = (RelativeLayout) findViewById(R.id.relativeLayoutContainer);
        blurView = (PolishBlurView) findViewById(R.id.drawingImageView);
        if (BitmapTransfer.bitmap != null) {
            bitmapClear = BitmapTransfer.bitmap;
        }
        bitmapBlur = blur(this, bitmapClear, blurView.opacity);
        this.imageViewColor = (ImageView) findViewById(R.id.imageViewColor);
        this.imageViewGray = (ImageView) findViewById(R.id.imageViewGray);
        this.textViewColor = (TextView) findViewById(R.id.textViewColor);
        this.textViewGray = (TextView) findViewById(R.id.textViewGray);
        seekBarSize = (SeekBar) findViewById(R.id.seekBarSize);
        seekBarBlur = (SeekBar) findViewById(R.id.seekBarBlur);
        BlurBrushView blurBrushView = (BlurBrushView) findViewById(R.id.brushView);
        brushView = blurBrushView;
        blurBrushView.setShapeRadiusRatio(((float) seekBarSize.getProgress()) / ((float) seekBarSize.getMax()));
        seekBarSize.setMax(100);
        seekBarSize.setProgress((int) blurView.radius);
        seekBarBlur.setMax(24);
        seekBarBlur.setProgress(blurView.opacity);
        new Canvas(Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true));
        new Paint(1).setColor(Color.GREEN);
        seekBarSize.setOnSeekBarChangeListener(this);
        seekBarBlur.setOnSeekBarChangeListener(this);
        blurView.initDrawing();
        this.progressBlurring = new ProgressDialog(this);
        findViewById(R.id.imageViewSaveBlur).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (BlurLayout.blurView.drawingBitmap != null) {
                    BitmapTransfer.bitmap = BlurLayout.blurView.drawingBitmap;
                }
                Intent intent = new Intent(BlurLayout.this, PolishEditorActivity.class);
                intent.putExtra("MESSAGE", "done");
                BlurLayout.this.setResult(-1, intent);
                BlurLayout.this.finish();
            }
        });
        findViewById(R.id.imageViewCloseBlur).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BlurLayout.this.onBackPressed();
                BlurLayout.this.finish();
            }
        });
        findViewById(R.id.relativeLayoutEraser).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BlurLayout.this.imageViewColor.setColorFilter(ContextCompat.getColor(BlurLayout.this, R.color.white));
                BlurLayout.this.imageViewGray.setColorFilter(ContextCompat.getColor(BlurLayout.this, R.color.gray));
                BlurLayout.this.textViewColor.setTextColor(ContextCompat.getColor(BlurLayout.this, R.color.white));
                BlurLayout.this.textViewGray.setTextColor(ContextCompat.getColor(BlurLayout.this, R.color.gray));
                BlurLayout.this.erase = true;
                BlurLayout.blurView.mode = 0;
                PolishBlurView touchImageView = BlurLayout.blurView;
                touchImageView.splashBitmap = BlurLayout.bitmapClear;
                touchImageView.updateRefMetrix();
                BlurLayout.blurView.changeShaderBitmap();
                BlurLayout.blurView.coloring = true;
            }
        });
        findViewById(R.id.relativeLayoutBlur).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BlurLayout.this.imageViewColor.setColorFilter(ContextCompat.getColor(BlurLayout.this, R.color.gray));
                BlurLayout.this.imageViewGray.setColorFilter(ContextCompat.getColor(BlurLayout.this, R.color.white));
                BlurLayout.this.textViewColor.setTextColor(ContextCompat.getColor(BlurLayout.this, R.color.gray));
                BlurLayout.this.textViewGray.setTextColor(ContextCompat.getColor(BlurLayout.this, R.color.white));
                BlurLayout.this.erase = false;
                BlurLayout.blurView.mode = 0;
                PolishBlurView touchImageView3 = BlurLayout.blurView;
                touchImageView3.splashBitmap = BlurLayout.bitmapBlur;
                touchImageView3.updateRefMetrix();
                BlurLayout.blurView.changeShaderBitmap();
                BlurLayout.blurView.coloring = false;
            }
        });
        findViewById(R.id.imageViewUndo).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            }
        });
        findViewById(R.id.imageViewRedo).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            }
        });
        findViewById(R.id.imageViewReset).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BlurLayout.blurView.initDrawing();
                BlurLayout.blurView.saveScale = 1.0f;
                BlurLayout.blurView.fitScreen();
                BlurLayout.blurView.updatePreviewPaint();
                BlurLayout.blurView.updatePaintBrush();
            }
        });
        findViewById(R.id.imageViewFit).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                PolishBlurView touchImageView2 = BlurLayout.blurView;
                touchImageView2.saveScale = 1.0f;
                touchImageView2.radius = ((float) (BlurLayout.seekBarSize.getProgress() + 10)) / BlurLayout.blurView.saveScale;
                BlurLayout.brushView.setShapeRadiusRatio(((float) (BlurLayout.seekBarSize.getProgress() + 10)) / BlurLayout.blurView.saveScale);
                BlurLayout.blurView.fitScreen();
                BlurLayout.blurView.updatePreviewPaint();
            }
        });
        findViewById(R.id.imageViewZoom).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BlurLayout.blurView.mode = 1;
            }
        });
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int id = seekBar.getId();
        if (id == R.id.seekBarBlur) {
            BlurBrushView brushView2 = brushView;
            brushView2.isBrushSize = false;
            brushView2.setShapeRadiusRatio(blurView.radius);
            brushView.brushSize.setPaintOpacity(seekBarBlur.getProgress());
            brushView.invalidate();
            PolishBlurView touchImageView = blurView;
            touchImageView.opacity = i + 1;
            touchImageView.updatePaintBrush();
        } else if (id == R.id.seekBarSize) {
            BlurBrushView brushView3 = brushView;
            brushView3.isBrushSize = true;
            brushView3.brushSize.setPaintOpacity(255);
            brushView.setShapeRadiusRatio(((float) (seekBarSize.getProgress() + 10)) / blurView.saveScale);
            brushView.invalidate();
            blurView.radius = ((float) (seekBarSize.getProgress() + 10)) / blurView.saveScale;
            blurView.updatePaintBrush();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {
            String str = this.mSelectedOutputPath;
            this.mSelectedImagePath = str;
            if (SupportedClass.stringIsNotEmpty(str)) {
                File fileImageClick = new File(this.mSelectedImagePath);
                if (fileImageClick.exists()) {
                    if (Build.VERSION.SDK_INT < 24) {
                        this.mSelectedImageUri = Uri.fromFile(fileImageClick);
                    } else {
                        this.mSelectedImageUri = FileProvider.getUriForFile(this, getString(R.string.file_provider), fileImageClick);
                    }
                    onPhotoTakenApp();
                }
            }
        } else if (data == null || data.getData() == null) {
            Log.e("TAG", "");
        } else {
            if (requestCode == 3) {
                Uri data2 = data.getData();
                this.mSelectedImageUri = data2;
                if (data2 != null) {
                    this.mSelectedImagePath = Constants.convertMediaUriToPath(this, data2);
                } else {
                    Toast.makeText(this, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                }
            } else {
                this.mSelectedImagePath = this.mSelectedOutputPath;
            }
            if (SupportedClass.stringIsNotEmpty(this.mSelectedImagePath)) {
                onPhotoTakenApp();
            }
        }
    }

    public void onPhotoTakenApp() {
        this.imageViewContainer.post(new Runnable() {

            public void run() {
                try {
                    BlurLayout blurLayout = BlurLayout.this;
                    BlurLayout.bitmapClear = Constants.getBitmapFromUri(blurLayout, blurLayout.mSelectedImageUri, (float) BlurLayout.this.imageViewContainer.getMeasuredWidth(), (float) BlurLayout.this.imageViewContainer.getMeasuredHeight());
                    BlurLayout.bitmapBlur = BlurLayout.blur(BlurLayout.this.getApplicationContext(), BlurLayout.bitmapClear, BlurLayout.blurView.opacity);
                    BlurLayout.blurView.initDrawing();
                    BlurLayout.blurView.saveScale = 1.0f;
                    BlurLayout.blurView.fitScreen();
                    BlurLayout.blurView.updatePreviewPaint();
                    BlurLayout.blurView.updatePaintBrush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar.getId() == R.id.seekBarBlur) {
            this.startBlurSeekbarPosition = seekBarBlur.getProgress();
        }
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getId() == R.id.seekBarBlur) {
            AlertDialog create = new AlertDialog.Builder(this).create();
            create.setTitle("Warning");
            create.setMessage("Changing Bluriness will lose your current drawing progress!");
            create.setButton(-1, "Continue", new DialogInterface.OnClickListener() {


                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    new BlurUpdater().execute(new String[0]);
                }
            });
            create.setButton(-2, "Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    BlurLayout.seekBarBlur.setProgress(BlurLayout.this.startBlurSeekbarPosition);
                }
            });
            create.show();
        }
    }

    private class BlurUpdater extends AsyncTask<String, Integer, Bitmap> {
        private BlurUpdater() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            BlurLayout.this.progressBlurring.setMessage("Blurring...");
            BlurLayout.this.progressBlurring.setIndeterminate(true);
            BlurLayout.this.progressBlurring.setCancelable(false);
            BlurLayout.this.progressBlurring.show();
        }

        public Bitmap doInBackground(String... strArr) {
            BlurLayout.bitmapBlur = BlurLayout.blur(BlurLayout.this.getApplicationContext(), BlurLayout.bitmapClear, BlurLayout.blurView.opacity);
            return BlurLayout.bitmapBlur;
        }

        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
        }

        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (!BlurLayout.this.erase) {
                BlurLayout.blurView.splashBitmap = BlurLayout.bitmapBlur;
                BlurLayout.blurView.updateRefMetrix();
                BlurLayout.blurView.changeShaderBitmap();
            }
            BlurLayout.blurView.initDrawing();
            BlurLayout.blurView.saveScale = 1.0f;
            BlurLayout.blurView.fitScreen();
            BlurLayout.blurView.updatePreviewPaint();
            BlurLayout.blurView.updatePaintBrush();
            if (BlurLayout.this.progressBlurring.isShowing()) {
                BlurLayout.this.progressBlurring.dismiss();
            }
        }
    }
}
