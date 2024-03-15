package com.relish.app.Activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.view.PointerIconCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.relish.app.Ad.AdUtils;
import com.relish.app.R;
import com.relish.app.Utils.Helper;
import com.relish.app.dialog.DetailsDialog;
import com.relish.app.picker.ImageCaptureManager;
import com.relish.app.polish.PolishPickerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PolishHomeActivity extends PolishBaseActivity {
    public static Context contextApp;
    private LinearLayout settingLout;
    private ImageCaptureManager imageCaptureManager;
    String isFrom;
    View.OnClickListener onClickListener = new View.OnClickListener() {

        public final void onClick(View view) {
            PolishHomeActivity.this.viewClick(view);
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(bundle);
        setFullScreen();
        setContentView(R.layout.activity_polish_home);
        contextApp = getApplicationContext();

        findViewById(R.id.relative_layout_edit_photo).setOnClickListener(this.onClickListener);
        findViewById(R.id.relative_layout_background_remover).setOnClickListener(this.onClickListener);
        findViewById(R.id.relative_layout_drip_effect).setOnClickListener(this.onClickListener);
        findViewById(R.id.relative_layout_splliter).setOnClickListener(this.onClickListener);
        findViewById(R.id.relative_layout_collection).setOnClickListener(this.onClickListener);
        findViewById(R.id.relative_layout_edit_collage).setOnClickListener(this.onClickListener);
        findViewById(R.id.relative_layout_edit_camera).setOnClickListener(this.onClickListener);
        this.imageCaptureManager = new ImageCaptureManager(this);
        settingLout = findViewById(R.id.settingLout);
        settingLout.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishHomeActivity.this.openSettings(view);
            }
        });


        try {
            new URL(getResources().getString(R.string.policy_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void openSettings(View view) {
        startActivity(new Intent(this, PolishSettingsActivity.class));
    }


    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.relative_layout_edit_camera:
                Dexter.withContext(this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {

                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                    PolishHomeActivity.this.takePhotoFromCamera();
                                }
                                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                    DetailsDialog.showDetailsDialog(PolishHomeActivity.this);
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).withErrorListener(new PermissionRequestErrorListener() {

                    @Override
                    public final void onError(DexterError dexterError) {
                        PolishHomeActivity.this.permissionDenied(dexterError);
                    }
                }).onSameThread().check();
                return;
            case R.id.relative_layout_edit_collage:
                isFrom = "collage";
                displayInterstitial();

                return;
            case R.id.relative_layout_edit_photo:
                isFrom = "edit";
                displayInterstitial();

                return;
            case R.id.relative_layout_background_remover:
                goToEditBackGroundPhoto();
                return;
            case R.id.relative_layout_drip_effect:
                isFrom = "edit";

                goToEditEffectPhoto();
                return;
            case R.id.relative_layout_splliter:
                goToSplitPhoto();
                return;
            case R.id.relative_layout_collection:

                return;
            default:
                return;
        }
    }

    public void permissionDenied(DexterError dexterError) {
        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
    }

    private void goToEditBackGroundPhoto() {
        String[] arrPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 29) {
            arrPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        Dexter.withContext(this).withPermissions(arrPermissions).withListener(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    PolishPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).setForwardEditBackground(true).start(PolishHomeActivity.this);
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    DetailsDialog.showDetailsDialog(PolishHomeActivity.this);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {

            @Override
            public final void onError(DexterError dexterError) {
                PolishHomeActivity.this.permissionDenied(dexterError);
            }
        }).onSameThread().check();
    }
    private void goToSplitPhoto() {
        String[] arrPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 29) {
            arrPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        Dexter.withContext(this).withPermissions(arrPermissions).withListener(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    PolishPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).setForwardSplitter(true).start(PolishHomeActivity.this);
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    DetailsDialog.showDetailsDialog(PolishHomeActivity.this);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {

            @Override
            public final void onError(DexterError dexterError) {
                PolishHomeActivity.this.permissionDenied(dexterError);
            }
        }).onSameThread().check();
    }

    private void goToEditEffectPhoto() {
        String[] arrPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 29) {
            arrPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        Dexter.withContext(this).withPermissions(arrPermissions).withListener(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    PolishPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).setForwardEffects(true).start(PolishHomeActivity.this);
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    DetailsDialog.showDetailsDialog(PolishHomeActivity.this);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {

            @Override
            public final void onError(DexterError dexterError) {
                PolishHomeActivity.this.permissionDenied(dexterError);
            }
        }).onSameThread().check();
    }

    private void goToEditPhoto() {
        String[] arrPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 29) {
            arrPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        Dexter.withContext(this).withPermissions(arrPermissions).withListener(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    PolishPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).start(PolishHomeActivity.this);
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    DetailsDialog.showDetailsDialog(PolishHomeActivity.this);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {

            @Override
            public final void onError(DexterError dexterError) {
                PolishHomeActivity.this.permissionDenied(dexterError);
            }
        }).onSameThread().check();
    }

    private void goToCollage() {
        String[] arrPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 29) {
            arrPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        Dexter.withContext(this).withPermissions(arrPermissions).withListener(new MultiplePermissionsListener() {


            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    Intent intent = new Intent(PolishHomeActivity.this, GridPickerActivity.class);
                    intent.putExtra(GridPickerActivity.KEY_LIMIT_MAX_IMAGE, 9);
                    intent.putExtra(GridPickerActivity.KEY_LIMIT_MIN_IMAGE, 2);
                    PolishHomeActivity.this.startActivityForResult(intent, PointerIconCompat.TYPE_CONTEXT_MENU);
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    DetailsDialog.showDetailsDialog(PolishHomeActivity.this);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {

            @Override
            public final void onError(DexterError dexterError) {
                PolishHomeActivity.this.permissionDenied(dexterError);
            }
        }).onSameThread().check();
    }

    @Override
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, intent);
        } else if (requestCode == 1) {
            if (this.imageCaptureManager == null) {
                this.imageCaptureManager = new ImageCaptureManager(this);
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                public final void run() {
                    PolishHomeActivity.this.OpenGallery();
                }
            });
            Intent intent2 = new Intent(getApplicationContext(), PolishEditorActivity.class);
            intent2.putExtra(PolishPickerView.KEY_SELECTED_PHOTOS, this.imageCaptureManager.getCurrentPhotoPath());
            startActivity(intent2);
        }
    }

    public void OpenGallery() {
        this.imageCaptureManager.galleryAddPic();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void takePhotoFromCamera() {
        try {
            startActivityForResult(this.imageCaptureManager.dispatchTakePictureIntent(), 1);
        } catch (ActivityNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Helper.clean();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void displayInterstitial() {
        AdUtils.showAds_full(this, new AdUtils.OnFinishAds() {
            public void onFinishAds(boolean z) {
                if (z) {

                }
                passActivity();
            }
        });
    }

    private void passActivity() {
        if (isFrom.equalsIgnoreCase("collage")) {
            goToCollage();
        } else {
            goToEditPhoto();
        }
    }

    public static Context getAppContext() {
        return contextApp;
    }
}
