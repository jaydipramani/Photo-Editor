package com.relish.app.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.relish.app.R;

import java.util.ArrayList;


public class SmallImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Bitmap> smallimages;
    private int imageWidth, imageHeight;
    boolean borderShowing;

    public SmallImageAdapter(Context c, ArrayList<Bitmap> images,boolean border){

        mContext = c;

        smallimages = images;

        imageWidth = images.get(0).getWidth();

        imageHeight = images.get(0).getHeight();

        borderShowing=border;

    }


    public int getCount() {

        return smallimages.size();

    }

    public Object getItem(int position) {

        return smallimages.get(position);

    }

    public long getItemId(int position) {

        return position;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_layout, null);
        }
        ImageView imageView = convertView.findViewById(R.id.imageView);
        imageView.setImageBitmap(smallimages.get(position));
        TextView number = convertView.findViewById(R.id.number);
        ConstraintLayout constraintSplit=convertView.findViewById(R.id.constraint_split);
        if (borderShowing) {
            constraintSplit.setBackground(mContext.getResources().getDrawable(R.drawable.back_border));
            number.setText(String.valueOf(position + 1));
            number.setVisibility(View.VISIBLE);
        }else {
            number.setVisibility(View.GONE);
            constraintSplit.setBackground(null);

        }
        return convertView;
    }

    public void SetBoarder(boolean status){
        borderShowing=status;
        notifyDataSetChanged();
    }

}

