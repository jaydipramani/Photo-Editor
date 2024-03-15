package com.relish.app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.relish.app.R;
import com.relish.app.interfac.OnAlbum;
import com.relish.app.model.ImageModel;


import java.io.File;
import java.util.ArrayList;

public class AlbumAdapter extends ArrayAdapter<ImageModel> {
    Context context;
    ArrayList<ImageModel> data;
    int layoutResourceId;
    OnAlbum onItem;
    int pHeightItem = 0;

    static class RecordHolder {
        ImageView image_view_album;
        RelativeLayout relative_layout_root;
        TextView text_view_name_album;

        RecordHolder() {
        }
    }

    public AlbumAdapter(Context context2, int i, ArrayList<ImageModel> arrayList) {
        super(context2, i, arrayList);
        this.layoutResourceId = i;
        this.context = context2;
        this.data = arrayList;
        this.pHeightItem = getDisplayInfo((Activity) context2).widthPixels / 6;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        RecordHolder recordHolder;
        if (view == null) {
            view = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, viewGroup, false);
            recordHolder = new RecordHolder();
            recordHolder.text_view_name_album = (TextView) view.findViewById(R.id.text_view_name_album);
            recordHolder.image_view_album = (ImageView) view.findViewById(R.id.image_view_album);
            recordHolder.relative_layout_root = (RelativeLayout) view.findViewById(R.id.relative_layout_root);
            view.setTag(recordHolder);
        } else {
            recordHolder = (RecordHolder) view.getTag();
        }
        ImageModel imageModel = this.data.get(i);
        recordHolder.text_view_name_album.setText(imageModel.getName());
        ((RequestBuilder) Glide.with(this.context).load(new File(imageModel.getPathFile())).placeholder((int) R.drawable.image_show)).into(recordHolder.image_view_album);

        view.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AlbumAdapter.this.AlbumAdapterItem(i, view);
            }
        });
        return view;
    }

    public void AlbumAdapterItem(int i, View view1) {
        OnAlbum onAlbum = this.onItem;
        if (onAlbum != null) {
            onAlbum.OnItemAlbumClick(i);
        }
    }

    public void setOnItem(OnAlbum onAlbum) {
        this.onItem = onAlbum;
    }

    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
}
