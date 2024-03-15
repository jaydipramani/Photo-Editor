package com.relish.app.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.relish.app.R;
import com.relish.app.assets.OverlayFileAsset;
import com.relish.app.listener.OverlayListener;

import java.util.List;

public class OverlayAdapter extends RecyclerView.Adapter<OverlayAdapter.ViewHolder> {
    private List<Bitmap> bitmaps;
    private Context context;
    public List<OverlayFileAsset.OverlayCode> effectsCodeList;
    public OverlayListener hardmixListener;
    public int selectIndex = 0;

    public OverlayAdapter(List<Bitmap> bitmapList, OverlayListener hardmixListener2, Context mContext, List<OverlayFileAsset.OverlayCode> effectsCodeList2) {
        this.hardmixListener = hardmixListener2;
        this.bitmaps = bitmapList;
        this.context = mContext;
        this.effectsCodeList = effectsCodeList2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_overlay, viewGroup, false));
    }

    public void reset() {
        this.selectIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.round_image_view_filter_item.setImageBitmap(this.bitmaps.get(i));
        if (this.selectIndex == i) {
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewSelected.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.bitmaps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View lockPro;
        RoundedImageView round_image_view_filter_item;
        View viewSelected;

        ViewHolder(View view) {
            super(view);
            this.round_image_view_filter_item = (RoundedImageView) view.findViewById(R.id.round_image_view_filter_item);
            this.viewSelected = view.findViewById(R.id.view_selected);
            this.lockPro = view.findViewById(R.id.img_lock_pro);
            view.setOnClickListener(view1 -> {
                OverlayAdapter.this.selectIndex = ViewHolder.this.getLayoutPosition();
                OverlayAdapter.this.hardmixListener.onOverlaySelected(OverlayAdapter.this.selectIndex, OverlayAdapter.this.effectsCodeList.get(OverlayAdapter.this.selectIndex).getImage());
                OverlayAdapter.this.notifyDataSetChanged();
            });
        }
    }
}
