package com.relish.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.relish.app.Adapter.PhotoPagerAdapter;
import com.relish.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImagePagerFragment extends Fragment {
    public static final String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";
    public static final String ARG_PATH = "PATHS";
    private int currentItem = 0;
    private PhotoPagerAdapter mPagerAdapter;
    private ArrayList<String> paths;
    private ViewPager view_pager__photos;

    public static ImagePagerFragment newInstance(List<String> list, int i) {
        ImagePagerFragment imagePagerFragment = new ImagePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(ARG_PATH, (String[]) list.toArray(new String[list.size()]));
        bundle.putInt(ARG_CURRENT_ITEM, i);
        imagePagerFragment.setArguments(bundle);
        return imagePagerFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setPhotos(List<String> list, int i) {
        this.paths.clear();
        this.paths.addAll(list);
        this.currentItem = i;
        this.view_pager__photos.setCurrentItem(i);
        this.view_pager__photos.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.paths = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] stringArray = arguments.getStringArray(ARG_PATH);
            this.paths.clear();
            if (stringArray != null) {
                this.paths = new ArrayList<>(Arrays.asList(stringArray));
            }
            this.currentItem = arguments.getInt(ARG_CURRENT_ITEM);
        }
        this.mPagerAdapter = new PhotoPagerAdapter(Glide.with(this), this.paths,getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_image_pager, viewGroup, false);
        ViewPager viewPager = (ViewPager) inflate.findViewById(R.id.view_pager__photos);
        this.view_pager__photos = viewPager;
        viewPager.setAdapter(this.mPagerAdapter);
        this.view_pager__photos.setCurrentItem(this.currentItem);
        this.view_pager__photos.setOffscreenPageLimit(5);
        return inflate;
    }

    public ArrayList<String> getPaths() {
        return this.paths;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.paths.clear();
        this.paths = null;
        ViewPager viewPager = this.view_pager__photos;
        if (viewPager != null) {
            viewPager.setAdapter(null);
        }
    }
}
