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

public class PolishToolsAdapter extends RecyclerView.Adapter<PolishToolsAdapter.ViewHolder> {
    public OnQuShotItemSelected onQuShotItemSelected;
    public List<ModuleModel> toolModelArrayList;

    public interface OnQuShotItemSelected {
        void onQuShotToolSelected(Module module);
    }

    public PolishToolsAdapter(OnQuShotItemSelected onItemSelected) {
        ArrayList arrayList = new ArrayList();
        this.toolModelArrayList = arrayList;
        this.onQuShotItemSelected = onItemSelected;
        arrayList.add(new ModuleModel("Crop", R.drawable.ic_crop, Module.CROP));
        this.toolModelArrayList.add(new ModuleModel("Filter", R.drawable.ic_filter, Module.FILTER));
        this.toolModelArrayList.add(new ModuleModel("Adjust", R.drawable.ic_set, Module.ADJUST));
        this.toolModelArrayList.add(new ModuleModel("HSL", R.drawable.ic_hsl, Module.HSL));
//        this.toolModelArrayList.add(new ModuleModel("Effect", R.drawable.ic_effect, Module.EFFECT));
        this.toolModelArrayList.add(new ModuleModel("Ratio", R.drawable.ic_ratio, Module.RATIO));
        this.toolModelArrayList.add(new ModuleModel("Text", R.drawable.ic_text, Module.TEXT));
        this.toolModelArrayList.add(new ModuleModel("Sticker", R.drawable.ic_sticker, Module.STICKER));
        this.toolModelArrayList.add(new ModuleModel("Blur", R.drawable.ic_blur, Module.BLURE));
        this.toolModelArrayList.add(new ModuleModel("Draw", R.drawable.ic_paint, Module.DRAW));
        this.toolModelArrayList.add(new ModuleModel("Mirror", R.drawable.ic_mirror, Module.MIRROR));
        this.toolModelArrayList.add(new ModuleModel("Frame", R.drawable.ic_frame, Module.BACKGROUND));
        this.toolModelArrayList.add(new ModuleModel("SQ/BG", R.drawable.ic_splash_square, Module.SQ_BG));
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
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_editing_tool, viewGroup, false));
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
            this.image_view_tool_icon = (ImageView) view.findViewById(R.id.image_view_tool_icon);
            this.text_view_tool_name = (TextView) view.findViewById(R.id.text_view_tool_name);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout_wrapper_tool);
            this.relative_layout_wrapper_tool = relativeLayout;
            relativeLayout.setOnClickListener(new View.OnClickListener() {

                public final void onClick(View view) {
                    ViewHolder.this.toolClick(view);
                }
            });
        }

        public  void toolClick(View view1) {
            PolishToolsAdapter.this.onQuShotItemSelected.onQuShotToolSelected(PolishToolsAdapter.this.toolModelArrayList.get(getLayoutPosition()).toolType);
        }
    }
}
