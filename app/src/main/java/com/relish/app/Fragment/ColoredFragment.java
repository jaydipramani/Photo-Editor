package com.relish.app.Fragment;

import static android.view.WindowManager.LayoutParams.ANIMATION_CHANGED;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.relish.app.Adapter.ColoredAdapter;
import com.relish.app.R;
import com.relish.app.assets.ColoredCodeAsset;
import com.relish.app.polish.PolishColoredView;


public class ColoredFragment extends DialogFragment implements ColoredAdapter.ColoredChangeListener {
    private static final String TAG = "ColoredFragment";
    public Bitmap adjustBitmap;
    private ImageView backgroundView;
    public Bitmap bitmap;
    public ColoredListener coloredListener;
    public PolishColoredView quShotColoredView;
    private RecyclerView recyclerViewColored;
    private RelativeLayout relative_layout_loading;
    private SeekBar seekbarColored;

    public interface ColoredListener {
        void onSaveMosaic(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static ColoredFragment show(AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, ColoredListener coloredListener2) {
        ColoredFragment coloredFragment = new ColoredFragment();
        coloredFragment.setBitmap(bitmap2);
        coloredFragment.setAdjustBitmap(bitmap3);
        coloredFragment.setColoredListener(coloredListener2);
        coloredFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return coloredFragment;
    }

    public void setColoredListener(ColoredListener coloredListener2) {
        this.coloredListener = coloredListener2;
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
        View inflate = layoutInflater.inflate(R.layout.fragment_colored, viewGroup, false);
        PolishColoredView polishColoredView = (PolishColoredView) inflate.findViewById(R.id.coloredView);
        this.quShotColoredView = polishColoredView;
        polishColoredView.setImageBitmap(this.bitmap);
        this.quShotColoredView.setColoredItems(new ColoredAdapter.ColoredItems(R.drawable.colored_1, 0, ColoredAdapter.COLORED.COLOR_1));
        this.backgroundView = (ImageView) inflate.findViewById(R.id.image_view_background);
        Bitmap coloredBitmap1 = ColoredCodeAsset.getColoredBitmap1(this.bitmap);
        this.adjustBitmap = coloredBitmap1;
        this.backgroundView.setImageBitmap(coloredBitmap1);
        RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading = relativeLayout;
        relativeLayout.setVisibility(View.GONE);
        this.seekbarColored = (SeekBar) inflate.findViewById(R.id.seekbarColored);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerViewColored);
        this.recyclerViewColored = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.recyclerViewColored.setHasFixedSize(true);
        this.recyclerViewColored.setAdapter(new ColoredAdapter(getContext(), this));
        inflate.findViewById(R.id.imageViewSaveColored).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                new SaveMosaicView().execute(new Void[0]);
            }
        });
        inflate.findViewById(R.id.imageViewCloseColored).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                ColoredFragment.this.dismiss();
            }
        });
        this.seekbarColored.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                ColoredFragment.this.quShotColoredView.setBrushBitmapSize(i + 25);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                ColoredFragment.this.quShotColoredView.updateBrush();
            }
        });
        inflate.findViewById(R.id.imageViewUndo).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                ColoredFragment.this.quShotColoredView.undo();
            }
        });
        inflate.findViewById(R.id.imageViewRedo).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                ColoredFragment.this.quShotColoredView.redo();
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
    public void onSelected(ColoredAdapter.ColoredItems coloredItems) {
        if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_1) {
            Bitmap coloredBitmap1 = ColoredCodeAsset.getColoredBitmap1(this.bitmap);
            this.adjustBitmap = coloredBitmap1;
            this.backgroundView.setImageBitmap(coloredBitmap1);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_2) {
            Bitmap coloredBitmap2 = ColoredCodeAsset.getColoredBitmap2(this.bitmap);
            this.adjustBitmap = coloredBitmap2;
            this.backgroundView.setImageBitmap(coloredBitmap2);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_3) {
            Bitmap coloredBitmap3 = ColoredCodeAsset.getColoredBitmap3(this.bitmap, -1.0f);
            this.adjustBitmap = coloredBitmap3;
            this.backgroundView.setImageBitmap(coloredBitmap3);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_4) {
            Bitmap coloredBitmap4 = ColoredCodeAsset.getColoredBitmap4(this.bitmap, 1.0f);
            this.adjustBitmap = coloredBitmap4;
            this.backgroundView.setImageBitmap(coloredBitmap4);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_5) {
            Bitmap coloredBitmap5 = ColoredCodeAsset.getColoredBitmap5(this.bitmap);
            this.adjustBitmap = coloredBitmap5;
            this.backgroundView.setImageBitmap(coloredBitmap5);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_6) {
            Bitmap coloredBitmap6 = ColoredCodeAsset.getColoredBitmap6(this.bitmap);
            this.adjustBitmap = coloredBitmap6;
            this.backgroundView.setImageBitmap(coloredBitmap6);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_7) {
            Bitmap coloredBitmap7 = ColoredCodeAsset.getColoredBitmap7(this.bitmap);
            this.adjustBitmap = coloredBitmap7;
            this.backgroundView.setImageBitmap(coloredBitmap7);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_8) {
            Bitmap coloredBitmap8 = ColoredCodeAsset.getColoredBitmap8(this.bitmap);
            this.adjustBitmap = coloredBitmap8;
            this.backgroundView.setImageBitmap(coloredBitmap8);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_9) {
            Bitmap coloredBitmap9 = ColoredCodeAsset.getColoredBitmap9(this.bitmap);
            this.adjustBitmap = coloredBitmap9;
            this.backgroundView.setImageBitmap(coloredBitmap9);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_10) {
            Bitmap coloredBitmap10 = ColoredCodeAsset.getColoredBitmap10(this.bitmap);
            this.adjustBitmap = coloredBitmap10;
            this.backgroundView.setImageBitmap(coloredBitmap10);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_11) {
            Bitmap coloredBitmap11 = ColoredCodeAsset.getColoredBitmap11(this.bitmap);
            this.adjustBitmap = coloredBitmap11;
            this.backgroundView.setImageBitmap(coloredBitmap11);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_12) {
            Bitmap coloredBitmap12 = ColoredCodeAsset.getColoredBitmap12(this.bitmap);
            this.adjustBitmap = coloredBitmap12;
            this.backgroundView.setImageBitmap(coloredBitmap12);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_13) {
            Bitmap coloredBitmap13 = ColoredCodeAsset.getColoredBitmap13(this.bitmap);
            this.adjustBitmap = coloredBitmap13;
            this.backgroundView.setImageBitmap(coloredBitmap13);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_14) {
            Bitmap coloredBitmap14 = ColoredCodeAsset.getColoredBitmap14(this.bitmap);
            this.adjustBitmap = coloredBitmap14;
            this.backgroundView.setImageBitmap(coloredBitmap14);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_15) {
            Bitmap coloredBitmap15 = ColoredCodeAsset.getColoredBitmap15(this.bitmap);
            this.adjustBitmap = coloredBitmap15;
            this.backgroundView.setImageBitmap(coloredBitmap15);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_16) {
            Bitmap coloredBitmap16 = ColoredCodeAsset.getColoredBitmap16(this.bitmap);
            this.adjustBitmap = coloredBitmap16;
            this.backgroundView.setImageBitmap(coloredBitmap16);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_17) {
            Bitmap coloredBitmap17 = ColoredCodeAsset.getColoredBitmap17(this.bitmap);
            this.adjustBitmap = coloredBitmap17;
            this.backgroundView.setImageBitmap(coloredBitmap17);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_18) {
            Bitmap coloredBitmap18 = ColoredCodeAsset.getColoredBitmap18(this.bitmap);
            this.adjustBitmap = coloredBitmap18;
            this.backgroundView.setImageBitmap(coloredBitmap18);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_19) {
            Bitmap coloredBitmap19 = ColoredCodeAsset.getColoredBitmap19(this.bitmap);
            this.adjustBitmap = coloredBitmap19;
            this.backgroundView.setImageBitmap(coloredBitmap19);
        }
        this.quShotColoredView.setColoredItems(coloredItems);
    }

    class SaveMosaicView extends AsyncTask<Void, Bitmap, Bitmap> {
        SaveMosaicView() {
        }

        public void onPreExecute() {
            ColoredFragment.this.mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return ColoredFragment.this.quShotColoredView.getBitmap(ColoredFragment.this.bitmap, ColoredFragment.this.adjustBitmap);
        }

        public void onPostExecute(Bitmap bitmap) {
            ColoredFragment.this.mLoading(false);
            ColoredFragment.this.coloredListener.onSaveMosaic(bitmap);
            ColoredFragment.this.dismiss();
        }
    }

    public void mLoading(boolean z) {
        if (z) {
            getActivity().getWindow().setFlags(ANIMATION_CHANGED, ANIMATION_CHANGED);
            this.relative_layout_loading.setVisibility(View.VISIBLE);
            return;
        }
        getActivity().getWindow().clearFlags(ANIMATION_CHANGED);
        this.relative_layout_loading.setVisibility(View.GONE);
    }
}
