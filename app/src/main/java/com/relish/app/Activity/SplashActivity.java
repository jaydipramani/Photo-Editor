package com.relish.app.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.MobileAds;
import com.relish.app.R;
import com.relish.app.Utils.Const;

import com.relish.app.Utils.Pref;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    Activity activity;
    ProgressBar progressBar;
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);
        activity = this;

        MobileAds.initialize(this);
//        showAd("https://relish-2b113-default-rtdb.firebaseio.com/ShowAd");

        progressBar=findViewById(R.id.progressbar);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom_delay);
        findViewById(R.id.imageViewLogo).startAnimation(animation);
        findViewById(R.id.textViewTitle).startAnimation(animation1);
//        Helper.initInstance(getApplicationContext());

        new CountDownTimer(3000, 12) {

            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(counter);
                counter++;
            }

            public void onFinish() {
                startActivity();
            }

        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    public void startActivity() {
        startActivity(new Intent(this, PolishHomeActivity.class));
        finish();
    }

//    public void showAd(String showAd) {
//        Firebase.setAndroidContext(this);
//        Firebase firebase = new Firebase(showAd);
//        firebase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Pref.setStringValue(activity, Const.showAd, dataSnapshot.getValue(String.class));
//
//                if (Pref.getStringValue(activity, Const.showAd, "").equalsIgnoreCase("true")) {
//                    fireAd("https://relish-2b113-default-rtdb.firebaseio.com/Interstitial");
//                    bannerAd("https://relish-2b113-default-rtdb.firebaseio.com/Banner");
//                    nativeAd("https://relish-2b113-default-rtdb.firebaseio.com/Native");
//                    openAd("https://relish-2b113-default-rtdb.firebaseio.com/OpenAd");
//                } else {
//                    Pref.setStringValue(activity, Const.interstitialAd, "");
//                    Pref.setStringValue(activity, Const.bannerAd, "");
//                    Pref.setStringValue(activity, Const.nativeAd, "");
//                    Pref.setStringValue(activity, Const.openAd, "");
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }

//    public void fireAd(String instAd) {
//        Firebase.setAndroidContext(this);
//        Firebase firebase = new Firebase(instAd);
//        firebase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Pref.setStringValue(activity, Const.interstitialAd, dataSnapshot.getValue(String.class));
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }

//    public void bannerAd(String bannerAd) {
//        Firebase.setAndroidContext(this);
//        Firebase firebase = new Firebase(bannerAd);
//        firebase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Pref.setStringValue(activity, Const.bannerAd, dataSnapshot.getValue(String.class));
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }

//    public void nativeAd(String nativeAd) {
//        Firebase.setAndroidContext(this);
//        Firebase firebase = new Firebase(nativeAd);
//        firebase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Pref.setStringValue(activity, Const.nativeAd, dataSnapshot.getValue(String.class));
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }

//    public void openAd(String openAd) {
//        Firebase.setAndroidContext(this);
//        Firebase firebase = new Firebase(openAd);
//        firebase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Pref.setStringValue(activity, Const.openAd, dataSnapshot.getValue(String.class));
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }

}