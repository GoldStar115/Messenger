//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

//------------------------------------------------------------------------------------------------------------------------------
import com.ironsource.mobilcore.AdUnitEventListener;
import com.ironsource.mobilcore.CallbackResponse;
import com.ironsource.mobilcore.MobileCore;
import com.ironsource.mobilcore.NativeAdsAdapter;


//==============================================================================================================================
public final class MobilecoreAds extends AdsProvider
{

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void init(Activity activity, boolean needNativeAds)
    {
        if (!notInitialized())
            return;

        init(activity);

        try
        {
            if (needNativeAds)
                MobileCore.init(activity, MOBILECORE_DEV_ID, LOG_TYPE, MobileCore.AD_UNITS.INTERSTITIAL,
                                MobileCore.AD_UNITS.NATIVE_ADS);

            else
                MobileCore.init(activity, MOBILECORE_DEV_ID, LOG_TYPE, MobileCore.AD_UNITS.INTERSTITIAL);

            MobileCore.setAdUnitEventListener(new AdUnitEventListener()
            {
                @Override
                public void onAdUnitEvent(MobileCore.AD_UNITS adUnit, EVENT_TYPE eventType)
                {
                    if (adUnit == MobileCore.AD_UNITS.NATIVE_ADS)
                    {
                        if (eventType == EVENT_TYPE.AD_UNIT_READY)
                            nativeAdsLoaded();

                        else
                            nativeAdsError();
                    }
                }
            });

            setToValidState();
        }
        catch (Exception error)
        {
            setToInvalidState();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void cacheInterstitialAd(Activity activity)
    {
        if (isValid() && callbacks() != null)
            callbacks().onAdLoaded();  // No method to cache Ad in this version of MobileCore SDK was found, so we just call
                                       // this method like Ad is already cached.
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void showInterstitialAd(Activity activity)
    {
        if (isValid())
            MobileCore.showInterstitial(activity, new CallbackResponse()
            {
                @Override
                public void onConfirmation(TYPE type)
                {
                    if (callbacks() != null)
                        callbacks().onAdClosed();
                }
            });
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void refreshInterstitialOffers()
    {
        if (isValid())
            MobileCore.refreshOffers();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void showNativeAds(Activity activity, ListView listView, int inflaterID)
    {
        NativeAdsAdapter nativeAdsAdapter = MobileCore.buildNativeAdsAdapter(activity, createAdapter(), inflaterID);

        nativeAdsAdapter.setAdsPositions       (NATIVE_AD_POSITION_FIRST, NATIVE_AD_POSITION_SECOND, NATIVE_AD_POSITION_THIRD);
        nativeAdsAdapter.disableGooglePlayBadge();

        listView.setAdapter(nativeAdsAdapter);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void resumeToNativeAds()
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private BaseAdapter createAdapter()
    {
        return new BaseAdapter()
        {
            @Override
            public int getCount()
            {
                return 1;
            }

            @Override
            public Object getItem(int position)
            {
                return new Object();
            }

            @Override
            public long getItemId(int position)
            {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                // We need to provide some dummy item. Because NativeAdsAdapter expected at least one item in BaseAdapter.
                return inflater().inflate(R.layout.upon_install_dummy_inflater, null);
            }
        };
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // private static final String              MOBILECORE_DEV_ID         = "7PV0WVPOQQJ9POV73X40MKAB9LI27";  // This key must be
    //                                                                                                        // changed to real
    //                                                                                                        // app key.

    private static final String              MOBILECORE_DEV_ID         = "3BV30FPDWHZ8J7WYT2LA5L557BQTF";
    private static final MobileCore.LOG_TYPE LOG_TYPE                  = MobileCore.LOG_TYPE.PRODUCTION;  // This flag must be
                                                                                                          // changed to
                                                                                                          // MobileCore.LOG_TYPE.PRODUCTION
    private static final int                 NATIVE_AD_POSITION_FIRST  = 0;
    private static final int                 NATIVE_AD_POSITION_SECOND = 1;
    private static final int                 NATIVE_AD_POSITION_THIRD  = 2;
}
