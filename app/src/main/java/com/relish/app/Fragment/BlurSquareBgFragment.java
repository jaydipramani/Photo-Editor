package com.relish.app.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.relish.app.Adapter.SplashSquareAdapter;
import com.relish.app.R;
import com.relish.app.assets.StickerFileAsset;
import com.relish.app.polish.PolishSplashSquareView;
import com.relish.app.polish.PolishSplashSticker;


public class BlurSquareBgFragment extends DialogFragment implements SplashSquareAdapter.SplashChangeListener {
    private static final String TAG = "BlurSquareBgFragment";
    private Bitmap BlurBitmap;
    public Bitmap bitmap;
    public BlurSquareBgListener blurSquareBgListener;
    private FrameLayout frameLayout;
    private ImageView imageViewBackground;
    public boolean isSplashView;
    private PolishSplashSticker polishSplashSticker;
    public PolishSplashSquareView polishSplashView;
    public RecyclerView recyclerViewBlur;
    public TextView textVewTitle;
    private ViewGroup viewGroup;

    public interface BlurSquareBgListener {
        void onSaveBlurBackground(Bitmap bitmap);
    }

    public void setPolishSplashView(boolean z) {
        this.isSplashView = z;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static BlurSquareBgFragment show(AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4, BlurSquareBgListener blurSquareBgListener2, boolean z) {
        BlurSquareBgFragment splashDialog = new BlurSquareBgFragment();
        splashDialog.setBitmap(bitmap2);
        splashDialog.setBlurBitmap(bitmap4);
        splashDialog.setBlurSquareBgListener(blurSquareBgListener2);
        splashDialog.setPolishSplashView(z);
        splashDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return splashDialog;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.BlurBitmap = bitmap2;
    }

    public void setBlurSquareBgListener(BlurSquareBgListener blurSquareBgListener2) {
        this.blurSquareBgListener = blurSquareBgListener2;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup2, Bundle bundle) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_square, viewGroup2, false);
        this.viewGroup = viewGroup2;
        this.imageViewBackground = (ImageView) inflate.findViewById(R.id.imageViewBackground);
        this.polishSplashView = (PolishSplashSquareView) inflate.findViewById(R.id.splashView);
        this.frameLayout = (FrameLayout) inflate.findViewById(R.id.frame_layout);
        this.imageViewBackground.setImageBitmap(this.bitmap);
        this.textVewTitle = (TextView) inflate.findViewById(R.id.textViewTitle);
        if (this.isSplashView) {
            this.polishSplashView.setImageBitmap(this.BlurBitmap);
            this.textVewTitle.setText("BLUR BG");
        }
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerViewSplashSquare);
        this.recyclerViewBlur = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.recyclerViewBlur.setHasFixedSize(true);
        this.recyclerViewBlur.setAdapter(new SplashSquareAdapter(getContext(), this, this.isSplashView));
        if (this.isSplashView) {
            PolishSplashSticker polishSplashSticker2 = new PolishSplashSticker(StickerFileAsset.loadBitmapFromAssets(getContext(), "frame/image_mask_1.webp"), StickerFileAsset.loadBitmapFromAssets(getContext(), "frame/image_frame_1.webp"));
            this.polishSplashSticker = polishSplashSticker2;
            this.polishSplashView.addSticker(polishSplashSticker2);
        }
        this.polishSplashView.refreshDrawableState();
        this.polishSplashView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        this.textVewTitle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BlurSquareBgFragment.this.polishSplashView.setcSplashMode(0);
                BlurSquareBgFragment.this.recyclerViewBlur.setVisibility(View.VISIBLE);
                BlurSquareBgFragment.this.polishSplashView.refreshDrawableState();
                BlurSquareBgFragment.this.polishSplashView.invalidate();
            }
        });
        inflate.findViewById(R.id.imageViewSaveSplash).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BlurSquareBgFragment.this.blurSquareBgListener.onSaveBlurBackground(BlurSquareBgFragment.this.polishSplashView.getBitmap(BlurSquareBgFragment.this.bitmap));
                BlurSquareBgFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.imageViewCloseSplash).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BlurSquareBgFragment.this.dismiss();
            }
        });
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.polishSplashView.getSticker().release();
        Bitmap bitmap2 = this.BlurBitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
        }
        this.BlurBitmap = null;
        this.bitmap = null;
    }

    @Override
    public void onSelected(PolishSplashSticker splashSticker2) {
        this.polishSplashView.addSticker(splashSticker2);
    }
}
