package com.relish.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.relish.app.R;
import com.relish.app.layout.PixLabLayout;
import com.relish.app.listener.LayoutItemListener;

import java.util.ArrayList;

public class PixLabAdapters extends RecyclerView.Adapter<PixLabAdapters.ViewHolder> {
    public LayoutItemListener clickListener;
    Context mContext;
    private ArrayList<String> pixLabItemList = new ArrayList<>();
    public int selectedPos = 0;

    public PixLabAdapters(Context context) {
        this.mContext = context;
    }

    public void addData(ArrayList<String> arrayList) {
        this.pixLabItemList.clear();
        this.pixLabItemList.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_drip, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        int i = 0;

        View view = holder.mSelectedBorder;
        if (position != this.selectedPos) {
            i = 8;
        }
        view.setVisibility(i);
        ((RequestBuilder) Glide.with(this.mContext).load("file:///android_asset/lab/" + this.pixLabItemList.get(position) + ".webp").fitCenter()).into(holder.mIvImage);
    }

    @Override
    public int getItemCount() {
        return this.pixLabItemList.size();
    }

    public void setClickListener(PixLabLayout clickListener2) {
        this.clickListener = clickListener2;
    }

    public ArrayList<String> getItemList() {
        return this.pixLabItemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View lockPro;
        ImageView mIvImage;
        View mSelectedBorder;

        ViewHolder(View view) {
            super(view);
            this.mIvImage = (ImageView) view.findViewById(R.id.imageViewItem);
            this.mSelectedBorder = view.findViewById(R.id.selectedBorder);
            this.lockPro = view.findViewById(R.id.img_lock_pro);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int position = PixLabAdapters.this.selectedPos;
            PixLabAdapters.this.selectedPos = getAdapterPosition();
            PixLabAdapters.this.notifyItemChanged(position);
            PixLabAdapters pixLabAdapters = PixLabAdapters.this;
            pixLabAdapters.notifyItemChanged(pixLabAdapters.selectedPos);
            PixLabAdapters.this.clickListener.onLayoutListClick(view, getAdapterPosition());
        }
    }
}
