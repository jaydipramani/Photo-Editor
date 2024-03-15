package com.relish.app.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.relish.app.Activity.PolishBaseActivity;
import com.relish.app.Activity.PolishEditorActivity;
import com.relish.app.Adapter.TetxColorAdapter;
import com.relish.app.R;
import com.relish.app.Utils.BitmapTransfer;
import com.relish.app.draw.SplashBrushView;
import com.relish.app.polish.PolishSplashView;
import com.relish.app.support.MyExceptionHandlerPix;
import com.relish.app.support.SupportedClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SplashLayout extends PolishBaseActivity implements SeekBar.OnSeekBarChangeListener, TetxColorAdapter.ColorListener {
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int REQUEST_CODE_GALLERY = 3;
    public static SplashBrushView brushView;
    public static Bitmap colorBitmap;
    public static int displayHight;
    public static int displayWidth;
    public static String drawPath;
    public static Bitmap grayBitmap;
    public static SeekBar seekBarOpacity;
    public static SeekBar seekBarSize;
    public static PolishSplashView splashView;
    public static Vector vector;
    private ImageView imageViewColor;
    private ImageView imageViewGray;
    private ImageView imageViewManual;
    private RecyclerView recyclerViewColor;
    private RelativeLayout relativeLayoutContainer;
    private RelativeLayout relativeLayoutView;
    private Runnable runnableCode;
    public String selectedImagePath;
    public Uri selectedImageUri;
    public String selectedOutputPath;
    private List<TetxColorAdapter.SquareView> squareViewListSaved = new ArrayList();
    TetxColorAdapter tetxColorAdapter;
    private TextView textViewColor;
    private TextView textViewGray;
    private TextView textViewManual;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_splash);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(this));
        this.relativeLayoutContainer = (RelativeLayout) findViewById(R.id.relativeLayoutContainer);
        brushView = (SplashBrushView) findViewById(R.id.brushView);
        vector = new Vector();
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        displayWidth = point.x;
        displayHight = point.y;
        splashView = (PolishSplashView) findViewById(R.id.drawingImageView);
        if (BitmapTransfer.bitmap != null) {
            colorBitmap = BitmapTransfer.bitmap;
        }
        grayBitmap = grayScaleBitmap(colorBitmap);
        this.imageViewColor = (ImageView) findViewById(R.id.imageViewColor);
        this.imageViewGray = (ImageView) findViewById(R.id.imageViewGray);
        this.imageViewManual = (ImageView) findViewById(R.id.imageViewManual);
        this.textViewColor = (TextView) findViewById(R.id.textViewColor);
        this.textViewGray = (TextView) findViewById(R.id.textViewGray);
        this.textViewManual = (TextView) findViewById(R.id.textViewManual);
        this.relativeLayoutView = (RelativeLayout) findViewById(R.id.relativeLayoutView);
        seekBarSize = (SeekBar) findViewById(R.id.seekBarSize);
        seekBarOpacity = (SeekBar) findViewById(R.id.seekBarOpacity);
        seekBarSize.setMax(100);
        seekBarOpacity.setMax(240);
        seekBarSize.setProgress((int) splashView.radius);
        seekBarOpacity.setProgress(splashView.opacity);
        seekBarSize.setOnSeekBarChangeListener(this);
        seekBarOpacity.setOnSeekBarChangeListener(this);
        splashView.initDrawing();
        final Handler handler = new Handler();

        Runnable r3 = new Runnable() {

            public void run() {
                handler.postDelayed(SplashLayout.this.runnableCode, 2000);
            }
        };
        this.runnableCode = r3;
        handler.post(r3);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewColor);
        this.recyclerViewColor = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewColor.setAdapter(new TetxColorAdapter(this, this));
        this.recyclerViewColor.setVisibility(View.VISIBLE);
        findViewById(R.id.imageViewSaveSplash).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (SplashLayout.splashView.drawingBitmap != null) {
                    BitmapTransfer.bitmap = SplashLayout.splashView.drawingBitmap;
                }
                Intent intent = new Intent(SplashLayout.this, PolishEditorActivity.class);
                intent.putExtra("MESSAGE", "done");
                SplashLayout.this.setResult(-1, intent);
                SplashLayout.this.finish();
            }
        });
        findViewById(R.id.imageViewCloseSplash).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SplashLayout.this.onBackPressed();
                SplashLayout.this.finish();
            }
        });
        findViewById(R.id.relativeLayoutManual).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SplashLayout.this.imageViewColor.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.imageViewGray.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.imageViewManual.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
                SplashLayout.this.textViewColor.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.textViewGray.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.textViewManual.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
                SplashLayout.this.relativeLayoutView.setVisibility(View.GONE);
                SplashLayout.splashView.mode = 0;
                SplashLayout.splashView.splashBitmap = SplashLayout.this.grayScaleBitmap(SplashLayout.colorBitmap);
                SplashLayout.splashView.updateRefMetrix();
                SplashLayout.splashView.changeShaderBitmap();
                SplashLayout.splashView.coloring = -2;
                SplashLayout splashLayout = SplashLayout.this;
                splashLayout.tetxColorAdapter = (TetxColorAdapter) splashLayout.recyclerViewColor.getAdapter();
                if (SplashLayout.this.tetxColorAdapter != null) {
                    SplashLayout.this.tetxColorAdapter.setSelectedColorIndex(0);
                }
                if (SplashLayout.this.tetxColorAdapter != null) {
                    SplashLayout.this.tetxColorAdapter.notifyDataSetChanged();
                }
            }
        });
        findViewById(R.id.relativeLayoutColor).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SplashLayout.this.imageViewColor.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
                SplashLayout.this.imageViewGray.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.imageViewManual.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.textViewColor.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
                SplashLayout.this.textViewGray.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.textViewManual.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.relativeLayoutView.setVisibility(View.VISIBLE);
                SplashLayout.splashView.mode = 0;
                PolishSplashView splashView = SplashLayout.splashView;
                splashView.splashBitmap = SplashLayout.colorBitmap;
                splashView.updateRefMetrix();
                SplashLayout.splashView.changeShaderBitmap();
                SplashLayout.splashView.coloring = -1;
            }
        });
        findViewById(R.id.relativeLayoutGray).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SplashLayout.this.imageViewColor.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.imageViewGray.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
                SplashLayout.this.imageViewManual.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.textViewColor.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.textViewGray.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
                SplashLayout.this.textViewManual.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.gray));
                SplashLayout.this.relativeLayoutView.setVisibility(View.VISIBLE);
                SplashLayout.splashView.mode = 0;
                SplashLayout.splashView.splashBitmap = SplashLayout.this.grayScaleBitmap(SplashLayout.colorBitmap);
                SplashLayout.splashView.updateRefMetrix();
                SplashLayout.splashView.changeShaderBitmap();
                SplashLayout.splashView.coloring = -2;
            }
        });
        findViewById(R.id.imageViewUndo).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SplashLayout splashLayout = SplashLayout.this;
                splashLayout.LoadView((TetxColorAdapter.SquareView) splashLayout.squareViewListSaved.get(SplashLayout.this.squareViewListSaved.size() - 1));
            }
        });
        findViewById(R.id.imageViewRedo).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            }
        });
        findViewById(R.id.imageViewReset).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                SplashLayout.this.resetClick(view);
            }
        });
        findViewById(R.id.imageViewFit).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                PolishSplashView touchImageView = SplashLayout.splashView;
                touchImageView.saveScale = 1.0f;
                touchImageView.radius = ((float) (SplashLayout.seekBarSize.getProgress() + 10)) / SplashLayout.splashView.saveScale;
                SplashLayout.splashView.fitScreen();
                SplashLayout.splashView.updatePreviewPaint();
            }
        });
        findViewById(R.id.imageViewZoom).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SplashLayout.splashView.mode = 1;
            }
        });
    }

    public void resetClick(View view) {
        this.imageViewColor.setColorFilter(ContextCompat.getColor(this, R.color.white));
        this.imageViewGray.setColorFilter(ContextCompat.getColor(this, R.color.gray));
        this.imageViewManual.setColorFilter(ContextCompat.getColor(this, R.color.gray));
        this.textViewColor.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewGray.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.textViewManual.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.relativeLayoutView.setVisibility(View.VISIBLE);
        grayBitmap = grayScaleBitmap(colorBitmap);
        splashView.initDrawing();
        splashView.saveScale = 1.0f;
        splashView.fitScreen();
        splashView.updatePreviewPaint();
        splashView.updatePaintBrush();
        vector.clear();
    }

    private void LoadView(TetxColorAdapter.SquareView squareView) {
        grayBitmap = grayScaleBitmap(colorBitmap);
        Canvas canvas = new Canvas(grayBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(new float[]{((float) ((squareView.drawableId >> 16) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 8) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) (squareView.drawableId & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 24) & 255)) / 256.0f, 0.0f}));
        canvas.drawBitmap(grayBitmap, 0.0f, 0.0f, paint);
        splashView.splashBitmap = grayBitmap;
        splashView.updateRefMetrix();
        splashView.changeShaderBitmap();
        splashView.coloring = squareView.drawableId;
    }

    @Override
    public void onColorSelected(TetxColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            grayBitmap = grayScaleBitmap(colorBitmap);
            Canvas canvas = new Canvas(grayBitmap);
            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(new float[]{((float) ((squareView.drawableId >> 16) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 8) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) (squareView.drawableId & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 24) & 255)) / 256.0f, 0.0f}));
            canvas.drawBitmap(grayBitmap, 0.0f, 0.0f, paint);
            splashView.splashBitmap = grayBitmap;
            splashView.updateRefMetrix();
            splashView.changeShaderBitmap();
            splashView.coloring = squareView.drawableId;
            this.squareViewListSaved.add(squareView);
            return;
        }
        grayBitmap = grayScaleBitmap(colorBitmap);
        Canvas canvas2 = new Canvas(grayBitmap);
        Paint paint2 = new Paint();
        paint2.setColorFilter(new ColorMatrixColorFilter(new float[]{((float) ((squareView.drawableId >> 16) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 8) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) (squareView.drawableId & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 24) & 255)) / 256.0f, 0.0f}));
        canvas2.drawBitmap(grayBitmap, 0.0f, 0.0f, paint2);
        splashView.splashBitmap = grayBitmap;
        splashView.updateRefMetrix();
        splashView.changeShaderBitmap();
        splashView.coloring = squareView.drawableId;
        this.squareViewListSaved.add(squareView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Bitmap grayScaleBitmap(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int id = seekBar.getId();
        if (id == R.id.seekBarOpacity) {
            SplashBrushView brushView2 = brushView;
            brushView2.isBrushSize = false;
            brushView2.setShapeRadiusRatio(splashView.radius);
            brushView.brushSize.setPaintOpacity(seekBarOpacity.getProgress());
            brushView.invalidate();
            PolishSplashView splashView2 = splashView;
            splashView2.opacity = i + 15;
            splashView2.updatePaintBrush();
        } else if (id == R.id.seekBarSize) {
            Log.wtf("radious :", seekBarSize.getProgress() + "");
            splashView.radius = ((float) (seekBarSize.getProgress() + 10)) / splashView.saveScale;
            splashView.updatePaintBrush();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 2) {
            String str = this.selectedOutputPath;
            this.selectedImagePath = str;
            if (SupportedClass.stringIsNotEmpty(str)) {
                File fileImageClick = new File(this.selectedImagePath);
                if (fileImageClick.exists()) {
                    if (Build.VERSION.SDK_INT < 24) {
                        this.selectedImageUri = Uri.fromFile(fileImageClick);
                    } else {
                        this.selectedImageUri = FileProvider.getUriForFile(this, getString(R.string.file_provider), fileImageClick);
                    }
                    onPhotoTakenApp();
                }
            }
        } else if (data == null || data.getData() == null) {
            Log.e("TAG", "");
        } else {
            if (requestCode == 3) {
                this.selectedImageUri = data.getData();
            } else {
                this.selectedImagePath = this.selectedOutputPath;
            }
            if (SupportedClass.stringIsNotEmpty(this.selectedImagePath)) {
                onPhotoTakenApp();
            }
        }
    }

    public void onPhotoTakenApp() {
        this.relativeLayoutContainer.post(new Runnable() {

            public void run() {
                SplashLayout.grayBitmap = SplashLayout.this.grayScaleBitmap(SplashLayout.colorBitmap);
                SplashLayout.splashView.initDrawing();
                SplashLayout.splashView.saveScale = 1.0f;
                SplashLayout.splashView.fitScreen();
                SplashLayout.splashView.updatePreviewPaint();
                SplashLayout.splashView.updatePaintBrush();
                SplashLayout.vector.clear();
            }
        });
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true);
        new Paint(1).setColor(Color.BLACK);
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
