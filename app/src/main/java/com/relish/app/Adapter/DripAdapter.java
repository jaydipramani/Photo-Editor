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
import com.relish.app.layout.DripLayout;
import com.relish.app.listener.LayoutItemListener;

import java.util.ArrayList;

public class DripAdapter extends RecyclerView.Adapter<DripAdapter.ViewHolder> {
    public LayoutItemListener clickListener;
    private ArrayList<String> dripItemList = new ArrayList<>();
    Context mContext;
    public int selectedPos = 0;

    public DripAdapter(Context context) {
        this.mContext = context;
    }

    public void addData(ArrayList<String> arrayList) {
        this.dripItemList.clear();
        this.dripItemList.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_drip, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSelectedBorder.setVisibility(position == this.selectedPos ? View.VISIBLE : View.GONE);

        ((RequestBuilder) Glide.with(this.mContext).load("file:///android_asset/drip/style/" + this.dripItemList.get(position) + ".webp").fitCenter()).into(holder.imageViewItem);
    }

    @Override 
    public int getItemCount() {
        return this.dripItemList.size();
    }

    public void setClickListener(DripLayout clickListener2) {
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
            int p = DripAdapter.this.selectedPos;
            DripAdapter.this.selectedPos = getAdapterPosition();
            DripAdapter.this.notifyItemChanged(p);
            DripAdapter dripAdapter = DripAdapter.this;
            dripAdapter.notifyItemChanged(dripAdapter.selectedPos);
            DripAdapter.this.clickListener.onLayoutListClick(view, getAdapterPosition());
        }
    }
}
