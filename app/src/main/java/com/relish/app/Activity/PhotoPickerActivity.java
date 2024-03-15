package com.relish.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.relish.app.Ad.AdUtils;
import com.relish.app.Fragment.ImagePagerFragment;
import com.relish.app.Fragment.PhotoPickerFragment;
import com.relish.app.R;
import com.relish.app.entity.Photo;
import com.relish.app.eraser.StickerEraseActivity;
import com.relish.app.event.OnItemCheckListener;
import com.relish.app.layout.NeonLayout;
import com.relish.app.polish.PolishPickerView;
import com.relish.app.support.Constants;

import java.io.File;
import java.util.ArrayList;

public class PhotoPickerActivity extends AppCompatActivity {
    private boolean forwardMain;
    private boolean forwardBackgroundRemover;
    private boolean forwardEffects;
    private boolean forwardSplitter;
    private ImagePagerFragment imagePagerFragment;
    private int maxCount = 9;
    private ArrayList<String> originalPhotos = null;
    private PhotoPickerFragment pickerFragment;
    private boolean showGif = false;
    Toolbar toolbar;

    public PhotoPickerActivity getActivity() {
        return this;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        boolean booleanExtra = getIntent().getBooleanExtra(PolishPickerView.EXTRA_SHOW_CAMERA, true);
        boolean booleanExtra2 = getIntent().getBooleanExtra(PolishPickerView.EXTRA_SHOW_GIF, false);
        boolean booleanExtra3 = getIntent().getBooleanExtra(PolishPickerView.EXTRA_PREVIEW_ENABLED, true);
        this.forwardMain = getIntent().getBooleanExtra(PolishPickerView.MAIN_ACTIVITY, false);
        this.forwardBackgroundRemover = getIntent().getBooleanExtra(PolishPickerView.BACK_GROUND_REMOVER, false);
        this.forwardEffects = getIntent().getBooleanExtra(PolishPickerView.EFFECTS, false);
        this.forwardSplitter = getIntent().getBooleanExtra(PolishPickerView.SPLITTER, false);
        setShowGif(booleanExtra2);
        setContentView(R.layout.activity_photo_picker);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.back);
        if (Build.VERSION.SDK_INT >= 21) {
            supportActionBar.setElevation(25.0f);
        }

        AdUtils.ShowAmBanner(this, (LinearLayout) findViewById(R.id.ll_banneradmidview), (LinearLayout) findViewById(R.id.lnr_ads));

        this.maxCount = getIntent().getIntExtra(PolishPickerView.EXTRA_MAX_COUNT, 9);
        int intExtra = getIntent().getIntExtra(PolishPickerView.EXTRA_GRID_COLUMN, 4);
        this.originalPhotos = getIntent().getStringArrayListExtra(PolishPickerView.EXTRA_ORIGINAL_PHOTOS);
        PhotoPickerFragment photoPickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        this.pickerFragment = photoPickerFragment;
        if (photoPickerFragment == null) {
            this.pickerFragment = PhotoPickerFragment.newInstance(booleanExtra, booleanExtra2, booleanExtra3, intExtra, this.maxCount, this.originalPhotos);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, this.pickerFragment, "tag").commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        this.pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {

            @Override
            public final boolean onItemCheck(int i, Photo photo, int i2) {
                if (PhotoPickerActivity.this.forwardBackgroundRemover){
                    photo.getPath();
                    Log.e("TAG", "onItemCheck: "+photo.getPath() );
//                    StickerEraseActivity.b = selectedBitmap;
                    File imgFile = new  File(photo.getPath());
                    if(imgFile.exists()){
                        StickerEraseActivity.b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    }
                    Intent intent = new Intent(PhotoPickerActivity.this, StickerEraseActivity.class);
                    intent.putExtra(Constants.KEY_OPEN_FROM, Constants.VALUE_OPEN_FROM_BACKGROUND_REMOVER);
                    startActivity(intent);
                    finish();
                    return true;
                }
                if (PhotoPickerActivity.this.forwardEffects){
                    Intent intent = new Intent(PhotoPickerActivity.this, PolishEditorActivity.class);
                    intent.putExtra(PolishPickerView.KEY_SELECTED_PHOTOS, photo.getPath());
                    intent.putExtra(PolishPickerView.EFFECTS, true);
                    PhotoPickerActivity.this.startActivity(intent);
                    PhotoPickerActivity.this.finish();
                    return true;
                }
                if (PhotoPickerActivity.this.forwardSplitter){
                    Intent intent = new Intent(PhotoPickerActivity.this, SpliterActivity.class);
                    intent.putExtra(PolishPickerView.KEY_SELECTED_PHOTOS, photo.getPath());
                    PhotoPickerActivity.this.startActivity(intent);
                    PhotoPickerActivity.this.finish();
                    return true;
                }
                if (!PhotoPickerActivity.this.forwardMain) {
                    Intent intent = new Intent(PhotoPickerActivity.this, PolishEditorActivity.class);
                    intent.putExtra(PolishPickerView.KEY_SELECTED_PHOTOS, photo.getPath());
                    PhotoPickerActivity.this.startActivity(intent);
                    PhotoPickerActivity.this.finish();
                    return true;
                }
                PolishCollageActivity.getQueShotGridActivityInstance().replaceCurrentPiece(photo.getPath());
                PhotoPickerActivity.this.finish();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        ImagePagerFragment imagePagerFragment2 = this.imagePagerFragment;
        if (imagePagerFragment2 == null || !imagePagerFragment2.isVisible()) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setShowGif(boolean z) {
        this.showGif = z;
    }
}
