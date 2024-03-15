package com.relish.app.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.relish.app.R;
import com.relish.app.Utils.Const;
import com.relish.app.Utils.Pref;
import com.relish.app.constants.Constants;
import com.relish.app.picker.ImageCaptureManager;

import java.io.File;
import java.io.IOException;

public class PhotoShareActivity extends PolishBaseActivity implements View.OnClickListener {
    ImageCaptureManager createImageFile1;
    private File file;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setFullScreen();
        setContentView(R.layout.activity_share_photo);
        this.createImageFile1 = new ImageCaptureManager(this);

        this.file = new File(getIntent().getExtras().getString("path"));
        Glide.with(getApplicationContext()).load(this.file).into((ImageView) findViewById(R.id.image_view_preview));
        findViewById(R.id.image_view_preview).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PhotoShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PhotoShareActivity.this.onBackPressed();
            }
        });
        findViewById(R.id.imageViewHome).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Intent intent = new Intent(PhotoShareActivity.this, PolishHomeActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                PhotoShareActivity.this.startActivity(intent);
                PhotoShareActivity.this.finish();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();
            if (id != R.id.image_view_preview) {
                switch (id) {
                    case R.id.linearLayoutShareOne:
                        return;
                    case R.id.linear_layout_facebook:
                        sharephotosocialplateforms(Uri.parse(this.file.getAbsolutePath().toString()), Constants.FACEBOOK);
                        return;
                    case R.id.linear_layout_instagram:
                        sharephotosocialplateforms(Uri.parse(this.file.getAbsolutePath().toString()), Constants.INSTAGRAM);
                        return;
                    case R.id.linear_layout_messenger:
                        sharephotosocialplateforms(Uri.parse(this.file.getAbsolutePath().toString()), Constants.MESSEGER);
                        return;
                    case R.id.linear_layout_share_more:
                        Uri createCacheFile = createCacheFile();
                        if (createCacheFile != null) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.setDataAndType(createCacheFile, getContentResolver().getType(createCacheFile));
                            intent.putExtra(Intent.EXTRA_STREAM, createCacheFile);
                            startActivity(Intent.createChooser(intent, "Choose an app"));
                            return;
                        }
                        Toast.makeText(this, "Fail to sharing", Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        switch (id) {
                            case R.id.linear_layout_twitter:
                                sharephotosocialplateforms(Uri.parse(this.file.getAbsolutePath().toString()), Constants.TWITTER);
                                return;
                            case R.id.linear_layout_whatsapp:
                                sharephotosocialplateforms(Uri.parse(this.file.getAbsolutePath().toString()), Constants.WHATSAPP);
                                return;
                            default:
                                return;
                        }
                }
            } else {
                Intent intent4 = new Intent();
                intent4.setAction(Intent.ACTION_VIEW);
                intent4.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), this.file), "image/*");
                startActivity(intent4);
            }
        }
    }

    public void sharephotosocialplateforms(Uri contentUri, String packagestr) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.setType("image/*");
        shareIntent.setPackage(packagestr);
        if (packagestr.equals("com.instagram.android")) {
            shareIntent.setType("image/jpeg");
        }
        startActivity(shareIntent);
    }


    public void sharePhoto(String str) {
        if (isPackageInstalled(this, str)) {
            Uri createCacheFile = createCacheFile();
            if (createCacheFile != null) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setDataAndType(createCacheFile, getContentResolver().getType(createCacheFile));
                intent.putExtra(Intent.EXTRA_STREAM, createCacheFile);
                intent.setPackage(str);
                startActivity(intent);
                return;
            }
            Toast.makeText(this, "Fail to sharing", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Can't find this App, please download and try it again", Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent("android.intent.action.VIEW");
        intent2.setData(Uri.parse("market://details?id=" + str));
        startActivity(intent2);
    }

    public static boolean isPackageInstalled(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private Uri createCacheFile() {
        try {
            return Uri.fromFile(new File(this.createImageFile1.createImageFile().getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
