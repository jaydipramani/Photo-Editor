package com.relish.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.relish.app.R;
import com.relish.app.draw.DrawModel;
import com.relish.app.listener.BrushMagicListener;

import java.util.ArrayList;
import java.util.List;

public class MagicBrushAdapter extends RecyclerView.Adapter<MagicBrushAdapter.ViewHolder> {
    public static List<DrawModel> drawBitmapModels = new ArrayList();
    public BrushMagicListener brushMagicListener;
    private Context context;
    public int selectedColorIndex;

    public MagicBrushAdapter(Context context2, BrushMagicListener brushMagicListener2) {
        this.context = context2;
        this.brushMagicListener = brushMagicListener2;
        drawBitmapModels = lstDrawBitmapModel(context2);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_brush, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.magicBrush.setImageResource(drawBitmapModels.get(i).getMainIcon());
        if (this.selectedColorIndex == i) {
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewSelected.setVisibility(View.GONE);
        }
    }

    public void setSelectedColorIndex(int i) {
        this.selectedColorIndex = i;
    }

    @Override
    public int getItemCount() {
        return drawBitmapModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View lockPro;
        RoundedImageView magicBrush;
        View viewSelected;

        ViewHolder(View view) {
            super(view);
            this.viewSelected = view.findViewById(R.id.view_selected);
            this.magicBrush = (RoundedImageView) view.findViewById(R.id.round_image_view_brush);
            this.lockPro = view.findViewById(R.id.img_lock_pro);
            this.magicBrush.setOnClickListener(view1 -> {
                MagicBrushAdapter.this.selectedColorIndex = ViewHolder.this.getLayoutPosition();
                MagicBrushAdapter.this.brushMagicListener.onMagicChanged(MagicBrushAdapter.this.selectedColorIndex, MagicBrushAdapter.drawBitmapModels.get(MagicBrushAdapter.this.selectedColorIndex));
                MagicBrushAdapter.this.notifyDataSetChanged();
            });
        }
    }

