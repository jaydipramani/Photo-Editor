package com.relish.app.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.relish.app.R;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.wysaid.nativePort.CGENativeLibrary;

public class HSlFragment extends DialogFragment {
    private static final String TAG = "HSlFragment";
    private Bitmap bitmap;
    private RadioGroup colorselection;
    public ImageView image_view_ratio;
    public OnFilterSavePhoto onFilterSavePhoto;
    private IndicatorSeekBar seekbarIntensityHue;
    private IndicatorSeekBar seekbarIntensityLightness;
    private IndicatorSeekBar seekbarIntensitySaturation;
    private Bitmap tempbitmap;
    private Bitmap tempbitmap2;

    public interface OnFilterSavePhoto {
        void onSaveFilter(Bitmap bitmap);
    }

    public void setOnFilterSavePhoto(OnFilterSavePhoto onFilterSavePhoto2) {
        this.onFilterSavePhoto = onFilterSavePhoto2;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static HSlFragment show(AppCompatActivity appCompatActivity, OnFilterSavePhoto onFilterSavePhoto2, Bitmap mBitmap) {
        HSlFragment ratioFragment = new HSlFragment();
        ratioFragment.setBitmap(mBitmap);
        ratioFragment.setOnFilterSavePhoto(onFilterSavePhoto2);
        ratioFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return ratioFragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_hsl, viewGroup, false);
        this.seekbarIntensityHue = (IndicatorSeekBar) inflate.findViewById(R.id.hue);
        this.seekbarIntensitySaturation = (IndicatorSeekBar) inflate.findViewById(R.id.sat);
        this.seekbarIntensityLightness = (IndicatorSeekBar) inflate.findViewById(R.id.light);
        this.colorselection = (RadioGroup) inflate.findViewById(R.id.colorselection);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageViewRatio);
        this.image_view_ratio = imageView;
        imageView.setImageBitmap(this.bitmap);
        inflate.findViewById(R.id.imageViewCloseRatio).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                HSlFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.imageViewSaveRatio).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                HSlFragment.this.onFilterSavePhoto.onSaveFilter(((BitmapDrawable) HSlFragment.this.image_view_ratio.getDrawable()).getBitmap());
                HSlFragment.this.dismiss();
            }
        });
        this.seekbarIntensityHue.setOnSeekChangeListener(new OnSeekChangeListener() {

            @Override
            public void onSeeking(SeekParams seekParams) {
                String ruleString = "";
                if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.red) {
                    ruleString = "@selcolor red(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.green) {
                    ruleString = "@selcolor green(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.blue) {
                    ruleString = "@selcolor blue(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.mergenta) {
                    ruleString = "@selcolor magenta(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.yellow) {
                    ruleString = "@selcolor yellow(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.cyan) {
                    ruleString = "@selcolor cyan(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.white) {
                    ruleString = "@selcolor white(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                }
                if (HSlFragment.this.tempbitmap == null) {
                    HSlFragment hSlFragment = HSlFragment.this;
                    hSlFragment.tempbitmap = hSlFragment.getBitmapFromView(hSlFragment.image_view_ratio);
                }
                HSlFragment hSlFragment2 = HSlFragment.this;
                hSlFragment2.tempbitmap2 = CGENativeLibrary.filterImage_MultipleEffects(hSlFragment2.tempbitmap, ruleString, HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 200.0f);
                HSlFragment.this.image_view_ratio.setImageBitmap(HSlFragment.this.tempbitmap2);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
        this.seekbarIntensitySaturation.setOnSeekChangeListener(new OnSeekChangeListener() {

            @Override
            public void onSeeking(SeekParams seekParams) {
                String ruleString = "";
                if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.red) {
                    ruleString = "@selcolor red(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.green) {
                    ruleString = "@selcolor green(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.blue) {
                    ruleString = "@selcolor blue(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.mergenta) {
                    ruleString = "@selcolor magenta(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.yellow) {
                    ruleString = "@selcolor yellow(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.cyan) {
                    ruleString = "@selcolor cyan(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.white) {
                    ruleString = "@selcolor white(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                }
                if (HSlFragment.this.tempbitmap == null) {
                    HSlFragment hSlFragment = HSlFragment.this;
                    hSlFragment.tempbitmap = hSlFragment.getBitmapFromView(hSlFragment.image_view_ratio);
                }
                HSlFragment hSlFragment2 = HSlFragment.this;
                hSlFragment2.tempbitmap2 = CGENativeLibrary.filterImage_MultipleEffects(hSlFragment2.tempbitmap, ruleString, HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 200.0f);
                HSlFragment.this.image_view_ratio.setImageBitmap(HSlFragment.this.tempbitmap2);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
        this.colorselection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                HSlFragment.this.tempbitmap = null;
            }
        });
        this.seekbarIntensityLightness.setOnSeekChangeListener(new OnSeekChangeListener() {


            @Override
            public void onSeeking(SeekParams seekParams) {
                String ruleString = "";
                if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.red) {
                    ruleString = "@selcolor red(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.green) {
                    ruleString = "@selcolor green(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.blue) {
                    ruleString = "@selcolor blue(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.mergenta) {
                    ruleString = "@selcolor magenta(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.yellow) {
                    ruleString = "@selcolor yellow(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.cyan) {
                    ruleString = "@selcolor cyan(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                } else if (HSlFragment.this.colorselection.getCheckedRadioButtonId() == R.id.white) {
                    ruleString = "@selcolor white(" + (HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensitySaturation.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + "," + (HSlFragment.this.seekbarIntensityLightness.getProgressFloat() / 100.0f) + ")";
                }
                if (HSlFragment.this.tempbitmap == null) {
                    HSlFragment hSlFragment = HSlFragment.this;
                    hSlFragment.tempbitmap = hSlFragment.getBitmapFromView(hSlFragment.image_view_ratio);
                }
                HSlFragment hSlFragment2 = HSlFragment.this;
                hSlFragment2.tempbitmap2 = CGENativeLibrary.filterImage_MultipleEffects(hSlFragment2.tempbitmap, ruleString, HSlFragment.this.seekbarIntensityHue.getProgressFloat() / 200.0f);
                HSlFragment.this.image_view_ratio.setImageBitmap(HSlFragment.this.tempbitmap2);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.bitmap = null;
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap bitmap2 = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap2));
        return bitmap2;
    }
}
