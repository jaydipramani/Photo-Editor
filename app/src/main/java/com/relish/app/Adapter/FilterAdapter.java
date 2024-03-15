package com.relish.app.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.relish.app.R;
import com.relish.app.assets.FilterFileAsset;
import com.relish.app.listener.FilterListener;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private List<Bitmap> bitmaps;
    private Context context;
    public List<FilterFileAsset.FiltersCode> filterBeanList;
    public FilterListener filterListener;
    public int selectedIndex = 0;

    public FilterAdapter(List<Bitmap> bitmapList, FilterListener filterListener2, Context mContext, List<FilterFileAsset.FiltersCode> mfilterBeanList) {
        this.filterListener = filterListener2;
        this.bitmaps = bitmapList;
        this.context = mContext;
        this.filterBeanList = mfilterBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filter, viewGroup, false));
    }

    public void reset() {
        this.selectedIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.text_view_filter_name.setText(this.filterBeanList.get(i).getName());
        viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(this.context, R.color.white));
        viewHolder.round_image_view_filter_item.setImageBitmap(this.bitmaps.get(i));
        if (this.selectedIndex == i) {
            viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(this.context, R.color.black));
            viewHolder.text_view_filter_name.setBackgroundColor(ContextCompat.getColor(this.context, R.color.white));
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(this.context, R.color.white));
        viewHolder.text_view_filter_name.setBackgroundColor(ContextCompat.getColor(this.context, R.color.black));
        viewHolder.viewSelected.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return this.bitmaps.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View lockPro;
        RoundedImageView round_image_view_filter_item;
        TextView text_view_filter_name;
        View viewSelected;

        ViewHolder(View view) {
            super(view);
            this.round_image_view_filter_item = (RoundedImageView) view.findViewById(R.id.round_image_view_filter_item);
            this.text_view_filter_name = (TextView) view.findViewById(R.id.text_view_filter_name);
            this.viewSelected = view.findViewById(R.id.view_selected);
            this.lockPro = view.findViewById(R.id.img_lock_pro);
            view.setOnClickListener(view1 -> {
                FilterAdapter.this.selectedIndex = ViewHolder.this.getLayoutPosition();
                FilterAdapter.this.filterListener.onFilterSelected(FilterAdapter.this.selectedIndex, FilterAdapter.this.filterBeanList.get(FilterAdapter.this.selectedIndex).getCode());
                FilterAdapter.this.notifyDataSetChanged();
            });
        }
    }
}
