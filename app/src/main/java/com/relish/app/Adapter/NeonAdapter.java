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

public class NeonAdapter extends RecyclerView.Adapter<NeonAdapter.ViewHolder> {
    Context context;
    public LayoutItemListener layoutItenListener;
    private ArrayList<String> neonIcons = new ArrayList<>();
    public int selectedItem = 0;

    public NeonAdapter(Context mContext) {
        this.context = mContext;
    }

    public void addData(ArrayList<String> arrayList) {
        this.neonIcons.clear();
        this.neonIcons.addAll(arrayList);
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
        ((RequestBuilder) Glide.with(this.context).load("file:///android_asset/neon/icon/" + this.neonIcons.get(position) + ".webp").fitCenter()).into(holder.imageViewItem1);
    }

    @Override
    public int getItemCount() {
        return this.neonIcons.size();
    }

    public ArrayList<String> getItemList() {
        return this.neonIcons;
    }

    public void setLayoutItenListener(LayoutItemListener layoutItenListener2) {
        this.layoutItenListener = layoutItenListener2;
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
            int p = NeonAdapter.this.selectedItem;
            NeonAdapter.this.selectedItem = getAdapterPosition();
            NeonAdapter.this.notifyItemChanged(p);
            NeonAdapter neonAdapter = NeonAdapter.this;
            neonAdapter.notifyItemChanged(neonAdapter.selectedItem);
            NeonAdapter.this.layoutItenListener.onLayoutListClick(view, getAdapterPosition());
        }
    }
}
