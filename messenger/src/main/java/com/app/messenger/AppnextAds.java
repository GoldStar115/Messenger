//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.util.TransparentProgressDialog;
import com.appnext.appnextinterstitial.InterstitialManager;
import com.appnext.appnextinterstitial.OnAdClicked;
import com.appnext.appnextinterstitial.OnAdClosed;
import com.appnext.appnextinterstitial.OnAdError;
import com.appnext.appnextinterstitial.OnAdLoaded;
import com.appnext.appnextinterstitial.OnVidoeEnded;
import com.appnext.appnextsdk.API.AppnextAPI;
import com.appnext.appnextsdk.API.AppnextAd;
import com.appnext.appnextsdk.API.AppnextAdRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.picasso.Picasso;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;


//==============================================================================================================================
public final class AppnextAds extends AdsProvider
{

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void init(Activity activity, boolean needNativeAds)
    {
        if (!notInitialized())
            return;

        init(activity);

        validateStateByPlayServiceAvailability(activity);

        if (!isValid())
            return;

        if (needNativeAds)
            initNativeAdsAPI(activity);

        initCallbacks();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void cacheInterstitialAd(Activity activity)
    {
        if (isValid())
            InterstitialManager.cacheInterstitial(activity, APPNEXT_PLACEMTNT_ID, InterstitialManager.INTERSTITIAL_VIDEO);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void showInterstitialAd(Activity activity)
    {
        if (isValid())
            InterstitialManager.showInterstitial(activity, APPNEXT_PLACEMTNT_ID, InterstitialManager.INTERSTITIAL_VIDEO);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void refreshInterstitialOffers()
    {
        // Does nothing because no such functionality is provided in this verstion of AppNext SDK.
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void showNativeAds(Activity activity, ListView listView, int inflaterID)
    {
        progressDialog_ = new TransparentProgressDialog(activity, R.drawable.loading_spinner_icon);

        listView_ = listView;

        listView_.setAdapter(createAdapter(activity, inflaterID));

        listView_.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                    selectedIndex_ = position;

                    nativeAdSelected();

                    nativeAdsAPI_.adClicked(adsData_.get(position));
            }
        });
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void resumeToNativeAds()
    {
        selectedIndex_ = -1;

        listView_.invalidateViews();

        progressDialog_.dismiss();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void initNativeAdsAPI(Activity activity)
    {
        nativeAdsAPI_ = new AppnextAPI(activity, APPNEXT_PLACEMTNT_ID);

        nativeAdsAPI_.setAdListener(new AppnextAPI.AppnextAdListener()
        {
            @Override
            public void onAdsLoaded(ArrayList<AppnextAd> ads)
            {
                adsData_ = ads;
                nativeAdsLoaded();
            }

            @Override
            public void onError(String error)
            {
                nativeAdsError();
            }
        });

        nativeAdsAPI_.loadAds(new AppnextAdRequest().setCount(NATIVE_ADS_COUNT));
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void initCallbacks()
    {
        InterstitialManager.setOnVideoEndedCallback(new OnVidoeEnded()
        {
            @Override
            public void videoEnded()
            {
                if (callbacks() != null)
                    callbacks().onVidoeEnded();
            }
        });

        InterstitialManager.setOnAdLoadedCallback(new OnAdLoaded()
        {
            @Override
            public void adLoaded()
            {
                if (callbacks() != null)
                    callbacks().onAdLoaded();
            }
        });

        InterstitialManager.setOnAdClickedCallback(new OnAdClicked()
        {
            @Override
            public void adClicked()
            {
                if (callbacks() != null)
                    callbacks().onAdClicked();
            }
        });

        InterstitialManager.setOnAdClosedCallback(new OnAdClosed()
        {
            @Override
            public void onAdClosed()
            {
                if (callbacks() != null)
                    callbacks().onAdClosed();
            }
        });

        InterstitialManager.setOnAdErrorCallback(new OnAdError()
        {
            @Override
            public void adError(String error)
            {
                if (callbacks() != null)
                    callbacks().onAdError();
            }
        });
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private BaseAdapter createAdapter(final Activity activity, final int inflaterID)
    {
        return new BaseAdapter()
        {
            @Override
            public int getCount()
            {
                return adsData_.size();
            }

            @Override
            public Object getItem(int position)
            {
                return adsData_.get(position);
            }

            @Override
            public long getItemId(int position)
            {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                AppnextAd data = adsData_.get(position);

                convertView = inflater().inflate(inflaterID, null);

                LinearLayout layout      = (LinearLayout) convertView.findViewById(R.id.base_layout);
                TextView     title       = (TextView)     convertView.findViewById(R.id.mc_nativeAd_mainTV);
                TextView     description = (TextView)     convertView.findViewById(R.id.mc_nativeAd_descriptionTV);
                com.app.util.RoundedCornersGaganImageView
                             image       = (com.app.util.RoundedCornersGaganImageView)
                                                          convertView.findViewById(R.id.mc_nativeAd_iconIV);

                if (position == selectedIndex_)
                    layout.setBackgroundColor(Color.LTGRAY);

                else if (selectedIndex_ != -1)
                    layout.setBackgroundColor(Color.GRAY);

                title.setText(data.getAdTitle());
                description.setText(data.getAdDescription());

                image.setRadius  (NATIVE_ADS_IMAGE_RADIUS);
                image.setImageUrl(activity, data.getImageURL());


                return convertView;
            }
        };
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void nativeAdSelected()
    {
        listView_.invalidateViews();

        progressDialog_.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void validateStateByPlayServiceAvailability(Activity activity)
    {
        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);

        if (result == ConnectionResult.SUCCESS)
        {
            setToValidState();

            return;
        }

        setToInvalidState();

        GoogleApiAvailability.getInstance().getErrorDialog(activity, result, 0).show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // private static final String APPNEXT_PLACEMTNT_ID    = "a00509c5-9570-4718-8a25-61d15489b85b";  // This key must be changed
    //                                                                                                // to real app key.

    private static final String APPNEXT_PLACEMTNT_ID    = "c2c5af32-b154-4872-b47e-c2b78669dae1";

    private static final int    NATIVE_ADS_COUNT        = 3;
    private static final int    NATIVE_ADS_IMAGE_RADIUS = 5;

    //--------------------------------------------------------------------------------------------------------------------------
    private ArrayList<AppnextAd>      adsData_        = null;
    private AppnextAPI                nativeAdsAPI_   = null;
    private ListView                  listView_       = null;
    private TransparentProgressDialog progressDialog_ = null;
    private int                       selectedIndex_  = -1;
}
