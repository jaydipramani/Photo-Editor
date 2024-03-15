package com.relish.app.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.relish.app.Activity.PolishBaseActivity;
import com.relish.app.Activity.PolishEditorActivity;
import com.relish.app.Adapter.NeonAdapter;
import com.relish.app.R;
import com.relish.app.Utils.BitmapTransfer;
import com.relish.app.Utils.ImageUtils;
import com.relish.app.crop.MLCropAsyncTask;
import com.relish.app.crop.MLOnCropTaskCompleted;
import com.relish.app.eraser.StickerEraseActivity;
import com.relish.app.listener.LayoutItemListener;
import com.relish.app.listener.MultiTouchListener;
import com.relish.app.support.Constants;

import java.util.ArrayList;

import io.reactivex.annotations.SchedulerSupport;

public class NeonLayout extends PolishBaseActivity implements LayoutItemListener {
    private static Bitmap faceBitmap;
    public static ImageView imageViewFont;
    public static Bitmap resultBitmap;
    private Context context;
    public int count = 0;
    private Bitmap cutBitmap;
    private int frameCount = 23;
    private ArrayList<String> frameEffectList = new ArrayList<>();
    private ImageView imageViewBack;
    private ImageView imageViewBackground;
    private ImageView imageViewCover;
    boolean isFirst = true;
    private NeonAdapter neonAdapter;
    private int neonCount = 31;
    private ArrayList<String> neonEffectList = new ArrayList<>();
    private RecyclerView recyclerViewNeon;
    private RelativeLayout relativeLayoutRootView;
    private SeekBar seekBarOpacity;
    private Bitmap selectedBitmap;
    private int shapeCount = 45;
    private ArrayList<String> shapeEffectList = new ArrayList<>();
    private TextView textViewFrame;
    private TextView textViewShape;
    private TextView textViewSpiral;
    private View viewFrame;
    private View viewShape;
    private View viewSpiral;

