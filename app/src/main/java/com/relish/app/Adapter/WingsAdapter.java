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
import com.relish.app.listener.LayoutItemListener;

import java.util.ArrayList;

public class WingsAdapter extends RecyclerView.Adapter<WingsAdapter.ViewHolder> {
    Context context;
    public LayoutItemListener menuItemClickLister;
    public int selectedItem = 0;
    private ArrayList<String> wingsIcons = new ArrayList<>();

    public WingsAdapter(Context mContext) {
        this.context = mContext;
    }

    public void addData(ArrayList<String> arrayList) {
        this.wingsIcons.clear();
        this.wingsIcons.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_neon, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        int i = 0;

        View view = holder.mSelectedBorder;
        if (position != this.selectedItem) {
            i = 8;
        }
        view.setVisibility(i);
        ((RequestBuilder) Glide.with(this.context).load("file:///android_asset/wings/" + this.wingsIcons.get(position) + ".webp").fitCenter()).into(holder.imageViewItem1);
    }

    @Override 
    public int getItemCount() {
        return this.wingsIcons.size();
    }

    public ArrayList<String> getItemList() {
        return this.wingsIcons;
    }

    public void setMenuItemClickLister(LayoutItemListener menuItemClickLister2) {
        this.menuItemClickLister = menuItemClickLister2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewItem1;
        View lockPro;
        View mSelectedBorder;

        ViewHolder(View view) {
            super(view);
            this.imageViewItem1 = (ImageView) view.findViewById(R.id.imageViewItem1);
            this.mSelectedBorder = view.findViewById(R.id.selectedBorder);
            this.lockPro = view.findViewById(R.id.img_lock_pro);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int p = WingsAdapter.this.selectedItem;
            WingsAdapter.this.selectedItem = getAdapterPosition();
            WingsAdapter.this.notifyItemChanged(p);
            WingsAdapter wingsAdapter = WingsAdapter.this;
            wingsAdapter.notifyItemChanged(wingsAdapter.selectedItem);
            WingsAdapter.this.menuItemClickLister.onLayoutListClick(view, getAdapterPosition());
        }
    }
}
