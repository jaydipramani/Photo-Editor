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
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.flipzeus.FlipDirection;
import com.github.flipzeus.ImageFlipper;
import com.relish.app.Activity.PolishBaseActivity;
import com.relish.app.Activity.PolishEditorActivity;
import com.relish.app.Adapter.ArtAdapter;
import com.relish.app.Adapter.ArtColorAdapter;
import com.relish.app.Adapter.DripColorAdapter;
import com.relish.app.R;
import com.relish.app.Utils.BitmapTransfer;
import com.relish.app.Utils.DripUtils;
import com.relish.app.Utils.ImageUtils;
import com.relish.app.crop.MLCropAsyncTask;
import com.relish.app.crop.MLOnCropTaskCompleted;
import com.relish.app.drip.imagescale.TouchListener;
import com.relish.app.eraser.StickerEraseActivity;
import com.relish.app.listener.LayoutItemListener;
import com.relish.app.polish.PolishDripView;
import com.relish.app.support.Constants;
import com.relish.app.support.MyExceptionHandlerPix;
import com.relish.app.widget.DripFrameLayout;

import java.util.ArrayList;

import io.reactivex.annotations.SchedulerSupport;

public class ArtLayout extends PolishBaseActivity implements LayoutItemListener, DripColorAdapter.ColorListener, ArtColorAdapter.ArtColorListener {
    private static Bitmap faceBitmap;
    public static Bitmap resultBitmap;
    private Bitmap OverLayBack = null;
    private Bitmap OverLayFront = null;
    private ArtAdapter artAdapter;
    private ArrayList<String> artEffectList = new ArrayList<>();
    private Bitmap bitmapColor = null;
    private Bitmap bitmapColorBack = null;
    public int count = 0;
    private Bitmap cutBitmap;
    private PolishDripView dripViewBack;
    private PolishDripView dripViewBackground;
    private PolishDripView dripViewFront;
    private PolishDripView dripViewImage;
    private DripFrameLayout frameLayoutBackground;
    private boolean isFirst = true;
    private boolean isReady = false;
    private LinearLayout linearLayoutBg;
    private LinearLayout linearLayoutStyle;
    private Bitmap mainBitmap = null;
    private RecyclerView recyclerViewBack;
    private RecyclerView recyclerViewFront;
    private RecyclerView recyclerViewStyle;
    private SeekBar seekBarZoom;
    private Bitmap selectedBitmap;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_art);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(this));
        this.dripViewFront = (PolishDripView) findViewById(R.id.dripViewFront);
        this.dripViewBack = (PolishDripView) findViewById(R.id.dripViewBack);
        this.dripViewImage = (PolishDripView) findViewById(R.id.dripViewImage);
        this.dripViewBackground = (PolishDripView) findViewById(R.id.dripViewBackground);
        this.frameLayoutBackground = (DripFrameLayout) findViewById(R.id.frameLayoutBackground);
        this.seekBarZoom = (SeekBar) findViewById(R.id.seekbarZoom);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutStyle);
        this.linearLayoutStyle = linearLayout;
        linearLayout.setVisibility(View.VISIBLE);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linearLayoutBg);
        this.linearLayoutBg = linearLayout2;
        linearLayout2.setVisibility(View.GONE);
        this.dripViewImage.setOnTouchListenerCustom(new TouchListener());
        this.seekBarZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Float scale = Float.valueOf((((float) progress) / 50.0f) + 1.0f);
                ArtLayout.this.dripViewBack.setScaleX(scale.floatValue());
                ArtLayout.this.dripViewFront.setScaleX(scale.floatValue());
                ArtLayout.this.dripViewBack.setScaleY(scale.floatValue());
                ArtLayout.this.dripViewFront.setScaleY(scale.floatValue());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        new Handler().postDelayed(new Runnable() {

            public void run() {
                ArtLayout.this.dripViewImage.post(new Runnable() {

                    public void run() {
                        if (ArtLayout.this.isFirst) {
                            ArtLayout.this.isFirst = false;
                            ArtLayout.this.initBitmap();
                        }
                    }
                });
            }
        }, 1000);
        findViewById(R.id.imageViewCloseDrip).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                ArtLayout.this.onBackPressed();
            }
        });
        findViewById(R.id.imageViewSaveDrip).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new saveFile().execute(new String[0]);
            }
        });
        findViewById(R.id.image_view_eraser).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                StickerEraseActivity.b = ArtLayout.this.selectedBitmap;
                Intent intent = new Intent(ArtLayout.this, StickerEraseActivity.class);
                intent.putExtra(Constants.KEY_OPEN_FROM, Constants.VALUE_OPEN_FROM_ART);
                ArtLayout.this.startActivityForResult(intent, 1024);
            }
        });
        findViewById(R.id.relativeLayoutStyle).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                ArtLayout.this.linearLayoutStyle.setVisibility(View.VISIBLE);
                ArtLayout.this.linearLayoutBg.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.relativeLayoutBackground).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                ArtLayout.this.linearLayoutStyle.setVisibility(View.GONE);
                ArtLayout.this.linearLayoutBg.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.relativeLayoutFlip).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                ImageFlipper.flip(ArtLayout.this.dripViewFront, FlipDirection.HORIZONTAL);
                ImageFlipper.flip(ArtLayout.this.dripViewBack, FlipDirection.HORIZONTAL);
            }
        });
        for (int i = 1; i <= 16; i++) {
            ArrayList<String> arrayList = this.artEffectList;
            arrayList.add("art_" + i);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFront);
        this.recyclerViewFront = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewFront.setAdapter(new DripColorAdapter(this, this));
        this.recyclerViewFront.setVisibility(View.VISIBLE);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recyclerViewBack);
        this.recyclerViewBack = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewBack.setAdapter(new ArtColorAdapter(this, this));
        this.recyclerViewBack.setVisibility(View.VISIBLE);
        RecyclerView recyclerView3 = (RecyclerView) findViewById(R.id.recyclerViewStyle);
        this.recyclerViewStyle = recyclerView3;
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        setDripList();
        this.dripViewImage.post(new Runnable() {

            public void run() {
                ArtLayout.this.initBitmap();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1024 && (bitmap = resultBitmap) != null) {
            this.cutBitmap = bitmap;
            this.dripViewImage.setImageBitmap(bitmap);
            this.isReady = true;
            Bitmap bitmapFromAssetFront = DripUtils.getBitmapFromAsset(this, "art/front/" + this.artAdapter.getItemList().get(this.artAdapter.selectedPos) + "_front.webp");
            Bitmap bitmapFromAssetBack = DripUtils.getBitmapFromAsset(this, "art/back/" + this.artAdapter.getItemList().get(this.artAdapter.selectedPos) + "_back.webp");
            if (!SchedulerSupport.NONE.equals(this.artAdapter.getItemList().get(0))) {
                this.OverLayFront = bitmapFromAssetFront;
                this.OverLayBack = bitmapFromAssetBack;
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
            this.mainBitmap = Bitmap.createScaledBitmap(DripUtils.getBitmapFromAsset(this, "art/white.webp"), this.selectedBitmap.getWidth(), this.selectedBitmap.getHeight(), true);
            Glide.with((FragmentActivity) this).load(Integer.valueOf((int) R.drawable.art_1_front)).into(this.dripViewFront);
            Glide.with((FragmentActivity) this).load(Integer.valueOf((int) R.drawable.art_1_back)).into(this.dripViewBack);
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
                ArtLayout.this.count++;
                if (progressBar.getProgress() <= 90) {
                    progressBar.setProgress(ArtLayout.this.count * 5);
                }
            }
        }.start();
        new MLCropAsyncTask(new MLOnCropTaskCompleted() {

            @Override
            public void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int left, int top) {
                int[] iArr = {0, 0, ArtLayout.this.selectedBitmap.getWidth(), ArtLayout.this.selectedBitmap.getHeight()};
                int width = ArtLayout.this.selectedBitmap.getWidth();
                int height = ArtLayout.this.selectedBitmap.getHeight();
                int i = width * height;
                ArtLayout.this.selectedBitmap.getPixels(new int[i], 0, width, 0, 0, width, height);
                Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                createBitmap.setPixels(new int[i], 0, width, 0, 0, width, height);
                ArtLayout artLayout = ArtLayout.this;
                artLayout.cutBitmap = ImageUtils.getMask(artLayout, artLayout.selectedBitmap, createBitmap, width, height);
                ArtLayout.this.cutBitmap = Bitmap.createScaledBitmap(bitmap, ArtLayout.this.cutBitmap.getWidth(), ArtLayout.this.cutBitmap.getHeight(), false);
                ArtLayout.this.runOnUiThread(new Runnable() {

                    public void run() {
                        if (Palette.from(ArtLayout.this.cutBitmap).generate().getDominantSwatch() == null) {
                            Toast.makeText(ArtLayout.this, ArtLayout.this.getString(R.string.txt_not_detect_human), Toast.LENGTH_SHORT).show();
                        }
                        ArtLayout.this.dripViewImage.setImageBitmap(ArtLayout.this.cutBitmap);
                        ArtLayout.this.isReady = true;
                        ArtLayout artLayout = ArtLayout.this;
                        Bitmap bitmapFromAssetFront = DripUtils.getBitmapFromAsset(artLayout, "art/front/" + ArtLayout.this.artAdapter.getItemList().get(0) + "_front.webp");
                        ArtLayout artLayout2 = ArtLayout.this;
                        Bitmap bitmapFromAssetBack = DripUtils.getBitmapFromAsset(artLayout2, "art/back/" + ArtLayout.this.artAdapter.getItemList().get(0) + "_back.webp");
                        if (!SchedulerSupport.NONE.equals(ArtLayout.this.artAdapter.getItemList().get(0))) {
                            ArtLayout.this.OverLayFront = bitmapFromAssetFront;
                            ArtLayout.this.OverLayBack = bitmapFromAssetBack;
                        }
                    }
                });
            }
        }, this, progressBar).execute(new Void[0]);
    }

    @Override
    public void onLayoutListClick(View view, int i) {

            Bitmap bitmapFromAssetFront = DripUtils.getBitmapFromAsset(this, "art/front/" + this.artAdapter.getItemList().get(i) + "_front.webp");
            Bitmap bitmapFromAssetBack = DripUtils.getBitmapFromAsset(this, "art/back/" + this.artAdapter.getItemList().get(i) + "_back.webp");
            if (!SchedulerSupport.NONE.equals(this.artAdapter.getItemList().get(i))) {
                this.OverLayFront = bitmapFromAssetFront;
                this.OverLayBack = bitmapFromAssetBack;
                this.dripViewFront.setImageBitmap(bitmapFromAssetFront);
                this.dripViewBack.setImageBitmap(this.OverLayBack);
                return;
            }
            this.OverLayFront = null;
            this.OverLayBack = null;
            return;

    }

    public void setDripList() {
        ArtAdapter artAdapter2 = new ArtAdapter(this);
        this.artAdapter = artAdapter2;
        artAdapter2.setClickListener(this);
        this.recyclerViewStyle.setAdapter(this.artAdapter);
        this.artAdapter.addData(this.artEffectList);
    }

    @Override
    public void onColorSelected(DripColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.dripViewFront.setColorFilter(squareView.drawableId);
            this.dripViewBack.setColorFilter(squareView.drawableId);
            return;
        }
        this.dripViewFront.setColorFilter(squareView.drawableId);
        this.dripViewBack.setColorFilter(squareView.drawableId);
    }

    @Override
    public void onArtColorSelected(ArtColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.dripViewBackground.setBackgroundColor(squareView.drawableId);
            this.dripViewBackground.setImageBitmap(this.bitmapColorBack);
            return;
        }
        this.dripViewBackground.setBackgroundColor(squareView.drawableId);
        this.dripViewBackground.setImageBitmap(this.bitmapColorBack);
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
            ArtLayout.this.frameLayoutBackground.setDrawingCacheEnabled(true);
            try {
                return getBitmapFromView(ArtLayout.this.frameLayoutBackground);
            } catch (Exception e) {
                return null;
            } finally {
                ArtLayout.this.frameLayoutBackground.setDrawingCacheEnabled(false);
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                BitmapTransfer.bitmap = bitmap;
            }
            Intent intent = new Intent(ArtLayout.this, PolishEditorActivity.class);
            intent.putExtra("MESSAGE", "done");
            ArtLayout.this.setResult(-1, intent);
            ArtLayout.this.finish();
        }
    }
}
