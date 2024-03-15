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

public class PolishDrawToolsAdapter extends RecyclerView.Adapter<PolishDrawToolsAdapter.ViewHolder> {
    public OnQuShotDrawItemSelected onQuShotDrawItemSelected;
    public List<ModuleModel> toolModelArrayList;

    public interface OnQuShotDrawItemSelected {
        void onQuShotDrawToolSelected(Module module);
    }

    public PolishDrawToolsAdapter(OnQuShotDrawItemSelected onItemSelected) {
        ArrayList arrayList = new ArrayList();
        this.toolModelArrayList = arrayList;
        this.onQuShotDrawItemSelected = onItemSelected;
        arrayList.add(new ModuleModel("Paint", R.drawable.ic_paint_black, Module.PAINT));
        this.toolModelArrayList.add(new ModuleModel("Magic", R.drawable.ic_colored, Module.MAGIC));
        this.toolModelArrayList.add(new ModuleModel("Neon", R.drawable.ic_neon, Module.NEON));
        this.toolModelArrayList.add(new ModuleModel("Mosaic", R.drawable.ic_mosaic, Module.MOSAIC));
    }

    public class ModuleModel {
        public int toolIcon;
        public String toolName;
        public Module toolType;

        ModuleModel(String str, int i, Module toolType2) {
            this.toolName = str;
            this.toolIcon = i;
            this.toolType = toolType2;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_effet_tool, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ModuleModel toolModel = this.toolModelArrayList.get(i);
        viewHolder.text_view_tool_name.setText(toolModel.toolName);
        viewHolder.image_view_tool_icon.setImageResource(toolModel.toolIcon);
    }

    @Override
    public int getItemCount() {
        return this.toolModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_tool_icon;
        RelativeLayout relative_layout_wrapper_tool;
        TextView text_view_tool_name;

        ViewHolder(View view) {
            super(view);
            this.image_view_tool_icon = (ImageView) view.findViewById(R.id.image_view_adjust_icon);
            this.text_view_tool_name = (TextView) view.findViewById(R.id.text_view_adjust_name);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.linear_layout_wrapper_adjust);
            this.relative_layout_wrapper_tool = relativeLayout;
            relativeLayout.setOnClickListener(new View.OnClickListener() {

                public final void onClick(View view) {
                    ViewHolder.this.draeToolClick(view);
                }
            });
        }

        public void draeToolClick(View view1) {
            PolishDrawToolsAdapter.this.onQuShotDrawItemSelected.onQuShotDrawToolSelected(PolishDrawToolsAdapter.this.toolModelArrayList.get(getLayoutPosition()).toolType);
        }
    }
}
