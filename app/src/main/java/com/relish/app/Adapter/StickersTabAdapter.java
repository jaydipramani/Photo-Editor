package com.relish.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.relish.app.R;


public class StickersTabAdapter extends RecyclerTabLayout.Adapter<StickersTabAdapter.ViewHolder> {
    private Context context;
    private PagerAdapter mAdapater = this.mViewPager.getAdapter();

    public StickersTabAdapter(ViewPager viewPager, Context context2) {
        super(viewPager);
        this.context = context2;
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tab_sticker, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        switch (i) {
            case 0:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.bubble));
                break;
            case 1:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.cartoon));
                break;
            case 2:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.child));
                break;
            case 3:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.flower));
                break;
            case 4:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.amoji));
                break;
            case 5:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.delicious));
                break;
            case 6:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.newyear));
                break;
            case 7:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.popular));
                break;
            case 8:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.emoj));
                break;
            case 9:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.rage));
                break;
            case 10:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.christmas));
                break;
            case 11:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.unicorn));
                break;
            case 12:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.sticker));
                break;
            case 13:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.plant));
                break;
            case 14:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.birthday));
                break;
            case 15:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.loveday));
                break;
            case 16:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.chicken));
                break;
            case 17:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.textneon));
                break;
            case 18:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.thuglife));
                break;
            case 19:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.sweet));
                break;
            case 20:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.celebrate));
                break;
            case 21:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.happy));
                break;
            case 22:
                viewHolder.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.textcolor));
                break;
        }
        viewHolder.imageView.setSelected(i == getCurrentIndicatorPosition());
    }

    @Override 
    public int getItemCount() {
        return this.mAdapater.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(view1 -> StickersTabAdapter.this.getViewPager().setCurrentItem(ViewHolder.this.getAdapterPosition()));
        }
    }
}
