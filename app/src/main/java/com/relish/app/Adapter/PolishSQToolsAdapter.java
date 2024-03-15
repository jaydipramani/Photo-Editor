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

public class PolishSQToolsAdapter extends RecyclerView.Adapter<PolishSQToolsAdapter.ViewHolder> {
    public OnQuShotSQItemSelected onQuShotSplashItemSelected;
    public List<ModuleModel> toolModelArrayList;

    public interface OnQuShotSQItemSelected {
        void onQuShotSQToolSelected(Module module);
    }

    public PolishSQToolsAdapter(OnQuShotSQItemSelected onItemSelected) {
        ArrayList arrayList = new ArrayList();
        this.toolModelArrayList = arrayList;
        this.onQuShotSplashItemSelected = onItemSelected;
        arrayList.add(new ModuleModel("Splash BG", R.drawable.ic_splash_icon, Module.SPLASH_BG));
        this.toolModelArrayList.add(new ModuleModel("Sketch BG", R.drawable.ic_sketch_bg, Module.SKETCH_BG));
        this.toolModelArrayList.add(new ModuleModel("Blur BG", R.drawable.ic_blur_bg, Module.BLUR_BG));
        this.toolModelArrayList.add(new ModuleModel("Splash SQ", R.drawable.ic_splash_sq, Module.SPLASH_SQ));
        this.toolModelArrayList.add(new ModuleModel("Sketch SQ", R.drawable.ic_sketck_sq, Module.SKETCH_SQ));
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
            relativeLayout.setOnClickListener(view1 -> ViewHolder.this.onClickTools(view1));
        }

        public  void onClickTools(View view1) {
            PolishSQToolsAdapter.this.onQuShotSplashItemSelected.onQuShotSQToolSelected(PolishSQToolsAdapter.this.toolModelArrayList.get(getLayoutPosition()).toolType);
        }
    }
}
