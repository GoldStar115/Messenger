//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.widget.ListView;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;


//==============================================================================================================================
public final class InAppAds
{

    //--------------------------------------------------------------------------------------------------------------------------
    public enum Provider
    {
        UNDEFINED,
        APPNEXT,
        MOBILECORE,
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void init(Provider provider, Activity activity, boolean needNativeAds)
    {
        createProviderIfNeeded(provider);

        AdsProvider adsProvider = getProvider(provider);

        if (adsProvider != null)
            adsProvider.init(activity, needNativeAds);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void cacheInterstitialAd(Provider provider, Activity activity)
    {
        AdsProvider adsProvider = getProvider(provider);

        if (adsProvider != null)
            adsProvider.cacheInterstitialAd(activity);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void showInterstitialAd(Provider provider, Activity activity)
    {
        AdsProvider adsProvider = getProvider(provider);

        if (adsProvider != null)
            adsProvider.showInterstitialAd(activity);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void refreshInterstitialOffers(Provider provider)
    {
        AdsProvider adsProvider = getProvider(provider);

        if (adsProvider != null)
            adsProvider.refreshInterstitialOffers();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void resetCallbacks(Provider provider, AdsCallbacks callbacks)
    {
        AdsProvider adsProvider = getProvider(provider);

        if (adsProvider != null)
            adsProvider.resetCallbacks(callbacks);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void showNativeAds(Provider provider, Activity activity, ListView listView, int inflaterID)
    {
        AdsProvider adsProvider = getProvider(provider);

        if (adsProvider != null)
            adsProvider.showNativeAds(activity, listView, inflaterID);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void resumeToNativeAds(Provider provider)
    {
        AdsProvider adsProvider = getProvider(provider);

        if (adsProvider != null)
            adsProvider.resumeToNativeAds();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static AdsProvider.NativeAdsStatus nativeAdsStatus(Provider provider)
    {
        AdsProvider adsProvider = getProvider(provider);

        if (adsProvider != null)
            return adsProvider.nativeAdsStatus();

        return AdsProvider.NativeAdsStatus.UNDEFINED;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private InAppAds()
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static void createProviderIfNeeded(Provider provider)
    {
        createProvidersListIfNeeded();

        int index = providerIndex(provider);

        if (index == PROVIDER_INDEX_INVALID)
            return;

        if (providers_.get(index) == null)
            providers_.set(index, createProvider(provider));
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static void createProvidersListIfNeeded()
    {
        if (providers_ != null)
            return;

        providers_ = new ArrayList<AdsProvider>();
        providers_.add(null);
        providers_.add(null);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static AdsProvider createProvider(Provider provider)
    {
        if (provider == Provider.APPNEXT)
            return new AppnextAds();

        else if (provider == Provider.MOBILECORE)
            return new MobilecoreAds();

        return null;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static int providerIndex(Provider provider)
    {
        if (provider == Provider.APPNEXT)
            return PROVIDER_INDEX_APPNEXT;

        else if (provider == Provider.MOBILECORE)
            return PROVIDER_INDEX_MOBILECORE;

        return PROVIDER_INDEX_INVALID;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static AdsProvider getProvider(Provider provider)
    {
        int index = providerIndex(provider);

        if (index == PROVIDER_INDEX_INVALID)
            return null;

        return providers_.get(index);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public final static int PROVIDER_INDEX_INVALID    = -1;
    public final static int PROVIDER_INDEX_BEGIN      =  0;
    public final static int PROVIDER_INDEX_APPNEXT    =  PROVIDER_INDEX_BEGIN;
    public final static int PROVIDER_INDEX_MOBILECORE =  1;
    public final static int PROVIDER_INDEX_END        =  PROVIDER_INDEX_MOBILECORE + 1;

    //--------------------------------------------------------------------------------------------------------------------------
    private static ArrayList<AdsProvider> providers_ = null;
}
