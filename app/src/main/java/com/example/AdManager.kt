package com.example

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdManager {
    private const val TAG = "AdManager"
    
    private fun getAdUnitId(context: Context): String {
        val prefs = context.getSharedPreferences("sarkari_guru_prefs", Context.MODE_PRIVATE)
        val forceProd = prefs.getBoolean("use_production_ads", false)
        return if (!BuildConfig.DEBUG || forceProd) {
            "ca-app-pub-6300818578767625/8600656696"
        } else {
            "ca-app-pub-3940256099942544/1033173712"
        }
    }
    
    private var mInterstitialAd: InterstitialAd? = null
    private var isInitializeCalled = false
    private var isAdLoading = false

    /**
     * Initializes the Google Mobile Ads SDK and preloads the first Interstitial Ad.
     */
    fun initialize(context: Context, onComplete: () -> Unit = {}) {
        if (isInitializeCalled) {
            onComplete()
            return
        }
        isInitializeCalled = true
        val unitId = getAdUnitId(context)
        Log.d(TAG, "Initializing MobileAds SDK with Ad Unit ID: $unitId")
        
        // MobileAds initialization is safe to run on any thread.
        MobileAds.initialize(context) { initializationStatus ->
            Log.d(TAG, "MobileAds SDK Initialized.")
            loadInterstitialAd(context)
            onComplete()
        }
    }

    /**
     * Loads an Interstitial Ad asynchronously.
     */
    fun loadInterstitialAd(context: Context) {
        if (isAdLoading || mInterstitialAd != null) {
            Log.d(TAG, "Ad is already loading or already loaded.")
            return
        }
        isAdLoading = true
        val unitId = getAdUnitId(context)
        Log.d(TAG, "Loading Interstitial Ad using ID: $unitId...")
        
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context.applicationContext,
            unitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e(TAG, "Ad failed to load: ${adError.message} (code: ${adError.code})")
                    mInterstitialAd = null
                    isAdLoading = false
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad loaded successfully.")
                    mInterstitialAd = interstitialAd
                    isAdLoading = false
                }
            }
        )
    }

    /**
     * Clears cached ad and forces a reload from AdMob.
     */
    fun clearAndReload(context: Context) {
        mInterstitialAd = null
        isAdLoading = false
        loadInterstitialAd(context)
    }

    /**
     * Shows the Interstitial ad if loaded, then handles callbacks gracefully.
     * When the ad is closed, dismissed, or fails to show, onAdDismissed is triggered.
     */
    fun showInterstitialAd(activity: Activity, onAdDismissed: () -> Unit) {
        val interstitialAd = mInterstitialAd
        if (interstitialAd != null) {
            Log.d(TAG, "Ad is ready. Showing full-screen Interstitial ad...")
            
            interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    Log.d(TAG, "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad dismissed full screen content.")
                    mInterstitialAd = null
                    // Preload the next ad immediately for the next major user trigger
                    loadInterstitialAd(activity)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG, "Ad failed to show: ${adError.message}")
                    mInterstitialAd = null
                    loadInterstitialAd(activity)
                    onAdDismissed()
                }

                override fun onAdImpression() {
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed full screen content.")
                }
            }
            interstitialAd.show(activity)
        } else {
            Log.d(TAG, "Ad was not loaded yet. Triggering next action immediately.")
            // Preload so it is ready next time
            loadInterstitialAd(activity)
            onAdDismissed()
        }
    }
}