    public static void setFaceBitmap(Bitmap bitmap) {
        faceBitmap = bitmap;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_neon);
        this.context = this;
        this.selectedBitmap = faceBitmap;
        new Handler().postDelayed(new Runnable() {

            public void run() {
                NeonLayout.this.imageViewCover.post(new Runnable() {
                    public void run() {
                        if (NeonLayout.this.isFirst && NeonLayout.this.selectedBitmap != null) {
                            NeonLayout.this.isFirst = false;
                            NeonLayout.this.initBitmap();
                        }
                    }
                });
            }
        }, 1000);
        this.neonEffectList.add(SchedulerSupport.NONE);
        this.shapeEffectList.add(SchedulerSupport.NONE);
        this.frameEffectList.add(SchedulerSupport.NONE);
        for (int i = 1; i <= this.neonCount; i++) {
            ArrayList<String> arrayList = this.neonEffectList;
            arrayList.add("line_" + i);
        }
        for (int i2 = 1; i2 <= this.shapeCount; i2++) {
            ArrayList<String> arrayList2 = this.shapeEffectList;
            arrayList2.add("shape_" + i2);
        }
        for (int i3 = 1; i3 <= this.frameCount; i3++) {
            ArrayList<String> arrayList3 = this.frameEffectList;
            arrayList3.add("frame_" + i3);
        }
        initViews();
    }


    private void initBitmap() {
        ImageView imageView;
        Bitmap bitmap = faceBitmap;
        if (bitmap != null) {
            this.selectedBitmap = ImageUtils.getBitmapResize(this.context, bitmap, this.imageViewCover.getWidth(), this.imageViewCover.getHeight());
            this.relativeLayoutRootView.setLayoutParams(new LinearLayout.LayoutParams(this.selectedBitmap.getWidth(), this.selectedBitmap.getHeight()));
            Bitmap bitmap2 = this.selectedBitmap;
            if (!(bitmap2 == null || (imageView = this.imageViewBackground) == null)) {
                imageView.setImageBitmap(bitmap2);
            }
            setStartNeon();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1024 && (bitmap = resultBitmap) != null) {
            this.cutBitmap = bitmap;
            this.imageViewCover.setImageBitmap(bitmap);
        }
    }

    public void initViews() {
        this.relativeLayoutRootView = (RelativeLayout) findViewById(R.id.mContentRootView);
        imageViewFont = (ImageView) findViewById(R.id.imageViewFont);
        this.imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        this.imageViewBackground = (ImageView) findViewById(R.id.imageViewBackground);
        this.imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewLine);
        this.recyclerViewNeon = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false));
        seAdapterList();
        this.textViewSpiral = (TextView) findViewById(R.id.text_view_spiral);
        this.textViewShape = (TextView) findViewById(R.id.text_view_shape);
        this.textViewFrame = (TextView) findViewById(R.id.text_view_frame);
        this.viewSpiral = findViewById(R.id.view_spiral);
        this.viewShape = findViewById(R.id.view_shape);
        this.viewFrame = findViewById(R.id.view_frame);
        this.seekBarOpacity = (SeekBar) findViewById(R.id.seekbarOpacity);
        findViewById(R.id.linearLayoutSpiral).performClick();
        this.imageViewBackground.setRotationY(0.0f);
        this.imageViewCover.post(new Runnable() {


            public void run() {
                NeonLayout.this.initBitmap();
            }
        });
        this.seekBarOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (NeonLayout.this.imageViewBack != null && NeonLayout.imageViewFont != null) {
                    float f = ((float) i) * 0.01f;
                    NeonLayout.this.imageViewBack.setAlpha(f);
                    NeonLayout.imageViewFont.setAlpha(f);
                }
            }
        });
        findViewById(R.id.imageViewCloseNeon).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NeonLayout.this.onBackPressed();
            }
        });
        findViewById(R.id.imageViewSaveNeon).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new saveFile().execute(new String[0]);
            }
        });
        findViewById(R.id.linearLayoutSpiral).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NeonLayout.this.neonAdapter.addData(NeonLayout.this.neonEffectList);
                NeonLayout.this.textViewSpiral.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.white));
                NeonLayout.this.textViewShape.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.gray));
                NeonLayout.this.textViewFrame.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.gray));
                NeonLayout.this.viewSpiral.setVisibility(View.VISIBLE);
                NeonLayout.this.viewShape.setVisibility(View.INVISIBLE);
                NeonLayout.this.viewFrame.setVisibility(View.INVISIBLE);
            }
        });
        findViewById(R.id.linearLayoutShape).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                NeonLayout.this.neonAdapter.addData(NeonLayout.this.shapeEffectList);
                NeonLayout.this.textViewSpiral.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.gray));
                NeonLayout.this.textViewShape.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.white));
                NeonLayout.this.textViewFrame.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.gray));
                NeonLayout.this.viewSpiral.setVisibility(View.INVISIBLE);
                NeonLayout.this.viewShape.setVisibility(View.VISIBLE);
                NeonLayout.this.viewFrame.setVisibility(View.INVISIBLE);
            }
        });
        findViewById(R.id.linearLayoutFrame).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                NeonLayout.this.neonAdapter.addData(NeonLayout.this.frameEffectList);
                NeonLayout.this.textViewSpiral.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.gray));
                NeonLayout.this.textViewShape.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.gray));
                NeonLayout.this.textViewFrame.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.white));
                NeonLayout.this.viewSpiral.setVisibility(View.INVISIBLE);
                NeonLayout.this.viewShape.setVisibility(View.INVISIBLE);
                NeonLayout.this.viewFrame.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.image_view_eraser).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                StickerEraseActivity.b = NeonLayout.this.selectedBitmap;
                Intent intent = new Intent(NeonLayout.this, StickerEraseActivity.class);
                intent.putExtra(Constants.KEY_OPEN_FROM, Constants.VALUE_OPEN_FROM_NEON);
                NeonLayout.this.startActivityForResult(intent, 1024);
            }
        });
    }

    public void seAdapterList() {
        NeonAdapter neonAdapter2 = new NeonAdapter(this.context);
        this.neonAdapter = neonAdapter2;
        neonAdapter2.setLayoutItenListener(this);
        this.recyclerViewNeon.setAdapter(this.neonAdapter);
        this.neonAdapter.addData(this.neonEffectList);
    }

    @Override
    public void onLayoutListClick(View view, int i) {
            if (i != 0) {
                Context context2 = this.context;
                Bitmap assetBitmapBack = ImageUtils.getBitmapFromAsset(context2, "neon/back/" + this.neonAdapter.getItemList().get(i) + "_back.webp");
                Context context3 = this.context;
                Bitmap assetBitmapFront = ImageUtils.getBitmapFromAsset(context3, "neon/front/" + this.neonAdapter.getItemList().get(i) + "_front.webp");
                this.imageViewBack.setImageBitmap(assetBitmapBack);
                imageViewFont.setImageBitmap(assetBitmapFront);
            } else {
                this.imageViewBack.setImageResource(0);
                imageViewFont.setImageResource(0);
            }
            this.imageViewBack.setOnTouchListener(new MultiTouchListener(this, true));
            return;

    }

    public void setStartNeon() {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.crop_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        new CountDownTimer(5000, 1000) {

            public void onFinish() {
            }

            public void onTick(long j) {
                NeonLayout.this.count++;
                if (progressBar.getProgress() <= 90) {
                    progressBar.setProgress(NeonLayout.this.count * 5);
                }
            }
        }.start();
        new MLCropAsyncTask(new MLOnCropTaskCompleted() {

            @Override
            public void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int left, int top) {
                int[] iArr = {0, 0, NeonLayout.this.selectedBitmap.getWidth(), NeonLayout.this.selectedBitmap.getHeight()};
                int width = NeonLayout.this.selectedBitmap.getWidth();
                int height = NeonLayout.this.selectedBitmap.getHeight();
                int i = width * height;
                NeonLayout.this.selectedBitmap.getPixels(new int[i], 0, width, 0, 0, width, height);
                Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                createBitmap.setPixels(new int[i], 0, width, 0, 0, width, height);
                NeonLayout neonLayout = NeonLayout.this;
                neonLayout.cutBitmap = ImageUtils.getMask(neonLayout.context, NeonLayout.this.selectedBitmap, createBitmap, width, height);
                NeonLayout.this.cutBitmap = Bitmap.createScaledBitmap(bitmap, NeonLayout.this.cutBitmap.getWidth(), NeonLayout.this.cutBitmap.getHeight(), false);
                NeonLayout.this.runOnUiThread(new Runnable() {

                    public void run() {
                        if (Palette.from(NeonLayout.this.cutBitmap).generate().getDominantSwatch() == null) {
                            Toast.makeText(NeonLayout.this, NeonLayout.this.getString(R.string.txt_not_detect_human), Toast.LENGTH_SHORT).show();
                        }
                        NeonLayout.this.imageViewCover.setImageBitmap(NeonLayout.this.cutBitmap);
                    }
                });
            }
        }, this, progressBar).execute(new Void[0]);
    }

    private class saveFile extends AsyncTask<String, Bitmap, Bitmap> {
        private saveFile() {
        }

        public void onPreExecute() {
            super.onPreExecute();
        }

        public Bitmap getBitmapFromView(View view) {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            view.draw(new Canvas(bitmap));
            return bitmap;
        }

        public Bitmap doInBackground(String... strings) {
            NeonLayout.this.relativeLayoutRootView.setDrawingCacheEnabled(true);
            Bitmap bitmap = getBitmapFromView(NeonLayout.this.relativeLayoutRootView);
            NeonLayout.this.relativeLayoutRootView.setDrawingCacheEnabled(false);
            return bitmap;
        }

        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                BitmapTransfer.bitmap = bitmap;
            }
            Intent intent = new Intent(NeonLayout.this, PolishEditorActivity.class);
            intent.putExtra("MESSAGE", "done");
            NeonLayout.this.setResult(-1, intent);
            NeonLayout.this.finish();
        }
    }
}
