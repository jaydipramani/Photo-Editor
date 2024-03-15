package com.relish.app.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.relish.app.Adapter.DirectoryListAdapter;
import com.relish.app.Adapter.PhotoGridAdapter;
import com.relish.app.R;
import com.relish.app.entity.Photo;
import com.relish.app.entity.PhotoDirectory;
import com.relish.app.picker.AndroidLifecycleUtils;
import com.relish.app.picker.ImageCaptureManager;
import com.relish.app.picker.MediaStoreHelper;
import com.relish.app.picker.PermissionsUtils;
import com.relish.app.polish.PolishPickerView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoPickerFragment extends Fragment {
    public static int COUNT_MAX = 4;
    private static final String EXTRA_CAMERA = "camera";
    private static final String EXTRA_COLUMN = "column";
    private static final String EXTRA_COUNT = "count";
    private static final String EXTRA_GIF = "gif";
    private static final String EXTRA_ORIGIN = "origin";
    public int SCROLL_THRESHOLD = 30;
    private ImageCaptureManager captureManager;
    int column;
    public List<PhotoDirectory> directories;
    private DirectoryListAdapter listAdapter;
    public ListPopupWindow listPopupWindow;
    public RequestManager mGlideRequestManager;
    private ArrayList<String> originalPhotos;
    public PhotoGridAdapter photoGridAdapter;

    public static PhotoPickerFragment newInstance(boolean z, boolean z2, boolean z3, int i, int i2, ArrayList<String> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_CAMERA, z);
        bundle.putBoolean(EXTRA_GIF, z2);
        bundle.putBoolean(PolishPickerView.EXTRA_PREVIEW_ENABLED, z3);
        bundle.putInt("column", i);
        bundle.putInt("count", i2);
        bundle.putStringArrayList("origin", arrayList);
        PhotoPickerFragment photoPickerFragment = new PhotoPickerFragment();
        photoPickerFragment.setArguments(bundle);
        return photoPickerFragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        this.mGlideRequestManager = Glide.with(this);
        this.directories = new ArrayList();
        this.originalPhotos = getArguments().getStringArrayList("origin");
        this.column = getArguments().getInt("column", 4);
        boolean z = getArguments().getBoolean(EXTRA_CAMERA, true);
        boolean z2 = getArguments().getBoolean(PolishPickerView.EXTRA_PREVIEW_ENABLED, true);
        PhotoGridAdapter photoGridAdapter2 = new PhotoGridAdapter(getActivity(), this.mGlideRequestManager, this.directories, this.originalPhotos, this.column);
        this.photoGridAdapter = photoGridAdapter2;
        photoGridAdapter2.setShowCamera(z);
        this.photoGridAdapter.setPreviewEnable(z2);
        this.listAdapter = new DirectoryListAdapter(this.mGlideRequestManager, this.directories);
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean(PolishPickerView.EXTRA_SHOW_GIF, getArguments().getBoolean(EXTRA_GIF));
        MediaStoreHelper.getPhotoDirs(getActivity(), bundle2, new MediaStoreHelper.PhotosResultCallback() {


            @Override
            public final void onResultCallback(List list) {
                PhotoPickerFragment.this.directories.clear();
                PhotoPickerFragment.this.directories.addAll(list);
                PhotoPickerFragment.this.photoGridAdapter.notifyDataSetChanged();
                PhotoPickerFragment.this.listAdapter.notifyDataSetChanged();
                PhotoPickerFragment.this.adjustHeight();
            }
        });
        this.captureManager = new ImageCaptureManager(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_photo_picker, viewGroup, false);
        RecyclerView recycler_view_photos = (RecyclerView) inflate.findViewById(R.id.recycler_view_photos);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(this.column, 1);
        staggeredGridLayoutManager.setGapStrategy(2);
        recycler_view_photos.setLayoutManager(staggeredGridLayoutManager);
        recycler_view_photos.setAdapter(this.photoGridAdapter);
        recycler_view_photos.setItemAnimator(new DefaultItemAnimator());
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.linear_layout_wrapper_folder);
        final ImageView image_view_icon = (ImageView) inflate.findViewById(R.id.image_view_icon);
        final TextView text_view_folder = (TextView) inflate.findViewById(R.id.text_view_folder);
        ListPopupWindow listPopupWindow2 = new ListPopupWindow(getActivity());
        this.listPopupWindow = listPopupWindow2;
        listPopupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {


            public void onDismiss() {
                image_view_icon.setImageResource(R.drawable.ic_arrow_up);
            }
        });
        this.listPopupWindow.setWidth(-1);
        this.listPopupWindow.setAnchorView(linearLayout);
        this.listPopupWindow.setAdapter(this.listAdapter);
        this.listPopupWindow.setModal(true);
        this.listPopupWindow.setDropDownGravity(80);
        this.listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                PhotoPickerFragment.this.listPopupWindow.dismiss();
                text_view_folder.setText(PhotoPickerFragment.this.directories.get(i).getName());
                PhotoPickerFragment.this.photoGridAdapter.setCurrentDirectoryIndex(i);
                PhotoPickerFragment.this.photoGridAdapter.notifyDataSetChanged();
            }
        });
        this.photoGridAdapter.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                if (PermissionsUtils.checkCameraPermission(PhotoPickerFragment.this) && PermissionsUtils.checkWriteStoragePermission(PhotoPickerFragment.this)) {
                    PhotoPickerFragment.this.openCamera();
                }
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (PhotoPickerFragment.this.listPopupWindow.isShowing()) {
                    PhotoPickerFragment.this.listPopupWindow.dismiss();
                    image_view_icon.setImageResource(R.drawable.ic_arrow_up);
                } else if (!PhotoPickerFragment.this.getActivity().isFinishing()) {
                    PhotoPickerFragment.this.adjustHeight();
                    image_view_icon.setImageResource(R.drawable.ic_arrow_up);
                    PhotoPickerFragment.this.listPopupWindow.show();
                }
            }
        });
        recycler_view_photos.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (Math.abs(i2) > PhotoPickerFragment.this.SCROLL_THRESHOLD) {
                    PhotoPickerFragment.this.mGlideRequestManager.pauseRequests();
                } else {
                    PhotoPickerFragment.this.resumeRequestsIfNotDestroyed();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                if (i == 0) {
                    PhotoPickerFragment.this.resumeRequestsIfNotDestroyed();
                }
            }
        });
        return inflate;
    }

    public void openCamera() {
        try {
            startActivityForResult(this.captureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e2) {
            Log.e("PhotoPickerFragment", "No Activity Found to handle Intent", e2);
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == -1) {
            if (this.captureManager == null) {
                this.captureManager = new ImageCaptureManager(getActivity());
            }
            this.captureManager.galleryAddPic();
            if (this.directories.size() > 0) {
                String currentPhotoPath = this.captureManager.getCurrentPhotoPath();
                PhotoDirectory photoDirectory = this.directories.get(0);
                photoDirectory.getPhotos().add(0, new Photo(currentPhotoPath.hashCode(), currentPhotoPath));
                photoDirectory.setCoverPath(currentPhotoPath);
                this.photoGridAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (iArr.length > 0 && iArr[0] == 0) {
            if ((i == 1 || i == 3) && PermissionsUtils.checkWriteStoragePermission(this) && PermissionsUtils.checkCameraPermission(this)) {
                openCamera();
            }
        }
    }

    public PhotoGridAdapter getPhotoGridAdapter() {
        return this.photoGridAdapter;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        this.captureManager.onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onViewStateRestored(Bundle bundle) {
        this.captureManager.onRestoreInstanceState(bundle);
        super.onViewStateRestored(bundle);
    }

    public void adjustHeight() {
        DirectoryListAdapter directoryListAdapter = this.listAdapter;
        if (directoryListAdapter != null) {
            int count = directoryListAdapter.getCount();
            if (count >= COUNT_MAX) {
                count = COUNT_MAX;
            }
            ListPopupWindow listPopupWindow2 = this.listPopupWindow;
            if (listPopupWindow2 != null) {
                listPopupWindow2.setHeight(getResources().getDimensionPixelOffset(R.dimen.picker_item_directory_height) * count);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        List<PhotoDirectory> list = this.directories;
        if (list != null) {
            for (PhotoDirectory next : list) {
                next.getPhotoPaths().clear();
                next.getPhotos().clear();
                next.setPhotos(null);
            }
            this.directories.clear();
            this.directories = null;
        }
    }

    public void resumeRequestsIfNotDestroyed() {
        if (AndroidLifecycleUtils.canLoadImage(this)) {
            this.mGlideRequestManager.resumeRequests();
        }
    }
}
