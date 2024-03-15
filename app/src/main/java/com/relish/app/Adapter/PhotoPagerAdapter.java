package com.relish.app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.relish.app.R;
import com.relish.app.picker.AndroidLifecycleUtils;

import java.io.File;
import java.util.List;

public class PhotoPagerAdapter extends PagerAdapter {
    private RequestManager mGlide;
    private List<String> paths;
    Context context;

    @Override
    public int getItemPosition(Object obj) {
        return -2;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public PhotoPagerAdapter(RequestManager requestManager, List<String> list,Context context) {
        this.paths = list;
        this.context = context;
        this.mGlide = requestManager;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        Uri uri;
        final Context context = viewGroup.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_photo_pager, viewGroup, false);
        ImageView image_view_pager = (ImageView) inflate.findViewById(R.id.image_view_pager);
        String str = this.paths.get(i);
        if (str.startsWith("http")) {
            uri = Uri.parse(str);
        } else {
            uri = Uri.fromFile(new File(str));
        }
        if (AndroidLifecycleUtils.canLoadImage(context)) {
            RequestOptions requestOptions = new RequestOptions();
            ((RequestOptions) ((RequestOptions) requestOptions.dontAnimate()).dontTransform()).override(800, 800);
            this.mGlide.setDefaultRequestOptions(requestOptions).load(uri).thumbnail(0.1f).into(image_view_pager);
        }
        image_view_pager.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if ((context instanceof Activity) && !((Activity) context).isFinishing()) {
                    ((Activity) context).onBackPressed();
                }
            }
        });
        viewGroup.addView(inflate);
        return inflate;
    }

    @Override
    public int getCount() {
        return this.paths.size();
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        View view = (View) obj;
        viewGroup.removeView(view);
        this.mGlide.clear(view);
    }
}