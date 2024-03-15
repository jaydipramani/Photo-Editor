package com.relish.app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.relish.app.R;
import com.relish.app.module.Module;

import java.util.ArrayList;
import java.util.List;

public class GridToolsAdapter extends RecyclerView.Adapter<GridToolsAdapter.ViewHolder> {
    public OnItemSelected mOnItemSelected;
    public List<ToolModel> mToolList;

    public interface OnItemSelected {
        void onToolSelected(Module module);
    }

    public GridToolsAdapter(OnItemSelected onItemSelected, boolean z) {
        ArrayList arrayList = new ArrayList();
        this.mToolList = arrayList;
        this.mOnItemSelected = onItemSelected;
        arrayList.add(new ToolModel("Collage", R.drawable.ic_collage2, Module.LAYER));
        this.mToolList.add(new ToolModel("Padding", R.drawable.ic_border, Module.PADDING));
        this.mToolList.add(new ToolModel("Ratio", R.drawable.ic_ratio, Module.RATIO));
        this.mToolList.add(new ToolModel("Text", R.drawable.ic_text, Module.TEXT));
        this.mToolList.add(new ToolModel("Background", R.drawable.ic_background2, Module.GRADIENT));
        this.mToolList.add(new ToolModel("Filter", R.drawable.ic_filter, Module.FILTER));
        this.mToolList.add(new ToolModel("Sticker", R.drawable.ic_sticker, Module.STICKER));
    }

    public class ToolModel {
        public int mToolIcon;
        public String mToolName;
        public Module mToolType;

        ToolModel(String str, int i, Module toolType) {
            this.mToolName = str;
            this.mToolIcon = i;
            this.mToolType = toolType;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collage_tool, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ToolModel toolModel = this.mToolList.get(i);
        viewHolder.text_view_tool_name.setText(toolModel.mToolName);
        viewHolder.image_view_tool_icon.setImageResource(toolModel.mToolIcon);
    }

    @Override
    public int getItemCount() {
        return this.mToolList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_tool_icon;
        RelativeLayout relative_layout_wrapper_tool;
        TextView text_view_tool_name;

        ViewHolder(View view) {
            super(view);
            this.image_view_tool_icon = (ImageView) view.findViewById(R.id.image_view_tool_icon);
            this.text_view_tool_name = (TextView) view.findViewById(R.id.text_view_tool_name);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout_wrapper_tool);
            this.relative_layout_wrapper_tool = relativeLayout;
            relativeLayout.setOnClickListener(new View.OnClickListener() {

                public final void onClick(View view) {
                    ViewHolder.this.GridToolsClick(view);
                }
            });
        }

        public void GridToolsClick(View view1) {
            GridToolsAdapter.this.mOnItemSelected.onToolSelected(GridToolsAdapter.this.mToolList.get(getLayoutPosition()).mToolType);
        }
    }
}
