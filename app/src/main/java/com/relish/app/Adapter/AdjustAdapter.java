package com.relish.app.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.relish.app.R;
import com.relish.app.listener.AdjustListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AdjustAdapter extends RecyclerView.Adapter<AdjustAdapter.ViewHolder> {
    public String ADJUST = "@adjust brightness {0} @adjust contrast {1} @adjust saturation {2} @vignette {3} 0.7 @adjust sharpen {4} 1 @adjust whitebalance {5} 1 @adjust hue {6} 1 @adjust exposure {7} 1";
    public AdjustListener adjustListener;
    public List<AdjustModel> adjustModelList;
    private Context context;
    public int selectedIndex = 0;

    public AdjustAdapter(Context context2, AdjustListener adjustListener2) {
        this.context = context2;
        this.adjustListener = adjustListener2;
        init();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adjust, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.text_view_adjust_name.setText(this.adjustModelList.get(i).name);
        ImageView imageView = viewHolder.image_view_adjust_icon;
        int i2 = this.selectedIndex;
        imageView.setImageDrawable(this.adjustModelList.get(i).icon);
        if (this.selectedIndex == i) {
            viewHolder.text_view_adjust_name.setTextColor(ContextCompat.getColor(this.context, R.color.white));
            viewHolder.image_view_adjust_icon.setColorFilter(ContextCompat.getColor(this.context, R.color.black));
            return;
        }
        viewHolder.text_view_adjust_name.setTextColor(ContextCompat.getColor(this.context, R.color.gray));
        viewHolder.image_view_adjust_icon.setColorFilter(ContextCompat.getColor(this.context, R.color.gray));
    }

    @Override
    public int getItemCount() {
        return this.adjustModelList.size();
    }

    public String getFilterConfig() {
        String adjust = this.ADJUST;
        return MessageFormat.format(adjust, this.adjustModelList.get(0).originValue + "", this.adjustModelList.get(1).originValue + "", this.adjustModelList.get(2).originValue + "", this.adjustModelList.get(3).originValue + "", this.adjustModelList.get(4).originValue + "", this.adjustModelList.get(5).originValue + "", this.adjustModelList.get(6).originValue + "", Float.valueOf(this.adjustModelList.get(7).originValue));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_adjust_icon;
        TextView text_view_adjust_name;

        ViewHolder(View view) {
            super(view);
            this.image_view_adjust_icon = (ImageView) view.findViewById(R.id.image_view_adjust_icon);
            this.text_view_adjust_name = (TextView) view.findViewById(R.id.text_view_adjust_name);
            view.setOnClickListener(new View.OnClickListener() {

                public final void onClick(View view) {
                    ViewHolder.this.lambda$new$0$AdjustAdapter$ViewHolder(view);
                }
            });
        }

        public void lambda$new$0$AdjustAdapter$ViewHolder(View view1) {
            AdjustAdapter.this.selectedIndex = getLayoutPosition();
            AdjustAdapter.this.adjustListener.onAdjustSelected(AdjustAdapter.this.adjustModelList.get(AdjustAdapter.this.selectedIndex));
            AdjustAdapter.this.notifyDataSetChanged();
        }
    }

    public AdjustModel getCurrentAdjustModel() {
        return this.adjustModelList.get(this.selectedIndex);
    }

    private void init() {
        ArrayList arrayList = new ArrayList();
        this.adjustModelList = arrayList;
        arrayList.add(new AdjustModel(this.context.getString(R.string.brightness), "brightness", this.context.getResources().getDrawable(R.drawable.ic_brightness), -1.0f, 0.0f, 1.0f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.contrast), "contrast", this.context.getResources().getDrawable(R.drawable.ic_contrast), 0.5f, 1.0f, 1.5f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.saturation), "saturation", this.context.getResources().getDrawable(R.drawable.ic_saturation), 0.0f, 1.0f, 2.0f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.vignette), "vignette", this.context.getResources().getDrawable(R.drawable.ic_vignette), 0.0f, 0.6f, 0.6f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.sharpen), "sharpen", this.context.getResources().getDrawable(R.drawable.ic_sharpen), 0.0f, 0.0f, 10.0f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.whitebalance), "whitebalance", this.context.getResources().getDrawable(R.drawable.ic_white_balance), -1.0f, 0.0f, 1.0f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.hue), "hue", this.context.getResources().getDrawable(R.drawable.ic_hue), -2.0f, 0.0f, 2.0f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.exposure), "exposure", this.context.getResources().getDrawable(R.drawable.ic_exposure), -2.0f, 0.0f, 2.0f));
    }

    public class AdjustModel {
        String code;
        Drawable icon;
        public float maxValue;
        public float minValue;
        String name;
        public float originValue;

        AdjustModel(String name2, String code2, Drawable icon2, float minValue2, float originalValue, float maxValue2) {
            this.name = name2;
            this.code = code2;
            this.icon = icon2;
            this.minValue = minValue2;
            this.originValue = originalValue;
            this.maxValue = maxValue2;
        }
    }
}
