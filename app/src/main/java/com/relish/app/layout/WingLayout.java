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
import android.widget.Toast;

import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.relish.app.Activity.PolishBaseActivity;
import com.relish.app.Activity.PolishEditorActivity;
import com.relish.app.Adapter.WingsAdapter;
import com.relish.app.R;
import com.relish.app.Utils.BitmapTransfer;
import com.relish.app.Utils.ImageUtils;
import com.relish.app.crop.MLCropAsyncTask;
import com.relish.app.crop.MLOnCropTaskCompleted;
import com.relish.app.eraser.StickerEraseActivity;
import com.relish.app.listener.LayoutItemListener;
import com.relish.app.listener.MultiTouchListener;
import com.relish.app.support.Constants;
import com.relish.app.support.MyExceptionHandlerPix;

import java.util.ArrayList;

import io.reactivex.annotations.SchedulerSupport;

public class WingLayout extends PolishBaseActivity implements LayoutItemListener {
    private static Bitmap faceBitmap;
    public static Bitmap resultBmp;
    private Context context;
    public int count = 0;
    private Bitmap cutBitmap;
    private ImageView imageViewBackground;
    private ImageView imageViewCover;
    private ImageView imageViewWings;
    boolean isFirst = true;
    private RecyclerView recyclerViewWings;
    private RelativeLayout relativeLayoutRootView;
    private Bitmap selectedBitmap;
    private WingsAdapter wingsAdapter;
    private int wingsCount = 44;
    private ArrayList<String> wingsList = new ArrayList<>();

