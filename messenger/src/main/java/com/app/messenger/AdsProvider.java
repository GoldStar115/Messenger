//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ListView;


//==============================================================================================================================
public abstract class AdsProvider
{

    //--------------------------------------------------------------------------------------------------------------------------
    public enum NativeAdsStatus
    {
        UNDEFINED,
        LOADED,
        ERROR
    }

    //--------------------------------------------------------------------------------------------------------------------------
    protected enum State
    {
        UNDEFINED,
        VALID,
        INVALID
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void init(Activity activity, boolean needNativeAds)
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void cacheInterstitialAd(Activity activity)
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void showInterstitialAd(Activity activity)
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void refreshInterstitialOffers()
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void showNativeAds(Activity activity, ListView listView, int inflaterID)
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void resumeToNativeAds()
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void resetCallbacks(AdsCallbacks callbacks)
    {
        callbacks_ = callbacks;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public AdsCallbacks callbacks()
    {
        return callbacks_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public NativeAdsStatus nativeAdsStatus()
    {
        return nativeAdsStatus_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    protected void init(Activity activity)
    {
        inflater_ = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    protected void nativeAdsLoaded()
    {
        nativeAdsStatus_ = NativeAdsStatus.LOADED;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    protected void nativeAdsError()
    {
        nativeAdsStatus_ = NativeAdsStatus.ERROR;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    protected void setToValidState()
    {
        state_ = State.VALID;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    protected void setToInvalidState()
    {
        state_ = State.INVALID;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    protected LayoutInflater inflater()
    {
        return inflater_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    protected boolean isValid()
    {
        return state_ == State.VALID;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    protected boolean notInitialized()
    {
        return state_ == State.UNDEFINED;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private AdsCallbacks    callbacks_       = null;
    private LayoutInflater  inflater_        = null;
    private NativeAdsStatus nativeAdsStatus_ = NativeAdsStatus.UNDEFINED;
    private State           state_           = State.UNDEFINED;
}