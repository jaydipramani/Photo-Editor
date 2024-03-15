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
import com.relish.app.layout.ArtLayout;
import com.relish.app.listener.LayoutItemListener;

import java.util.ArrayList;

public class ArtAdapter extends RecyclerView.Adapter<ArtAdapter.ViewHolder> {
    public LayoutItemListener clickListener;
    private ArrayList<String> dripItemList = new ArrayList<>();
    Context mContext;
    public int selectedPos = 0;

    public ArtAdapter(Context context) {
        this.mContext = context;
    }

    public void addData(ArrayList<String> arrayList) {
        this.dripItemList.clear();
        this.dripItemList.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_art, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        int i = 0;
        View view = holder.mSelectedBorder;
        if (position != this.selectedPos) {
            i = 8;
        }
        view.setVisibility(i);
        ((RequestBuilder) Glide.with(this.mContext).load("file:///android_asset/art/icon/" + this.dripItemList.get(position) + ".webp").fitCenter()).into(holder.imageViewItem);
    }

    @Override
    public int getItemCount() {
        return this.dripItemList.size();
    }

    public void setClickListener(ArtLayout clickListener2) {
        this.clickListener = clickListener2;
    }

    public ArrayList<String> getItemList() {
        return this.dripItemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewItem;
        View lockPro;
        View mSelectedBorder;

        ViewHolder(View view) {
            super(view);
            this.imageViewItem = (ImageView) view.findViewById(R.id.imageViewItem);
            this.mSelectedBorder = view.findViewById(R.id.selectedBorder);
            this.lockPro = view.findViewById(R.id.img_lock_pro);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int p = ArtAdapter.this.selectedPos;
            ArtAdapter.this.selectedPos = getAdapterPosition();
            ArtAdapter.this.notifyItemChanged(p);
            ArtAdapter artAdapter = ArtAdapter.this;
            artAdapter.notifyItemChanged(artAdapter.selectedPos);
            ArtAdapter.this.clickListener.onLayoutListClick(view, getAdapterPosition());
        }
    }
}