    public static void setFaceBitmap(Bitmap bitmap) {
        faceBitmap = bitmap;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_wing);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(this));
        this.context = this;
        this.selectedBitmap = faceBitmap;
        new Handler().postDelayed(new Runnable() {

            public void run() {
                WingLayout.this.imageViewBackground.post(new Runnable() {

                    public void run() {
                        if (WingLayout.this.isFirst && WingLayout.this.selectedBitmap != null) {
                            WingLayout.this.isFirst = false;
                            WingLayout.this.initBitmap();
                        }
                    }
                });
            }
        }, 1000);
        this.wingsList.add(SchedulerSupport.NONE);
        for (int i = 1; i <= this.wingsCount; i++) {
            ArrayList<String> arrayList = this.wingsList;
            arrayList.add("wing_" + i);
        }
        initViews();
    }


    private void initBitmap() {
        ImageView imageView;
        Bitmap bitmap = faceBitmap;
        if (bitmap != null) {
            this.selectedBitmap = ImageUtils.getBitmapResize(this.context, bitmap, this.imageViewBackground.getWidth(), this.imageViewBackground.getHeight());
            this.relativeLayoutRootView.setLayoutParams(new LinearLayout.LayoutParams(this.selectedBitmap.getWidth(), this.selectedBitmap.getHeight()));
            Bitmap bitmap2 = this.selectedBitmap;
            if (!(bitmap2 == null || (imageView = this.imageViewCover) == null)) {
                imageView.setImageBitmap(bitmap2);
            }
            setStartWings();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1024 && (bitmap = resultBmp) != null) {
            this.cutBitmap = bitmap;
            this.imageViewBackground.setImageBitmap(bitmap);
        }
    }

    public void initViews() {
        this.relativeLayoutRootView = (RelativeLayout) findViewById(R.id.relativeLayoutRootView);
        this.imageViewWings = (ImageView) findViewById(R.id.imageViewWings);
        this.imageViewCover = (ImageView) findViewById(R.id.imageViewBackground);
        this.imageViewBackground = (ImageView) findViewById(R.id.imageViewCover);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewWings);
        this.recyclerViewWings = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false));
        WingsAdapter wingsAdapter2 = new WingsAdapter(this.context);
        this.wingsAdapter = wingsAdapter2;
        wingsAdapter2.setMenuItemClickLister(this);
        this.recyclerViewWings.setAdapter(this.wingsAdapter);
        this.wingsAdapter.addData(this.wingsList);
        findViewById(R.id.imageViewCloseWings).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                WingLayout.this.onBackPressed();
            }
        });
        this.imageViewCover.setRotationY(0.0f);
        this.imageViewBackground.post(new Runnable() {

            public void run() {
                WingLayout.this.initBitmap();
            }
        });
        ((SeekBar) findViewById(R.id.seekbarOpacity)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (WingLayout.this.imageViewWings != null) {
                    WingLayout.this.imageViewWings.setAlpha(((float) i) * 0.01f);
                }
            }
        });
        findViewById(R.id.imageViewSaveWings).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new saveFile().execute(new String[0]);
            }
        });
        findViewById(R.id.image_view_eraser).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                StickerEraseActivity.b = WingLayout.this.cutBitmap;
                Intent intent = new Intent(WingLayout.this, StickerEraseActivity.class);
                intent.putExtra(Constants.KEY_OPEN_FROM, Constants.VALUE_OPEN_FROM_WING);
                WingLayout.this.startActivityForResult(intent, 1024);
            }
        });
    }

    @Override
    public void onLayoutListClick(View view, int i) {
        if (i != 0) {
            Context context2 = this.context;
            StringBuilder sb = new StringBuilder();
            sb.append("wings/");
            sb.append(this.wingsAdapter.getItemList().get(i));
            sb.append(this.wingsAdapter.getItemList().get(i).startsWith("b") ? "_back.webp" : ".webp");
            this.imageViewWings.setImageBitmap(ImageUtils.getBitmapFromAsset(context2, sb.toString()));
        } else {
            this.imageViewWings.setImageResource(0);
        }
        this.imageViewWings.setOnTouchListener(new MultiTouchListener(this, true));
        return;
    }

    public void setStartWings() {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.crop_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        new CountDownTimer(5000, 1000) {


            public void onFinish() {
            }

            public void onTick(long j) {
                WingLayout.this.count++;
                if (progressBar.getProgress() <= 90) {
                    progressBar.setProgress(WingLayout.this.count * 5);
                }
            }
        }.start();
        new MLCropAsyncTask(new MLOnCropTaskCompleted() {

            @Override
            public void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int left, int top) {
                int[] iArr = {0, 0, WingLayout.this.selectedBitmap.getWidth(), WingLayout.this.selectedBitmap.getHeight()};
                int width = WingLayout.this.selectedBitmap.getWidth();
                int height = WingLayout.this.selectedBitmap.getHeight();
                int i = width * height;
                WingLayout.this.selectedBitmap.getPixels(new int[i], 0, width, 0, 0, width, height);
                Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                createBitmap.setPixels(new int[i], 0, width, 0, 0, width, height);
                WingLayout wingLayout = WingLayout.this;
                wingLayout.cutBitmap = ImageUtils.getMask(wingLayout.context, WingLayout.this.selectedBitmap, createBitmap, width, height);
                WingLayout.this.cutBitmap = Bitmap.createScaledBitmap(bitmap, WingLayout.this.cutBitmap.getWidth(), WingLayout.this.cutBitmap.getHeight(), false);
                WingLayout.this.runOnUiThread(new Runnable() {


                    public void run() {
                        if (Palette.from(WingLayout.this.cutBitmap).generate().getDominantSwatch() == null) {
                            Toast.makeText(WingLayout.this, WingLayout.this.getString(R.string.txt_not_detect_human), Toast.LENGTH_SHORT).show();
                        }
                        WingLayout.this.imageViewBackground.setImageBitmap(WingLayout.this.cutBitmap);
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
            WingLayout.this.relativeLayoutRootView.setDrawingCacheEnabled(true);
            Bitmap bitmap = getBitmapFromView(WingLayout.this.relativeLayoutRootView);
            WingLayout.this.relativeLayoutRootView.setDrawingCacheEnabled(false);
            return bitmap;
        }

        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                BitmapTransfer.bitmap = bitmap;
            }
            Intent intent = new Intent(WingLayout.this, PolishEditorActivity.class);
            intent.putExtra("MESSAGE", "done");
            WingLayout.this.setResult(-1, intent);
            WingLayout.this.finish();
        }
    }
}
