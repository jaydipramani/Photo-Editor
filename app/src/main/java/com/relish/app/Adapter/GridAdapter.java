package com.relish.app.Adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.relish.app.R;
import com.relish.app.layer.slant.NumberSlantLayout;
import com.relish.app.layer.straight.NumberStraightLayout;
import com.relish.app.polish.PolishSquareView;
import com.relish.app.polish.grid.PolishLayout;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
    private List<Bitmap> bitmapData = new ArrayList();
    private List<PolishLayout> layoutData = new ArrayList();
    public OnItemClickListener onItemClickListener;
    public int selectedIndex = 0;

    public interface OnItemClickListener {
        void onItemClick(PolishLayout polishLayout, int i);
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GridViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collage, viewGroup, false));
    }

    public void setSelectedIndex(int i) {
        this.selectedIndex = i;
    }

    public void onBindViewHolder(GridViewHolder collageViewHolder, final int i) {
        final PolishLayout collageLayout = this.layoutData.get(i);
        collageViewHolder.square_collage_view.setNeedDrawLine(true);
        collageViewHolder.square_collage_view.setNeedDrawOuterLine(true);
        collageViewHolder.square_collage_view.setTouchEnable(false);
        collageViewHolder.square_collage_view.setLineSize(6);
        collageViewHolder.square_collage_view.setQueShotLayout(collageLayout);
        if (this.selectedIndex == i) {
            collageViewHolder.square_collage_view.setBackgroundColor(Color.parseColor("#AFACE5"));
        } else {
            collageViewHolder.square_collage_view.setBackgroundColor(0);
        }
        collageViewHolder.itemView.setOnClickListener(view -> {
            if (GridAdapter.this.onItemClickListener != null) {
                int i1 = 0;
                PolishLayout polishLayout = collageLayout;
                if (polishLayout instanceof NumberSlantLayout) {
                    i1 = ((NumberSlantLayout) polishLayout).getTheme();
                } else if (polishLayout instanceof NumberStraightLayout) {
                    i1 = ((NumberStraightLayout) polishLayout).getTheme();
                }
                GridAdapter.this.onItemClickListener.onItemClick(collageLayout, i1);
            }
            GridAdapter.this.selectedIndex = i;
            GridAdapter.this.notifyDataSetChanged();
        });
        List<Bitmap> list = this.bitmapData;
        if (list != null) {
            int size = list.size();
            if (collageLayout.getAreaCount() > size) {
                for (int i2 = 0; i2 < collageLayout.getAreaCount(); i2++) {
                    collageViewHolder.square_collage_view.addQuShotCollage(this.bitmapData.get(i2 % size));
                }
                return;
            }
            collageViewHolder.square_collage_view.addPieces(this.bitmapData);
        }
    }

    @Override
    public int getItemCount() {
        List<PolishLayout> list = this.layoutData;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void refreshData(List<PolishLayout> list, List<Bitmap> list2) {
        this.layoutData = list;
        this.bitmapData = list2;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        PolishSquareView square_collage_view;

        public GridViewHolder(View view) {
            super(view);
            this.square_collage_view = (PolishSquareView) view.findViewById(R.id.squareCollageView);
        }
    }
}
