package com.relish.app.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.relish.app.Activity.PolishBaseActivity;
import com.relish.app.Activity.PolishEditorActivity;
import com.relish.app.Adapter.DripColorAdapter;
import com.relish.app.Adapter.PixLabAdapters;
import com.relish.app.R;
import com.relish.app.Utils.BitmapTransfer;
import com.relish.app.Utils.DripUtils;
import com.relish.app.Utils.ImageUtils;
import com.relish.app.drip.imagescale.TouchListener;
import com.relish.app.listener.LayoutItemListener;
import com.relish.app.polish.PolishDripView;
import com.relish.app.support.MyExceptionHandlerPix;
import com.relish.app.widget.DripFrameLayout;

import java.util.ArrayList;

import io.reactivex.annotations.SchedulerSupport;

public class PixLabLayout extends PolishBaseActivity implements LayoutItemListener, DripColorAdapter.ColorListener {
    private static Bitmap faceBitmap;
    public static Bitmap resultBmp;
    private Bitmap OverLayBackground = null;
    private Bitmap bitmapColor = null;
    private Bitmap cutBitmap;
    private ArrayList<String> dripEffectList = new ArrayList<>();
    private PixLabAdapters dripItemAdapter;
    private PolishDripView dripViewBack;
    private PolishDripView dripViewColor;
    private PolishDripView dripViewStyle;
    private DripFrameLayout frameLayoutBackground;
    private boolean isFirst = true;
    private boolean isReady = false;
    private Bitmap mainBitmap = null;
    private RecyclerView recyclerViewColor;
    private RecyclerView recyclerViewStyle;
    private Bitmap selectedBitmap;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_pixlab);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(this));
        this.dripViewColor = (PolishDripView) findViewById(R.id.dripViewColor);
        this.dripViewStyle = (PolishDripView) findViewById(R.id.dripViewStyle);
        this.dripViewBack = (PolishDripView) findViewById(R.id.dripViewBack);
        this.frameLayoutBackground = (DripFrameLayout) findViewById(R.id.frameLayoutBackground);
        this.dripViewBack.setOnTouchListenerCustom(new TouchListener());
        new Handler().postDelayed(new Runnable() {

            public void run() {
                PixLabLayout.this.dripViewBack.post(new Runnable() {

                    public void run() {
                        if (PixLabLayout.this.isFirst) {
                            PixLabLayout.this.isFirst = false;
                            PixLabLayout.this.initBitmap();
                        }
                    }
                });
            }
        }, 1000);
        findViewById(R.id.imageViewCloseSplash).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                PixLabLayout.this.onBackPressed();
            }
        });
        findViewById(R.id.imageViewSaveSplash).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new saveFile().execute(new String[0]);
            }
        });
        for (int i = 1; i <= 100; i++) {
            ArrayList<String> arrayList = this.dripEffectList;
            arrayList.add("style_" + i);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewColor);
        this.recyclerViewColor = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewColor.setAdapter(new DripColorAdapter(this, this));
        this.recyclerViewColor.setVisibility(View.VISIBLE);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recyclerViewStyle);
        this.recyclerViewStyle = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        setDripList();
        this.dripViewBack.post(new Runnable() {
            public void run() {
                PixLabLayout.this.initBitmap();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1024 && (bitmap = resultBmp) != null) {
            this.cutBitmap = bitmap;
            this.dripViewBack.setImageBitmap(bitmap);
            this.isReady = true;
            Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(this, "lab/" + this.dripItemAdapter.getItemList().get(this.dripItemAdapter.selectedPos) + ".webp");
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
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(DripUtils.getBitmapFromAsset(this, "lab/white.webp"), this.selectedBitmap.getWidth(), this.selectedBitmap.getHeight(), true);
            this.mainBitmap = createScaledBitmap;
            this.bitmapColor = createScaledBitmap;
            Glide.with((FragmentActivity) this).load(Integer.valueOf((int) R.drawable.style_1)).into(this.dripViewStyle);
            this.dripViewBack.setImageBitmap(this.selectedBitmap);
        }
    }

    @Override
    public void onLayoutListClick(View view, int i) {
            Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(this, "lab/" + this.dripItemAdapter.getItemList().get(i) + ".webp");
            if (!SchedulerSupport.NONE.equals(this.dripItemAdapter.getItemList().get(i))) {
                this.OverLayBackground = bitmapFromAsset;
                this.dripViewStyle.setImageBitmap(bitmapFromAsset);
                return;
            }
            this.OverLayBackground = null;
            return;

    }

    public void setDripList() {
        PixLabAdapters pixLabAdapters = new PixLabAdapters(this);
        this.dripItemAdapter = pixLabAdapters;
        pixLabAdapters.setClickListener(this);
        this.recyclerViewStyle.setAdapter(this.dripItemAdapter);
        this.dripItemAdapter.addData(this.dripEffectList);
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
            PixLabLayout.this.frameLayoutBackground.setDrawingCacheEnabled(true);
            try {
                return getBitmapFromView(PixLabLayout.this.frameLayoutBackground);
            } catch (Exception e) {
                return null;
            } finally {
                PixLabLayout.this.frameLayoutBackground.setDrawingCacheEnabled(false);
            }
        }

        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                BitmapTransfer.bitmap = bitmap;
            }
            Intent intent = new Intent(PixLabLayout.this, PolishEditorActivity.class);
            intent.putExtra("MESSAGE", "done");
            PixLabLayout.this.setResult(-1, intent);
            PixLabLayout.this.finish();
        }
    }
}
