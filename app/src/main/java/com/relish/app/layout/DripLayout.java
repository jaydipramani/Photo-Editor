package com.relish.app.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.relish.app.Activity.PolishBaseActivity;
import com.relish.app.Activity.PolishEditorActivity;
import com.relish.app.Adapter.DripAdapter;
import com.relish.app.Adapter.DripBackgroundAdapter;
import com.relish.app.Adapter.DripColorAdapter;
import com.relish.app.R;
import com.relish.app.Utils.BitmapTransfer;
import com.relish.app.Utils.BrushColorAsset;
import com.relish.app.Utils.DripUtils;
import com.relish.app.Utils.ImageUtils;
import com.relish.app.crop.MLCropAsyncTask;
import com.relish.app.crop.MLOnCropTaskCompleted;
import com.relish.app.drip.imagescale.TouchListener;
import com.relish.app.eraser.StickerEraseActivity;
import com.relish.app.listener.BackgroundItemListener;
import com.relish.app.listener.LayoutItemListener;
import com.relish.app.listener.MultiTouchListener;
import com.relish.app.polish.PolishDripView;
import com.relish.app.support.Constants;
import com.relish.app.support.MyExceptionHandlerPix;
import com.relish.app.widget.DripFrameLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.SchedulerSupport;

public class DripLayout extends PolishBaseActivity implements LayoutItemListener, BackgroundItemListener, DripColorAdapter.ColorListener {
    private static Bitmap faceBitmap;
    public static Bitmap resultBmp;
    private Bitmap OverLayBackground = null;
    private Bitmap bitmapColor = null;
    public int count = 0;
    private Bitmap cutBitmap;
    private DripBackgroundAdapter dripBackgroundAdapter;
    private ArrayList<String> dripBackgroundList = new ArrayList<>();
    private List<String> dripColorList = BrushColorAsset.listColorBrush();
    private ArrayList<String> dripEffectList = new ArrayList<>();
    private DripAdapter dripItemAdapter;
    private PolishDripView dripViewBackground;
    private PolishDripView dripViewColor;
    private PolishDripView dripViewImage;
    private PolishDripView dripViewStyle;
    private DripFrameLayout frameLayoutBackground;
    private boolean isFirst = true;
    private boolean isReady = false;
    private View lastSelectedColor = null;
    private LinearLayout linearLayoutStyle;
    private Bitmap mainBitmap = null;
    private RecyclerView recyclerViewBackground;
    private RecyclerView recyclerViewColor;
    private RecyclerView recyclerViewStyle;
    private Bitmap selectedBitmap;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_drip);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(this));
        this.dripViewColor = (PolishDripView) findViewById(R.id.dripViewColor);
        this.dripViewStyle = (PolishDripView) findViewById(R.id.dripViewStyle);
        this.dripViewImage = (PolishDripView) findViewById(R.id.dripViewImage);
        this.dripViewBackground = (PolishDripView) findViewById(R.id.dripViewBackground);
        this.frameLayoutBackground = (DripFrameLayout) findViewById(R.id.frameLayoutBackground);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutStyle);
        this.linearLayoutStyle = linearLayout;
        linearLayout.setVisibility(View.VISIBLE);
        this.dripViewStyle.setOnTouchListenerCustom(new TouchListener());
        this.dripViewImage.setOnTouchListenerCustom(new TouchListener());
        new Handler().postDelayed(new Runnable() {

            public void run() {
                DripLayout.this.dripViewImage.post(new Runnable() {

                    public void run() {
                        if (DripLayout.this.isFirst) {
                            DripLayout.this.isFirst = false;
                            DripLayout.this.initBitmap();
                        }
                    }
                });
            }
        }, 1000);
        findViewById(R.id.imageViewCloseDrip).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DripLayout.this.onBackPressed();
            }
        });
        findViewById(R.id.imageViewSaveDrip).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new saveFile().execute(new String[0]);
            }
        });
        for (int i = 1; i <= 20; i++) {
            ArrayList<String> arrayList = this.dripEffectList;
            arrayList.add("drip_" + i);
        }
        for (int i2 = 1; i2 <= 60; i2++) {
            ArrayList<String> arrayList2 = this.dripBackgroundList;
            arrayList2.add("background_" + i2);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewColor);
        this.recyclerViewColor = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewColor.setAdapter(new DripColorAdapter(this, this));
        this.recyclerViewColor.setVisibility(View.VISIBLE);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recyclerViewStyle);
        this.recyclerViewStyle = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        RecyclerView recyclerView3 = (RecyclerView) findViewById(R.id.recyclerViewBackground);
        this.recyclerViewBackground = recyclerView3;
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        setDripList();
        setBgList();
        this.dripViewImage.post(new Runnable() {

            public void run() {
                DripLayout.this.initBitmap();
            }
        });
        findViewById(R.id.relativeLayoutStyle).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                DripLayout.this.recyclerViewStyle.setVisibility(View.VISIBLE);
                DripLayout.this.recyclerViewBackground.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.relativeLayoutEraser).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                StickerEraseActivity.b = DripLayout.this.selectedBitmap;
                Intent intent = new Intent(DripLayout.this, StickerEraseActivity.class);
                intent.putExtra(Constants.KEY_OPEN_FROM, Constants.VALUE_OPEN_FROM_DRIP);
                DripLayout.this.startActivityForResult(intent, 1024);
            }
        });
        findViewById(R.id.relativeLayoutBackground).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                DripLayout.this.recyclerViewStyle.setVisibility(View.GONE);
                DripLayout.this.recyclerViewBackground.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1024 && (bitmap = resultBmp) != null) {
            this.cutBitmap = bitmap;
            this.dripViewImage.setImageBitmap(bitmap);
            this.isReady = true;
            Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(this, "drip/style/" + this.dripItemAdapter.getItemList().get(this.dripItemAdapter.selectedPos) + ".webp");
            if (!SchedulerSupport.NONE.equals(this.dripItemAdapter.getItemList().get(0))) {
                this.OverLayBackground = bitmapFromAsset;
            }
        }
    }

    public static void setFaceBitmap(Bitmap bitmap) {
        faceBitmap = bitmap;
    }


    private void initBitmap() {
        Bitmap bitmap = faceBitmap;
        if (bitmap != null) {
            this.selectedBitmap = ImageUtils.getBitmapResize(this, bitmap, 1024, 1024);
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(DripUtils.getBitmapFromAsset(this, "drip/style/white.webp"), this.selectedBitmap.getWidth(), this.selectedBitmap.getHeight(), true);
            this.mainBitmap = createScaledBitmap;
            this.bitmapColor = createScaledBitmap;
            Glide.with((FragmentActivity) this).load(Integer.valueOf((int) R.drawable.drip_1)).into(this.dripViewStyle);
            setStartDrip();
        }
    }

    public void setStartDrip() {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.crop_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        new CountDownTimer(5000, 1000) {

            public void onFinish() {
            }

            public void onTick(long j) {
                DripLayout.this.count++;
                if (progressBar.getProgress() <= 90) {
                    progressBar.setProgress(DripLayout.this.count * 5);
                }
            }
        }.start();

        new MLCropAsyncTask(new MLOnCropTaskCompleted() {

            @Override
            public void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int left, int top) {
                int[] iArr = {0, 0, DripLayout.this.selectedBitmap.getWidth(), DripLayout.this.selectedBitmap.getHeight()};
                int width = DripLayout.this.selectedBitmap.getWidth();
                int height = DripLayout.this.selectedBitmap.getHeight();
                int i = width * height;
                DripLayout.this.selectedBitmap.getPixels(new int[i], 0, width, 0, 0, width, height);
                Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                createBitmap.setPixels(new int[i], 0, width, 0, 0, width, height);
                DripLayout dripLayout = DripLayout.this;
                dripLayout.cutBitmap = ImageUtils.getMask(dripLayout, dripLayout.selectedBitmap, createBitmap, width, height);
                DripLayout.this.cutBitmap = Bitmap.createScaledBitmap(bitmap, DripLayout.this.cutBitmap.getWidth(), DripLayout.this.cutBitmap.getHeight(), false);
                DripLayout.this.runOnUiThread(new Runnable() {

                    public void run() {
                        if (Palette.from(DripLayout.this.cutBitmap).generate().getDominantSwatch() == null) {
                            Toast.makeText(DripLayout.this, DripLayout.this.getString(R.string.txt_not_detect_human), Toast.LENGTH_SHORT).show();
                        }
                        DripLayout.this.dripViewImage.setImageBitmap(DripLayout.this.cutBitmap);
                        DripLayout.this.isReady = true;
                        DripLayout dripLayout = DripLayout.this;
                        Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(dripLayout, "drip/style/" + DripLayout.this.dripItemAdapter.getItemList().get(0) + ".webp");
                        if (!SchedulerSupport.NONE.equals(DripLayout.this.dripItemAdapter.getItemList().get(0))) {
                            DripLayout.this.OverLayBackground = bitmapFromAsset;
                        }
                    }
                });
            }
        }, this, progressBar).execute(new Void[0]);
    }

    @Override
    public void onLayoutListClick(View view, int i) {

            Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(this, "drip/style/" + this.dripItemAdapter.getItemList().get(i) + ".webp");
            if (!SchedulerSupport.NONE.equals(this.dripItemAdapter.getItemList().get(i))) {
                this.OverLayBackground = bitmapFromAsset;
                this.dripViewStyle.setImageBitmap(bitmapFromAsset);
                return;
            }
            this.OverLayBackground = null;
            return;

    }

    @Override
    public void onBackgroundListClick(View view, int i) {

            if (i != 0) {
                this.dripViewBackground.setImageBitmap(ImageUtils.getBitmapFromAsset(this, "drip/background/" + this.dripBackgroundAdapter.getItemList().get(i) + ".webp"));
            } else {
                this.dripViewBackground.setImageResource(0);
            }
            this.dripViewBackground.setOnTouchListener(new MultiTouchListener(this, true));
            return;

    }

    public void setDripList() {
        DripAdapter dripAdapter = new DripAdapter(this);
        this.dripItemAdapter = dripAdapter;
        dripAdapter.setClickListener(this);
        this.recyclerViewStyle.setAdapter(this.dripItemAdapter);
        this.dripItemAdapter.addData(this.dripEffectList);
    }

    public void setBgList() {
        DripBackgroundAdapter dripBackgroundAdapter2 = new DripBackgroundAdapter(this);
        this.dripBackgroundAdapter = dripBackgroundAdapter2;
        dripBackgroundAdapter2.setClickListener(this);
        this.recyclerViewBackground.setAdapter(this.dripBackgroundAdapter);
        this.dripBackgroundAdapter.addData(this.dripBackgroundList);
    }

    @Override
    public void onColorSelected(DripColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            Bitmap changeBitmapColor = DripUtils.changeBitmapColor(this.mainBitmap, squareView.drawableId);
            this.bitmapColor = changeBitmapColor;
            this.dripViewColor.setImageBitmap(changeBitmapColor);
            this.frameLayoutBackground.setBackgroundColor(squareView.drawableId);
            this.dripViewColor.setBackgroundColor(squareView.drawableId);
            this.dripViewStyle.setColorFilter(squareView.drawableId);
            return;
        }
        Bitmap changeBitmapColor2 = DripUtils.changeBitmapColor(this.mainBitmap, squareView.drawableId);
        this.bitmapColor = changeBitmapColor2;
        this.dripViewColor.setImageBitmap(changeBitmapColor2);
        this.frameLayoutBackground.setBackgroundColor(squareView.drawableId);
        this.dripViewColor.setBackgroundColor(squareView.drawableId);
        this.dripViewStyle.setColorFilter(squareView.drawableId);
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
            DripLayout.this.frameLayoutBackground.setDrawingCacheEnabled(true);
            try {
                return getBitmapFromView(DripLayout.this.frameLayoutBackground);
            } catch (Exception e) {
                return null;
            } finally {
                DripLayout.this.frameLayoutBackground.setDrawingCacheEnabled(false);
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                BitmapTransfer.bitmap = bitmap;
            }
            Intent intent = new Intent(DripLayout.this, PolishEditorActivity.class);
            intent.putExtra("MESSAGE", "done");
            DripLayout.this.setResult(-1, intent);
            DripLayout.this.finish();
        }
    }
}