    public static List<DrawModel> lstDrawBitmapModel(Context context2) {
        List<DrawModel> list = drawBitmapModels;
        if (list != null && !list.isEmpty()) {
            return drawBitmapModels;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf((int) R.drawable.bg1));
        arrayList.add(Integer.valueOf((int) R.drawable.bg2));
        arrayList.add(Integer.valueOf((int) R.drawable.bg3));
        arrayList.add(Integer.valueOf((int) R.drawable.bg4));
        drawBitmapModels.add(new DrawModel(R.drawable.bg_icon, arrayList, false, context2));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(Integer.valueOf((int) R.drawable.bt1));
        arrayList2.add(Integer.valueOf((int) R.drawable.bt2));
        arrayList2.add(Integer.valueOf((int) R.drawable.bt3));
        arrayList2.add(Integer.valueOf((int) R.drawable.bt4));
        arrayList2.add(Integer.valueOf((int) R.drawable.bt5));
        arrayList2.add(Integer.valueOf((int) R.drawable.bt6));
        arrayList2.add(Integer.valueOf((int) R.drawable.bt7));
        arrayList2.add(Integer.valueOf((int) R.drawable.bt8));
        arrayList2.add(Integer.valueOf((int) R.drawable.bt9));
        drawBitmapModels.add(new DrawModel(R.drawable.bt_icon, arrayList2, true, context2));
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(Integer.valueOf((int) R.drawable.bb1));
        arrayList3.add(Integer.valueOf((int) R.drawable.bb2));
        arrayList3.add(Integer.valueOf((int) R.drawable.bb3));
        arrayList3.add(Integer.valueOf((int) R.drawable.bb4));
        drawBitmapModels.add(new DrawModel(R.drawable.bb_icon, arrayList3, true, context2));
        ArrayList arrayList11 = new ArrayList();
        arrayList11.add(Integer.valueOf((int) R.drawable.ba1));
        arrayList11.add(Integer.valueOf((int) R.drawable.ba2));
        arrayList11.add(Integer.valueOf((int) R.drawable.ba3));
        arrayList11.add(Integer.valueOf((int) R.drawable.ba4));
        arrayList11.add(Integer.valueOf((int) R.drawable.ba5));
        drawBitmapModels.add(new DrawModel(R.drawable.ba_icon, arrayList11, true, context2));
        ArrayList arrayList12 = new ArrayList();
        arrayList12.add(Integer.valueOf((int) R.drawable.b21));
        arrayList12.add(Integer.valueOf((int) R.drawable.b22));
        arrayList12.add(Integer.valueOf((int) R.drawable.b23));
        arrayList12.add(Integer.valueOf((int) R.drawable.b24));
        drawBitmapModels.add(new DrawModel(R.drawable.b26, arrayList12, true, context2));
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add(Integer.valueOf((int) R.drawable.bw1));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw2));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw3));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw4));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw5));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw6));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw7));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw8));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw9));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw10));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw11));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw12));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw13));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw14));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw15));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw16));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw17));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw18));
        arrayList4.add(Integer.valueOf((int) R.drawable.bw19));
        drawBitmapModels.add(new DrawModel(R.drawable.bw_icon, arrayList4, true, context2));
        ArrayList arrayList5 = new ArrayList();
        arrayList5.add(Integer.valueOf((int) R.drawable.bc1));
        arrayList5.add(Integer.valueOf((int) R.drawable.bc2));
        arrayList5.add(Integer.valueOf((int) R.drawable.bc3));
        arrayList5.add(Integer.valueOf((int) R.drawable.bc4));
        drawBitmapModels.add(new DrawModel(R.drawable.bc_icon, arrayList5, true, context2));
        ArrayList arrayList6 = new ArrayList();
        arrayList6.add(Integer.valueOf((int) R.drawable.be1));
        arrayList6.add(Integer.valueOf((int) R.drawable.be2));
        arrayList6.add(Integer.valueOf((int) R.drawable.be3));
        arrayList6.add(Integer.valueOf((int) R.drawable.be4));
        arrayList6.add(Integer.valueOf((int) R.drawable.be5));
        arrayList6.add(Integer.valueOf((int) R.drawable.be6));
        arrayList6.add(Integer.valueOf((int) R.drawable.be7));
        arrayList6.add(Integer.valueOf((int) R.drawable.be8));
        drawBitmapModels.add(new DrawModel(R.drawable.be_icon, arrayList6, true, context2));
        ArrayList arrayList7 = new ArrayList();
        arrayList7.add(Integer.valueOf((int) R.drawable.bh1));
        arrayList7.add(Integer.valueOf((int) R.drawable.bh2));
        arrayList7.add(Integer.valueOf((int) R.drawable.bh3));
        arrayList7.add(Integer.valueOf((int) R.drawable.bh4));
        arrayList7.add(Integer.valueOf((int) R.drawable.bh5));
        arrayList7.add(Integer.valueOf((int) R.drawable.bh6));
        arrayList7.add(Integer.valueOf((int) R.drawable.bh7));
        arrayList7.add(Integer.valueOf((int) R.drawable.bh8));
        drawBitmapModels.add(new DrawModel(R.drawable.bh_icon, arrayList7, true, context2));
        ArrayList arrayList8 = new ArrayList();
        arrayList8.add(Integer.valueOf((int) R.drawable.bd1));
        arrayList8.add(Integer.valueOf((int) R.drawable.bd2));
        arrayList8.add(Integer.valueOf((int) R.drawable.bd3));
        arrayList8.add(Integer.valueOf((int) R.drawable.bd4));
        arrayList8.add(Integer.valueOf((int) R.drawable.bd5));
        drawBitmapModels.add(new DrawModel(R.drawable.bd_icon, arrayList8, true, context2));
        ArrayList arrayList9 = new ArrayList();
        arrayList9.add(Integer.valueOf((int) R.drawable.bf1));
        arrayList9.add(Integer.valueOf((int) R.drawable.bf2));
        arrayList9.add(Integer.valueOf((int) R.drawable.bf3));
        arrayList9.add(Integer.valueOf((int) R.drawable.bf4));
        drawBitmapModels.add(new DrawModel(R.drawable.bf_icon, arrayList9, true, context2));
        ArrayList arrayList10 = new ArrayList();
        arrayList10.add(Integer.valueOf((int) R.drawable.bi1));
        arrayList10.add(Integer.valueOf((int) R.drawable.bi2));
        arrayList10.add(Integer.valueOf((int) R.drawable.bi3));
        arrayList10.add(Integer.valueOf((int) R.drawable.bi4));
        arrayList10.add(Integer.valueOf((int) R.drawable.bi5));
        arrayList10.add(Integer.valueOf((int) R.drawable.bi6));
        drawBitmapModels.add(new DrawModel(R.drawable.bi_icon, arrayList10, true, context2));
        ArrayList arrayList13 = new ArrayList();
        arrayList13.add(Integer.valueOf((int) R.drawable.bn1));
        arrayList13.add(Integer.valueOf((int) R.drawable.bn2));
        arrayList13.add(Integer.valueOf((int) R.drawable.bn3));
        arrayList13.add(Integer.valueOf((int) R.drawable.bn4));
        arrayList13.add(Integer.valueOf((int) R.drawable.bn5));
        drawBitmapModels.add(new DrawModel(R.drawable.bn_icon, arrayList13, true, context2));
        ArrayList arrayList14 = new ArrayList();
        arrayList14.add(Integer.valueOf((int) R.drawable.bk1));
        arrayList14.add(Integer.valueOf((int) R.drawable.bk2));
        arrayList14.add(Integer.valueOf((int) R.drawable.bk3));
        arrayList14.add(Integer.valueOf((int) R.drawable.bk4));
        arrayList14.add(Integer.valueOf((int) R.drawable.bk5));
        arrayList14.add(Integer.valueOf((int) R.drawable.bk6));
        arrayList14.add(Integer.valueOf((int) R.drawable.bk7));
        drawBitmapModels.add(new DrawModel(R.drawable.bk_icon, arrayList14, true, context2));
        ArrayList arrayList15 = new ArrayList();
        arrayList15.add(Integer.valueOf((int) R.drawable.bs1));
        arrayList15.add(Integer.valueOf((int) R.drawable.bs2));
        arrayList15.add(Integer.valueOf((int) R.drawable.bs3));
        arrayList15.add(Integer.valueOf((int) R.drawable.bs4));
        arrayList15.add(Integer.valueOf((int) R.drawable.bs5));
        arrayList15.add(Integer.valueOf((int) R.drawable.bs6));
        arrayList15.add(Integer.valueOf((int) R.drawable.bs7));
        arrayList15.add(Integer.valueOf((int) R.drawable.bs8));
        arrayList15.add(Integer.valueOf((int) R.drawable.bs9));
        arrayList15.add(Integer.valueOf((int) R.drawable.bs10));
        drawBitmapModels.add(new DrawModel(R.drawable.bs_icon, arrayList15, true, context2));
        ArrayList arrayList16 = new ArrayList();
        arrayList16.add(Integer.valueOf((int) R.drawable.bl1));
        arrayList16.add(Integer.valueOf((int) R.drawable.bl2));
        arrayList16.add(Integer.valueOf((int) R.drawable.bl3));
        arrayList16.add(Integer.valueOf((int) R.drawable.bl4));
        drawBitmapModels.add(new DrawModel(R.drawable.bl_icon, arrayList16, true, context2));
        ArrayList arrayList17 = new ArrayList();
        arrayList17.add(Integer.valueOf((int) R.drawable.br1));
        arrayList17.add(Integer.valueOf((int) R.drawable.br2));
        arrayList17.add(Integer.valueOf((int) R.drawable.br3));
        arrayList17.add(Integer.valueOf((int) R.drawable.br4));
        arrayList17.add(Integer.valueOf((int) R.drawable.br5));
        arrayList17.add(Integer.valueOf((int) R.drawable.br6));
        arrayList17.add(Integer.valueOf((int) R.drawable.br7));
        arrayList17.add(Integer.valueOf((int) R.drawable.br8));
        arrayList17.add(Integer.valueOf((int) R.drawable.br9));
        arrayList17.add(Integer.valueOf((int) R.drawable.br10));
        arrayList17.add(Integer.valueOf((int) R.drawable.br11));
        arrayList17.add(Integer.valueOf((int) R.drawable.br12));
        arrayList17.add(Integer.valueOf((int) R.drawable.br13));
        drawBitmapModels.add(new DrawModel(R.drawable.br_icon, arrayList17, true, context2));
        ArrayList arrayList18 = new ArrayList();
        arrayList18.add(Integer.valueOf((int) R.drawable.bm1));
        arrayList18.add(Integer.valueOf((int) R.drawable.bm2));
        arrayList18.add(Integer.valueOf((int) R.drawable.bm3));
        arrayList18.add(Integer.valueOf((int) R.drawable.bm4));
        arrayList18.add(Integer.valueOf((int) R.drawable.bm5));
        drawBitmapModels.add(new DrawModel(R.drawable.bm_icon, arrayList18, true, context2));
        ArrayList arrayList19 = new ArrayList();
        arrayList19.add(Integer.valueOf((int) R.drawable.bx1));
        arrayList19.add(Integer.valueOf((int) R.drawable.bx2));
        arrayList19.add(Integer.valueOf((int) R.drawable.bx3));
        arrayList19.add(Integer.valueOf((int) R.drawable.bx4));
        arrayList19.add(Integer.valueOf((int) R.drawable.bx5));
        arrayList19.add(Integer.valueOf((int) R.drawable.bx6));
        drawBitmapModels.add(new DrawModel(R.drawable.bx_icon, arrayList19, true, context2));
        return drawBitmapModels;
    }
}
