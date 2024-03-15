package com.relish.app.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;


import com.relish.app.Adapter.FilterAdapter;
import com.relish.app.R;
import com.relish.app.Utils.FilterUtils;
import com.relish.app.assets.FilterFileAsset;
import com.relish.app.listener.FilterListener;

import java.util.Arrays;
import java.util.List;

public class FilterFragment extends DialogFragment implements FilterListener {
    private static final String TAG = "FilterFragment";
    public Bitmap bitmap;
    public ImageView image_view_preview;
    private List<Bitmap> lstFilterBitmap;
    public OnFilterSavePhoto onFilterSavePhoto;
    private RecyclerView recycler_view_filter;
    public TextView textViewTitle;

    public interface OnFilterSavePhoto {
        void onSaveFilter(Bitmap bitmap);
    }

    public void setOnFilterSavePhoto(OnFilterSavePhoto onFilterSavePhoto2) {
        this.onFilterSavePhoto = onFilterSavePhoto2;
    }

    public void setLstFilterBitmap(List<Bitmap> list) {
        this.lstFilterBitmap = list;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static FilterFragment show(AppCompatActivity appCompatActivity, OnFilterSavePhoto onFilterSavePhoto2, Bitmap bitmap2, List<Bitmap> list) {
        FilterFragment filterDialogFragment = new FilterFragment();
        filterDialogFragment.setBitmap(bitmap2);
        filterDialogFragment.setOnFilterSavePhoto(onFilterSavePhoto2);
        filterDialogFragment.setLstFilterBitmap(list);
        filterDialogFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return filterDialogFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.lstFilterBitmap.clear();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_filter, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view_filter_all);
        this.recycler_view_filter = recyclerView;
        recyclerView.setAdapter(new FilterAdapter(this.lstFilterBitmap, this, getContext(), Arrays.asList(FilterFileAsset.FILTERS)));
        ImageView imageView = (ImageView) inflate.findViewById(R.id.image_view_preview);
        this.image_view_preview = imageView;
        imageView.setImageBitmap(this.bitmap);
        TextView textView = (TextView) inflate.findViewById(R.id.textViewTitle);
        this.textViewTitle = textView;
        textView.setText("FILTER");
        inflate.findViewById(R.id.imageViewSaveFilter).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                FilterFragment.this.onFilterSavePhoto.onSaveFilter(((BitmapDrawable) FilterFragment.this.image_view_preview.getDrawable()).getBitmap());
                FilterFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.imageViewCloseFilter).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                FilterFragment.this.dismiss();
            }
        });
        return inflate;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
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
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }
    }

    @Override
    public void onFilterSelected(int item, String str) {
        new LoadBitmapWithFilter().execute(str);
    }

    class LoadBitmapWithFilter extends AsyncTask<String, Bitmap, Bitmap> {
        LoadBitmapWithFilter() {
        }

        public Bitmap doInBackground(String... strArr) {
            return FilterUtils.getBitmapWithFilter(FilterFragment.this.bitmap, strArr[0]);
        }

        public void onPostExecute(Bitmap bitmap) {
            FilterFragment.this.image_view_preview.setImageBitmap(bitmap);
        }
    }
}
