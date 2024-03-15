package com.relish.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.relish.app.R;

import java.util.ArrayList;
import java.util.List;

public class Mirror2DAdapter extends RecyclerView.Adapter<Mirror2DAdapter.ViewHolder> {
    private Context context;
    public Mirror2Listener frameListener;
    public int selectedSquareIndex;
    public List<SquareView> squareViewList = new ArrayList();

    public interface Mirror2Listener {
        void onBackgroundSelected(SquareView squareView);
    }

    public Mirror2DAdapter(Context context2, Mirror2Listener frameListener2) {
        this.context = context2;
        this.frameListener = frameListener2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mirror, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.squareView.setBackgroundResource(this.squareViewList.get(i).drawableId);
        if (this.selectedSquareIndex == i) {
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewSelected.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.squareViewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView squareView;
        View viewSelected;
        public ConstraintLayout wrapSquareView;

        public ViewHolder(View view) {
            super(view);
            this.squareView = (ImageView) view.findViewById(R.id.square_view);
            this.viewSelected = view.findViewById(R.id.view_selected);
            this.wrapSquareView = (ConstraintLayout) view.findViewById(R.id.constraint_layout_wrapper_square_view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            Mirror2DAdapter.this.selectedSquareIndex = getAdapterPosition();
            Mirror2DAdapter.this.frameListener.onBackgroundSelected(Mirror2DAdapter.this.squareViewList.get(Mirror2DAdapter.this.selectedSquareIndex));
            Mirror2DAdapter.this.notifyDataSetChanged();
        }
    }

    public class SquareView {
        public int drawableId;
        public String text;

        SquareView(int i, String string) {
            this.drawableId = i;
            this.text = string;
        }

        SquareView(int i, String string, boolean z) {
            this.drawableId = i;
            this.text = string;
        }
    }
}
