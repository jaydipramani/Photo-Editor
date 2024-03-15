package com.relish.app.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.github.flipzeus.FlipDirection;
import com.github.flipzeus.ImageFlipper;
import com.isseiaoki.simplecropview.CropImageView;
import com.relish.app.Adapter.AspectAdapter;
import com.relish.app.R;
import com.steelkiwi.cropiwa.AspectRatio;

public class CropFragment extends DialogFragment implements AspectAdapter.OnNewSelectedListener {
    private static final String TAG = "CropFragment";
    private Bitmap bitmap;
    public CropImageView crop_image_view;
    public OnCropPhoto onCropPhoto;
    private RelativeLayout relative_layout_loading;

    public interface OnCropPhoto {
        void finishCrop(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static CropFragment show(AppCompatActivity appCompatActivity, OnCropPhoto onCropPhoto2, Bitmap bitmap2) {
        CropFragment cropDialogFragment = new CropFragment();
        cropDialogFragment.setBitmap(bitmap2);
        cropDialogFragment.setOnCropPhoto(onCropPhoto2);
        cropDialogFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return cropDialogFragment;
    }

    @Override 
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void setOnCropPhoto(OnCropPhoto onCropPhoto2) {
        this.onCropPhoto = onCropPhoto2;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
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
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_crop, viewGroup, false);
        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter();
        aspectRatioPreviewAdapter.setListener(this);
        RecyclerView recycler_view_ratio = (RecyclerView) inflate.findViewById(R.id.recycler_view_ratio);
        recycler_view_ratio.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recycler_view_ratio.setAdapter(aspectRatioPreviewAdapter);
        CropImageView cropImageView = (CropImageView) inflate.findViewById(R.id.crop_image_view);
        this.crop_image_view = cropImageView;
        cropImageView.setCropMode(CropImageView.CropMode.FREE);
        inflate.findViewById(R.id.relativeLayoutRotate).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                CropFragment.this.crop_image_view.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
            }
        });
        inflate.findViewById(R.id.relativeLayouRotate90).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                CropFragment.this.crop_image_view.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
            }
        });
        inflate.findViewById(R.id.relativeLayoutVFlip).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                ImageFlipper.flip(CropFragment.this.crop_image_view, FlipDirection.VERTICAL);
            }
        });
        inflate.findViewById(R.id.relativeLayoutHFlip).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                ImageFlipper.flip(CropFragment.this.crop_image_view, FlipDirection.HORIZONTAL);
            }
        });
        inflate.findViewById(R.id.imageViewSaveCrop).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new OnSaveCrop().execute(new Void[0]);
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading = relativeLayout;
        relativeLayout.setVisibility(View.GONE);
        inflate.findViewById(R.id.imageViewCloseCrop).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                CropFragment.this.dismiss();
            }
        });
        return inflate;
    }

    @Override 
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        CropImageView cropImageView = (CropImageView) view.findViewById(R.id.crop_image_view);
        this.crop_image_view = cropImageView;
        cropImageView.setImageBitmap(this.bitmap);
    }

    @Override
    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        if (aspectRatio.getWidth() == 10 && aspectRatio.getHeight() == 10) {
            this.crop_image_view.setCropMode(CropImageView.CropMode.FREE);
        } else {
            this.crop_image_view.setCustomRatio(aspectRatio.getWidth(), aspectRatio.getHeight());
        }
    }

    class OnSaveCrop extends AsyncTask<Void, Bitmap, Bitmap> {
        OnSaveCrop() {
        }

        public void onPreExecute() {
            CropFragment.this.mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return CropFragment.this.crop_image_view.getCroppedBitmap();
        }

        public void onPostExecute(Bitmap bitmap) {
            CropFragment.this.mLoading(false);
            CropFragment.this.onCropPhoto.finishCrop(bitmap);
            CropFragment.this.dismiss();
        }
    }

    public void mLoading(boolean z) {
        if (z) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.ANIMATION_CHANGED, WindowManager.LayoutParams.ANIMATION_CHANGED);
            this.relative_layout_loading.setVisibility(View.VISIBLE);
            return;
        }
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.ANIMATION_CHANGED);
        this.relative_layout_loading.setVisibility(View.GONE);
    }
}
