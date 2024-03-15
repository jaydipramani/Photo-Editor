package com.relish.app.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.net.MailTo;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.relish.app.Ad.AdUtils;
import com.relish.app.R;


public class PolishSettingsActivity extends AppCompatActivity {
    public LinearLayout relativeLayoutApps;
    public LinearLayout relativeLayoutFeedBack;
    public LinearLayout relativeLayoutPrivacy;
    public LinearLayout relativeLayoutRate;
    public LinearLayout relativeLayoutShare;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_polish_settings);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setTitle("Settings");

        AdUtils.ShowAmBanner(this, (LinearLayout) findViewById(R.id.ll_banneradmidview), (LinearLayout) findViewById(R.id.lnr_ads));

        this.relativeLayoutShare = (LinearLayout) findViewById(R.id.linearLayoutShare);
        this.relativeLayoutRate = (LinearLayout) findViewById(R.id.linearLayoutRate);
        this.relativeLayoutFeedBack = (LinearLayout) findViewById(R.id.linearLayoutFeedback);
        this.relativeLayoutPrivacy = (LinearLayout) findViewById(R.id.linearLayoutPrivacy);
        this.relativeLayoutApps = (LinearLayout) findViewById(R.id.linearLayoutApps);
        this.relativeLayoutShare.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishSettingsActivity.this.shareApp(view);
            }
        });
        this.relativeLayoutRate.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishSettingsActivity.this.rateApp(view);
            }
        });
        this.relativeLayoutFeedBack.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishSettingsActivity.this.feedback(view);
            }
        });
        this.relativeLayoutPrivacy.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishSettingsActivity.this.privacyPolicy(view);
            }
        });
        this.relativeLayoutApps.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d("qq", "moreApp");
                try {
                    PolishSettingsActivity polishSettingsActivity = PolishSettingsActivity.this;
                    polishSettingsActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + PolishSettingsActivity.this.getString(R.string.developer_account_link))));
                } catch (ActivityNotFoundException e) {
                    PolishSettingsActivity polishSettingsActivity2 = PolishSettingsActivity.this;
                    polishSettingsActivity2.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=" + PolishSettingsActivity.this.getString(R.string.developer_account_link))));
                }
            }
        });
    }

    public void shareApp(View view) {
        Intent myapp = new Intent(Intent.ACTION_SEND);
        myapp.setType("text/plain");
        myapp.putExtra(Intent.EXTRA_TEXT, "Download this awesome app\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n");
        startActivity(myapp);
    }

    public void rateApp(View view) {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getPackageName())));
        }
    }

    public void feedback(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
        boolean isIntentSafe = true;
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email_feedback)});
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        if (emailIntent.resolveActivity(getPackageManager()) == null) {
            isIntentSafe = false;
        }
        if (isIntentSafe) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, (int) R.string.email_app_not_installed, Toast.LENGTH_SHORT).show();
        }
    }



    public void privacyPolicy(View view) {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.policy_url))));
        } catch (Exception e) {
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
