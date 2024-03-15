package com.relish.app.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.relish.app.R;
import com.relish.app.assets.StickerFileAsset;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    public Context context;
    public OnClickSplashListener onClickSplashListener;
    public int screenWidth;
    public List<String> stringList;

    public interface OnClickSplashListener {
        void addSticker(int i, Bitmap bitmap);
    }

    public StickerAdapter(Context context2, List<String> list, int i, OnClickSplashListener onClickSplashListener2) {
        this.context = context2;
        this.stringList = list;
        this.screenWidth = i;
        this.onClickSplashListener = onClickSplashListener2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_sticker, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Glide.with(this.context).load(StickerFileAsset.loadBitmapFromAssets(this.context, this.stringList.get(i))).into(viewHolder.sticker);
    }

    @Override
    public int getItemCount() {
        return this.stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View lockPro;
        public ImageView sticker;

        public ViewHolder(View view) {
            super(view);
            this.sticker = (ImageView) view.findViewById(R.id.image_view_item_sticker);
            this.lockPro = view.findViewById(R.id.img_lock_pro);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            StickerAdapter.this.onClickSplashListener.addSticker(getAdapterPosition(), StickerFileAsset.loadBitmapFromAssets(StickerAdapter.this.context, StickerAdapter.this.stringList.get(getAdapterPosition())));
        }
    }
}
