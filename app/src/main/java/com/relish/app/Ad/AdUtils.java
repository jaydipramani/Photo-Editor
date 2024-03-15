package com.relish.app.Ad;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
//import com.google.firebase.database.annotations.NotNull;
import com.relish.app.R;
import com.relish.app.Utils.Const;
import com.relish.app.Utils.Pref;

import org.jetbrains.annotations.NotNull;


public class AdUtils {
    public static NativeAd admobnativeAd = null;
    public static OnFinishAds onFinishAds = null;
    public static boolean isShowingAd = false;
    public static AppOpenAd.AppOpenAdLoadCallback loadCallback = null;


    public interface OnFinishAds {
        void onFinishAds(boolean z);
    }

    public static void fast_Admob_native(Activity context2) {
        AdLoader.Builder builder = new AdLoader.Builder(context2, Pref.getStringValue(context2, Const.nativeAd, ""));
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            public void onNativeAdLoaded(NativeAd nativeAd) {
                if (admobnativeAd != null) {
                    admobnativeAd = null;
                }
                admobnativeAd = nativeAd;
            }
        });
        AdLoader build = builder.withAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (admobnativeAd != null) {
                    admobnativeAd = null;
                }

            }

        }).build();
        build.loadAd(new AdRequest.Builder().build());
    }

    public static void Show_Am_Native(Activity context2, LinearLayout linearLayout, LinearLayout linearLayout2) {
        if (admobnativeAd != null) {
            NativeAdView nativeAdView = (NativeAdView) ((LayoutInflater) context2.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.am_native_ad_layout, (ViewGroup) null);
            linearLayout2.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            populateUnifiedNativeAdView(admobnativeAd, nativeAdView);
            linearLayout.removeAllViews();
            linearLayout.addView(nativeAdView);
            fast_Admob_native(context2);
        } else {
            linearLayout2.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            fast_Admob_native(context2);
        }
    }

    public static void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {

        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);


        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {


            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        }
    }

    public static void showAds_full(Activity activity, OnFinishAds onFinishAds) {
        AdUtils.onFinishAds = onFinishAds;
        try {
//            Utils.showProgress(activity);
            InterstitialAd.load(activity, Pref.getStringValue(activity, Const.interstitialAd, ""), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    interstitialAd.show(activity);
                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            onFinishAds.onFinishAds(false);
//                           Utils.dismissProgress();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            onFinishAds.onFinishAds(true);
//                            Utils.dismissProgress();
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                        }
                    });

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    onFinishAds.onFinishAds(false);
//                   Utils.dismissProgress();

                }
            });

        } catch (Exception e2) {
            Log.d("Utillss", "showads saven");
            onFinishAds.onFinishAds(true);
            e2.printStackTrace();
        }
    }

    public static void ShowAmBanner(Context context, LinearLayout linearLayout, LinearLayout linearLayout2) {
        linearLayout.removeAllViews();
        AdView adView2 = new AdView(context);
        adView2.setAdUnitId(Pref.getStringValue(context, Const.bannerAd, ""));
        linearLayout.addView(adView2);
        AdRequest build = new AdRequest.Builder().build();
        adView2.setAdSize(getAdSize(context));
        adView2.loadAd(build);
        adView2.setAdListener(new AdListener() {
            public void onAdClicked() {
            }

            public void onAdClosed() {
            }

            public void onAdLeftApplication() {
            }

            public void onAdOpened() {
            }

            public void onAdLoaded() {
                Log.d("Admob_Banner_Ads", "onAdFailedToLoad: Admob Banner loaded ");
                linearLayout2.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }

            public void onAdFailedToLoad(int i) {
                Log.d("Admob_Banner_Ads", "onAdFailedToLoad: Admob Banner fail" + i);
            }
        });
    }

    public static AdSize getAdSize(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, (int) (((float) displayMetrics.widthPixels) / displayMetrics.density));
    }

    public static void OpenAppAds(Activity activity) {
        try {
            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                    super.onAdLoaded(appOpenAd);

                    FullScreenContentCallback fullScreenContentCallback =
                            new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Set the reference to null so isAdAvailable() returns false.
                                    isShowingAd = false;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {

                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    isShowingAd = true;
                                }
                            };

                    appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                    appOpenAd.show(activity);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);

                }
            };
            if (!isShowingAd) {
                AppOpenAd.load(activity, Pref.getStringValue(activity, Const.openAd, ""), new AdRequest.Builder().build(), 1, loadCallback);
            }
        } catch (Exception activity2) {
            activity2.printStackTrace();
        }
    }
}
