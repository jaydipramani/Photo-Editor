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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.relish.app.Adapter.MosaicAdapter;
import com.relish.app.R;
import com.relish.app.Utils.DegreeSeekBar;
import com.relish.app.Utils.FilterUtils;
import com.relish.app.assets.MosaicAsset;
import com.relish.app.polish.PolishMosaicView;


public class MosaicFragment extends DialogFragment implements MosaicAdapter.MosaicChangeListener {
    private static final String TAG = "MosaicFragment";
    public Bitmap adjustBitmap;
    private DegreeSeekBar adjustSeekBar;
    private ImageView backgroundView;
    public Bitmap bitmap;
    public MosaicListener mosaicListener;
    public PolishMosaicView queShotMosaicView;
    private RecyclerView recyclerViewMosaic;
    private RelativeLayout relative_layout_loading;

    public interface MosaicListener {
        void onSaveMosaic(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static MosaicFragment show(AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, MosaicListener mosaicDialogListener2) {
        MosaicFragment mosaicDialog = new MosaicFragment();
        mosaicDialog.setBitmap(bitmap2);
        mosaicDialog.setAdjustBitmap(bitmap3);
        mosaicDialog.setMosaicListener(mosaicDialogListener2);
        mosaicDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return mosaicDialog;
    }

    public void setMosaicListener(MosaicListener mosaicListener2) {
        this.mosaicListener = mosaicListener2;
    }

    public void setAdjustBitmap(Bitmap bitmap2) {
        this.adjustBitmap = bitmap2;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_mosaic, viewGroup, false);
        PolishMosaicView polishMosaicView = (PolishMosaicView) inflate.findViewById(R.id.mosaic_view);
        this.queShotMosaicView = polishMosaicView;
        polishMosaicView.setImageBitmap(this.bitmap);
        this.queShotMosaicView.setMosaicItem(new MosaicAdapter.MosaicItem(R.drawable.background_blur, 0, MosaicAdapter.BLUR.BLUR));
        this.backgroundView = (ImageView) inflate.findViewById(R.id.image_view_background);
        Bitmap blurImageFromBitmap = FilterUtils.getBlurImageFromBitmap(this.bitmap);
        this.adjustBitmap = blurImageFromBitmap;
        this.backgroundView.setImageBitmap(blurImageFromBitmap);
        RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading = relativeLayout;
        relativeLayout.setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerViewMoasic);
        this.recyclerViewMosaic = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.recyclerViewMosaic.setHasFixedSize(true);
        this.recyclerViewMosaic.setAdapter(new MosaicAdapter(getContext(), this));
        inflate.findViewById(R.id.imageViewSaveMosaic).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new SaveMosaicView().execute(new Void[0]);
            }
        });
        inflate.findViewById(R.id.imageViewCloseMosaic).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                MosaicFragment.this.dismiss();
            }
        });
        DegreeSeekBar degreeSeekBar = (DegreeSeekBar) inflate.findViewById(R.id.seekbarMoasic);
        this.adjustSeekBar = degreeSeekBar;
        degreeSeekBar.setCenterTextColor(getResources().getColor(R.color.yellow));
        this.adjustSeekBar.setTextColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setPointColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setDegreeRange(0, 70);
        this.adjustSeekBar.setCurrentDegrees(15);
        this.adjustSeekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {

            @Override
            public void onScrollEnd() {
            }

            @Override
            public void onScrollStart() {
            }

            @Override
            public void onScroll(int i) {
                MosaicFragment.this.queShotMosaicView.setBrushBitmapSize(i + 15);
                MosaicFragment.this.queShotMosaicView.updateBrush();
            }
        });
        inflate.findViewById(R.id.imageViewUndo).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                MosaicFragment.this.queShotMosaicView.undo();
            }
        });
        inflate.findViewById(R.id.imageViewRedo).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                MosaicFragment.this.queShotMosaicView.redo();
            }
        });
        return inflate;
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
        this.bitmap.recycle();
        this.bitmap = null;
        this.adjustBitmap.recycle();
        this.adjustBitmap = null;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSelected(MosaicAdapter.MosaicItem mosaicItem) {
        if (mosaicItem.mode == MosaicAdapter.BLUR.BLUR) {
            Bitmap blurImageFromBitmap = FilterUtils.getBlurImageFromBitmap(this.bitmap);
            this.adjustBitmap = blurImageFromBitmap;
            this.backgroundView.setImageBitmap(blurImageFromBitmap);
        } else if (mosaicItem.mode == MosaicAdapter.BLUR.MOSAIC) {
            Bitmap mosaic = MosaicAsset.getMosaic(this.bitmap);
            this.adjustBitmap = mosaic;
            this.backgroundView.setImageBitmap(mosaic);
        }
        this.queShotMosaicView.setMosaicItem(mosaicItem);
    }

    class SaveMosaicView extends AsyncTask<Void, Bitmap, Bitmap> {
        SaveMosaicView() {
        }

        public void onPreExecute() {
            MosaicFragment.this.mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return MosaicFragment.this.queShotMosaicView.getBitmap(MosaicFragment.this.bitmap, MosaicFragment.this.adjustBitmap);
        }

        public void onPostExecute(Bitmap bitmap) {
            MosaicFragment.this.mLoading(false);
            MosaicFragment.this.mosaicListener.onSaveMosaic(bitmap);
            MosaicFragment.this.dismiss();
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
