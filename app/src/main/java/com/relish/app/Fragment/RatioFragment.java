package com.relish.app.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.relish.app.Adapter.AspectAdapter;
import com.relish.app.R;
import com.relish.app.Utils.DegreeSeekBar;
import com.relish.app.Utils.FilterUtils;
import com.relish.app.Utils.SystemUtil;
import com.steelkiwi.cropiwa.AspectRatio;

public class RatioFragment extends DialogFragment implements AspectAdapter.OnNewSelectedListener {
    private static final String TAG = "RatioFragment";
    private DegreeSeekBar adjustSeekBar;
    private AspectRatio aspectRatio;
    private Bitmap bitmap;
    private Bitmap blurBitmap;
    private ConstraintLayout constraint_layout_ratio;
    public FrameLayout frame_layout_wrapper;
    private ImageView image_view_blur;
    public ImageView image_view_ratio;
    public RatioSaveListener ratioSaveListener;
    public RecyclerView recycler_view_ratio;
    private RelativeLayout relative_layout_loading;

    public interface RatioSaveListener {
        void ratioSavedBitmap(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static RatioFragment show(AppCompatActivity appCompatActivity, RatioSaveListener ratioSaveListener2, Bitmap mBitmap, Bitmap iBitmap) {
        RatioFragment ratioFragment = new RatioFragment();
        ratioFragment.setBitmap(mBitmap);
        ratioFragment.setBlurBitmap(iBitmap);
        ratioFragment.setRatioSaveListener(ratioSaveListener2);
        ratioFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return ratioFragment;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.blurBitmap = bitmap2;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void setRatioSaveListener(RatioSaveListener iRatioSaveListener) {
        this.ratioSaveListener = iRatioSaveListener;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_ratio, viewGroup, false);
        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(true);
        aspectRatioPreviewAdapter.setListener(this);
        RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading = relativeLayout;
        relativeLayout.setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerViewRatio);
        this.recycler_view_ratio = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_ratio.setAdapter(aspectRatioPreviewAdapter);
        this.aspectRatio = new AspectRatio(1, 1);
        DegreeSeekBar degreeSeekBar = (DegreeSeekBar) inflate.findViewById(R.id.seekbarOverlay);
        this.adjustSeekBar = degreeSeekBar;
        degreeSeekBar.setCenterTextColor(getResources().getColor(R.color.yellow));
        this.adjustSeekBar.setTextColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setPointColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setDegreeRange(0, 50);
        this.adjustSeekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {

            @Override
            public void onScrollEnd() {
            }

            @Override
            public void onScrollStart() {
            }

            @Override
            public void onScroll(int i) {
                int dpToPx = SystemUtil.dpToPx(RatioFragment.this.getContext(), i);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) RatioFragment.this.image_view_ratio.getLayoutParams();
                layoutParams.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
                RatioFragment.this.image_view_ratio.setLayoutParams(layoutParams);
            }
        });
        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageViewRatio);
        this.image_view_ratio = imageView;
        imageView.setImageBitmap(this.bitmap);
        this.image_view_ratio.setAdjustViewBounds(true);
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.constraint_layout_ratio = (ConstraintLayout) inflate.findViewById(R.id.constraintLayoutRatio);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.imageViewBlur);
        this.image_view_blur = imageView2;
        imageView2.setImageBitmap(this.blurBitmap);
        FrameLayout frameLayout = (FrameLayout) inflate.findViewById(R.id.frameLayoutWrapper);
        this.frame_layout_wrapper = frameLayout;
        frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(point.x, point.x));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_ratio);
        constraintSet.connect(this.frame_layout_wrapper.getId(), 3, this.constraint_layout_ratio.getId(), 3, 0);
        constraintSet.connect(this.frame_layout_wrapper.getId(), 1, this.constraint_layout_ratio.getId(), 1, 0);
        constraintSet.connect(this.frame_layout_wrapper.getId(), 4, this.constraint_layout_ratio.getId(), 4, 0);
        constraintSet.connect(this.frame_layout_wrapper.getId(), 2, this.constraint_layout_ratio.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_ratio);
        inflate.findViewById(R.id.imageViewCloseRatio).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                RatioFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.imageViewSaveRatio).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                SaveRatioView saveRatioView = new SaveRatioView();
                RatioFragment ratioFragment = RatioFragment.this;
                saveRatioView.execute(ratioFragment.getBitmapFromView(ratioFragment.frame_layout_wrapper));
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
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private int[] calculateWidthAndHeight(AspectRatio aspectRatio2, Point point) {
        int height = this.constraint_layout_ratio.getHeight();
        if (aspectRatio2.getHeight() > aspectRatio2.getWidth()) {
            int mRatio = (int) (aspectRatio2.getRatio() * ((float) height));
            if (mRatio < point.x) {
                return new int[]{mRatio, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio2.getRatio())};
        }
        int iRatio = (int) (((float) point.x) / aspectRatio2.getRatio());
        if (iRatio > height) {
            return new int[]{(int) (((float) height) * aspectRatio2.getRatio()), height};
        }
        return new int[]{point.x, iRatio};
    }

    @Override
    public void onNewAspectRatioSelected(AspectRatio aspectRatio2) {
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.aspectRatio = aspectRatio2;
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio2, point);
        this.frame_layout_wrapper.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_ratio);
        constraintSet.connect(this.frame_layout_wrapper.getId(), 3, this.constraint_layout_ratio.getId(), 3, 0);
        constraintSet.connect(this.frame_layout_wrapper.getId(), 1, this.constraint_layout_ratio.getId(), 1, 0);
        constraintSet.connect(this.frame_layout_wrapper.getId(), 4, this.constraint_layout_ratio.getId(), 4, 0);
        constraintSet.connect(this.frame_layout_wrapper.getId(), 2, this.constraint_layout_ratio.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_ratio);
    }

    class SaveRatioView extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        SaveRatioView() {
        }

        public void onPreExecute() {
            RatioFragment.this.mLoading(true);
        }

        public Bitmap doInBackground(Bitmap... bitmapArr) {
            Bitmap cloneBitmap = FilterUtils.cloneBitmap(bitmapArr[0]);
            bitmapArr[0].recycle();
            bitmapArr[0] = null;
            return cloneBitmap;
        }

        public void onPostExecute(Bitmap bitmap) {
            RatioFragment.this.mLoading(false);
            RatioFragment.this.ratioSaveListener.ratioSavedBitmap(bitmap);
            RatioFragment.this.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bitmap bitmap2 = this.blurBitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.blurBitmap = null;
        }
        this.bitmap = null;
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap bitmap2 = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap2));
        return bitmap2;
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
